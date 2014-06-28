(ns omodoro.routes.four-oh-four
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn four-oh-four []
  (reify
    om/IRender
    (render [_]
      (dom/div nil "Page not found"))))

(defn init []
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (om/build four-oh-four app)))))
