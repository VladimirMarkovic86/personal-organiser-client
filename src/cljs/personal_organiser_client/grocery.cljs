(ns personal-organiser-client.grocery
  (:require [personal-organiser-client.ajax  :as ajx]
            [personal-organiser-client.manipulate-dom :as md]
            [cljs.reader                              :as reader])
  (:require-macros [personal-organiser-client.html-generator  :as hg]))

(hg/deftmpl nav "public/html/grocery/nav.html")
(hg/deftmpl form "public/html/grocery/form.html")
(hg/deftmpl table "public/html/grocery/table.html")
(hg/deftmpl template "public/html/grocery/template.html")

(def anim-time 100)

(defn start-please-wait
  ""
  []
  
  )

(defn end-please-wait
  ""
  []
  
  )

(defn grocery-table-data-success
  ""
  [xhr]
  (let [response       (reader/read-string (aget xhr "response"))
        grocery-header (:grocery-header response)
        groceries      (:groceries response)]
   (md/fade-in "#mainContent" (md/table-with-data grocery-header groceries) anim-time))
  (end-please-wait)
  )

(defn grocery-table-data-error
  ""
  [xhr]
  (let [response      (reader/read-string (aget xhr "response"))
        error-message (:error-message response)]
   (md/fade-in "#mainContent" (str "<div>" error-message "</div>") anim-time))
  (end-please-wait)
  )

(defn get-groceries
  ""
  [query-map]
  (start-please-wait)
  (ajx/uni-ajax-call
   {:url                  "https://personal-organiser:8443/clojure/grocery-table-data"
    :request-method       "POST"
    :success-fn           grocery-table-data-success
    :error-fn             grocery-table-data-error
    :request-header-map
     {"Accept"       "application/json"
      "Content-Type" "application/json"}
    :request-property-map
     {"responseType" "application/json"}
    :entity               query-map
    }))

(defn grocery-nav-link
  ""
  []
  (md/fade-in ".content" template anim-time)
  (md/fade-in "#sidebar" nav anim-time)
  (get-groceries {:search "all" :data-type "grocery"}))

