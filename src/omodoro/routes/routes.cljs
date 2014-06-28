(ns omodoro.routes.routes
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [omodoro.routes.timer :as timer]
            [omodoro.routes.intro :as intro]
            [omodoro.routes.settings :as settings]
            [omodoro.routes.analytics :as analytics]
            [omodoro.routes.planning :as planning]
            [omodoro.routes.four-oh-four :as fof]))

(defn timer [app owner opts]
  (om/build timer/init app))

(defn intro [app owner opts]
  (om/build intro/init app))

(defn settings [app owner opts]
  (om/build settings/init app))

(defn analytics [app owner opts]
  (om/build analytics/init app))

(defn planning [app owner opts]
  (om/build planning/init app))

(defn four-oh-four [app owner opts]
  (om/build fof/init app))


