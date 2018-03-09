(ns personal-organiser-client.grocery.html
 (:require [personal-organiser-client.generate-html :refer [gen crt]]
           [personal-organiser-client.display-data :refer [create-entity table]]
           [personal-organiser-client.grocery.entity :refer [table-conf]]))

(defn nav
 "Generate ul HTML element
  that represents navigation menu"
 []
 (gen
  (crt "ul"
       [(crt "li"
             (crt "a"
                  "Create"
                  {:id "aCreateGroceryId"}
                  {:onclick {:evt-fn create-entity
                             :evt-p table-conf}}))
        (crt "li"
             (crt "a"
                  "Show all"
                  {:id "aShowAllGroceriesId"}
                  {:onclick {:evt-fn table
                             :evt-p table-conf}})
         )]))
 )
