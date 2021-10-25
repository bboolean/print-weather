(ns print-weather.core
  (:gen-class)
  (:require
   [clojure.data.json :as json]
   [clj-http.client :as client]
   [clj-time.core :as t]
   [clj-time.local :as l]
   [clj-time.format :as f]
   [java-time :as jt]))

(defn get-api [lat lon]
  (client/get "https://www.7timer.info/bin/api.pl?product=astro&lat=!&lon=!&ac=0&lang=en&unit=british&tzshift=0&output=json"))

(def body (get (get-api 1 2) :body))

(def data (drop 1 (get-in (json/read-str body :key-fn keyword) [:dataseries])))

(defn get-time [data]
  (java-time/plus
   (java-time/local-date-time)
   (java-time/hours (- (get data :timepoint) 6))))

(defn red [a]
  (str "\033[31m" a "\033[0m"))

(defn blue [a]
  (str "\033[34m" a "\033[0m"))

(defn magenta [a]
  (str "\033[35m" a "\033[0m"))

(defn yellow [a]
  (str "\033[33m" a "\033[0m"))

(defn color-cond [a]
  (cond
    (> a 20) red
    (> a 10) yellow
    (> a 5) blue
    :else magenta))

(defn to-f [a]
  (+ (* a 1.8) 32))

(defn frmat [data]
  (str

   (if (<
        (Integer/parseInt
         (java-time/format "H"
                           (get-time data))) 3) "\n" "")

   (clojure.string/replace
    (clojure.string/replace

     (java-time/format "E hh:00 a" (get-time data))
     #"AM"
     "am")
    #"PM"
    (str "pm"))

   "  "
   (apply
    (color-cond (get data :temp2m))
    [(clojure.string/join "" (map (fn [a] "#") (range 0 (get data :temp2m))))])
   " " (Math/round (to-f  (get data :temp2m))) " °F"
   " " (if (= "rain" (get data :rain)) "🌧" "") "\n"))

(defn -main
  "Print out the weather"
  [& args]
  (println (str "\n" (clojure.string/join "" (map frmat data)))))
