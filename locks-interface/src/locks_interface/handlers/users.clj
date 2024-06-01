(ns locks-interface.handlers.users
  (:require [cheshire.core :as json]
            [clj-http.client :as client]))
(import [clojure.lang ExceptionInfo])


;; todo get base url from env file
;; "http://lock-manager:3000/get-lock"



(def noOfInstances 3)
(def LockingInstances ["http://lock-manager:3000/get-lock","http://lock-manager-1:3000/get-lock","http://lock-manager-2:3000/get-lock"])
(def UnlockingInstances ["http://lock-manager:3000/release-lock","http://lock-manager-1:3000/release-lock","http://lock-manager-2:3000/release-lock"])
(defn get-i-locks [turl resource duration identity]
  (println "starting locking of " turl)
  (let [url turl
        headers {"Content-Type" "application/json"
                 ;;  "Authorization" "Bearer your-token"
                 }
        body {:id "189"
              :duration duration
              :resource resource
              :identity identity}]
    (try
      (let [response (client/post url {:headers headers
                                       :body (json/generate-string body)
                                       :content-type :json
                                       :accept :json})]
        (println "finishing locking of " turl)
        {:status (:status response)
         :headers (:headers response)
         :body (:body response)})
      (catch clojure.lang.ExceptionInfo e
        (let [status (-> e ex-data :status)
              body (-> e ex-data :body)]
          (if (not= status 200)
            (do
              (println "Bad Request:" body))))))))






(defn release-i-locks [turl resource identity]
  (println "starting release of " turl)
  (let [url turl
        headers {"Content-Type" "application/json"
                 ;; "Authorization" "Bearer your-token"
                 }
        body {:id "189"
              :key "3600"
              :resource resource
              :identity identity}]
    (try
      (let [response (client/post url {:headers headers
                                       :body (json/generate-string body)
                                       :content-type :json
                                       :accept :json})]
        (println "finishing release of " turl)
        response)
      (catch clojure.lang.ExceptionInfo e
        (let [status (-> e ex-data :status)
              body (-> e ex-data :body)]
          (if (not= status 200)
            (do
              (println "Bad Request:" body))))))))
;; (comment

;;   (defn post-request [base-url]
;;   (let [url base-url
;;         headers {"Content-Type" "application/json"
;;                  ;;  "Authorization" "Bearer your-token"
;;                  }
;;         body {:id "189"
;;               :duration "85"
;;               :resource "resource1"
;;               :identity "user1"}]
;;     (try
;;       (client/post url {:headers headers
;;                         :body (cheshire.core/generate-string body)
;;                         :content-type :json
;;                         :accept :json})
;;       (catch ExceptionInfo e
;;         (if (= (:status e) 400)
;;           (throw (ex-info "Bad Request" {:body (:body e)}))
;;           (throw e))))))

  ;; (get-locks "http://lock-manager:3000/get-lock" "r1" "100" "s2")
;;   (release-locks  "r1" "s1")
;;   )

(defn lmHealth [_]
  (println "health check function called")
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string {:message "This Lock manager Interface and it is Healthy"})})

(defn giveLocks [body]
  (try
    (let [strBody (slurp body)
          data (json/parse-string strBody true)
          {:keys [duration resource identity]} data]

      (if (and duration resource identity)
        (do
          (println "starting locking......")
                    ;; (set-key-with-ttl identity resource duration)
          (def *res resource)
          (def *ide identity)
          (def *dur duration)

         (let [lock-count (atom 0)]
           (doseq [it LockingInstances]
             (if (= (:status (get-i-locks it resource duration identity)) 200)
               (swap! lock-count inc)))
         
           (let [lcc @lock-count
                 threshold (/ noOfInstances 2)]
             (println "Lock count:" lcc)
             (println "Threshold (noOfInstances / 2):" threshold)
         
             (if (> lcc threshold)
               (do
                 (println "locked")
                 {:status 200
                  :headers {"Content-Type" "application/json"}
                  :body (json/generate-string {:message "Locks granted"
                                               :duration duration
                                               :resource resource
                                               :identity identity})})
               (do
                 (println "not able to acquire more than half lock")
                 {:status 200
                  :headers {"Content-Type" "application/json"}
                  :body (json/generate-string {:message "Not able to acquire more than half lock"
                                               :duration duration
                                               :resource resource
                                               :identity identity})}))))


                    ;;---------------------------------------------------------------------------
          )
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
      (if (and  resource identity )
        (do
          (println (str "releasing locks....."))
          

          (let [ulock-count (atom 0)]
            (doseq [it UnlockingInstances]
              (if (= (:status (release-i-locks it resource  identity)) 200)
                (swap! ulock-count inc)))
          
            (if (> @ulock-count (/ noOfInstances 2))
              (do
                (println "lock released")
                {:status 200
                 :headers {"Content-Type" "application/json"}
                 :body (json/generate-string {:message "Locks released" 
                                              :resource resource
                                              :identity identity})})
              (do
                (println "not able to acquire more then half lock")
                {:status 200
                 :headers {"Content-Type" "application/json"}
                 :body (json/generate-string {:message "not able to release more then half lock" 
                                              :resource resource
                                              :identity identity})})))
          
          ;; (delete-key identity) 
          )
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string {:error "Missing required fields"})}))
    (catch Exception e
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string {:error (.getMessage e)})})))


