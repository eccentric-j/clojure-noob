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

(defn format-code
  [forms]
  (->> forms
       (map #(with-out-str (pprint %)))
       (map string/trim)
       (map #(format-code-lines (string/split % #"\n")))
       (string/join "\n\n")))

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
      `(do (println (str "Section " ~section-id " :: " ~title))
           (println "\n  Notes:")
           (println ~notes)
           (println "")
           (println ~code)
           (println (str "   => " (do ~@forms)))))
    (let [code (format-code forms)]
      `(do (println (str "Section " ~section-id " :: " ~title))
           (println (string/join "\n  " (quote  ~forms)))
           ; (println ~code)
           (println (str "   => " (do ~@forms)))))))

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
