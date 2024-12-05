(ns oseias-master.core.adapters.web.handlers
  (:require 
   [oseias-master.core.service.combate :refer [atacar brigar confrontar
                                               encerrar virar-mare]] 
   [oseias-master.core.service.aventura :refer [enfrentar-perigo]]
   [oseias-master.core.model.efeito :as efeito]
   [oseias-master.core.service.dados :as dados]
   [ring.util.response :as response]))

(defn- executar
  "Executa rolagem a partir do body da requisição
     e retorna o resultado [checagem narrativa] a 
     partir do resultado"
  [body]
  (let [personagem (:personagem body)
        atributo   (:atributo body)
        bonus      ((keyword atributo) (:atributos personagem))
        rolagem    (if (:rolagem body) (map (:rolagem body) [:d6 :d10a :d10b]) (dados/rolar-desafio bonus))
        checagem   (dados/checar rolagem)
        narrativa  (efeito/narrativa checagem)] [checagem narrativa]))

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

(defn enfrentar-perigo-handler [request]
  (-> (:body request) executar enfrentar-perigo response/response))