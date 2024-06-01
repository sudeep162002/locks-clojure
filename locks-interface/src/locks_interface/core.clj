(ns locks-interface.core
  (:gen-class)
   (:require [ring.adapter.jetty :as raj]
             [ring.middleware.params :as rmp]
             [ring.middleware.keyword-params :as rmkp]
             [ring.middleware.format :refer [wrap-restful-format]]
             [locks-interface.routes.routes :as rty]))
  
  (defonce server (atom nil))
  
  (defn start-server []
    (reset! server
            (raj/run-jetty (-> rty/app-routes
                               rmkp/wrap-keyword-params
                               rmp/wrap-params
                               wrap-restful-format)
                           {:port 4000
                            :join? false}))
    (println "Server started on port 4000"))
  
  (defn stop-server []
    (when-let [s @server]
      (.stop s)
      (reset! server nil)
      (println "Server stopped")))
  
  (defn restart-server []
    (stop-server)
    (start-server)
    (println "Server restarted"))
  
  (defn -main
    [& _]
    (start-server))
