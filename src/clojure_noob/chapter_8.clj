(ns clojure-noob.chapter-8)

;; Chapter 8 :: Writing Macros

(defmacro infix
  "Use this macro when you pine for the notation of your childhood"
  [[l m r]]
  (list m l r))

(infix (1 + 1))

(macroexpand '(infix (1 + 1)))
;; => (+ 1 1)

;; (and) is a macro

(source and)

(comment
  (defmacro and
    "Evaluates exprs one at a time, from left to right. If a form
  returns logical false (nil or false), and returns that value and
  doesn't evaluate any of the other expressions, otherwise it returns
  the value of the last expr. (and) returns true."
    {:added "1.0"}
    ([] true)
    ([x] x)
    ([x & next]
     `(let [and# ~x]
        (if and# (and ~@next) and#)))))

;; Create a macro that prints a result but returns input
;; similar to tap function

(defmacro my-print-whoopsie
  [expression]
  (list let [result expression]
        (list println result)
        result))

(my-print-whoopsie "hello world")

;; Fails because let refers to the value of the expression itself

(defmacro my-print
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result))

(my-print "hello world")
;; => "hello world"

;; Quoting

(+ 1 2)
;; -> 3

(quote (+ 1 2))
;; => (+ 1 2)

+
;; => #function[clojure.core/+]

(quote +)
;; => +
(symbol? (quote +))
;; => true

(comment sweating-to-the-oldies)
;; Would raise an exception because it's unbound

(quote sweating-to-the-oldies)
;; => sweating-to-the-oldies
;; Works because it returns a symbol

;; ' is a reader macro for quote
'(1 + 2)
;; => (1 + 2)

(source when)

(comment
  (defmacro when
    "Evaluates test. If logical true, evaluates body in an implicit do."
    {:added "1.0"}
    [test & body]
    (list 'if test (cons 'do body))))

(macroexpand '(when (the-cows-come :home)
                (call me :pappy)
                (slap me :silly)))
;; =>
(if (the-cows-come :home)
  (do (call me :pappy)
      (slap me :silly)))


(defmacro unless
  "Inverted 'if'"
  [test & branches]
  (conj (reverse branches) test 'if))


(macroexpand '(unless (done-been slapped? me)
                      (slap me :silly)
                      (say "I reckon that'll learn me")))

(if (done-been slapped? me)
  (say "I reckon that'll learn me")
  (slap me :silly))

;; Syntax Quoting

'+
;; => +

'clojure.core/+
;; => 'clojure.core/+

; Syntax quoting will always include symbol's full namespace

`+
;; => 'clojure.core/+

; Quoting recursively quotes all elements
'(+ 1 2)
;; => (+ 1 2)

; Syntax quoting is also recursive
`(+ 1 2)
;; => (clojure.core/+ 1 2)

`(+ 1 ~(inc 1))
;; => (clojure.core/+ 1 2)

;; ~ evaluates a form
