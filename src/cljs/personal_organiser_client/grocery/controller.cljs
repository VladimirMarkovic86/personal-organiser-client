(ns personal-organiser-client.grocery.controller
  (:require [js-lib.core :as md]
            [framework-lib.core :refer [gen-table]]
            [personal-organiser-client.grocery.entity :refer [table-conf-fn]]
            [personal-organiser-client.grocery.html :as ghtml]))

(defn nav-link
  "Process these functions after link is clicked in main menu"
  []
  (md/remove-element-content
    ".content")
  (md/append-element
    ".content"
    (gen-table
      (table-conf-fn)))
  (md/remove-element-content
    ".sidebar-menu")
  (md/append-element
    ".sidebar-menu"
    (ghtml/nav))
 )

