(ns locks.routes.routes 
  
  (:require [compojure.core :refer [defroutes GET POST PUT DELETE ANY]]
            [locks.handlers.users :as wahu])
  (:import (java.time LocalDate))
  )



(defroutes app-routes 
;;   (GET "/" _ wahu/getHandler)
(POST "/add-users" {body :body} (wahu/add-users body))
  (GET "/get-users/:name" [name] (wahu/get-users name))
  (DELETE "/delete-users/:name" [name] (wahu/delete-users name)))