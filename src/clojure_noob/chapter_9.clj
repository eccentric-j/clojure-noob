;; Chapter 9 ::  The Sacred Art of Concurrent and Parallel Programming
(ns clojure-noob.chapter-9
  (:require [clojure.string :as string]))

(defn format-notes
  [sep notes]
  (if (sequential? notes)
    (dorun (map #(println (str sep %)) notes))
    (format-notes sep (string/split notes #"\n"))))

(defmacro lesson
  [section-id title notes & forms]
  `(do (println (str "Section " ~section-id " :: " ~title))
       (println "\n  Notes:")
       (format-notes "   - " ~notes)
       (print "\n")
       (println (str "  " '~@forms))
       (println (str "   => " ~@forms))))

(lesson 8.1
        "This is a test lesson"
        "Should return true"
        (+ 1 1))

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
