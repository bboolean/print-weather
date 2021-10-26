(ns print-weather.core-test
  (:require [clojure.test :refer :all]
            [print-weather.core :as core]))

(deftest color-cond-test
  (testing "color-cond"
    (is (=
         core/magenta
         (core/color-cond 0)))
    (is (=
         core/blue
         (core/color-cond 5)))
    (is (=
         core/blue
         (core/color-cond 6)))
    (is (=
         core/yellow
         (core/color-cond 12)))
    (is (=
         core/red
         (core/color-cond 23)))))

(deftest to-fahrenheit-test
  (testing "to-fahrenheit"
    (is (=
         32
         (core/to-fahrenheit 0)))
    (is (=
         212
         (core/to-fahrenheit 100)))
    (is (=
         50
         (core/to-fahrenheit 10)))))

(deftest temp-test
  (testing "temp"
    (is (=
         "32 °F"
         (core/temp 0)))))

(deftest new-day-newline-test
  (testing "new-day-newline"
    (is (=
         "\n"
         (core/new-day-newline
          (java-time/local-date-time 2000 2))))
    (is (=
         ""
         (core/new-day-newline
          (java-time/local-date-time 2000 2 1 5 0))))))

(deftest timestamp-test
  (testing "timestamp"
    (is (=
         "Tue 12:00 am"
         (core/timestamp
          (java-time/local-date-time 2000 2))))))

(deftest temp-hashes-test
  (testing "temp-hashes"
    (is (=
         (core/blue "##########")
         (core/temp-hashes 10)))))
