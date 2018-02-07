(ns personal-organiser-client.grocery
  (:require [personal-organiser-client.ajax           :as ajx]
            [personal-organiser-client.manipulate-dom :as md]
            [cljs.reader                              :as reader])
  (:require-macros [personal-organiser-client.html-generator  :as hg]))

(hg/deftmpl nav "public/html/grocery/nav.html")
(hg/deftmpl form "public/html/grocery/form.html")
(hg/deftmpl table "public/html/grocery/table.html")
(hg/deftmpl template "public/html/grocery/template.html")

(def anim-time 300)

(defn grocery-table-data-success
  "Handle table generation success"
  [xhr]
  (let [response         (reader/read-string (aget xhr "response"))
        grocery-header   (:grocery-header response)
        grocery-columns  (:grocery-columns response)
        groceries        (:groceries response)]
   (md/fade-in "#mainContent" (md/table-with-data grocery-header
                                                  grocery-columns
                                                  groceries
                                                  [["class"  "groceries"]])
                              anim-time))
  )

(defn grocery-table-data-error
  "Handle table generation error"
  [xhr]
  (let [response      (reader/read-string (aget xhr "response"))
        error-message (:error-message response)]
   (md/fade-in "#mainContent" (str "<div>" error-message "</div>") anim-time))
  )

(defn get-groceries
  "Get data about groceries from server"
  [query-map]
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

(defn create-grocery-form
  "Render from for grocery creation"
  []
  (md/fade-out "#mainContent"
               anim-time
               "mainContent"
               true)
  (md/timeout #(md/fade-in "#mainContent" form anim-time)
              anim-time))

(defn grocery-nav-link
  "Process these functions after link is clicked in main menu"
  []
  (md/inner-html ".content" "")
  (md/fade-in ".content" template anim-time)
  (md/fade-in "#sidebar" nav anim-time)
  (md/event "#aCreateGroceryId" "click" #(create-grocery-form))
  (get-groceries {:search "all" :data-type "grocery"}))

