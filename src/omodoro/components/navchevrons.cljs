(ns omodoro.components.navchevrons
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))


(defn by-id [id]
  (. js/document (getElementById "intro")))

(defn scroll [id direction]
  (let [el (by-id id)
        dir (if (= direction "left") 1 -1)
        vp-width (.. js/document -documentElement -clientWidth)
        cont-width (. el -offsetWidth)
        panels (/ cont-width vp-width)
        min (* (- panels 1) (* vp-width -1))
        max 0
        empty (empty?  (.. el -style -left))
        cur-pos (if empty 0 (.. el -style -left))
        new-pos-raw (+ (js/parseInt cur-pos) (* vp-width dir))
        constrain (cond
                   (< new-pos-raw min) min
                   (> new-pos-raw max) max
                   :else new-pos-raw)
        new-pos (str constrain "px")]
    #_(js/console.log "min: " max
                    "\nel: " el
                    "\nvp-width:" vp-width
                    "\ncont-width:" cont-width
                    "\npanels:" panels
                    "\nmin:" min
                    "\nmax:" max
                    "\nempty:" empty
                    "\ncur-pos:" cur-pos
                    "\nnew-pos-raw:" new-pos-raw
                    "\nconstrain:" constrain
                    "\nnew-pos:" new-pos)
    (aset el "style" "left" new-pos)))

(defn nav-chevrons [container-id]
  (dom/div #js {:className "navChevronContainer"}
           (dom/i #js {:className (str "navChevron fa fa-chevron-left")
                       :onClick (fn [e]
                                  (scroll container-id "left"))})
           (dom/i #js {:className (str "navChevron fa fa-chevron-right")
                       :onClick (fn [e]
                                  (scroll container-id "right"))})
           (. js/console log "Hi!")))
