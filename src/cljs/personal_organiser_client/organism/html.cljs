(ns personal-organiser-client.organism.html
  (:require [htmlcss-lib.core :refer [gen ul li a]]
            [framework-lib.core :refer [create-entity gen-table]]
            [personal-organiser-client.organism.entity :refer [table-conf]]
            [language-lib.core :refer [get-label]]))

(defn nav
  "Generate ul HTML element
   that represents navigation menu"
  []
  (gen
    (ul
      [(li
         (a
           (get-label 4)
           {:id "aCreateOrganismId"}
           {:onclick {:evt-fn create-entity
                      :evt-p table-conf}}))
       (li
         (a
           (get-label 5)
           {:id "aShowAllOrganismsId"}
           {:onclick {:evt-fn gen-table
                      :evt-p table-conf}}))]
     ))
 )

