(ns doll-smuggler.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string]
            [clojure.java.io :as io])
  (:gen-class))


;;;; Structure definition
(defstruct item :name :weight :value)


;;;; Command-line processing

;;; Define command-line options
(def cli-options
  [["-h" "--help"]
   ["-d" "--dolls INPUT" "Dolls input file"]
   ["-w" "--max-weight WEIGHT" "Maximum weight"]])

(defn usage
  "Display usage information"
  [options-summary]
  (->> ["doll-smuggler: Maximize the total street value of drugs delivered"
        ""
        "Usage: doll-smuggler [options]"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn error-msg
  "Display error message information"
  [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit
  "Display message, then exit"
  [status msg]
  (println msg)
  (System/exit status))  


;;;; Number-handling utilities

(defn is-number?
  "Indicate via Boolean value whether given argument is a number"
  [possible-number]
  (if (= (count (re-seq #"^[0-9.,]+$" possible-number)) 1) true false))

(defn ->number
  "Perform input to number conversion if possible. If input is a number,
  simply return it, otherwise attempt to parse number from string. Return
  nil upon failure."
  [possible-number]
  (if (is-number? possible-number) (read-string possible-number) nil))


;;;; Input handling

(defn prepare-doll
  "Convert doll weights and values to numbers for application to item structure."
  [doll]
  [(get doll 0) (->number (get doll 1)) (->number (get doll 2))])

(defn read-dolls
  "Read doll data from file"
  [string]
  (map #(clojure.string/split % #"\ +")
    (clojure.string/split string #"\n")))

(defn get-dolls
  "Get dolls from input file"
  [filename]
  (let [doll-data (read-dolls (slurp filename))]
    (vec (map #(apply struct item (prepare-doll %)) doll-data))))


;;;; Maximum value calulation

;; Note: the following function was adapted from
;; [http://rosettacode.org/wiki/Knapsack_problem/0-1#Clojure] under the GNU FDL 1.2
;;
;; To help in understanding of the algorithm, variables names have been updated to
;; be more descriptive.
(defn calculate-maximum-value
  "Calculate maximum value from item set based on maximum weight"
  [items item-index maximum-weight]
  
  (cond
    (< item-index 0) [0 []]
    (= maximum-weight 0) [0 []]
    :else
    (let [{item-weight :weight item-value :value} (get items item-index)]
      (if (> item-weight maximum-weight)
        (calculate-maximum-value items (dec item-index) maximum-weight)
        (let [[value-not-accepted set-not-accepted :as not-accepted] (calculate-maximum-value items (dec item-index) maximum-weight)
          [value-accepted set-accepted :as accepted] (calculate-maximum-value items (dec item-index) (- maximum-weight item-weight))]
          (if (> (+ value-accepted item-value) value-not-accepted)
            [(+ value-accepted item-value) (conj set-accepted item-index)]
            not-accepted))))))

(defn accepted-dolls
  "Reduce the set of dolls to those which were accepted"
  [dolls indexes]
  (map (vec dolls) indexes))

(defn doll-smuggler
  "Determine maximum value given a set of dolls and a maximum weight
  This function is called from the main function (for command-line usage)
  and can be called from any other function (i.e. within test code). This
  function does not handle any display of results as that it dependant
  upon mode of usage."
  [dolls max-weight]
  
  (if (not= (count dolls) 0)
    (let [[value indexes] (calculate-maximum-value dolls (-> dolls count dec) max-weight)]
    (accepted-dolls dolls indexes))))


;;;; Output handling
 
(defn printable-header
  "Prepare field header for display"
  []
  (str "packed dolls:\n\n"
       "name    weight value"))

(defn printable-doll
  "Prepare single doll for display"
  [doll]
  (format "%-8s %4d  %4d" (:name doll) (:weight doll) (:value doll)))

(defn printable-dolls
  "Display the set of dolls for display"
  [dolls]
  (string/join "\n" (map printable-doll dolls)))

(defn -main
  "Main entry point (for command-line use)"
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    ;; Handle help and error conditions  
    (cond
      (:help options) (exit 0 (usage summary))
      (not= (count options) 2) (exit 1 (usage summary))
      (not (.exists (io/file (:dolls options)))) (exit 1 (println "Specified input file is not found:" (:dolls options)))
      (not (is-number? (:max-weight options))) (exit 1 (println "Specified max-weight is not a number:" (:max-weight options)))
      errors (exit 1 (error-msg errors)))
    
    ;; Execute program with options
    (println (printable-header))
    (let [dolls (get-dolls (:dolls options))]
      (let [accepted-dolls (doll-smuggler dolls (->number (:max-weight options)))]
        (println (printable-dolls accepted-dolls))))))
