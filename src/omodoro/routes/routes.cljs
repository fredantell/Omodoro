(ns omodoro.routes.routes
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omodoro.routes.timer :as timer]
            [omodoro.routes.intro :as intro]
            [omodoro.routes.settings :as settings]
            [omodoro.routes.analytics :as analytics]
            [omodoro.routes.planning :as planning]))

(defn timer [app owner opts]
  (om/build timer/init app))

(defn intro [app owner opts]
  (om/build intro/init app))

