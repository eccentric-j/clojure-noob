(ns pegthing.core
  (require [clojure.set :as set])
  (:gen-class))

;; note in proto-repl\lein repl
;; (in-ns 'pegthing.core)
;; Run that if it seems like the namespaces are not communicating

(declare successful-move prompt-move game-over query-rows)

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

;; Updated the board with all the connections between src and dest positions
(defn connect
  "Form a mutual connection between two positions"
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              ;; Takes a vector of keys and creates a submap for each one
              (assoc-in new-board [p1 :connections p2] neighbor))
            board
            [[pos destination] [destination pos]])
    board))

(defn connect-right
  [board max-pos pos]
  ;; neighbor is one position to the right,
  ;; destination is two positions to the right
  (let [neighbor (inc pos)
        destination (inc neighbor)]
    ;; if either the neighbor or pos is not triangular
    ;; retrieve connections
    (if-not (or (triangular? neighbor) (triangular? pos))
      (connect board max-pos pos neighbor destination)
      board)))

(defn connect-down-left
  [board max-pos pos]
  ;; set the row to the row the pos falls on
  ;; row 12 => 5 [11 - 15]
  ;; neighbor is set to the value of the next row
  ;; that is one left of the input pos
  ;; destination is one row down and one value left
  (let [row (row-num pos)
        neighbor (+ row pos)
        destination (+ 1 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn connect-down-right
  [board max-pos pos]
  ;; set the row to what row the pos falls on
  ;; row 12 => 3 [4 - 6]
  ;; neighbor is one row down and one value to the right
  ;; destination is one row down and one value right
  (let [row (row-num pos)
        neighbor (+ 1 row pos)
        destination (+ 2 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn add-pos
  "Pegs the position and performs connections"
  [board max-pos pos]
  ;; first update the board to specify which pos is pegged: true
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    ;; iterate through each connect function and apply it to each
    ;; board update. Just like compose!
    (reduce (fn [new-board connection-creation-fn]
              (connection-creation-fn new-board max-pos pos))
            pegged-board
            [connect-right connect-down-left connect-down-right])))

(defn new-board
  "Creates a new board with the given number of rows"
  [rows]
  (let [initial-board {:rows rows}
        max-pos (row-tri rows)]
    (reduce (fn [board pos] (add-pos board max-pos pos))
            initial-board
            (range 1 (inc max-pos)))))

(defn pegged?
  "Does the position have a peg in it?"
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg
  "Take the peg at given position out of the board"
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn place-peg
  "Put a peg in the board at given position"
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  "Take peg out of p1 and place it in p2"
  [board p1 p2]
  (place-peg (remove-peg board p1) p2))

(defn valid-moves
  "Return a map of all valid moves for pos, where the key is the
  destination and the value is the jumped position"
  [board pos]
  (into {}
        (filter (fn [[destination jumped]]
                  (and (not (pegged? board destination))
                       (pegged? board jumped)))
                (get-in board [pos :connections]))))

(defn valid-move?
  "Return jumped position if the move from p1 to p2 is valid, nil otherwise"
  [board p1 p2]
  (get (valid-moves board p1) p2))

(defn make-move
  "Move peg from p1 to p2, removing jumped peg"
  [board p1 p2]
  (if-let [jumped (valid-move? board p1 p2)]
    (move-peg (remove-peg board jumped) p1 p2)))

(defn can-move?
  "Do any of the pegged positions have valid moves?"
  [board]
  (some (comp not-empty (partial valid-moves board))
        (map first (filter #(get (second %) :pegged) board))))

;; Printing now
(def alpha-start 97)
(def alpha-end 123)
(def letters (map (comp str char) (range alpha-start alpha-end)))
; (str (char 97)) => "a"
; (str (char 122)) => "z"
(def pos-chars 3)

;; these style functions come from the original source as it wasn't mentioned
;; in the book
;; https://github.com/flyingmachine/pegthing/blob/master/src/pegthing/core.clj
(def ansi-styles
  {:red "[31m"
   :green "[32m"
   :blue "[34m"
   :reset "[0m"})

(defn ansi
  "Produce a string which will apply an ansi style"
  [style]
  (str \u001b (style ansi-styles)))

(defn colorize
  "Apply ansi color to text"
  [text color]
  (str (ansi color) text (ansi :reset)))

(defn render-pos
  [board pos]
  (str (nth letters (dec pos))
       (if (get-in board [pos :pegged])
         (colorize "0" :blue)
         (colorize "-" :red))))

(defn row-positions
  "Return all positions in the given row"
  [row-num]
  ;; limit start to be from 1
  (range (inc (or (row-tri (dec row-num)) 0))
         (inc (row-tri row-num))))

;; (map row-tri (range 1 10))) => (1 3 6 10 15 21 28 36 45)

(defn row-padding
  "String of spaces to add to the beginning of a row to center it"
  [row-num rows]
  (let [pad-length (/ (* (- rows row-num) pos-chars) 2)]
    (apply str (take pad-length (repeat " ")))))

(defn render-row
  [board row-num]
  (str (row-padding row-num (:rows board))
       (clojure.string/join " " (map (partial render-pos board)
                                     (row-positions row-num)))))

(defn print-board
  [board]
  (doseq [row-num (range 1 (inc (:rows board)))]
    (println (render-row board row-num))))

(defn show-board [] (print-board {:rows 5}))

(defn letter->pos
  "Converts a letter string to the corresponding position number"
  [letter]
  (inc (- (int (first letter)) alpha-start)))

(defn get-input
  "Waits for user to enter text and hit enter, then cleans the input"
  ([] (get-input nil))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input)
       default
       (clojure.string/lower-case input)))))

(defn characters-as-strings
  "Returns a list of the characters in a given string"
  [input-string]
  (filter #(re-find #"[a-z]" %)
          (clojure.string/split input-string #"")))

(defn user-entered-invalid-move
  "Handles the next step after a user has entered an invalid move"
  [board]
  (println "\n!!!! That was an invalid move :(\n")
  (prompt-move board))

(defn user-entered-valid-move
  "Handles the next step after a user has entered a valid move"
  [board]
  (if (can-move? board)
      (prompt-move board)
      (game-over board)))

(defn prompt-move
  [board]
  (println "\nHere's your board:")
  (print-board board)
  (println "Move from where to where? Enter two letters:")
  (let [input (map letter->pos (characters-as-strings (get-input)))]
    (if-let [new-board (make-move board (first input) (second input))]
      (user-entered-valid-move new-board)
      (user-entered-invalid-move board))))

(defn prompt-empty-peg
  [board]
  (println "Here's your board:")
  (print-board board)
  (println "Remove which peg? [e]")
  (prompt-move (remove-peg board (letter->pos (get-input "e")))))

(defn prompt-rows
  []
  (println "How many rows? [5]")
  (let [rows (Integer. (get-input 5))
        board (new-board rows)]
    (prompt-empty-peg board)))

(defn game-over
  "Announce the game is over and prompt to play again"
  [board]
  (let [remaining-pegs (count (filter :pegged (vals board)))]
    (println "Game over! You had" remaining-pegs "pegs left:")
    (print-board board)
    (println "Play again? y/n [y]")
    (let [input (get-input "y")]
      (if (= "y" input)
        (prompt-rows)
        (do
          (println "Bye!")
          (System/exit 0))))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (prompt-rows))
