(ns clojure-noob.chapter-8
  (:require [clojure.repl :refer :all]))

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

;; Regular quoting
(defmacro code-critic
  "Phrases are courtesy Hermes Conrad from Futurama"
  [bad good]
  (list 'do
        (list 'println
              "Great squid of Madrid, this is bad code:"
              (list 'quote bad))
        (list 'println
              "Sweet gorilla of Manilla, this is good code:"
              (list 'quote good))))

(code-critic (1 + 1) (+ 1 1))

;; Syntax quoting
(defmacro code-critic
  "Phrases are courtesy Hermes Conrad from Futurama"
  [bad good]
  `(do (println "Great squid of Madrid, this is bad code:"
                (quote ~bad))
       (println "Sweet gorilla of Manilla, this is good code:"
                (quote ~good))))

(code-critic (1 + 1) (+ 1 1))

(defn criticize-code
  [criticism code]
  `(println ~criticism (quote ~code)))

(defmacro code-critic
  [bad good]
  `(do ~(criticize-code "Cursed bacteria of Liberia, this is bad code: "
                        bad)
       ~(criticize-code "Sweet sacred boa of Western and Eastern Samoa, this is good code:"
                        good)))

(defmacro code-critic
  [bad good]
  `(do ~(map #(apply criticize-code %)
             [["Great squid of Madrid, this is bad code:" bad]
              ["Sweet gorilla of Manilla, this is good code:" good]])))

;; Results in NullPointerPosition
;; Because it evalutes to (do (nil nil))

(code-critic (1 + 1) (+ 1 1))

;; Can be fixed with unquote slicing

`(+ ~@(list 1 2 3))
;; => (clojure.core/+ 1 2 3)

(defmacro code-critic
  [bad good]
  `(do ~@(map #(apply criticize-code %)
              [["Sweet lion of Zion, this is bad code:" bad]
               ["Great cow of Moscow, this is good code:" good]])))

(code-critic (1 + 1) (+ 1 1))


;; Variable Capture

(def message "Good job!")

(defmacro with-mischief
  [& stuff-to-do]
  (concat (list 'let ['message "Oh big deal!"])
          stuff-to-do))

(with-mischief
  (println "Here's how I feel about that thing you did:" message))

;; GASP! Prints Oh big deal! Instead of good job

(gensym)
(gensym)

(= (gensym 'message) (gensym 'message))
;; => false

(defmacro without-mischief
  [& stuff-to-do]
  (let [macro-message (gensym 'message)]
    `(let [~macro-message "Oh big deal"]
       ~@stuff-to-do
       (println "I still need to say:" ~macro-message))))

(without-mischief
 (println "Here's how I feel about that thing you did:" message))

;; More concise gensym syntax

`(blarg# blarg#)

`(let [name# "Larry Potter"] name#)

;; Double Evaluation

(defmacro report
  [to-try]
  `(if ~to-try
     (println (quote ~to-try) "was successful:" ~to-try)
     (println (quote ~to-try) "was not successful:" ~to-try)))

(report (do (Thread/sleep 1000) (+ 1 1)))

;; The (Thread/sleep 1000) will be executed 3 times
;; Instead...

(defmacro report
  [to-try]
  `(let [result# ~to-try]
     (if result#
       (println (quote ~to-try) "was successful:" result#)
       (println (quote ~to-try) "was not successful:" result#))))

;; Macros all the way down
;; Sometimes you may end up having to write multiple macros to get anything
;; useful accomplished.

(report (= 1 1))
(report (= 1 2))

(doseq [code ['(= 1 1) '(= 1 2)]]
  (report code))

;; Will not work as expected because (report code) receives the symbol 'code
;; instead of the value of code

(defmacro doseq-macro
  [macroname & args]
  `(do
     ~@(map (fn [arg] (list macroname arg)) args)))

(doseq-macro report (= 1 1) (= 1 2))

;; If you find yourself in this situation you may want to rethink your
;; approach

;; Brews for the Brave and True

(def order-details-validations
  {:name
   ["Please enter a name" not-empty]
   :email
   ["Please enter an email address" not-empty
    "Your email address doesn't look like an email address."
    #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
  "Return a seq of error messages"
  [to-validate message-validator-pairs]
  (map first (filter #(not ((second %) to-validate))
                     (partition 2 message-validator-pairs))))


(error-messages-for "" ["Please enter a name" not-empty])

(defn validate
  "Returns a map with a vector of errors for each key"
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[fieldname validation-check-groups] validation
                  value (get to-validate fieldname)
                  error-messages (error-messages-for value validation-check-groups)]
              (if (empty? error-messages)
                errors
                (assoc errors fieldname error-messages))))
          {}
          validations))


(def order-details {:name "Mitchard Blimmons"
                    :email "mitchard.blimmonsgmail.com"})

(validate order-details order-details-validations)
