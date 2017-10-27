(ns pegthing.core
  (require [clojure.set :as set])
  (:gen-class))

(declare successful-move prompt-move game-over query-rows)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

;; Increments the sum by an increasing incrementer
; (tri 0 1)
; (tri 1 2)
; (tri 3 3)
; (tri 6 4)
; (tri 10 5)
(defn tri*
  "Generates lazy sequence of triangular numbers"
  ([] (tri* 0 1))
  ([sum n]
   (let [new-sum (+ sum n)]
     (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

(def tri (tri*))

;; Takes a number then generates a seq ending on the first triangular number
;; that is less than or equal to the input number. Return true if the last
;; triangular number is the same as the input number
(defn triangular?
  "Is the number triangular? e.g. 1, 3, 6, 10, 15, etc"
  [n]
  (= n (last (take-while #(>= n %) tri))))

(triangular? 6)

;; Takes 0 - N of the lazy tri seq and returns the last value
; (row-tri 1)
; => 1
; (row-tri 3)
; => 6
; (row-tri 4)
; => 10
;; it uses row it seems in terms of this model
; [ 1 1 ]
; [ 2 3 ]
; [ 3 6 ]
; [ 4 10 ]
; [ 5 15 ]
(defn row-tri
  "The triangular number at the end of row n"
  [n]
  (last (take n tri)))

;; Kind of the opposite of row-tri. Retreives the row number based on
;; the tri value. We increment the count because we get the values up to our
;; pos
(defn row-num
  "Returns row number in the position belongs to: pos 1 in row 1,
  positions 2 and 3 in row 2, etc.."
  [pos]
  (inc (count (take-while #(> pos %) tri))))

(defn connect
  "Form a mutual connection between two positions"
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))
            board
            [[pos destination] [destination pos]])
    board))

(connect {} 15 1 2 4)

