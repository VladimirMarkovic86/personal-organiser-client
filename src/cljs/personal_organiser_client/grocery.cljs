(ns personal-organiser-client.grocery
  (:require [personal-organiser-client.ajax                :as ajx]
            [personal-organiser-client.manipulate-dom      :as md]
            [personal-organiser-client.display-data        :as dd]
            [personal-organiser-client.http.mime-type      :as mt]
            [personal-organiser-client.http.request-header :as rh]
            [personal-organiser-client.http.entity-header  :as eh]
            [personal-organiser-client.grocery-entity      :as ge])
  (:require-macros [personal-organiser-client.html-generator  :as hg]))

(hg/deftmpl nav "public/html/grocery/nav.html")

(def anim-time 100)

(def table-conf
     {:table-conf              ge/table-grocery-conf
      :header-and-cell-styles  ge/header-and-cell-styles
      :render-in               ".content"
      :animation               md/fade-in
      :animation-duration      anim-time
      ;:details-conf            ge/grocery-conf
      :edit-conf               ge/grocery-entity
      ;:delete-conf             ge/delete-grocery-conf
      :pagination              true
      :row-number              3
      :table-class             "groceries"})

(defn grocery-nav-link
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
  (md/timeout #(do (md/fade-in ".sidebar-menu" nav anim-time)
                   (md/event "#aCreateGroceryId"
                             "onclick"
                             dd/create-entity
                             [table-conf])
                   (md/event "#aShowAllGroceriesId"
                             "onclick"
                             dd/table
                             [table-conf])
                   (dd/table [table-conf]))
              anim-time))

