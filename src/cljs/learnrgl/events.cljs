(ns learnrgl.events
    (:require [re-frame.core :as re-frame]
              [learnrgl.db :as mydb]))

(re-frame/reg-event-db
  :initialize-db
  (fn  [_ _]
    mydb/default-db))

(defn updatetable [tableconfig changeData]
  (let [dataTable (get-in tableconfig [:data] (:data mydb/init-tableconfig))
        newDataTable (assoc-in dataTable (subvec changeData 0 2) (js/parseFloat (nth changeData 3)))
        tableconfig (assoc-in tableconfig [:data] newDataTable)]
    tableconfig))

(re-frame/reg-event-db
  :set-tablevalue
  (fn [db [_ inchangeDatas]]
    (let [changeDatas (js->clj inchangeDatas)
          tableconfig (get-in db [:tableconfig] mydb/init-tableconfig)
          newtableconfig (reduce updatetable tableconfig changeDatas)]
      (assoc db :tableconfig newtableconfig))))