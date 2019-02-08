(ns primetab.core
  "Print a multiplication table of prime numbers using the sieve of Eratosthenes.

  Default behavior is to print column headers, row labels, in color,
  in TSV format."
  ;; Not unit-tested
  (:gen-class)
  (:require
   [clojure.string :as str]
   [clojure.tools.cli :as cli]
   [primetab.primes :as primes]))

(def cli-options
  "List of all command-line options.
  Used for generating `--help` text too!"
  [["-n" "--num-primes NUM" "Number of primes (default: 10)"
    :default 10
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 100) "Must be integer less than 100"]]
   ["-b" "--bland"      "Bland output (no color)"
    :default false]
   ["-r" "--raw"        "Omit column headers and row labels"
    :default false]
   ["-t" "--tsv"        "Print in TSV format"]
   ["-c" "--csv"        "Print in CSV format (bugs here!)"]
   ["-h" "--help"]])

(defn- usage
  "Generate a string representing full CLI usage.
  NOTE: no args, just options."
  [opts-summary]
  (let [usg  "Usage: primetab [options]"
        desc (-> 'primetab.core find-ns meta :doc) ; DRY from above
        more "Full online help: <http://example.com/primetab>"
        ex   "Example: primetab -n 5 -b"]
    (str/join "\n\n" [usg desc opts-summary more ex])))

(defn- error-message [errors]
  (str "Errors:" (map println errors)))

(defn- validate-args
  "Validate command-line `args`, generating help, errors, or main flow."
  [args]
  (let [{:keys [options arguments errors summary]}
        (cli/parse-opts args cli-options)]
    (cond
      ;; NOTE: help text is auto-generated from `cli-options` above.
      (:help options)           {:exit-message (usage summary), :ok? true}
      errors                    {:exit-message (error-message errors)}
      (zero? (count arguments)) {:action nil, :options options}
      :else                     {:exit-message (usage summary)})))

(defn- exit
  "Exit the app with appropriate status code indicating success or error."
  [ok? msg]
  (println msg)
  (System/exit (if ok? 0 1)))

(defn -main
  "Parse CLI args and run program."
  [& args]
  (let [{:as ctx :keys [action options exit-message ok?]}
        (validate-args args)]
    (if exit-message
      (exit ok? exit-message)
      (primes/tabulate options))))
