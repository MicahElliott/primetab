(defproject primetab "0.1.0-SNAPSHOT"
  :description "Print a multiplication table of prime numbers using the sieve of Eratosthenes"
  :url "http://github.com/MicahElliott/primetab"
  :license {:name "EPL-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [io.aviso/pretty "0.1.37"]
                 [org.clojure/tools.cli "0.4.1"]]
  :main ^:skip-aot primetab.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :test-selectors {:default (complement :integration)
                   :all     (constantly true)}
  :plugins [[lein-codox "0.10.5"]])
