(defproject org.clojars.vladimirmarkovic86/personal-organiser-client "0.2.1"
  :description "Personal ograniser client"
  :url "http://github.com/VladimirMarkovic86/personal-organiser-client"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [org.clojars.vladimirmarkovic86/ajax-lib "0.1.11"]
                 [org.clojars.vladimirmarkovic86/htmlcss-lib "0.1.6"]
                 [org.clojars.vladimirmarkovic86/js-lib "0.1.16"]
                 [org.clojars.vladimirmarkovic86/framework-lib "0.2.45"]
                 [org.clojars.vladimirmarkovic86/utils-lib "0.4.10"]
                 [org.clojars.vladimirmarkovic86/language-lib "0.2.33"]
                 [org.clojars.vladimirmarkovic86/personal-organiser-middle "0.2.10"]
                 [org.clojars.vladimirmarkovic86/common-middle "0.2.9"]
                 [org.clojars.vladimirmarkovic86/common-client "0.3.43"]
                 [org.clojars.vladimirmarkovic86/server-lib "0.3.31"]
                 [org.clojars.vladimirmarkovic86/validator-lib "0.1.34"]
                 ]

  :min-lein-version "2.0.0"
  
  :resource-paths ["resources"]
  :source-paths ["src/clj"]
  
  :main ^:skip-aot personal-organiser-client.core
  
  :uberjar-name "personal-organiser-client-standalone.jar"
  :profiles {:uberjar {:aot :all}}
  
  :plugins [[lein-cljsbuild  "1.1.7"]
            ]
  
  :cljsbuild
   {:builds
    {:dev
     {:source-paths ["src/cljs"]
      :compiler     {:main personal-organiser-client.controller
                     :output-to "resources/public/assets/js/main.js"
                     :output-dir "resources/public/assets/js/out"
                     :asset-path "assets/js/out"
                     :pretty-print true}}
     :prod
     {:source-paths ["src/cljs"]
      :compiler     {:main personal-organiser-client.controller
                     :output-to "resources/public/assets/jsprod/main.js"
                     :output-dir "resources/public/assets/jsprod/out"
                     :asset-path "assets/jsprod/out"
                     :optimizations :advanced}}
     }})

