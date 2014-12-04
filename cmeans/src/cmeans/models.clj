(ns cmeans.models)

(defstruct point :index :coords :potential)
(defstruct app-state :centers :points :has-new)
