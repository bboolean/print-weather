(defproject print-weather "0.1.0-SNAPSHOT"
  :description "Print the weather for a specific latitude and longitude."
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "2.4.0"]
                 [clj-http "3.12.3"]
                 [clojure.java-time "0.3.3"]]
  :main ^:skip-aot print-weather.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
