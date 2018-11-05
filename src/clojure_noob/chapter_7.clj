(ns clojure-noob.chapter-7
  (:require [clojure.pprint :refer [pprint]]))

;; Chapter 7 :: Reading, Evaluation, and Macros

(defmacro backwards
  [form]
  (reverse form))


;; Doesn't follow typical syntax convention. Instead it allows us to use
;; a new syntax
(pprint (backwards (" backwards" " am" "I" str)))

;; # Evaluation

(+ 1 2)

;; Is read as '(+ 1 2)
;; The power of a LISP is that there isn't an inaccessible AST structure
;; between the text you type and what the interpreter or compiler executes

(eval '(+ 1 2))
(eval (list + 1 2))

(def addition-list (list + 1 2))
(eval addition-list)
;; => 3

(eval (concat addition-list [10]))
;; => 13

(eval (list 'def 'lucky-number (concat addition-list [10])))

lucky-number
;; => 13

;; # Reading

(read-string "(+ 1 2)")
(= (read-string "(+ 1 2)") '(+ 1 2))
;; => true

;; Read string doesn't evaluate. It only parses a string into a clojure list.
(list? (read-string "(+ 1 2)"))
;; => true

(conj (read-string "(+ 1 2)") :zagglewag)
;; => (:zagglewag + 1 2)


;; Combining reading and evaluation

(eval (read-string "(+ 1 2)"))

;; A lot of symbols evaluate to themselves in a one-to-one relationship
(= '() (read-string "()"))
(= 'str (read-string "str"))
(= '[1 2] (read-string "[1 2]"))
(= '{:sound "hoot"} (read-string "{:sound \"hoot\"}"))

;; but the reader can converting text to data structures
;; The following examples use reader macros to transform the text into
;; data other than the original input string

(pprint (read-string "#(+ 1 %)"))
;; => (fn* [p1__15530#] (+ 1 p1__15530#))

(= (quote '(a b c)) (read-string "'(a b c)"))

;; Reader macros are designated by marco characters like
(list \' \# \@)

(read-string "@var")
;; => (clojure.core/deref var)

(read-string "; ignore!\n(+ 1 2)")
;; => (+ 1 2)

;; Evalualuator rules
(+ 1 2)
;; First the + symbol resolves to the + function
;; Second 1 evalutes to 1
;; Third 2 evalues to 2
;; Finally the params are applied to the + function

;; These evaluate to themselves
true
;; => true

false
;; => false

{}
;; => {}

:huzzah
;; => :huzzah

()
;; => ()

;; How clojure looks up symbols:
;; 1. See if symbol is a special form. If not...
;; 2. Does the symbol reference a local binding? Otherwise..
;; 3. Look up a namespace mapping introduced by def. If not...
;; 4. Throw an exception

;; Special Forms

(if true :a :b)
;; => a

;; if
;; Evaluating throws a compiler exception

;; The if form works differently because it's not a function. Plus its
;; operands are not evaluated prior to the if form being applied. This
;; ensures that only the target branch is executed when the conditional
;; operand is true.

;; What makes special forms special?
;; They implement core behavior that can't be expressed with functions.

;; Local binding

(let [x 5]
  (+ x 3))
;; => 8

(def x 15)
(+ x 3)
;; => 18

(def x 15)
(let [x 5]
  (+ x 3))
;; => 8

;; The inner most binding is resolved first. The x variable in the let binding is scoped to the let form body.

;; Macros

;; Say we wanted to use infix notation

(read-string "(+ 1 1)")
;; => (1 + 1)
;; Produces a list of symbols and doesn't evaluate.

(eval (read-string "(1 + 1)"))
;; Cannot be evaluated and throws an exception

(let [infix (read-string "(1 + 1)")]
  (list (second infix) (first infix) (last infix)))
;; => (+ 1 1)

(eval (let [infix (read-string "(1 + 1)")]
        (list (second infix) (first infix) (last infix))))
;; => 2


(defmacro ignore-last-operand
  [function-call]
  ;; Function call is the list of symbols being defined
  (butlast function-call))

(ignore-last-operand (+ 1 2 10))
;; => 3

(ignore-last-operand (+ 1 2 (println "look at me!!!")))
;; => 3

(macroexpand '(ignore-last-operand (+ 1 2 10)))
;; => (+ 1 2)

(macroexpand '(ignore-liast-operand (+ 1 2 (println "look at me!!!"))))
;; => (+ 1 2)


;; primitive infix macro
;; DO NOT CHANGE - KEEP FOR REFERENCE FOR EXERCISE 2
(defmacro infix
  [infixed]
  (list (second infixed)
        (first infixed)
        (last infixed)))

(infix (1 + 2))
;; => 3

;; Syntax abstraction

;; -> Thread macro

;; Let's start with nested function calls

(+ (* (+ (- 11 2) 10) 2) 5)
;; => 43

;; Using the thread macro it can be broken down

(-> 11
    (- 2)
    (+ 10)
    (* 2)
    (+ 5))
;; => 43

(pprint (macroexpand '(-> 11
                          (- 2)
                          (+ 10)
                          (* 2)
                          (+ 5))))

;; => (+ (* (+ (- 11 2) 10) 2) 5)

;; The thread macro is not a function it actually modifies all the forms
;; in the body

;; Exercise 7.1
; Use the list function, quoting, and read-string to create a list that,
; when evaluated, prints your first name and your favorite sci-fi movie

;; No idea if it's "right" but it did force me to get familiar with the
;; expectations of the eval, and read-string functions.
(eval (list (read-string "println") (quote (list "Jay" "Blade Runner"))))


;; Exercise 7.2

;; Create an infix function that takes a list like (1 + 3 * 4 - 5) and
;; transforms it into the lists that Clojure needs in order to correctly
;; evaluate the expression using operator precedence rules.

;; Actual Input

(def input [1 + 3 * 4 - 5])

;; Expected Output
'(- (+ (* 3 4) 1) 5)
;; => 8


;; Example given
(defmacro infix
  [infixed]
  (list (second infixed)
        (first infixed)
        (last infixed)))

(infix (1 + 2))

;; Notes:
;; - First, let's just write a function to transform the input
;;   into a valid list.
;; - After, turn that into a macro.
;; - This may be solvable in two phases.
;;   - The first phase is to group our pairs together.
;;   - The second phase is to nest them

(def order-of-operations '(* / + -) )

(defn drop-slice
  [v start end]
  (let [pre (subvec v 0 start)
        post (subvec v end)]
    (concat pre post)))

(defn get-first-group
  [infixed]
  (let [length (count infixed)]
    (loop [start 0
           [target-op & remaining-ops :as ops] order-of-operations]
      (let [end (+ start 3)
            group (subvec infixed start end)
            [l op r] group]
        (cond (>= start length) (recur 0
                                       remaining-ops)
              (not= target-op op) (recur (inc start)
                                         ops)
               :else [(list op l r) (drop-slice infixed start end)])))))

(defn pair-ops
  [infixed]
  (loop [in infixed
         pairs ()]
    (let [[l r & remaining] in]
      (cond
        (number? l) (recur remaining
                           (conj pairs (list r l)))
        (number? r) (recur remaining
                           (conj pairs (list l r)))
        (empty? remaining) pairs))))

(defn get-opt-index
  [ops op]
  (.indexOf ops op))

(defn sort-pairs
 [pairs]
 (let [ops (vec order-of-operations)]
   (sort-by #(get-opt-index ops (first %)) pairs)))

(defn nest-pairs
  [pairs initial]
  (reduce (fn nest-pair
            [nested [op x]]
            (list op nested x)) initial pairs))

(defmacro infix
  [infixed]
  (let [infixed (vec infixed)
        [first-pair remaining-infixed] (get-first-group infixed)]
    (-> remaining-infixed
        (doto println)
        (pair-ops)
        (doto println)
        (sort-pairs)
        (nest-pairs first-pair))))

;; So this works
(infix (1 + 3 * 4 - 5))
;; => 8

;; But this does not
(infix (1 + 3 * 4 - 5 * 3))
;; => 32 should be -2

;; So my logic is incorrect but for the purpose of the lesson I think the
;; macro works correctly.

;; A more robust and correct infix operator may be found here:
;; https://groups.google.com/forum/m/#!topic/clojure/PsC1cX5_MsA


;; It works in a simiilar manner using a recursive loop but far more
;; simple.
