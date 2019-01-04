(ns personal-organiser-client.grocery.html
  (:require [framework-lib.core :refer [create-entity gen-table]]
            [common-client.allowed-actions.controller :refer [allowed-actions]]
            [language-lib.core :refer [get-label]]
            [personal-organiser-client.grocery.entity :refer [table-conf-fn]]
            [personal-organiser-middle.functionalities :as pomfns]))

(defn nav
  "Returns map of menu item and it's sub items"
  []
  (when (or (contains?
              @allowed-actions
              pomfns/grocery-create)
            (contains?
              @allowed-actions
              pomfns/grocery-read))
    {:label (get-label 1009)
     :id "grocery-nav-id"
     :sub-menu [(when (contains?
                        @allowed-actions
                        pomfns/grocery-create)
                  {:label (get-label 4)
                   :id "grocery-create-nav-id"
                   :evt-fn create-entity
                   :evt-p (table-conf-fn)})
                (when (contains?
                        @allowed-actions
                        pomfns/grocery-read)
                  {:label (get-label 5)
                   :id "grocery-show-all-nav-id"
                   :evt-fn gen-table
                   :evt-p (table-conf-fn)})]}
   ))

