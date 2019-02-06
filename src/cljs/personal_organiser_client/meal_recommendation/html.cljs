(ns personal-organiser-client.meal-recommendation.html
  (:require [language-lib.core :refer [get-label]]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [personal-organiser-middle.functionalities :as pomfns]
            [personal-organiser-client.meal-recommendation.view :as mrv]))

(defn nav
  "Returns map of menu item and it's sub items"
  []
  (when (and (contains?
               @allowed-actions
               pomfns/organism-read)
             (contains?
               @allowed-actions
               pomfns/meal-recommendation))
    {:label (get-label
              1069)
     :id "meal-recommendation-nav-id"
     :evt-fn mrv/meal-recommendation-pure-html}))

