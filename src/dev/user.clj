(ns user
  (:require [org.httpkit.server :as http]
            [bhlie.fulcro.tutorial.server :refer [middleware]]
            [clojure.tools.namespace.repl :as tools-ns]
            [taoensso.timbre :as log]))

(tools-ns/set-refresh-dirs "src" "dev")

(defonce server (atom nil))

(defn start []
  (let [result (http/run-server middleware {:port 3000})]
    (log/info "started web server on port 3000")
    (reset! server result)
    :ok))

(defn stop []
  (when @server
    (log/info "stopped web server")
    (@server)))

(defn restart []
  (stop)
  (log/info "reloading code")
  (tools-ns/refresh :after 'user/start))

(comment
  (type @server)
  (start)
  (restart)
  )