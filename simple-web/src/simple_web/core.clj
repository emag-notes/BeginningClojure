(ns simple-web.core
  (:use org.httpkit.server
        clojure.pprint))

(defn app [req]
  (pprint req)
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "hello HTTP Kit!"})

(defn -main []
  (run-server app {:port 8080}))