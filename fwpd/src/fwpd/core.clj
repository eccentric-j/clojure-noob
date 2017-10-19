;; Chapter 4: Vampire processor
(ns fwpd.core)

(def filename "suspects.csv")

;; (slurp "filename") returns a new line separated string

(def vamp-keys [:name :glitter-index])

;; Converts string input to an integer
(defn str->int
  [str]
  (Integer. str))

;; map of functions
(def conversions {:name identity
                  :glitter-index str->int})

(defn glitter-index?
  [num]
  (and (number? num)
       (>= num 1)
       (<= num 10)))

(def validators {:name string?
                 :glitter-index glitter-index?})

(defn convert
  ;; takes a keyword and a value
  [vamp-key value]
  ;; gets a function from our conversions and applies
  ;; it to the value
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Returns a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  ;; Iterate through each pair of values [name, value]
  (map (fn [unmapped-row]
         ;; Reduce each pair into a single map
         (reduce (fn [row-map [vamp-key value]]
                   ;; Run the conversion function on each value using the key
                   ;; to retreive the proper conversion function:
                   ;;  name: identity
                   ;;  glitter-index: str->int
                   (assoc row-map vamp-key (convert vamp-key value)))
                 ;; seed value
                 {}
                 ;; No idea yet...
                 ;; Creates a list containing two vectors pairing each value
                 ;; to their key name ([:name value] [:glitter-index value])
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (map :name
       (filter #(>= (:glitter-index %) minimum-glitter) records)))

(def suspects (mapify (parse (slurp filename))))

(defn validate
  "Returns true or false if suspect is valid"
  [validators suspect]
  (let [entries (vec suspect)] (and (contains? suspect :name)
                                    (contains? suspect :glitter-index)
                                    (every? identity 
                                            (map (fn [[vamp-key value]]
                                                    ((get validators vamp-key) value))
                                                entries)))))


  ;; Test to see that both a :name and :glitter-index is present
  ;; Test to see that :name is a string
  ;; Test to see that :glitter-index is an integer between 1 - 10

(defn append
  "Returns a seq of maps with the new {:name \"string\" :glitter-index 0}"
  [suspects new-suspect]
  (if (validate validators new-suspect)
      (conj suspects new-suspect)
      (do (pprint (str "Invalid suspect: " new-suspect) *err*)
          suspects)))

(defn map->csv
  [suspects]
  (clojure.string/join "\n" (map (fn [{:keys [name glitter-index]}]
                                    (str name "," glitter-index))
                              suspects)))
  

;; Exercises:
;; 1. [X] Turn the result of your glitter filter into a list of names
;; 2. [X] Write a function, append, which will append a new suspect to your list
;;        of suspects
;; 3. [X] Write a function, validate, which will check that :name and
;;        :glitter-index are present when you append. The validate function should
;;        accept two arguments: a map of keywords to validating functions, similar
;;        to conversions, and the record to be validated.
;; 4. [X] write a function that will take your list of maps and convert it back to
;;        a CSV string. You'll need to use the clojure.string/join function.
