(ns clojure-noob.chapter-5
  (:require [book-report.core :refer [lesson]]
            [clojure.string :as s]))

;; Chapter 5: Functional Programming

;; Updates

(lesson 5 "Comments"
  (notes "It's good particie to use two ;; to denote a header"
         "Use a single semi-colon (;) for line comments"))

(lesson 5 "What makes a function pure?"
  (notes "Given the same set of args they always return the same result"
         "This makes them referentially transparent.
          Where you could replace calls to them with their output
          and the program would still work."
         "If a function relies on and returns immutable data it's
         a pure function."
         "Pure functions are referentially transparent")
  (+ 1 2)
  (notes "The above is a pure function"
         "If a function reads from a file or has other side-effects
         it's not referentially transparent.")
  (print 3)
  (notes "The above is not a pure function since it is changing the output of
         the program like writing to a file or stdout.")
  (rand 5)
  (notes "Also not a pure function as it returns a different number each run."))

(lesson 5 "Variable scope"
  (def great-baby-name "Rosanthony")
  (prn great-baby-name)

  (let [great-baby-name "Bloodthunder"]
    (prn great-baby-name))

  great-baby-name)

(lesson 5 "Recursion"
        (title "Basic Recursion")
        (notes "Calls sum-basic directly by name to recurse"
               "Not tail-call-optimized"
               "Each iteration adds to the stack and no performance optimizations
          have been made.")
        (run (defn sum-basic
               ([vals] (sum-basic vals 0)) ; -> sum([1 2 3] 0)
               ([vals accumulating-total] ; -> sum([1 2 3] 0) -> sum([2 3] (+ 1 0))
                (if (empty? vals)
                  accumulating-total
                  (sum-basic (rest vals) (+ (first vals) accumulating-total))))))
        (sum-basic [1 2 3])
        (notes "(sum [1 2 3] 0)"
               "(sum [2 3] (+ 1 0))"
               "(sum [3] (+ 2 1))"
               "(sum [] (+ 3 3))")
        (title "Recursion with recur")
        (notes "Uses the recur form to call the parent defn to recurse"
               "Better optimized for performance"
               "Better for big collections"
               "Uses parallelization")
        (run (defn sum-recur
               ([vals] (sum-recur vals 0))
               ([vals accumulating-total]
                (if (empty? vals)
                  accumulating-total
                  (recur (rest vals) (+ (first vals) accumulating-total))))))
        (sum-recur [1 2 3])

        (notes "Both functions are equivalent")
        (= (sum-basic [1 2 3]) (sum-recur [1 2 3])))

(lesson 5 "Composition"
        (notes "When the return value of one function is passed as
               an argument to another"
               "f . g (x) = f(g(x))")
        (title "Manual Composition")
        (defn clean
          [text]
          (s/replace (s/trim text) #"lol" "LOL"))
        (clean "My boa constrictor is so sassy lol!   ")

        (title "comp function")
        ((comp inc *) 2 3)
        (notes "Returns 7 but not sure why?")
        (= (inc (* 2 3))
           ((comp inc *) 2 3)
           7)
        (notes "The last function passed into comp can take any
               number of args"
               "The rest of the functions only work with an
               arity of 1"))

(lesson 5 "Composing Getters"
        (title "Character Attributes")
        (def character
          {:name "Smooches McCutes"
           :attributes {:intelligence 10
                        :strength 4
                        :dexterity 5}})
        character

        (title "c-int")

        (def c-int (comp :intelligence :attributes))
        (c-int character)

        (notes "much better than R.compose(R.prop('dexterity'), R.prop('attributes))"
               "could be expressed as (fn [c] (:strength (:attributes c)))")

        (title "c-str")

        (def c-str (comp :strength :attributes))
        (c-str character)

        (title "c-dex")
        (def c-dex (comp :dexterity :attributes))
        (c-dex character)

        (title "spell slots")
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
        (run (defn multi-comp
               [& fns]
               (fn [& args]
                 (let [[first-f & fs] (reverse fns)]
                   (reduce (fn [res f]
                             (f res)) (apply first-f args) fs)))))
        (= ((multi-comp inc +) 1 2) 4))

(lesson 5 "Redefine clean with reduce"
  (defn clean
    [text]
    (reduce (fn [string string-fn] (string-fn string))
            text
            [s/trim #(s/replace % #"lol" "LOL")]))
  (clean "My boa constrictor is so sassy lol!   "))


(lesson 5 "Memoization"
       (notes "Can be used to speed up performance"
              "Stores results and attempts to map args to directly to result")
       (title "Without")
       (notes "Prints Mr. Fantastico (after 1 second)"
              "seems to work by wrapping a function and checking its arguments
          storing its return value")
       (defn sleepy-identity
         "Returns the given value after 1 second"
         [x]
         (Thread/sleep 1000)
         x)
       (sleepy-identity "Mr. Fantastico")

       (title "With Memoize")
       (def memo-sleepy-identity (memoize sleepy-identity))
       (time (memo-sleepy-identity "Mr. Fantastico"))
       (time (memo-sleepy-identity "Mr. Fantastico")))

(lesson "5 Exercise 1" "Implement attr"
        (run
          (def character
            {:name "Smooches McCutes"
             :attributes {:intelligence 10
                          :strength 4
                          :dexterity 5}})
          (defn attr
            [attribute]
            (fn [c] (reduce #(%2 %1) c [:attributes attribute]))))
        (= ((attr :intelligence) character)
           10))

(lesson "5 Exercise 2" "Implement comp"
        (run
          (defn brave-comp
            [& fns]
            (fn [& args]
              (let [[f1 & fns] (reverse fns)]
                (loop [[f & fns] fns
                       result (apply f1 args)]
                  (if (empty? fns)
                    (f result)
                    (recur fns
                           (f result))))))))
        (= ((brave-comp :c :b :a) {:a {:b {:c "pie"}}})
           "pie"))
