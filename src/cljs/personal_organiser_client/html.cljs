(ns personal-organiser-client.html
  (:require [htmlcss-lib.core :refer [h2 p div]]
            [personal-organiser-middle.functionalities :as pomfns]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [personal-organiser-client.grocery.html :as gh]
            [personal-organiser-client.meal.html :as mh]
            [personal-organiser-client.organism.html :as oh]
            [personal-organiser-client.meal-recommendation.html :as mrh]
            [language-lib.core :refer [get-label]]))

(defn home-page-content
  "Home page content"
  []
  [(div
     [(h2
        (get-label 62))
      (p
        (get-label 63))
      ]
     {:class "row-1-4"})
   (div
     [(div
        nil
        {:class "col-1-4"})
      (div
        nil
        {:class "col-2-4 logo-hi-res"})
      (div
        nil
        {:class "col-1-4"})
      ]
     {:class "row-2-4"})
   (div
     nil
     {:class "row-1-4"})
   ])

(defn custom-menu
  "Render menu items for user that have privilege for them"
  []
  [(gh/nav)
   (mh/nav)
   (oh/nav)
   (mrh/nav)
   ])

