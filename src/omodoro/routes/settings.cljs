(ns omodoro.routes.settings
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omodoro.components.menubar :as mb]))

(defn settings [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil "Setting page goes here")))) 

(defn init [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (om/build settings app)
               (om/build mb/menubar app)))))
