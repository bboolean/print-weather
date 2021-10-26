(ns print-weather.core
  (:gen-class)
  (:require
   [clojure.data.json :as json]
   [clj-http.client :as client]
   [java-time :as jt]))

(defn red [a]
  (str "\033[31m" a "\033[0m"))

(defn blue [a]
  (str "\033[34m" a "\033[0m"))

(defn magenta [a]
  (str "\033[35m" a "\033[0m"))

(defn yellow [a]
  (str "\033[33m" a "\033[0m"))

(defn get-api [lat lon]
  (client/get (str "https://www.7timer.info/bin/api.pl?product=astro&lat=" lat " &lon=" lon "&ac=0&lang=en&unit=british&tzshift=0&output=json")))

(defn get-time [data]
  (java-time/plus
   (java-time/local-date-time)
   (java-time/hours (- (get data :timepoint) 6))))

(defn color-cond [a]
  (cond
    (> a 20) red
    (> a 10) yellow
    (> a 5) blue
    :else magenta))

(defn to-fahrenheit [a]
  (Math/round (+ (* a 1.8) 32)))

(defn rain [rain]
  (if (= "rain" rain) "🌧" ""))

(defn temp [temp]
  (str (to-fahrenheit temp) " °F"))

(defn new-day-newline [time]
  (if (<
       (Integer/parseInt (java-time/format "H" time))
       3)
    "\n"
    ""))

(defn timestamp [time]
  (-> (java-time/format "E hh:00 a" time)
      (clojure.string/replace #"AM" "am")
      (clojure.string/replace #"PM" "pm")))

(defn temp-hashes [temp]
  (apply
   (color-cond temp)
   [(clojure.string/join "" (map (fn [a] "#") (range 0 temp)))]))

(defn make-line [data]
  (str
   (new-day-newline (get-time data))

   (timestamp (get-time data))

   "  " (temp-hashes (get data :temp2m))

   " " (temp (get data :temp2m))

   " " (rain (get data :rain))

   "\n"))

(defn get-data [lat lon]
  (as-> (get-api lat lon) $
    (get $ :body)
    (json/read-str $ :key-fn keyword)
    (get $ :dataseries)
    (drop 1 $)
    (map make-line $)
    (clojure.string/join "" $)
    (str "\n" $)))

(defn -main
  "Print the weather for a specific latitude and longitude"
  [& args]
  (println (get-data (first args) (second args))))
