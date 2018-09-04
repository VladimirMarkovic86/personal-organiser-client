(ns personal-organiser-client.organism.html
  (:require [htmlcss-lib.core :refer [gen div a]]
            [framework-lib.core :refer [create-entity gen-table]]
            [personal-organiser-client.organism.entity :refer [table-conf-fn]]
            [personal-organiser-middle.functionalities :as pomfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [language-lib.core :refer [get-label]]))

(defn nav
  "Generate ul HTML element
   that represents navigation menu"
  []
  (gen
    [(when (contains?
             @allowed-actions
             pomfns/organism-create)
       (div
         (a
           (get-label 4)
           {:id "aCreateOrganismId"}
           {:onclick {:evt-fn create-entity
                      :evt-p (table-conf-fn)}})
        ))
     (when (contains?
             @allowed-actions
             pomfns/organism-read)
       (div
         (a
           (get-label 5)
           {:id "aShowAllOrganismsId"}
           {:onclick {:evt-fn gen-table
                      :evt-p (table-conf-fn)}})
        ))]
   ))

