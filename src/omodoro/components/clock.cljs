(ns omodoro.components.clock
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

#_(defn commit-widget [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil "I'm a clock widget"))))

(defn commit-widget [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
        (dom/h1 nil "Today, I am commiting to")
        (dom/button nil "-")
        (dom/h2 nil "0")
        (dom/button nil "+")
        (dom/div nil "Pomodoros")))))
