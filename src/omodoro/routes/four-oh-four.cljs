(ns omodoro.routes.four-oh-four
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn four-oh-four [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil "Page not found"))))
