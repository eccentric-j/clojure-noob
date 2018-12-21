(ns clojure-noob.chapter-5
  (:require [clojure-noob.note :refer :all]
            [clojure.string :as s]
            [clojure.pprint :refer [pprint]]))

;; Chapter 5: Functional Programming

;; Updates
; It's good practice to use two ;; to denote a header
; and a single semi-colon (;) for line comments

(lesson 5 "What makes a function pure?"
  (notes "Given the same set of args they always return the same result"
         "This makes them referentially transparent.
          Where you could replace calls to them with their output
          and the program would still work.")
  (+ 1 2))

; If a function relies on immutable data it's referentially transparent. If
; a function reads from a file it's also not referentially transparent.

; There are no side effects!
; (+ 1 2)     ; is a pure function because it does not observably change
              ; anything in the program
; (print 3)   ; is not a pure function since it is changing the output of the
              ; program like writing to a file or stdout. Also includes rand.

(lesson 5 "Variable scope"
  (def great-baby-name "Rosanthony")
  (prn great-baby-name)

  (let [great-baby-name "Bloodthunder"]
    (prn great-baby-name))

  great-baby-name)

;; not tail-call-optimized (TCO) meaning that each recursion adds to the stack
;; and no performance optimizations can be made
(lesson 5 "sum-basic"
  (defn sum-basic
    ([vals] (sum-basic vals 0))      ; -> sum([1 2 3] 0)
    ([vals accumulating-total] ; -> sum([1 2 3] 0) -> sum([2 3] (+ 1 0))
     (if (empty? vals)
       accumulating-total
       (sum-basic (rest vals) (+ (first vals) accumulating-total)))))
  (sum-basic [1 2 3]))

(lesson 5 "Sum-recur"
  (defn sum-recur
    ([vals] (sum-recur vals 0))
    ([vals accumulating-total]
     (if (empty? vals)
       accumulating-total
       (recur (rest vals) (+ (first vals) accumulating-total)))))
  (sum-recur [1 2 3]))

(lesson 5 "Recursion"
        (= (sum-basic [1 2 3]) (sum-recur [1 2 3])))
        ; => true

(lesson 5 "Composition instead of Attribute Mutation"
        ;; When the return value of one function is passed as an argument
        ;; to another is called function composition.
        ;; f . g (x) == f(g(x))
        (defn clean
          [text]
          (s/replace (s/trim text) #"lol" "LOL"))
        (clean "My boa constrictor is so sassy lol!   "))

(lesson 5 "Composition introduction"
        ; => "My boa constrictor is so sassy LOL!"
        ((comp inc *) 2 3)
        ; => 7
        ; But I'm not entirely sure why?
        (* 2 3))

(lesson 5 "Composition breakdown"
        ; Aha! * is multiply function and inc = x => x + 1
        ; so it's like calling (inc (* 2 3))
        (= ((comp inc *) 2 3) (inc (* 2 3))))
        ; => true

(lesson 5 "Setup Character Attributes"
        ; Note that the last function passed into comp can take any number of
        ; args but the rest of the functions do not.
        (def character
          {:name "Smooches McCutes"
           :attributes {:intelligence 10
                        :strength 4
                        :dexterity 5}})
        character)

(lesson 5 "c-int getter"
        (def c-int (comp :intelligence :attributes))
        (c-int character))

(lesson 5 "c-str getter"
        (notes "much better than R.compose(R.prop('dexterity'), R.prop('attributes))"
               "could be expressed as (fn [c] (:strength (:attributes c)))")
        (def c-str (comp :strength :attributes))
        (c-str character))

(lesson 5 "c-dex getter"
        (def c-dex (comp :dexterity :attributes))
        (c-dex character))

(lesson 5 "spell slots"
        (defn spell-slots
          [char]
          (int (inc (/ (c-int char) 2))))
        (spell-slots character))

(lesson 5 "Manual comp implementation with 2 functions"
        (defn two-comp
          [f g]
          (fn [& args]
            (f (apply g args))))
        ((two-comp inc +) 1 2))

(lesson 5 "Manual comp implementation"
        (notes "
               the source of comp shows it uses a recursive reduce like
               (list* f g fs) which creates a new seq contaiing the items
               prepended to the rest. The last arg is treated as the rest
               (list 1 2 [3 4 5]) ; => [1 2 3 4 5]
               ")
        (defn multi-comp
          [& fns]
          (fn [& args]
            (let [[first-f & fs] (reverse fns)]
              (reduce (fn [res f]
                        (f res)) (apply first-f args) fs))))
        ((multi-comp inc +) 1 2))

(lesson 5 "Redefine clean"
  (defn clean
    [text]
    (reduce (fn [string string-fn] (string-fn string))
            text
            [s/trim #(s/replace % #"lol" "LOL")]))
  (clean "My boa constrictor is so sassy lol!   "))

(lesson 5 "Without memoize"
  (notes "Prints Mr. Fantastico (after 1 second)"
         "seems to work by wrapping a function and checking its arguments
          storing its return value")
  (defn sleepy-identity
    "Returns the given value after 1 second"
    [x]
    (Thread/sleep 1000)
    x)
  (sleepy-identity "Mr. Fantastico"))

(def memo-sleepy-identity (memoize sleepy-identity))

(lesson 5 "With Memoize"
  (time (memo-sleepy-identity "Mr. Fantastico"))
  (time (memo-sleepy-identity "Mr. Fantastico")))

(def character
  {:name "Smooches McCutes"
   :attributes {:intelligence 10
                :strength 4
                :dexterity 5}})
(defn attr
  [attribute]
  (fn [c] (reduce #(%2 %1) c [:attributes attribute])))

(lesson 5 "Exercise 1 - Implement attr"
  ((attr :intelligence) character))

(defn brave-comp
  [& fns]
  (fn [& args]
    (let [[f1 & fns] (reverse fns)]
      (loop [[f & fns] fns
             result (apply f1 args)]
        (if (empty? fns)
          (f result)
          (recur fns
                 (f result)))))))

(lesson 5 "Exercise 2 - Implement comp"
  ((brave-comp :c :b :a) {:a {:b {:c "pie"}}}))
