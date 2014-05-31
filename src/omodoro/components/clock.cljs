(ns omodoro.components.clock
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [chan <! timeout]]))

(defn tick! [data]
  (let [tick-once! (fn []
                     (when-not (zero? (:seconds @data))
                       (om/transact! data :seconds dec)))]
    (js/setInterval tick-once! 1000)))

(defn pad [n]
  (if (<= 0 n 9)
    (str "0" n)
    (str n)))

(defn clock [seconds]
  (let [minutes (int (/ seconds 60))
        seconds (rem seconds 60)]
    (dom/p #js {:id "clock"}
           (str (pad minutes) ":" (pad seconds)))))

(defn reset-button [app owner interval]
  (dom/button #js {:onClick
                   (fn [e]
                     (om/update! app :seconds (* 25 60)))}
              "reset"))

(defn clock-widget [app owner opts]
  (reify
    om/IInitState
    (init-state [_]
      {:interval nil})
    om/IDidMount
    (did-mount [_]
      (om/set-state! :interval (tick! app)))
    om/IRenderState
    (render-state [_ {:keys [interval]}]
      (dom/div nil
        (reset-button app owner interval)
        (clock (:seconds app))))))

