(ns learnrgl.events
    (:require [re-frame.core :as re-frame]
              [learnrgl.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))
