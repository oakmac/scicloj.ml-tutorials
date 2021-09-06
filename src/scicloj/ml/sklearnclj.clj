(ns scicloj.ml.sklearnclj
  (:require
   [notespace.api :as note]
   [notespace.kinds :as kind]
   [scicloj.sklearn-clj.ml]
   [scicloj.ml.ug-utils :refer :all]
   ))



(comment
  (note/init-with-browser)
  (notespace.api/update-config
   #(assoc % :source-base-path "userguide"))

  (note/eval-this-notespace)
  (note/reread-this-notespace)
  (note/render-static-html "docs/userguide-sklearnclj.html")
  (note/init)

  )
["# sklearn-clj"]

["The scicloj.ml plugin [sklearn-clj](https://github.com/scicloj/sklearn-clj)
 gives easy access to all models from [scikit-learn](https://scikit-learn.org/stable/)" ]

["After [libpython.clj](https://github.com/clj-python/libpython-clj)
 has been setup with the python package sklearn installed,
the following lines show how to use any sklearn model in a usual scicloj.ml pipeline:"]

(require '[scicloj.ml.core :as ml]
         '[scicloj.ml.metamorph :as mm]
         '[scicloj.ml.dataset :as ds]
         '[tech.v3.dataset.tensor :as dst]
         '[scicloj.sklearn-clj]
         '[scicloj.metamorph.ml.toydata :as toydata]
         )

(def ds (-> (dst/tensor->dataset [[0 0 0 ] [1 1 1 ] [2 2 2]])




            ))
(def pipe
  (ml/pipeline
   (mm/set-inference-target 2)
   (mm/model {:model-type :sklearn.classification/logistic-regression
              :max-iter 100
              })))

(pipe {:metamorph/data ds
       :metamorph/mode :fit})

["SVM"]

(def iris-split
  (->
   (toydata/iris-ds)
   (ds/split->seq  :holdout))
  )


(def pipe-fn
  (ml/pipeline
   ;; {:metamorph/id :model}
   (mm/model {:model-type
              :sklearn.classification/svc})
   )

  )

(def fitted-ctx
  (ml/fit-pipe
   (:train (first  iris-split))
   pipe-fn))



(ml/transform-pipe
 (:test (first  iris-split))
 pipe-fn
 fitted-ctx
 )

(def iris
  (ds/dataset
   "https://raw.githubusercontent.com/scicloj/metamorph.ml/main/test/data/iris.csv" {:key-fn keyword})
  )
^kind/vega
;; (surface-plot iris [:sepal_width :sepal_length]
;;               {:model-type
;;                :sklearn.classification/svc})








["# Models"]

["Below all models are listed with their parameters and the original documentation.

The parameters are given as Clojure keys in kebap-case. As the document texts are imported from python
they refer to the python spelling of the parameter. But the translation between the two should be obvious."
]



["## Sklearn classification"]
^kind/hiccup-nocode
(render-key-info ":sklearn.classification")


["## Sklearn regression"]
^kind/hiccup-nocode
(render-key-info ":sklearn.regression")
