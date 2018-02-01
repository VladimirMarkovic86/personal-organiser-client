(ns personal-organiser-client.core
  (:require [personal-organiser-client.ajax           :as ajx]
            [personal-organiser-client.grocery        :as gr]
            [personal-organiser-client.manipulate-dom :as md]
            [cljs.reader                              :as reader])
  (:require-macros [personal-organiser-client.html-generator  :as hg]))

;; BEGIN login

(def anim-time 500)

(defn remove-main
  ""
  []
  (md/fade-out ".header" anim-time)
  (md/fade-out ".content" anim-time)
  (md/fade-out ".footer" anim-time))

(defn set-cookie
  ""
  [cookie-value]
  (aset js/document "cookie" cookie-value))

(defn destroy-session-cookie
  "Destroy session cookie"
  []
  (set-cookie (str "session=destroyed; "
                   "expires=Thu, 01 Jan 1970 00:00:01 GMT; "))
  )

(defn login-success
  "Login success"
  [xhr]
  (md/fade-out "table.login" anim-time)
  (open-main-page))

(defn login-error
  "Login error"
  [xhr
   params-map]
  (let [resp               (reader/read-string (aget xhr "response"))
        email              (md/get-by-id "txtEmailId")
        password           (md/get-by-id "pswLoginId")]
       (md/remove-class email "error")
       (md/remove-class password "error")
       (md/remove-class email "success")
       (md/remove-class password "success")
       (md/add-class email (:email resp))
       (md/add-class password (:password resp))
   ))

; document.getElementById("MyElement").classList.add('MyClass');

; document.getElementById("MyElement").classList.remove('MyClass');

; if ( document.getElementById("MyElement").classList.contains('MyClass') )

; document.getElementById("MyElement").classList.toggle('MyClass');

(defn read-login-form
  "Read data from login form"
  []
  (let [email    (md/get-by-id "txtEmailId")
        password (md/get-by-id "pswLoginId")]
    {:email      (md/get-value email)
     :password   (md/get-value password)}))

(hg/deftmpl login-form "public/html/login/form.html")

(defn redirect-to-login
  ""
  []
  (md/fade-in "body" login-form anim-time)
  (md/event "#btnLoginId"
            "click"
            #(ajx/uni-ajax-call
              {:url                   "https://personal-organiser:8443/clojure/login"
               :request-method        "POST"
               :success-fn            login-success
               :error-fn              login-error
               :request-header-map
                {"Accept"        "application/json"
                 "Content-Type"  "application/json"}
               :request-property-map
                {"responseType"  "application/json"}
               :entity                (read-login-form)
                }))
  )

;; END login
;; BEGIN main

(defn logout
  "Logout"
  []
  (remove-main)
  (destroy-session-cookie)
  (redirect-to-login))

(hg/deftmpl template "public/html/main/template.html")
(hg/deftmpl nav "public/html/main/nav.html")
(hg/deftmpl footer "public/html/main/footer.html")

(defn open-main-page
  ""
  []
  (md/fade-in "body" template anim-time)
  (md/fade-in ".header" nav anim-time)
  (md/fade-in ".footer" footer anim-time)
  (md/event "#aGroceryId" "click" #(gr/grocery-display))
  (md/event "#aLogoutId" "click" #(logout))
  )

;; END main

(defn am-i-logged-in
  "Check if session is active"
  []
  (ajx/uni-ajax-call
   {:url                  "https://personal-organiser:8443/clojure/am-i-logged-in"
    :request-method       "POST"
    :success-fn           open-main-page
    :error-fn             redirect-to-login
    :request-header-map
     {"Accept"       "application/json"
      "Content-Type" "application/json"}
    :request-property-map
     {"responseType" "application/json"}
    :entity               {:user "it's me"}
    }))

(set! (.-onload js/window) am-i-logged-in)

