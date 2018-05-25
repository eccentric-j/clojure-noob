;; Chapter 3: Clojure Crash Course
(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  "Returns a right-* for every left-* body part"
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

(defn hit
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

;; Ch. 3 Excersizes:

;; Exercise 3
;; Write a function, dec-maker, that works exactly like the function inc-maker
;; except with subtraction:
;; (def dec9 (dec-maker 9))
;; (dec9 10)
;;; => 1
(defn dec-maker
  "Expects a number that it can subtract from any subsequent number"
  [x]
  #(- % x))

;; Exercise 4
;; Write a function, mapset, that works like map except the return value
;; is a set.
;; (mapset inc [1 1 2 2])
;;; => #{2 3}
(defn mapset
  "Expects a fn to apply to each item in vector and returns a set"
  [map-fn values]
  (set (map map-fn values)))

;; Exercise 5
;; Create a function that's similar to symmetrize-body-parts except that it has
;; to work with weird space aliens with radial symmetry. Instead of two eyes,
;; arms, legs, and so on, they have five.

;; Solution 1: It works but feels kinda verbose
(defn matching-penta-parts
  "Expects a hobbit part and generates 5 limbs"
  [part]
  (loop [index 1
         new-parts []]
    (if (> index 5)
      new-parts
      (recur (inc index)
        (conj new-parts { :name (str
                                  (clojure.string/replace (:name part) #"^left-" "")
                                  (str "-" index))
                          :size (:size part)})))))

;; Solution 2: Much better!
(defn better-matching-penta-parts
  "Expects a hobbit part and generates 5 limbs"
  [part]
  (if (nil? (re-find #"^left-" (:name part)))
    [part]
    (map (fn [pre]
           {:name (clojure.string/replace (:name part) #"^left-" (str pre  "-"))
            :size (:size part)})
         ["first" "second" "third" "fourth" "fifth"])))

(defn penta-parts
  "Expects a seq of maps that have :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts
                  (set (better-matching-penta-parts part))))
          []
          asym-body-parts))

;; Exercise 6

;; Solution 1:
;; Is there a simple way to generate a list of numbers in a range?

(defn make-limbs
  "Expects a hobbit part and limb count and generates x limbs"
  [limbs part]
  (loop [index 1
         new-parts []]
    (if (> index limbs)
      new-parts
      (recur (inc index)
        (conj new-parts { :name (str (clojure.string/replace (:name part) #"^left-" "")
                                  (str "-" index))
                          :size (:size part)})))))

;; Solution 2:
;; Turns out there is!
(defn better-make-limbs
  "Expects a number and a map with :name and :size"
  [limbs {:keys [name size] :as part}]
  (if (nil? (re-find #"^left-" name))
    [part]
    (map (fn [part-number]
           {:name (str (clojure.string/replace name #"^left-" "")
                       (str "-" part-number))
            :size size})
         (range 1 (inc limbs)))))


(defn generate-body-parts
  "Expects a number and a seq of maps that have :name and :size"
  [limbs asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts
                  (set (better-make-limbs limbs part))))
          []
          asym-body-parts))

(println "LOADED `hobbit.clj`")

