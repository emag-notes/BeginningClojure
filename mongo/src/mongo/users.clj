(ns mongo.users
  (:require [monger.core :as mg]
            [monger.collection :as collection]
            [monger.operators :refer :all]
            [monger.result :as result]
            [crypto.password.bcrypt :as password]))

(defonce db
  (let [conn (mg/connect)]
    (mg/get-db conn "monger-test")))

(defn add-user! [email password]
  (collection/insert db "users"
                     {:_id        email
                      :password   (password/encrypt password)
                      :created-at (java.util.Date.)}))

(defn users []
  (let [users (collection/find-maps db "users")]
    (map #(dissoc % :password) users)))

(defn authentication [email password]
  (if-let [user (collection/find-one-as-map db "users" {:_id email})]
    (if (password/check password (:password user))
      (dissoc user :password))))

(defn change-password! [email old-password new-password]
  (if-let [user (authentication email old-password)]
    (result/updated-existing?
      (collection/update db "users"
                         {:id (:_id user)}
                         {$set {:password (password/encrypt new-password)
                                :updated-at (java.util.Date.)}}))))