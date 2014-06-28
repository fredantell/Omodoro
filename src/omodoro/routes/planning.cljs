(ns omodoro.routes.planning
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omodoro.components.menubar :as mb]))

(defn planning [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil "Planning page goes here")))) 

(defn init [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (om/build planning app)
               (om/build mb/menubar app)))))
