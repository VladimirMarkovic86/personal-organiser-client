(ns personal-organiser-client.meal.html
 (:require [htmlcss-lib.core :refer [gen crt]]
           [framework-lib.core :refer [create-entity gen-table]]
           [personal-organiser-client.meal.entity :refer [table-conf]]))

(defn nav
 "Generate ul HTML element
  that represents navigation menu"
 []
 (gen
  (crt "ul"
       [(crt "li"
             (crt "a"
                  "Create"
                  {:id "aCreateMealId"}
                  {:onclick {:evt-fn create-entity
                             :evt-p table-conf}}))
        (crt "li"
             (crt "a"
                  "Show all"
                  {:id "aShowAllMealsId"}
                  {:onclick {:evt-fn gen-table
                             :evt-p table-conf}})
         )]))
 )
                   
