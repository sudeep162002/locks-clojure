(ns locks.handlers.users
(:require [cheshire.core :as json]) 
)



;; (defn getHandler [{:keys body}] 
;;   {:status 200
;;    :body (str "Hello  form handler function")}
;;   )

;; (defn get-users [name] 
;;   (println str "this is my get users function : " name )
;;   {:status 200
;;    :headers {"Content-Type" "application/json"}
;;    :body (json/generate-string {:message (str "handler got the message by: " name)})})


;; (defn add-users [bd]
  
  ;; (println str "this is my get users function : " name)
  ;; (def *p1 bd)
  ;; (try
  ;;   (let [body (slurp bd)
  ;;         data (json/parse-string body true) 
  ;;         {:keys [name email]} data]
  ;;     (def *p2 body)
  ;;     ;; (println (str "this is data bof request" data))
  ;;     (if (and name email)
  ;;       {:status 200
  ;;        :headers {"Content-Type" "application/json"}
  ;;        :body (json/generate-string {:message "User added successfully"})}
  ;;       {:status 400
  ;;        :headers {"Content-Type" "application/json"}
  ;;        :body (json/generate-string {:error "Missing name or email"})}))
  ;;   (catch Exception e
  ;;     {:status 500
  ;;      :headers {"Content-Type" "application/json"}
  ;;      :body (json/generate-string {:error "Internal server error"})})))


;; (defn delete-users [name]
  
;;   {:status 200
;;    :body (str "user deleted sucessfully for name = " name)})


(defn lmHealth [_]
  (println "health check function called")
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string {:message "This Lock manager is Healthy"})})


(defn giveLocks [body]
  (try
    (let [strBody (slurp body)
          data (json/parse-string strBody true)
          {:keys [id duration resource identity]} data]
      (if (and id duration resource identity)
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string {:message "Locks granted"
                                      :id id
                                      :duration duration
                                      :resource resource
                                      :identity identity})}
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string {:error "Missing required fields"})}))
    (catch Exception e
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string {:error (.getMessage e)})})))




(defn releaseLocks [body]
  (try
    (let [strBody (slurp body)
          data (json/parse-string strBody true)
          {:keys [id resource identity key]} data]
      (if (and id resource identity key)
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string {:message "Locks Released"
                                      :id id
                                      :key key
                                      :resource resource
                                      :identity identity})}
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string {:error "Missing required fields"})}))
    (catch Exception e
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string {:error (.getMessage e)})})))