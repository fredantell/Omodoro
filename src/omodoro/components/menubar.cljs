;;Create menu widget with
;;plan / work / review / settings

(ns omodoro.components.menubar
  (:require
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(def menu-items
  [""])
(defn menu-item [id fa-icon]
  (dom/div nil
          (dom/i #js {:id id
                      :className (str "navItem fa " fa-icon)})))

(defn menubar [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/nav #js {:id "menubar"}
               (menu-item "plan" "fa-pencil-square-o")
               (menu-item "work" "fa-clock-o")
               (menu-item "review" "fa-bar-chart-o")
               (menu-item "settings" "fa-cog")))))

;; Possible fa icons for navbar
;; fa-clock-o
;; fa-cog
;; fa-pencil fa-pencil-square fa-pencil-square-o fa-list-ol
;; fa-bar-chart-o fa-tachometer

