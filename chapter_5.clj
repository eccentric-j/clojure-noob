;; Chapter 5: Functional Programming

;; First import! More coming in chapter 6
(require '[clojure.string :as s])

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
  ([vals] (sum vals 0))      ; -> sum([1 2 3] 0)
  ([vals accumulating-total] ; -> sum([1 2 3] 0) -> sum([2 3] (+ 1 0))
   (if (empty? vals)
     accumulating-total
     (sum (rest vals) (+ (first vals) accumulating-total)))))

(defn sum-recur
  ([vals] (sum vals 0))
  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals) (+ (first vals) accumulating-total)))))

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
        (= ((comp inc *) 2 3) (inc (* 2 3))))
        ; => true
        ; Note that the last function passed into comp can take any number of
        ; args but 
        ; tes


