(ns omodoro.routes.analytics
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omodoro.components.menubar :as mb]))

(defn analytics [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil "Analytics page goes here")))) 

(defn init [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (om/build analytics app)
               (om/build mb/menubar app)))))
