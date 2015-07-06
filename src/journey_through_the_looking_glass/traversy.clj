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

(defn employees-with-role
  "A lens focusing on employees of a specified role."
  [role]
  (*> (in [:employees])
      (only #(-> % :role (= role)))))

(defn raise
  "Calculates a new salary from the old one and a percentage."
  [salary percentage]
  (* salary (+ 1 (/ percentage 100))))

(-> bank
    (update
      (*> (employees-with-role :customer-assistant) (in [:salary]))
      #(raise % 3))
    pprint/pprint)
