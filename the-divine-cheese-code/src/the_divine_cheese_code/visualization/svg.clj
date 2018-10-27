(ns the-divine-cheese-code.visualization.svg
  (:require [clojure.string :as s])
  (:refer-clojure :exclude [min max]))


(defn comparator-over-maps
  "Takes a comparison function that takes multiple args and a list of keys.
  Returns a function that takes a list of maps returns a map with the minimum
  for each key.

  (def min (comparator-over-amps clojure.core/min [:a :b]))
  (min [{:a 1 :b 3} {:a 5 :b 0}])
  ;; => {:a 1 :b 0}
  "
  [comparison-fn ks]
  (fn [maps]
    (zipmap ks
            (map (fn [k] (apply comparison-fn (map k maps)))
                 ks))))

;; Book uses (min [{:a 1 :b 3} {:a 5 :b 0}]) as the test function but this
;; raises a NullPointerException because min and max are expecting :lat and :lng
;; instead of :a and :b
;; (min [{:lat 1 :lng 3} {:lat 5 :lng 0}])
;; => {:lat 1 :lng 0}

(def min (comparator-over-maps clojure.core/min [:lat :lng]))
(def max (comparator-over-maps clojure.core/max [:lat :lng]))

(defn translate-to-00
  "Takes a collection of maps {:lat ## :lng ##} and returns a map with a
  relative difference between minimum coords in the collection.
  Returns a collection of maps with relative coords."
  [locations]
  (let [mincoords (min locations)]
    (map #(merge-with - % mincoords) locations)))

(defn scale
  "Takes canvas width, height and a collection of {:lat ## :lng ##} maps with
  relative coordinates.
  Returns a collection of relative coordinates scaled to best fit the canvas."
  [width height locations]
  (let [maxcoords (max locations)
        ratio {:lat (/ height (:lat maxcoords))
               :lng (/ width (:lng maxcoords))}]
    (map #(merge-with * % ratio) locations)))

(defn latlng->point
  "Convert lat/lng map to comma-separated string.
  Takes a map of {:lat ## :lng ##} and returns a string of pairs like
  `lat,lng`
  "
  [latlng]
  (str (:lng latlng) "," (:lat latlng)))

(defn points
  "Takes a collection of coordinate maps like {:lat ## :lng ##} and returns
  string of space separated coordinate pairs."
  [locations]
  (clojure.string/join " " (map latlng->point locations)))

(defn line
  "Takes a string of space separated coordinate pairs and returns an svg
  polyline element string."
  [points]
  (str "<polyline points=\"" points "\" />"))

(defn transform
  "Just chains other functions. Takes canvas width, height, and absolute
  coordinates like {:lat ## :lng ##} and returns a list of coordinates
  with relative measurements scaled to best fit the canvas size."
  [width height locations]
  (->> locations
       translate-to-00
       (scale width height)))

(defn xml
  "SVG 'template', which also flips the coordinate system.
  Takes canvas width, height, and a list of absolute coordinate pairs like
  {:lat ## :lng ##}.
  Returns a SVG string with a line mapping the locations."
  [width height locations]
  (str "<svg height=\"" height "\" width=\"" width "\">"
       ;; These two <g> tags change the coodrinate system so that
       ;; 0,0 is in the lower-left corner, instead of SVG's default
       ;; upper-left corner
       "<g transform=\"translate(0, " height ")\">"
       "<g transform=\"rotate(-90)\">"
       (-> (transform width height locations)
           points
           line)
       "</g></g>"
       "</svg>"))
