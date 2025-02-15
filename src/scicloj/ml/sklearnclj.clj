(ns scicloj.ml.sklearnclj
  (:require
   [notespace.api :as note]
   [notespace.kinds :as kind]
   [scicloj.sklearn-clj.ml]
   [scicloj.ml.ug-utils]))
   



(comment
  (note/init-with-browser)
  (note/eval-this-notespace)
  (note/reread-this-notespace)
  (note/render-static-html "docs/userguide-sklearnclj.html")
  (note/init))

  
["# sklearn-clj"]

["The [scicloj.ml](https://github.com/scicloj/scicloj.ml) plugin [sklearn-clj](https://github.com/scicloj/sklearn-clj)
 gives easy access to all models from [scikit-learn](https://scikit-learn.org/stable/)"]

["After [libpython.clj](https://github.com/clj-python/libpython-clj)
 has been setup with the python package sklearn installed,
the following lines show how to use any sklearn model in a usual `scicloj.ml` pipeline:"]

(require '[scicloj.ml.core :as ml]
         '[scicloj.ml.metamorph :as mm]
         '[scicloj.ml.dataset :as ds]
         '[tech.v3.dataset.tensor :as dst]
         '[scicloj.sklearn-clj :as sklearn-clj]
         '[scicloj.sklearn-clj.ml]
         '[scicloj.metamorph.ml.toydata :as toydata]
         '[libpython-clj2.python :refer [py.-] :as py])
         

["Example: logistic regression"]

(def ds (dst/tensor->dataset [[0 0 0 ] [1 1 1 ] [2 2 2]]))

["Make pipe with sklearn model 'logistic-regression'"]
(def pipe
  (ml/pipeline
   (mm/set-inference-target 2)
   {:metamorph/id :model}
   (mm/model {:model-type :sklearn.classification/logistic-regression
              :max-iter 100})))


["Train model"]
(def fitted-ctx
  (pipe {:metamorph/data ds
         :metamorph/mode :fit}))

["Predict on new data"]
(->
 (ml/transform-pipe
  (dst/tensor->dataset [[3 4 5]])
  pipe
  fitted-ctx)
 :metamorph/data)

["Access model details via python interop (libpython-clj)"]
(-> fitted-ctx :model :model-data :model
    (py.- coef_)
    (py/->jvm))





["All model attributes are as well in the context"]

(def model-attributes
  (-> fitted-ctx :model :model-data :attributes))

^kind/hiccup-nocode
[:dl (map
      (fn [[k v]]
        [:span
         (vector :dt k)
         (vector :dd  (clojure.pprint/write v :stream nil))])
      model-attributes)]



["# Models"]

["Below all models are listed with their parameters and the original documentation.

The parameters are given as Clojure keys in kebap-case. As the document texts are imported from python
they refer to the python spelling of the parameter. But the translation between the two should be obvious."]

^kind/hiccup-nocode
[:ul


 (->>
  (ml/model-definition-names)
  (filter #(contains? #{"sklearn.classification"
                        "sklearn.regression"}

                     (namespace %)))
  sort
  (map
   #(vector :li [:a {:href (str "#" (str %))} (str %)])))]
  



["## Sklearn classification"]
^kind/hiccup-nocode
(scicloj.ml.ug-utils/render-key-info ":sklearn.classification")


["## Sklearn regression"]
^kind/hiccup-nocode
(scicloj.ml.ug-utils/render-key-info ":sklearn.regression")
