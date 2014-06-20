;; This is an introductory panel
(ns omodoro.routes.intro
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omodoro.components.navchevrons :as nav]))

(defn intro [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (nav/nav-chevrons 4)  ;; 4 -> num of panels
               (dom/div #js {:id "intro"}
                        (dom/div #js {:id "panel1" :className "panel"} "Intro Panel 1")
                        (dom/div #js {:id "panel2" :className "panel"} "Intro Panel 2")
                        (dom/div #js {:id "panel3" :className "panel"} "Intro Panel 3")
                        (dom/div #js {:id "panel4" :className "panel"} "Intro Panel 4"))))))
