;; Chapter 4: Core Functions in Depth

;; Iterable types are fully compatible with the seq functions map, reduce, into,
;; conj, concat, some, filter, take, drop, sort, sort-by, and identity.

;; The seq functions work interchangably with lists, vectors, sets, and maps

(defn inspect
  [arg]
  (when-not (ifn? arg)
    (print "  -> ")
    (pprint arg)
    (println "")))

(defn title
  [text]
  (print (str "\n" text "\n")))


(defn titleize
  [topic]
  (str topic " for the Brave and True"))

(defn lesson
  [text & results]
  (title (str text "\n"))
  (run! inspect results)
  nil) 

(map titleize ["Hamsters" "Ragnarok"])

(map titleize '("Empathy" "Decorating"))

(map titleize #{"Elbows" "Soap Carving"})

(map #(titleize (second %)) {:uncomfortable-thing "Winking"})

;; Does it turn maps into a list of key value pairs?

;; Yes it does!
(map (fn [[_ second]] (titleize second)) {:uncomfortable-thing "Winking"})
;; => ("Winking for the Brave and True")

;; Of note: '(hello world) seems to behave similarly to ["hello" "world"]
(reduce #(str %1 " " %2) (map str ["why " "there "] '(hello world)))
;; => "why hello there world"

;; Can join be used instead?
;; (println (join ["one" "two"]))
;; Nope. Join is not a function.

;; But they are not equal.
(= '(hello world) ["hello" "world"])
;; => false

(= '("hello" "world") ["hello" "world"])
;; => true

;; What is the type of values in (hello world) then?


;; Clojure's map can work on multiple lists kinda like Ramda's lift?
(list (str "a" "A") (str "b" "B") (str "c" "C"))
;; => ("aA" "bB" "cC")

;; Map receives another arg for each list
(map #(first %&) ["a" "b" "c"] ["A" "B" "C"])
;; => ("a" "b" "c")
(map #(second %&) ["a" "b" "c"] ["A" "B" "C"])
;; => ("A" "B" "C")

(def human-consumption [8.1 7.3 6.6 5.0])
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data
  [human critter]
  {:human human
   :critter critter})

(println (map unify-diet-data human-consumption critter-consumption))

;; A usecase of map is to supply an array of functions to run against one
;; set of data
(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(println (stats [3 4 10]))

(println (stats [80 1 44 13 6]))

(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spider-Main" :real "Peter Parker"}
   {:alias "Santa" :real "Your mom"}
   {:alais "Easter Bunny" :real "Your dad"}])

(map :real identities)

;; I guess this is why FP is not as prevalent in JS?
;; Object
;;  .entries({ max: 30, min: 10 })
;;  .reduce((newMap, [key, val]) => ({ ...newMap, [key]: val + 1 }), {})
;; Hmm I do see the appeal in clojure's syntax. Fewer symbols and very
;; consistent syntax. Pretty cool!
(println (reduce (fn [new-map [key val]]            ; reducer function
                   (assoc new-map key (inc val)))   ; sets key of val + 1 on new-map
                 {}                                 ; seed val
                 {:max 30 :min 10}))                ; list to reduce



(println (reduce (fn [new-map [key val]]
                   ; if the value of each key-value pair is 4
                   (if (> val 4)
                     ; update the new-map with the key & value
                     (assoc new-map key val)
                     new-map))
                 {}
                 {:human 4.1
                  :critter 3.9}))

(println "Take\\Drop\n")

(println (take 3 [1 2 3 4 5 6 7 8 9 10]))

;; Does take work with ratios?
;; Guess take rounds up to the nearest whole number.
;; const take = (x, xs) => xs.slice(0, x);
;; => (1 2)
(println (take 6/5 [1 2 3 4 5]))

;; const drop = (x, xs) => xs.slice(x);
(println (drop 3 [1 2 3 4 5 6 7 8 9 10]))

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

(println "Take while")

;; Take-while seems to take from the start until the predicate returns false
(pprint (take-while #(< (:month %) 3) food-journal))

(println "Drop while")

;; Skip from the beginning until the predicate returns true
(pprint (drop-while #(< (:month %) 3) food-journal))

(println "Take while + drop while")

(pprint (take-while #(< (:month %) 4)
                    (drop-while #(< (:month %) 2) food-journal)))

(title "Filter and some")

(inspect (filter #(< (:human %) 5) food-journal))

(inspect (filter #(< (:month %) 3) food-journal))

;; shouldn't this return false?
(inspect (some #(> (:critter %) 3) food-journal))
; => nil

;; Still nil
(inspect (some (fn [x] false) food-journal))
; => nil

(inspect (some #(+ (:critter %) 1) food-journal))
; => 3.3

;; Some seems to return the return value of the predicate that first returns a
;; truthy value otherwise nil. For that behavior I think there is not-every?

(inspect (some #(> (:critter %) 3) food-journal))
; => true

(inspect (some #(and (> (:critter %) 3) %) food-journal))
; => {:month 3 :day 1 :human 4.2 :critter 3.3}

(lesson "Sort and Sort-by"
        ;; sorts in ascending order
        (sort [3 1 2])
        (sort-by count ["aaa" "c" "bb"]))

(lesson "Concat"
        (concat [1 2] [3 4])
        ;; => [1 2 3 4]
        (concat [] [1 2 3 4]))
        
(def vampire-database
  {0 {:makes-blook-puns? false :has-pulse? true  :name "McFishwich"}
   1 {:makes-blood-puns? false :has-pulse? true  :name "McMackson"}
   2 {:makes-blood-puns? true  :has-pulse? false :name "Damon Salvator"}
   3 {:makes-blood-puns? true  :has-pulse? true  :name "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  ;; (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

(lesson "Lazy Seqs"
        (time (vampire-related-details 0))
        (time (def mapped-details (map vampire-related-details (range 0 1000000))))
        ;; accessing this again will be significantly faster
        ;; Also clojure will preemptively load the first 31 items which may
        ;; take more time but result in better performance
        (time (first mapped-details))
        (time (identify-vampire (range 0 1000000))))

(defn even-numbers
  ([] (even-numbers 0))
  ;; apparently lisp devs call it consing when using cons
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(lesson "Infinite Sequences"
        ;; Kinda behaves like a stream!
        (concat (take 8 (repeat "na")) ["Batman!"])
        ; => ("na" .... "na" "Batman!")
        (take 3 (repeatedly (fn [] (rand-int 10))))
        (take 10 (even-numbers)))
        

;; Sequence abstrations deal with each item in the list individually
;; where as Collection abstractions deal with the entire list

(lesson "Collection Abstraction")
(lesson "empty?"
        (empty? [])
        ; => true
        (empty? ["no!"])
        ; => false
        (empty? [nil])
        ; => false
        (empty? nil)
        ; => true
        (empty? {})
        ; => true
        (empty? [])
        ; => true
        (empty? ""))
        ; => true

;; See this one mentioned a lot in the community
;; "Adds two colletions and adds all the elements from the second to the first"
(lesson "into"
        ;; list of key value pair vectors
        (map identity {:sunlight-reaction "Glitter!"})
        ; => ([:sunlight-reaction "Glitter!"])
        ;; creates a map from list of key value pairs
        (into {} (map identity {:sunlight-reaction "Glitter!"}))
        ; => {:sunlight-reaction "Glitter!"}
        (into [] (map identity {:sunlight-reaction "Glitter!"}))
        ; => [[:sunlight-reaction "Glitter!"]]
        (map identity [:garlic :sesame-oil :fried-eggs])
        ; => (:garlic :sesame-oil :fried-eggs)
        (into [] (map identity [:garlic :sesame-oil :fried-eggs]))
        ; => [:garlic :sesame-oil :fried-eggs]
        (map identity [:garlic-clove :garlic-clove])
        ; => [:garlic-clove :garlic-clove]
        ;; Kinda like calling [1, 1, 2, 3].(dedupe, []) in JS
        (into #{} (map identity [:garlic-clove :garlic-clove]))
        ; => #{:garlic-clove}
        ;; The dest arg doesn't have to be empty
        ;; NOTE: Into concats the items at the end of the list
        (into {:favorite-emotion "gloomy"} [[:sunlight-reaction "Glitter!"]])
        ; => {:favorite-emotion "gloomy" :sunlight-reaction "Glitter!"}
        (into ["cherry"] '("pine" "spruce"))
        ; => ["cherry" "pine" "spruce"]
        ;; Of course works with same types :P
        (into {:favorite-animal "kitty"} {:last-favorite-smell "dog"
                                          :relationship-with-teenager "creepy"}))

;; Does conj adds one element to the end of a list?
(lesson "conj"
        (conj [0] [1])
        ; => [0 [1]]
        (into [0] [1])
        ; => [0 1]
        ;; Yes. It does!
        (conj [0] 1)
        ; => [0 1]
        (conj [0] 1 2 3 4)
        ; => [0 1 2 3 4]
        ;; In this case the item is a key value vector pair
        (conj {:time "midnight"} [:place "ye olde cemetarium"])
        (defn my-conj
          [target & additions]
          (into target additions))
        (my-conj [0] 1 2 3 4))

(lesson "apply"
        (max 0 1 2)
        ; => 2
        (max [0 1 2])
        ; => [0 1 2]
        ;; Does this have anything to do with lift?
        (apply max [0 1 2 3])
        ; => 3
        (defn my-into
          [target additions]
          (apply conj target additions))
        (my-into [0] [1 2 3 4]))
        ; => [0 1 2 3 4]

(lesson "partial"
        (def add10 (partial + 10))
        (add10 3)
        ; => 13
        (add10 5)
        ; => 15

        (def add-missing-elements
          ;; conj takes a list and ...args to append to the list
          (partial conj ["water" "earth" "air"]))
        (add-missing-elements "unobtanium" "adamantium")

        (defn my-partial
          [partialized-fn & args]
          ;; manual curry but for 2 variadic functions
          (fn [& more-args]
            ;; into takes two lists and adds the right list to the left list
            (apply partialized-fn (into args more-args))))
        (def add20 (my-partial + 20))
        (add20 3)
        ; => 23

        (defn lousy-logger
          [log-level message]
          ;; Whoa?! This is awesome!
          (condp = log-level
            :warn (clojure.string/lower-case message)
            :emergency (clojure.string/upper-case message)))
        (def warn (partial lousy-logger :warn))
        (warn "Red light ahead")

        (def not-vampire? (complement vampire?))
        (defn identify-humans
          [social-security-numbers]
          (filter not-vampire?
                  (map vampire-related-details social-security-numbers)))
        (identify-humans (range 0 4))
        
        (defn my-complement
          [fun]
          (fn [& args]
            (not (apply fun args))))
        
        ;; Written in book using complement, should it be my-complement?
        (def my-pos? (my-complement neg?))
        (my-pos? 1)
        ; => true
        (my-pos? -1))
        ; => false
