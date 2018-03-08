(ns personal-organiser-client.meal.controller
  (:require [personal-organiser-client.manipulate-dom :as md]
            [personal-organiser-client.display-data :refer [table]]
            [personal-organiser-client.meal.entity :refer [table-conf]]
            [personal-organiser-client.meal.html :as mhtml]))


(def anim-time 100)

(defn nav-link
  "Process these functions after link is clicked in main menu"
  []
  (md/fade-out ".content"
               anim-time
               "content"
               true)
  (md/fade-out ".sidebar-menu"
               anim-time
               "sidebar-menu"
               true)
  (md/timeout #(do (md/fade-in ".sidebar-menu"
                               (mhtml/nav)
                               anim-time)
                   (table [table-conf]))
              anim-time))

