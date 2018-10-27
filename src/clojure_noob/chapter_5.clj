(ns clojure-noob.chapter-5)
;; Chapter 5: Functional Programming

;; First import! More coming in chapter 6
(require '[clojure.string :as s]
         '[clojure.pprint :refer [pprint]])

(defn inspect
  [arg]
  (when-not (ifn? arg)
    (print "  -> ")
    (pprint arg)
    (println "")))

(defn title
  [text]
  (print (str "\n" text "\n")))


(defn titleize
  [topic]
  (str topic " for the Brave and True"))

(defn lesson
  [text & results]
  (title (str text "\n"))
  (run! inspect results)
  nil)

;; Updates
; It's good practice to use two ;; to denote a header
; and a single semi-colon (;) for line comments


;; Pure functions.
; What makes a pure function?

; Given the same set of args they always return the same result
; (+ 1 2)
; => 3
; This makes them referentially transparent where you could replace calls
; to them with their output and the program would still work.

; If a function relies on immutable data it's referentially transparent. If
; a function reads from a file it's also not referentially transparent.

; There are no side effects!
; (+ 1 2)     ; is a pure function because it does not observably change
              ; anything in the program
; (print 3)   ; is not a pure function since it is changing the output of the
              ; program like writing to a file or stdout. Also includes rand.


;; not tail-call-optimized (TCO) meaning that each recursion adds to the stack
;; and no performance optimizations can be made
(defn sum-basic
  ([vals] (sum-basic vals 0))      ; -> sum([1 2 3] 0)
  ([vals accumulating-total] ; -> sum([1 2 3] 0) -> sum([2 3] (+ 1 0))
   (if (empty? vals)
     accumulating-total
     (sum-basic (rest vals) (+ (first vals) accumulating-total)))))

(defn sum-recur
  ([vals] (sum-recur vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals) (+ (first vals) accumulating-total)))))

(sum-recur [1 2 3])

(lesson "Recursion"
        (def great-baby-name "Rosanthony")
        great-baby-name
        ; => "Rosanthony"

        (let [great-baby-name "Bloodthunder"]
          great-baby-name)
        ; => "Bloodthunder"

        great-baby-name
        ; => "Rosanthony"

        (sum-basic [1 2 3])
        ; => 6
        (= (sum-basic [1 2 3]) (sum-recur [1 2 3])))
        ; => true

(lesson "Composition instead of Attribute Mutation"
        ;; When the return value of one function is passed as an argument
        ;; to another is called function composition.
        ;; f . g (x) == f(g(x))
        (defn clean
          [text]
          (s/replace (s/trim text) #"lol" "LOL"))
        (clean "My boa constrictor is so sassy lol!   ")
        ; => "My boa constrictor is so sassy LOL!"
        ((comp inc *) 2 3)
        ; => 7
        ; But I'm not entirely sure why?
        (* 2 3)
        ; Aha! * is multiply function and inc = x => x + 1
        ; so it's like calling (inc (* 2 3))
        (= ((comp inc *) 2 3) (inc (* 2 3)))
        ; => true
        ; Note that the last function passed into comp can take any number of
        ; args but the rest of the functions do not.
        (def character
          {:name "Smooches McCutes"
           :attributes {:intelligence 10
                        :strength 4
                        :dexterity 5}})
        (def c-int (comp :intelligence :attributes))
        (def c-str (comp :strength :attributes))
        ;; much better than R.compose(R.prop('dexterity'), R.prop('attributes))
        (def c-dex (comp :dexterity :attributes))
        (c-int character)
        ; => 10
        ;; could be expressed as (fn [c] (:strength (:attributes c)))
        (c-str character)
        ; => 4
        (c-dex character)
        ; => 5
        (defn spell-slots
          [char]
          (int (inc (/ (c-int char) 2))))
        (spell-slots character)
        ; => 6
        (defn two-comp
          [f g]
          (fn [& args]
            (f (apply g args))))
        ((two-comp inc +) 1 2)
        ; => 4
        (defn multi-comp
          [& fns]
          (fn [& args]
            (let [[first-f & fs] (reverse fns)]
              ;; the source of comp shows it uses a recursive reduce like
              ;; (list* f g fs) which creates a new seq contaiing the items
              ;; prepended to the rest. The last arg is treated as the rest
              ;; (list 1 2 [3 4 5]) ; => [1 2 3 4 5]
              (reduce (fn [res f]
                        (f res)) (apply first-f args) fs))))
        ((multi-comp inc +) 1 2))

(lesson "Ch. 6 Redefine clean"
  (defn clean
    [text]
    (reduce (fn [string string-fn] (string-fn string))
            text
            [s/trim #(s/replace % #"lol" "LOL")]))
  (clean "My boa constrictor is so sassy lol!   "))




(lesson "Memoize"
  (defn sleepy-identity
    "Returns the given value after 1 second"
    [x]
    (Thread/sleep 1000)
    x)
  ;(sleepy-identity "Mr. Fantastico")
  ; => "Mr. Fantastico (after 1 second)"
  ;; memoize  seems to work by wrapping a function and checking its arguments
  ;; and storing its return value
  (def memo-sleepy-identity (memoize sleepy-identity)))
  ;; (memo-sleepy-identity "Mr. Fantastico"))
  ;; => "Mr. Fantastico" (after 1 second)
  ;; (memo-sleepy-identity "Mr. Fantastico"))
  ;; => "Mr. Fantastico" (immediately)

(lesson "Exercises")

(lesson "Exercise 1 - Implement attr"
  (def character
    {:name "Smooches McCutes"
     :attributes {:intelligence 10
                  :strength 4
                  :dexterity 5}})
  (defn attr
    [attribute]
    (fn [c] (reduce #(%2 %1) c [:attributes attribute])))

  (str "Exercise 1 result: " ((attr :intelligence) character)))

(lesson "Exercise 2 - Implement comp"
  (defn my-comp
    [& fns]
    (fn [v]
      (loop [result v
             [f & rest-fns] fns]
        (if (empty? rest-fns)
          (f result)
          (recur (f result) rest-fns)))))
  (str "Exercise 2 result: " ((my-comp :a :b) {:a {:b 5}})))
