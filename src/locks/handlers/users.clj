(ns locks.handlers.users
  (:require [cheshire.core :as json])
  (:import (redis.clients.jedis Jedis JedisPool JedisPoolConfig)))


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

;; (def pool (JedisPool. (JedisPoolConfig.) "redis-container" 6379))
(def REDIS_HOST (System/getenv "REDIS_HOST"))  ; Get Redis host from environment variable

(println "Redis Host:" REDIS_HOST)

(def pool (JedisPool.
           (JedisPoolConfig.)  ; Default Jedis pool configuration
           REDIS_HOST          ; Use the retrieved host
           6379                ; Port remains 6379
           ))




(defn get-connection []
  (.getResource pool))



(defn set-key-with-ttl [key value ttl]
  (with-open [jedis (get-connection)]
    (.setex jedis key (int ttl) value)))


;; Function to set a key-value pair in Redis
(defn set-key [key value]
  (with-open [jedis (get-connection)]
    (.set jedis key value)))

;; Function to get a value by key from Redis
(defn get-value [key]
  (with-open [jedis (get-connection)]
    (.get jedis key)))


;; Function to delete a key from Redis
(defn delete-key [key]
  (with-open [jedis (get-connection)]
    (.del jedis key)))

(comment
  (set-key "foo2" "bar2")
  (set-key-with-ttl "foo34" "bar34" 5)
  (println "Value for 'foo':" (get-value "foo34"))
  
  )


;; Example usage:
;; (set-key "foo" "bar")
;; (println "Value for 'foo':" (get-value "foo"))

(defn lmHealth [_]
  (println "health check function called")
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string {:message "This Lock manager is Healthy"})})




;; key-value pair woudl be like

;; identity-> resource
;; resource->identity
(defn giveLocks [body]
  (try
    (let [strBody (slurp body)
          data (json/parse-string strBody true)
          {:keys [id duration resource identity]} data ]
      
      (if (and id duration resource identity)
        (if (not (nil? (get-value resource)))
          {:status 400
           :headers {"Content-Type" "application/json"}
           :body (json/generate-string {:message (str "Lock already exists on resource " resource "  by " (get-value resource) ". Please wait, we don't want conflicts.")
                                        })}
          (do
            (println "starting locking......")
            ;; (set-key-with-ttl identity resource duration)
            (def *res resource)
            (def *ide identity)
            (def *dur duration)
            (set-key-with-ttl resource identity (Integer/parseInt duration))
            (println "locked")
            {:status 200
             :headers {"Content-Type" "application/json"}
             :body (json/generate-string {:message "Locks granted"
                                          :id id
                                          :duration duration
                                          :resource resource
                                          :identity identity})}))
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string {:error "Missing required fields"})}))
    (catch Exception e
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string e)})))


(defn releaseLocks [body]
  (try
    (let [strBody (slurp body)
          data (json/parse-string strBody true)
          {:keys [id resource identity key]} data]
      (if (and id resource identity key)
        (do
          (println (str "releasing locks.....")) 
          (delete-key resource)
          ;; (delete-key identity)
          (println (str "lock released"))
          {:status 200
           :headers {"Content-Type" "application/json"}
           :body (json/generate-string {:message "Locks Released"
                                        :id id
                                        :key key
                                        :resource resource
                                        :identity identity})})
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string {:error "Missing required fields"})}))
    (catch Exception e
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string {:error (.getMessage e)})})))


