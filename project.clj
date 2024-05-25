(defproject locks "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring "1.9.1"]
                 [ring/ring-jetty-adapter "1.9.1"]
                 [compojure "1.6.1"]
                ;;  [com.taoensso/carmine "3.2.0"] ;; Updated to version 3.3.2
                 [cheshire "5.9.0"]
                ;;  [clj-redis "2.1.0"]
                 [redis.clients/jedis "3.7.1"]
                  ;; [com.taoensso/encore "3.112.0"]
                 [ring-middleware-format "0.7.4"]
                 [org.xerial/sqlite-jdbc "3.30.1"]]
  :main ^:skip-aot locks.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
