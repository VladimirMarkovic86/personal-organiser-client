(ns personal-organiser-client.ajax)

(defn- onload
  "Ajax onload function"
  [xhr
   params-map]
  (if (and (= (aget xhr "readyState")
              4)
           (= (aget xhr "status")
              200))
    ((:success-fn params-map) xhr params-map)
    (case (aget xhr "status")
          1 (.log js/console "OPENED")
          2 (.log js/console "HEADERS_RECEIVED")
          3 (.log js/console "LOADING")
          ((:error-fn params-map) xhr params-map))
    ))

(defn- onready
  "Ajax onreadystatechange function"
  [xhr
   params-map]
  (if (and (= (aget xhr "readyState")
              4)
           (= (aget xhr "status")
              200))
    ((:success-fn params-map) xhr params-map)
    (case (aget xhr "readyState")
          1 (.log js/console "OPENED")
          2 (.log js/console "HEADERS_RECEIVED")
          3 (.log js/console "LOADING")
          ((:error-fn params-map) xhr params-map))
    ))

(defn- set-request-header
  "Set request header"
  [xhr
   [key value]]
  (.setRequestHeader xhr key value))

(defn- set-request-property
  "Set request property"
  [xhr
   [key value]]
  (aset xhr key value))

(defn uni-ajax-call
  "Universal ajax call
  
  :url                     Define url address to communicate with
  :request-method          Define request method ex. GET, POST...
  :success-fn              Specify function name which will handle success

                           example: (defn success-handler
                                      \"Simple ajax success handler\"
                                      [xhr
                                       params-map]
                                      implementation..)

  :error-fn                Specify function name which will handle error

                           example: (defn error-handler
                                      \"Simple ajax error handler\"
                                      [xhr
                                       params-map]
                                      implementation..)

  :request-header-map      Define map with key value pairs for request header
  :request-property-map    Define map with key value pairs for setting property values
  :entity                  Define content that you want to send"
  [params-map]
  (let [xhr (js/XMLHttpRequest.)]
    (aset xhr
          "onload"
          #(onload xhr params-map))
;    (aset xhr "onreadystatechange" onready)
;    (aset xhr "onprogress" onprogress)
    (.open xhr
           (:request-method params-map)
           (:url params-map)
           true)
    (doseq [[k v] (:request-header-map params-map)]
     (set-request-header xhr [k v]))
    (doseq [[k v] (:request-property-map params-map)]
     (set-request-property xhr [k v]))
    (.send xhr (:entity params-map))
    ))

