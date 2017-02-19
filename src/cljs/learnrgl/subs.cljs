(ns learnrgl.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]
              [learnrgl.db :as mydb]))

(re-frame/reg-sub
  :name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  :gsoption
  (fn [db]
    (:gsoption db)))

(re-frame/reg-sub
  :tableconfig
  (fn [db _]
    (get-in db [:tableconfig] mydb/init-tableconfig)))