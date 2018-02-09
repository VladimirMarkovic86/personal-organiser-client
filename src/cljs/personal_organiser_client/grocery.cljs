(ns personal-organiser-client.grocery
  (:require [personal-organiser-client.ajax                :as ajx]
            [personal-organiser-client.manipulate-dom      :as md]
            [personal-organiser-client.http.mime-type      :as mt]
            [personal-organiser-client.http.request-header :as rh]
            [personal-organiser-client.http.entity-header  :as eh])
  (:require-macros [personal-organiser-client.html-generator  :as hg]))

(hg/deftmpl nav "public/html/grocery/nav.html")
(hg/deftmpl form "public/html/grocery/form.html")
(hg/deftmpl table "public/html/grocery/table.html")
(hg/deftmpl template "public/html/grocery/template.html")

(def anim-time 300)

(defn grocery-update-success
  ""
  [xhr]
  (grocery-nav-link))

(defn grocery-update-error
  ""
  [xhr]
  (grocery-nav-link))

(defn update-grocery
  ""
  []
  (let [input-name           (md/query-selector "#txtName")
        input-calories       (md/query-selector "#numCalories")
        input-fats           (md/query-selector "#numFats")
        input-proteins       (md/query-selector "#numProteins")
        input-carbonhydrates (md/query-selector "#numCarbohydrates")
        input-water          (md/query-selector "#numWater")
        textarea-description (md/query-selector "#taDescription")
        radio-all            (md/query-selector "#rOriginAll")
        radio-vegetarian     (md/query-selector "#rOriginVegetarian")]
   (ajx/uni-ajax-call
    {:url              "https://personal-organiser:8443/clojure/update-grocery"
     :request-method   "POST"
     :success-fn       grocery-update-success
     :error-fn         grocery-update-error
     :request-header-map
      {(rh/accept)       (mt/text-plain)
       (eh/content-type) (mt/text-plain)}
     :request-property-map
      {"responseType"    (mt/text-plain)}
     :entity
      {:gname          (md/get-value input-name)
       :calories       (md/get-value input-calories)
       :fats           (md/get-value input-fats)
       :proteins       (md/get-value input-proteins)
       :carbonhydrates (md/get-value input-carbonhydrates)
       :water          (md/get-value input-water)
       :description    (md/get-value textarea-description)
       :origin         (md/checked-value "rOrigin")}}))
  )

(defn fill-out-grocery-form
  ""
  [grocery]
  (let [html-form            (first (md/parse-html form))
        input-name           (md/query-selector-on-element html-form "#txtName")
        input-calories       (md/query-selector-on-element html-form "#numCalories")
        input-fats           (md/query-selector-on-element html-form "#numFats")
        input-proteins       (md/query-selector-on-element html-form "#numProteins")
        input-carbonhydrates (md/query-selector-on-element html-form "#numCarbohydrates")
        input-water          (md/query-selector-on-element html-form "#numWater")
        radio-all            (md/query-selector-on-element html-form "#rOriginAll")
        radio-vegetarian     (md/query-selector-on-element html-form "#rOriginVegetarian")
        textarea-description (md/query-selector-on-element html-form "#taDescription")
        input-submit         (md/query-selector-on-element html-form "#btnSubmit")]
   (md/set-value input-name (:gname grocery))
   (md/set-value input-calories (:calories grocery))
   (md/set-value input-fats (:fats grocery))
   (md/set-value input-proteins (:proteins grocery))
   (md/set-value input-carbonhydrates (:carbonhydrates grocery))
   (md/set-value input-water (:water grocery))
   (if (= "All" (:origin grocery))
    (md/set-attr radio-all "checked" true)
    (md/set-attr radio-vegetarian "checked" true))
   (md/set-value textarea-description (:description grocery))
   (md/set-value input-submit "Update")
   (md/event input-submit "click" (fn [sl-node] (update-grocery))
    )
   html-form))

(defn grocery-edit-success
  "Handle grocery edit success"
  [xhr]
  (let [response  (ajx/get-response xhr)
        grocery   (:grocery response)]
   (md/fade-out "#mainContent"
                anim-time
                "mainContent"
                true)
   (md/timeout #(md/fade-in "#mainContent"
                            (fill-out-grocery-form grocery)
                            anim-time)
               anim-time))
  )

(defn grocery-edit-error
  "Handle grocery edit error"
  [xhr]
  (let [response      (ajx/get-response xhr)
        error-message (:error-message response)]
   (md/fade-in "#mainContent"
               (str "<div>" error-message "</div>")
               anim-time))
  )

(defn edit-grocery
  ""
  [sl-node]
  (ajx/uni-ajax-call
   {:url              "https://personal-organiser:8443/clojure/get-grocery-by-name"
    :request-method   "POST"
    :success-fn       grocery-edit-success
    :error-fn         grocery-edit-error
    :request-header-map
     {(rh/accept)       (mt/text-plain)
      (eh/content-type) (mt/text-plain)}
    :request-property-map
     {"responseType"    (mt/text-plain)}
    :entity           {:search    {:gname (md/get-attr sl-node "edit-id")}
                       :data-type "grocery"}}))

(defn grocery-to-vector
  ""
  [grocery
   grocery-header]
  (let [grocery-vector      (atom [])
        grocery-header-keys (into [] (map keys grocery-header))]
   (doseq [grocery-header-key grocery-header-keys]
    (let [grocery-header-key-unwrapped (first grocery-header-key)]
     (if (= :option grocery-header-key-unwrapped)
      (swap! grocery-vector conj (str "<a"
                                      " edit-id=\""
                                      (:gname grocery)
                                      "\" >edit</a>")
                                 (str "<a"
                                      " delete-id=\""
                                      (:gname grocery)
                                      "\" >delete</a>"))
      (swap! grocery-vector conj (grocery-header-key-unwrapped grocery))
      ))
    )
   @grocery-vector))

(defn conj-grocery
  ""
  [accumulation
   grocery-as-map
   grocery-header]
  (conj accumulation
        (grocery-to-vector grocery-as-map
                           grocery-header))
  )

(defn groceries-as-vectors
  ""
  [groceries-as-map
   grocery-header]
  (reduce (fn [accumulation
               grocery-as-map]
           (conj-grocery accumulation
                         grocery-as-map
                         grocery-header)) [] groceries-as-map))

(defn grocery-table-data-success
  "Handle table generation success"
  [xhr]
  (let [response         (ajx/get-response xhr)
        grocery-header   (:grocery-header response)
        grocery-columns  (:grocery-columns response)
        groceries        (groceries-as-vectors (:groceries response)
                                               grocery-header)]
   (md/fade-in "#mainContent" (md/table-with-data grocery-header
                                                  groceries
                                                  [["class"  "groceries"]])
                              anim-time)
   (md/event "a[edit-id]" "click" (fn [sl-node] (edit-grocery sl-node))
    ))
  )

(defn grocery-table-data-error
  "Handle table generation error"
  [xhr]
  (let [response      (ajx/get-response xhr)
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
     {(rh/accept)       (mt/text-plain)
      (eh/content-type) (mt/text-plain)}
    :request-property-map
     {"responseType" (mt/text-plain)}
    :entity               query-map}))

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
  (md/event "#aCreateGroceryId" "click" (fn [sl-node] (create-grocery-form))
   )
  (get-groceries {:search    {}
                  :data-type "grocery"}))

