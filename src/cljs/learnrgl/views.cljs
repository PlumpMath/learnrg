(ns learnrgl.views
    (:require [reagent.core :as reagent]
              [re-frame.core :refer [subscribe dispatch]]))


(defn gen-chart-config-handson
  [tableconfig]
  (let [ret (reagent/atom {
                           :title    {:text "Historic World Population by Region"}
                           :subtitle {:text "Source: Wikipedia.org"}
                           :xAxis    {:categories ["Africa" "America" "Asia" "Europe" "Oceania"]
                                      :title      {:text nil}}
                           :yAxis    {:min       0
                                      :title     {:text  "Population (millions)"
                                                  :align "high"}
                                      :labels    {:overflow "justify"}
                                      :plotLines [{
                                                   :value 0
                                                   :width 1}]}
                           :tooltip  {:valueSuffix " millions"}
                           :legend   {:layout        "vertical"
                                      :align         "left"
                                      :verticalAlign "middle"
                                      :shadow        false}
                           :credits  {:enabled false}})]
    (let [tabledata (:data tableconfig)
          categories (vec (rest (:colHeaders tableconfig)))
          mydata (reduce (fn [outdata input] (conj outdata (assoc {} :name (str (first input)) :data (vec (rest input))))) [] tabledata)]
      (println mydata)
      (swap! ret assoc-in [:xAxis :categories] categories)
      (swap! ret assoc-in [:series] mydata))
    ret))

(defn sampleTable-render [this]
  [:div {:style {:height "100%" :width "100%" :position "relative"}}])

(defn sampleTable-did-mount [this]
  (let [[_ tableconfig] (reagent/argv this)
        tableconfigext (assoc-in tableconfig [:afterChange] #(dispatch [:set-tablevalue %]))]
    (do
      (js/Handsontable (reagent/dom-node this) (clj->js tableconfigext)))))

(defn sampleTable [tableconfig]
  (reagent/create-class {:reagent-render      sampleTable-render
                         :component-did-mount sampleTable-did-mount}))

(defn sampleHighchart-render []
  [:div  {:style {:height "100%" :width "100%" :position "relative"}}])

;(defn sampleHighchart-render []
;  [:div  {:style {:height 300 :width 300}}])

(defn sampleHighchart-did-mount [this]
  (let [[_ tableconfig gsoption] (reagent/argv this)
        my-chart-config (gen-chart-config-handson tableconfig)]
    (do
      (js/Highcharts.Chart. (reagent/dom-node this) (clj->js @my-chart-config)))))


(defn sampleHighchart-did-update [this]
  (let [[_ tableconfig gsoption] (reagent/argv this)
        my-chart-config (gen-chart-config-handson tableconfig)]
    (do
      (js/Highcharts.Chart. (reagent/dom-node this) (clj->js @my-chart-config)))))


(defn sampleHighchart [tableconfig]
  (reagent/create-class {:reagent-render      sampleHighchart-render
                         :component-did-mount sampleHighchart-did-mount
                         :component-did-update sampleHighchart-did-update}))

(def RGL (aget js/window "deps" "rgl"))
(def ReactGridLayout (reagent/adapt-react-class RGL))
(def ResponsiveReactGridLayout (reagent/adapt-react-class (RGL.WidthProvider RGL.Responsive)))

(def layout
  [
   {:i "a" :x 0 :y 0 :w 2 :h 2}
   {:i "b" :x 2 :y 0 :w 2 :h 2}
   {:i "c" :x 4 :y 0 :w 2 :h 2}])

;(defn mylayout [tableconfig]
;  [ReactGridLayout {:class "layout"
;                    :layout layout
;                    :cols 12
;                    :rowHeight 30
;                    :width 1200
;                    :isResizable true
;                    :style {:background-color "lightgrey"}}
;   [:div {:key "a" } "a"
;    [sampleTable tableconfig]]
;   [:div {:key "b" } "b"
;    [sampleHighchart tableconfig]]
;   [:div {:key "c" :style {:background-color "blue"}} "c"]])

;; Example 0
(defn generateHelper [i]
  (let [y (+ (.ceil js/Math (* (rand) 4)) 1)]
    {:i (str i)
     :x (mod (* (rand-int 6) 2) 12)
     :y (* (.floor js/Math (/ i 6.0)) y)
     :w 2
     :h y}))

(defn generateLayout []
  (mapv generateHelper (take 25 (range))))

(defn generateDom [layout]
  (for [item layout]
    [:div {:key (:i item) :style {:background-color "green"}}]))

(defn example0 []
  [:div
   [:button "Generate New Layout"]
   ;[:div (str (generateLayout))]
   ;[:div (str layout)]
   ;[:div (str (generateDom (generateLayout)))]
   [ResponsiveReactGridLayout {:class "layout"
                               :layouts {:lg (generateLayout)}
                               :breakpoints {:lg 1200 :md 996 :sm 768 :xs 480 :xxs 0}
                               :cols {:lg 12 :md 10 :sm 6 :xs 4 :xxs 2}
                               :measureBeforeMount false
                               :style {:background-color "lightgrey"}}
    [:div {:key "0" :style {:background-color "green"}}]
    [:div {:key "1" :style {:background-color "green"}}]
    [:div {:key "2" :style {:background-color "green"}}]]])


(defn mylayout [tableconfig]
  [ResponsiveReactGridLayout {:class "layout"
                              :layouts {:lg (generateLayout)}
                              :breakpoints {:lg 1200 :md 996 :sm 768 :xs 480 :xxs 0}
                              :cols {:lg 12 :md 10 :sm 6 :xs 4 :xxs 2}
                              :currentBreakpoint "lg"
                              :isResizable true
                              :style {:background-color "lightgrey"}}
   [:div {:key "0" :style {:background-color "white"}}
    [sampleTable tableconfig]]
   [:div {:key "1"}
    [sampleHighchart tableconfig]]
   [:div {:key "2" :style {:background-color "blue"}} "c"]])


(defn main-panel []
  (let [name (subscribe [:name])
        tableconfig (subscribe[:tableconfig])]
    (fn []
      [:div  "Hello from " @name
       [example0 @tableconfig]])))
