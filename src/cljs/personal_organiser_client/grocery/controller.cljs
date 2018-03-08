(ns personal-organiser-client.grocery.controller
  (:require [personal-organiser-client.manipulate-dom :as md]
            [personal-organiser-client.display-data :refer [table]]
            [personal-organiser-client.grocery.entity :refer [table-conf]]
            [personal-organiser-client.grocery.html :as ghtml]))


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
                               (ghtml/nav)
                               anim-time)
                   (table table-conf))
              anim-time))

