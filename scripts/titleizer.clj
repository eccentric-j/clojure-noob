(ns titleizer
  (:require [clojure.string :refer [capitalize]])
  (:use [clojure.string :rename {split str-split} :only [split]]
        clojure.repl :reload))

(def char-list " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")

(defn split
  "Returns a vector of characters by using a separator pattern to split a string"
  [sep s]
  (str-split s sep))

(defn valid-char?
  "Returns true if character is found in char-list"
  [c]
  (clojure.string/includes? char-list (str c)))

(defn titleize
  "Returns a title formatted string. abc-def-2_ig.0 > Abc Def 2"
  [title]
  (->> title
    (split #"-")
    (map capitalize)
    (map #(str % " "))
    (mapcat vec)
    (take-while valid-char?)
    (clojure.string/join "")))


(titleize "functional-programming-a-pragpub-anthology_p1_0")
(titleize "domain-modeling-made-functional_b5_0")
