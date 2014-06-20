(ns omodoro.components.navchevrons
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))


(defn scroll [el magnitude min max]
  (let [empty (empty?  (.. el -style -left))
        cur-pos (if empty 0 (.. el -style -left))
        new-pos-raw (+ (js/parseInt cur-pos) magnitude)
        constrain (cond
                   (< new-pos-raw min) min
                   (> new-pos-raw max) max
                   :else new-pos-raw)
        new-pos (str constrain "px")]
    (js/console.log "min: " max)
    (aset el "style" "left" new-pos)))

(defn nav-chevrons [num-panels]
  (let [width 320
        min (* (- num-panels 1) -320)
        max 0]
      (dom/div #js {:className "navChevronContainer"}
               (dom/i #js {:className (str "navChevron fa fa-chevron-left")
                           :onClick (fn [e]
                                      (let [intro (. js/document (getElementById "intro"))]
                                        (scroll intro width min max)))})
               (dom/i #js {:className (str "navChevron fa fa-chevron-right")
                           :onClick (fn [e]
                                      (let [intro (. js/document (getElementById "intro"))]
                                        (scroll intro (* -1 width) min max)))})
               (. js/console log "Hi!"))))
