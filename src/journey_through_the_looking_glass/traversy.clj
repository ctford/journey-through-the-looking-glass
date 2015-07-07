(ns journey-through-the-looking-glass.traversy
  (:require [traversy.lens
             :refer [update view in each only *>]]
            [midje.sweet :refer :all]
            [clojure.pprint :as pprint]))

;;;;;;;;;;;;
; Traversy ;
;;;;;;;;;;;;


(def bank
  {:name "Smithson's"
   :address {:line-1 "Duckett Building"
             :line-2 "22 Welcome Road"
             :postcode "N7 8SE"}
   :employees [{:name "Susan Tushabe"
                :role :manager
                :salary 35000}
               {:name "Alexandra D'Souza"
                :role :customer-assistant
                :salary 22000}
               {:name "Jason Allen"
                :role :customer-assistant
                :salary 21000}
               {:name "Wei Wang"
                :role :security-guard
                :salary 20000}]})

(defn raise-customer-assistant-salaries
  "Raise customer assistant salaries by the specified percentage."
  [organisation percentage]
  (update-in organisation [:employees]
             (fn [employees]
               (map
                 (fn [employee]
                   (if (= (:role employee) :customer-assistant)
                     (update-in employee [:salary] #(raise % percentage))
                     employee))
                 employees))))

(comment
  (-> bank
      (raise-customer-assistant-salaries 3)
      pprint/pprint))


(def customer-assistant-salaries
  "A lens focusing on customer assistant salaries."
  (*> (in [:employees])
      (only #(= :customer-assistant (:role %)))
      (in [:salary])))

(defn raise
  "Calculate a new salary from the old one and a percentage."
  [salary percentage]
  (* salary (+ 1 (/ percentage 100))))

(comment
  (-> bank
      (update customer-assistant-salaries #(raise % 3))
      pprint/pprint))
