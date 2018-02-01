(ns personal-organiser-client.grocery
  (:require [personal-organiser-client.ajax  :as ajx]
            [personal-organiser-client.manipulate-dom :as md])
  (:require-macros [personal-organiser-client.html-generator  :as hg]))

(hg/deftmpl nav "public/html/grocery/nav.html")
(hg/deftmpl form "public/html/grocery/form.html")
(hg/deftmpl table "public/html/grocery/table.html")
(hg/deftmpl template "public/html/grocery/template.html")

(def anim-time 300)

(defn grocery-display
  ""
  []
  (md/fade-in ".content" template anim-time)
  (md/fade-in "#sidebar" nav anim-time)
  (md/fade-in "#mainContent" form anim-time)
  (md/fade-in "#mainContent" table anim-time))

