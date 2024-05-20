(ns locks.handlers.users
(:require [cheshire.core :as json]) 
)



;; (defn getHandler [{:keys body}] 
;;   {:status 200
;;    :body (str "Hello  form handler function")}
;;   )

(defn get-users [name] 
  (println str "this is my get users function : " name )
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string {:message (str "handler got the message by: " name)})})


(defn add-users [bd]
  
  (println str "this is my get users function : " name)
  (def *p1 bd)
  (try
    (let [body (slurp bd)
          data (json/parse-string body true) 
          {:keys [name email]} data]
      (def *p2 body)
      ;; (println (str "this is data bof request" data))
      (if (and name email)
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string {:message "User added successfully"})}
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string {:error "Missing name or email"})}))
    (catch Exception e
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string {:error "Internal server error"})})))


(defn delete-users [name]
  
  {:status 200
   :body (str "user deleted sucessfully for name = " name)})