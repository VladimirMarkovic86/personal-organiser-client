(ns personal-organiser-client.ajax
  (:require [personal-organiser-client.manipulate-dom :as md]
            [personal-organiser-client.http.mime-type :as mt]
            [personal-organiser-client.http.request-header :as rh]
            [personal-organiser-client.http.entity-header :as eh]
            [personal-organiser-client.generate-html :refer [gen crt]]
            [cljs.reader :as reader]))

(def anim-time 100)

(defn get-response
 "Get response from XMLHttpRequest"
 [xhr]
 (try
  (reader/read-string (aget xhr "response"))
  (catch js/Error e
   (.log js/console e))
  ))

(defn ajax-error
 "Handle details error"
 [xhr]
 (let [response      (get-response xhr)
       error-message (:error-message response)]
  (md/fade-out-and-fade-in ".content"
                           anim-time
                           (gen
                            (crt "div"
                                 error-message))
   ))
 )

(defn- onload
 "Ajax onload function"
 [xhr
  params-map]
 (if (and (= (aget xhr "readyState")
             4)
          (= (aget xhr "status")
             200))
   (do (.log js/console xhr)
       ((:success-fn params-map) xhr params-map))
   (let [error-fn (:error-fn params-map)
         error-fn (if error-fn
                   error-fn
                   ajax-error)]
    (case (aget xhr "status")
     1 (.log js/console "OPENED")
     2 (.log js/console "HEADERS_RECEIVED")
     3 (.log js/console "LOADING")
     (do (.log js/console xhr)
         (error-fn xhr params-map))
     ))
   )
 (md/end-please-wait))

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
  )
 (md/end-please-wait))

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

(defn ajax
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
 :entity                  Define content that you want to send
 :entity-fn-params        In case entity is a function, define vector of it's params
                           example: [param1 param2]"
 [params-map]
 (md/start-please-wait)
 (let [xhr (js/XMLHttpRequest.)
       url (:url params-map)
       request-method (or (:request-method params-map)
                          "POST")
       request-header-map (or (:request-header-map params-map)
                              {(rh/accept)       (mt/text-plain)
                               (eh/content-type) (mt/text-plain)})
       request-property-map (or (:request-property-map params-map)
                                {"responseType" (mt/text-plain)})
       entity (:entity params-map)
       entity-fn-params (:entity-fn-params params-map)
       entity (if (fn? entity)
               (entity entity-fn-params)
               entity)]
   (aset xhr
         "onload"
         #(onload xhr params-map))
 ;    (aset xhr "onreadystatechange" onready)
 ;    (aset xhr "onprogress" onprogress)
   (.open xhr
          request-method
          url
          true)
   (doseq [[k v] request-header-map]
    (set-request-header xhr [k v]))
   (doseq [[k v] request-property-map]
    (set-request-property xhr [k v]))
   (.send xhr entity)) 
 )

