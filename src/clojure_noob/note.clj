(ns clojure-noob.note
  (:require [clojure.string :as string]
            [clojure.pprint :refer [pprint]]))

(defn format-note
  [[note & lines]]
  (cons note
        (->> lines
             (map #(str "     " (string/trim %))))))

(defn format-notes
  [notes]
  (->> notes
       (map #(str "   - " (string/trim %)))
       (mapcat #(format-note (string/split % #"\n")))
       (string/join "\n")))

(defn format-code-lines
  [lines]
  (->> lines
       (map #(str "  " %))
       (string/join "\n")))

(defn space-code-blocks
  [code-str block]
  (let [line-count (count (string/split block #"\n"))]
    (if (< line-count 2)
      (str code-str block "\n")
      (str code-str block "\n\n"))))

(defn format-code
  [forms]
  (->> forms
       (map #(with-out-str (pprint %)))
       (map string/trim)
       (map #(format-code-lines (string/split % #"\n")))
       (reduce space-code-blocks "")
       (string/trimr)))

(defmacro lesson
  "Render code and its output grouped as a lesson from a chapter.
  Takes a section-id number, title string, notes, and code forms.
  Returns a list of expressions to display it nicely.
  (lesson 1.1
          \"How does add work?\"
          \"Should return 2\"
          (+ 1 1))
  "
  [section-id title & forms]
  (if (= 'notes (-> forms first first))
    (let [notes (-> forms first rest (format-notes))
          forms (rest forms)
          code (format-code forms)]
      `(do (println (str "Chapter " ~section-id " :: " ~title))
           (println "\n  Notes:")
           (println ~notes)
           (println "")
           (println ~code)
           (print "   => ")
           (pr (do ~@forms))
           (println "\n")))
    (let [code (format-code forms)]
      `(do (println (str "Chapter " ~section-id " :: " ~title))
           (println ~code)
           (print "   => ")
           (pr (do ~@forms))
           (println "\n")))))

(comment
  (macroexpand
   '(lesson 8.1
          "This is a test lesson"
          (notes  "Should return 2")
          (+ 1 1)))
  (lesson 8.1
          "This is a test lesson"
          (notes  "Should return 2")
          (+ 1 1))
  (lesson 8.1
          "This is a test lesson"
          (notes "Should return 2")
          (+ 1 1)
          (+ 2 3))
  (lesson 8.1
          "This is a test lesson"
          (+ 1 1))
  (lesson 8.1
          "This is a test lesson"
          (notes  "Should return 2
                   Plus another note")
          (+ 1 1)))
