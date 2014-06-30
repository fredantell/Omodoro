;;Create menu widget with
;;plan / work / review / settings

(ns omodoro.components.menubar
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(defn menu-item [route id fa-icon]
  (dom/div nil
          (dom/i #js {:id id
                      :className (str "navItem fa " fa-icon)
                      :onClick #(aset js/window "location" "hash" route)})))

(defn menubar [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/nav #js {:id "menubar"}
               (menu-item "#planning" "plan" "fa-pencil-square-o")
               (menu-item "#timer" "work" "fa-clock-o")
               (menu-item "#analytics" "review" "fa-bar-chart-o")
               (menu-item "#settings" "settings" "fa-cog")))))

;; Possible fa icons for navbar
;; fa-clock-o
;; fa-cog
;; fa-pencil fa-pencil-square fa-pencil-square-o fa-list-ol
;; fa-bar-chart-o fa-tachometer

