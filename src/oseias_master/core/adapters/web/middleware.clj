(ns oseias-master.core.adapters.web.middleware
  (:require [ring.middleware.json :refer [wrap-json-body wrap-json-response]]))

(defn wrap-json [handler]
  (-> handler
      (wrap-json-body {:keywords? true})
      wrap-json-response))
