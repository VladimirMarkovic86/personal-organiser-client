(ns personal-organiser-client.meal.controller
  (:require [js-lib.core :as md]
            [framework-lib.core :refer [gen-table]]
            [personal-organiser-client.meal.entity :refer [table-conf]]
            [personal-organiser-client.meal.html :as mhtml]))

(defn nav-link
  "Process these functions after link is clicked in main menu"
  []
  (md/remove-element-content
    ".content")
  (md/append-element
    ".content"
    (gen-table
      table-conf))
  (md/remove-element-content
    ".sidebar-menu")
  (md/append-element
    ".sidebar-menu"
    (mhtml/nav))
 )
