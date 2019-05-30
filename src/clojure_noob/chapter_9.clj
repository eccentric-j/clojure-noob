;; Chapter 9 ::  The Sacred Art of Concurrent and Parallel Programming
(ns clojure-noob.chapter-9
  (:require [book-report.core :refer [lesson]]))

(lesson 9 "Futures"
  (notes "Define a task that will execute on another thread.")
  (future (Thread/sleep 4000)
          (println "I'll print after 4 seconds."))
  (println "I'll print immediately")
  (notes "The return value of a future can be dereferenced or"
         "derefed using the deref function or @ reader macro as a prefix.")
  (let [result (future (println "this prints once")
                       (+ 1 1))]
    (println "deref: " (deref result))
    (println "@: "  @result)))


;; Results must be dereferencing
;; Call will black until a result can be dereferenced
(comment
 (let [result (future (println "this prints once")
                      (+ 1 1))]
   (println "deref:" (deref result))
   (println "@:" @result)))

;; Dereferencing can be done with (deref ref) or the reader macro @ref
;; Results are cached.
