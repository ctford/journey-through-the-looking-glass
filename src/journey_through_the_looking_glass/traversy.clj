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
                :grade :manager
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

(def customer-assistant-salaries
  "A lens focusing on customer assistant salaries."
  (*> (in [:employees])
      (only #(-> % :role (= :customer-assistant)))
      (in [:salary])))

(defn raise
  "Calculate a new salary from the old one and a percentage."
  [salary percentage]
  (* salary (+ 1 (/ percentage 100))))

(-> bank
    (update customer-assistant-salaries #(raise % 3))
    pprint/pprint)


(defn raise-customer-assistant-salaries
  "Raise customer assistant salaries by the specified percentage."
  [organisation percentage]
  (update-in organisation [:employees]
             (partial map
                      (fn [employee]
                        (if (= (:role employee) :customer-assistant)
                          (update-in employee [:salary] #(raise % percentage))
                          employee)))))

(-> bank
    (raise-customer-assistants 3)
    pprint/pprint)
