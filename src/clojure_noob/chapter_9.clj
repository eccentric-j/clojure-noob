;; Chapter 9 ::  The Sacred Art of Concurrent and Parallel Programming
(ns clojure-noob.chapter-9
  (:require [clojure-noob.note :refer :all]))

;; Define a task and place it on another thread.
;; Will not require an immediate result
(comment
 (future (Thread/sleep 4000)
         (println "I'll print after 4 seconds."))
 (println "I'll print immediately"))

;; Results must be dereferencing
;; Call will black until a result can be dereferenced
(comment
 (let [result (future (println "this prints once")
                      (+ 1 1))]
   (println "deref:" (deref result))
   (println "@:" @result)))

;; Dereferencing can be done with (deref ref) or the reader macro @ref
;; Results are cached.
