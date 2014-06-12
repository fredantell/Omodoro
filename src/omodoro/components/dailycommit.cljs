(ns omodoro.components.dailycommit
  (:require 
   [om.core :as om :include-macros true]
   [om.dom :as dom :include-macros true]))

(defn button-props [app fa-class id-name click-fn]
  #js {:id id-name
       :className (str "commitButton fa " fa-class)
       :onClick
       (fn [e]
         (om/transact! app :commitment click-fn))})

(defn commit-widget [app owner opts]
  (reify
    om/IRender
    (render [_]
      (dom/div nil
        (dom/h2 nil "Today, I am commiting to")
        (dom/div nil
          (dom/i (button-props app "fa-minus-square" "commit-minus" dec))
          (dom/h2 nil (:commitment app))
          (dom/i (button-props app "fa-plus-square" "commit-plus" inc)))
        (dom/h2 nil "Pomodoros")
        (dom/button #js {:onClick (fn [e]
                                    (om/update! app :committed? true))}
                    "COMMIT!")))))

         (dom/i #js {:className (str "fa " icon-class)})
