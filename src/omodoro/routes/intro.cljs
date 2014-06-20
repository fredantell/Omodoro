;; This is an introductory panel
(ns omodoro.routes.intro
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omodoro.components.navchevrons :as nav]))

(defn by-id [id]
  (. js/document (getElementById "intro")))

(defn panels [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:id "intro"}
               (dom/div #js {:id "panel1" :className "panel"} "Intro Panel 1")
               (dom/div #js {:id "panel2" :className "panel"} "Intro Panel 2")
               (dom/div #js {:id "panel3" :className "panel"} "Intro Panel 3")
               (dom/div #js {:id "panel4" :className "panel"} "Intro Panel 4"))))) 

(defn nav [app owner opts]
  (reify
    om/IRender
    (render [_]
      (nav/nav-chevrons "intro"))))

(defn init [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
               (om/build panels app)
               (om/build nav app)))))
