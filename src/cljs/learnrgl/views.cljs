(ns learnrgl.views
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]))

(def layout
  [
   {:i "a" :x 0 :y 0 :w 4 :h 8 :minW 4 :maxw 8 :minHeight 8}
   {:i "b" :x 1 :y 0 :w 4 :h 8 :minW 4 :maxW 8}
   {:i "c" :x 4 :y 0 :w 4 :h 8 :minW 4 :maxw 8}])

(def ReactGridLayout (reagent/adapt-react-class (aget js/window "deps" "rgl")))

(defn mylayout []
  [ReactGridLayout {:class "layout"
                    :layout layout
                    :cols 12
                    :rowHeight 30
                    :width 1200}
   [:div {:key "a" :style {:background-color "red"}} "a"]
   [:div {:key "b" :style {:background-color "green"}} "b"]
   [:div {:key "c" :style {:background-color "blue"}} "c"]])

(defn main-panel []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [:div  "Hello from " @name
       [mylayout]])))
