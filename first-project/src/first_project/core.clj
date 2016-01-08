(ns first-project.core
  (:gen-class))

(defn parse-int [str]
  (Integer/parseInt str))

(defn -main [& args]
  (println "Sum:" (reduce + (map parse-int args))))

