(ns personal-organiser-client.html
  (:require [htmlcss-lib.core :refer [a]]
            [personal-organiser-middle.functionalities :as pomfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [personal-organiser-client.grocery.controller :as gc]
            [personal-organiser-client.meal.controller :as mc]
            [personal-organiser-client.organism.controller :as oc]
            [language-lib.core :refer [get-label]]))

(defn custom-menu
  "Render menu items for user that have privilege for them"
  []
  [(when (contains?
           @allowed-actions
           pomfns/grocery-read)
     (a
       (get-label 1009)
       {:id "aGroceryId"}
       {:onclick {:evt-fn gc/nav-link}}))
   (when (contains?
           @allowed-actions
           pomfns/meal-read)
     (a
       (get-label 1017)
       {:id "aMealId"}
       {:onclick {:evt-fn mc/nav-link}}))
   (a
     "Plan ishrane"
     {:id "aPlanishraneId"})
   (when (contains?
           @allowed-actions
           pomfns/organism-read)
     (a
       (get-label 1026)
       {:id "aOrganismId"}
       {:onclick {:evt-fn oc/nav-link}}))]
 )

