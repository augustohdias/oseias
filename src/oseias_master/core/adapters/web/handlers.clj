(ns oseias-master.core.adapters.web.handlers
  (:require [oseias-master.core.service.combate :refer [atacar confrontar brigar virar-mare encerrar]]
            [ring.util.response :as response]))

(defn atacar-handler [request]
  (response/response (atacar (:body request))))

(defn confrontar-handler [request]
  (response/response (confrontar (:body request))))

(defn brigar-handler [request]
  (response/response (brigar (:body request))))

(defn virar-mare-handler [request]
  (response/response (virar-mare (:body request))))

(defn encerrar-handler [request]
  (response/response (encerrar (:body request))))
