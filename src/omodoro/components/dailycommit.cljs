(ns omodoro.components.dailycommit
  (:require 
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(defn button-props [app id-name click-fn]
  #js {:id id-name
       :onClick
       (fn [e]
         (om/transact! app :commitment click-fn))})

(defn commit-widget [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
        (dom/h1 nil "Today, I am commiting to")
        (dom/button (button-props app "commit-minus" dec) "-")
        (dom/h2 nil (:commitment app))
        (dom/button (button-props app "commit-plus" inc) "+")
        (dom/h2 nil "Pomodoros")))))

