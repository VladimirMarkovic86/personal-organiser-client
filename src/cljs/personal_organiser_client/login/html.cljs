(ns personal-organiser-client.login.html
  (:require [htmlcss-lib.core :refer [gen table tr td label
                                      input div a nav]]
            [ajax-lib.core :refer [ajax get-response]]
            [personal-organiser-client.grocery.controller :as gc :refer [nav-link]]
            [personal-organiser-client.meal.controller :as mc :refer [nav-link]]
            [personal-organiser-client.organism.controller :as oc :refer [nav-link]]
            [personal-organiser-client.language.controller :as lc]
            [language-lib.core :refer [get-label]]))

(defn form
  "Generate table HTML element that contains login form"
  [login-evt
   sign-up-evt]
  (gen
    (table
      [(tr
         [(td
            (label
              (get-label 14)
              {:for "txtEmailId"}))
          (td
            (input
              ""
              {:id "txtEmailId"
               :name "txtEmailN"
               :type "text"
               :required "required"}))]
        )
       (tr
         [(td
            (label
              (get-label 15)
              {:for "pswLoginId"}))
          (td
            (input
              ""
              {:id "pswLoginId"
               :name "pswLoginN"
               :type "password"
               :required "required"}))]
        )
       (tr
         [(td
            (label
              (get-label 16)
              {:for "chkRememberMeId"}))
          (td
            (input
              ""
              {:id "chkRememberMeId"
               :type "checkbox"}))]
        )
       (tr
         [(td)
          (td
            (input
              ""
              {:id "btnLoginId"
               :name "btnLoginN"
               :type "button"
               :value (get-label 17)}
              login-evt))]
        )
       (tr
         [(td)
          (td
            (a
              (get-label 18)
              {:id "aSignUpId"
               :style
                 {:float "right"}}
              sign-up-evt))
          ])
       (tr
         (td
           ""
           {:id "error-msgs"
            :colspan "2"}))]
      {:class "login"}))
 )

(defn nav-fn
  "Header navigation menu"
  [logout-fn
   username]
  (nav
    [(div
       username
       {:class "dropDownMenu"})
     (a
       (get-label 3)
       {:id "aHomeId"})
     (a
       (get-label 35)
       {:id "aGroceryId"}
       {:onclick {:evt-fn gc/nav-link}})
     (a
       (get-label 43)
       {:id "aMealId"}
       {:onclick {:evt-fn mc/nav-link}})
     (a
       "Plan ishrane"
       {:id "aPlanishraneId"})
     (a
       (get-label 52)
       {:id "aOrganismId"}
       {:onclick {:evt-fn oc/nav-link}})
     (a
       (get-label 23)
       {:id "aLanguageId"}
       {:onclick {:evt-fn lc/nav-link}})
     (a
       (get-label 2)
       {:id "aLogoutId"}
       {:onclick {:evt-fn logout-fn}})])
  )

(defn language-fn
  ""
  [change-language-fn
   language-name]
  (div
    [(div
       language-name
       {:class "languageDropDownMenu"})
     (a
       "English"
       {:id "aEnglishId"}
       {:onclick {:evt-fn change-language-fn
                  :evt-p {:language :english
                          :language-name "English"}}})
     (a
       "Srpski"
       {:id "aSerbianId"}
       {:onclick {:evt-fn change-language-fn
                  :evt-p {:language :serbian
                          :language-name "Srpski"}}
        })])
 )

(defn footer
  "Footer of main page"
  []
  [(div
     ""
     {:class "clj img"})
   (div
     ""
     {:class "lein img"})
   (div
     ""
     {:class "drools img"})
   (div
     [(div
        "made by Vladimir Marković")
      (div
        "email: markovic.vladimir86@gmail.com")]
     {:class "made-by"})])

(defn template
  "Template of main page"
  [logout-fn
   username
   change-language-fn
   language-name]
  (gen
    [(div
       [(div
          (nav-fn
            logout-fn
            username)
          {:class "dropDownMenuContainer"})
        (div
          (language-fn
            change-language-fn
            language-name)
          {:class "languageDropDownMenuContainer"})]
       {:class "header"})
     (div
       ""
       {:class "sidebar-menu"})
     (div
       ""
       {:class "content"})
     (div
       (footer)
       {:class "footer"})])
 )

