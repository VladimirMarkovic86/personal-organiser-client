(ns personal-organiser-client.login.controller
 (:require [personal-organiser-client.ajax :refer [ajax get-response]]
           [personal-organiser-client.manipulate-dom :as md]
           [personal-organiser-client.login.html :as lhtml]))

(def anim-time 100)

(def login-url "/clojure/login")

(defn remove-main
 "Remove main page from HTML document"
 []
 (md/fade-out ".header" anim-time)
 (md/fade-out ".sidebar-menu" anim-time)
 (md/fade-out ".content" anim-time)
 (md/fade-out ".footer" anim-time))

(defn set-cookie
 "Set cookie in browser"
 [cookie-value]
 (aset js/document "cookie" cookie-value))

(defn destroy-session-cookie
 "Destroy session cookie"
 []
 (set-cookie (str "session=destroyed; "
                  "expires=Thu, 01 Jan 1970 00:00:01 GMT; "))
 )

(defn main-page
 "Open main page"
 [xhr
  {logout-fn :logout-fn}]
 (md/fade-in ".body"
             (lhtml/template logout-fn)
             anim-time))

(defn login-success
 "Login success"
 [xhr
  ajax-params]
 (md/fade-out "table.login" anim-time)
 (main-page xhr
            ajax-params))

(defn login-error
 "Login error"
 [xhr]
 (let [response           (get-response xhr)
       email              (md/get-by-id "txtEmailId")
       password           (md/get-by-id "pswLoginId")]
      (md/remove-class email "error")
      (md/remove-class password "error")
      (md/remove-class email "success")
      (md/remove-class password "success")
      (md/add-class email (:email response))
      (md/add-class password (:password response))
  ))

(defn read-login-form
 "Read data from login form"
 []
 (let [email    (md/get-by-id "txtEmailId")
       password (md/get-by-id "pswLoginId")]
  {:email      (md/get-value email)
   :password   (md/get-value password)}))

(defn redirect-to-login
 "Redirect to login page"
 [logout-fn]
 (md/fade-in ".body"
             (lhtml/form
              {:onclick {:evt-fn ajax
                         :evt-p {:url login-url
                                 :success-fn login-success
                                 :error-fn login-error
                                 :entity read-login-form
                                 :logout-fn logout-fn}}
               })
             anim-time))

(defn logout
 "Logout"
 [& optional]
 (remove-main)
 (destroy-session-cookie)
 (redirect-to-login logout))

