(ns oseias-master.core.service.aventura
  (:require [oseias-master.core.model.consequencia :as consequencia]
            [oseias-master.core.model.efeito :as efeito]
            [oseias-master.core.service.avaliacoes :refer [avaliar-rolagem avaliar-progresso calcular-resultado]]
            [oseias-master.core.model.movimentos :as mov]
            [oseias-master.core.service.dados :as dados]))

(defn enfrentar-perigo [body] (let [personagem        (:personagem body)
                                    atributo          (:atributo body)
                                    bonus             ((keyword atributo) (:atributos personagem))
                                    rolagem           (if (:rolagem body) (map (:rolagem body) [:d6 :d10a :d10b]) (dados/rolar-desafio bonus))
                                    checagem          (dados/checar rolagem)
                                    narrativa         (efeito/narrativa checagem)
                                    efeitos-mecanicos (efeito/quando checagem 
                                                                     (efeito/mecanica [:momentum 1]) 
                                                                     (map efeito/mecanica [[:momentum -1] [:saude -1] [:espirito -1] [:suprimentos -1]]) 
                                                                     ())                       
                                    movimentos        (efeito/quando checagem 
                                                                     (map (mov/movimentos :aventura) (remove #(= :enfrentar-perigo %) (keys (mov/movimentos :aventura))))
                                                                     (map (mov/movimentos :punicao) [:suportar-dano :suportar-estresse]) 
                                                                     ((mov/movimentos :destino) :pagar-preco))]
                                (efeito/efeito efeitos-mecanicos narrativa movimentos)))

(defn coletar-info [body]
  (let [vinculo (:vinculo body)] {}))

(defn acampar [body] {})

(defn empreender-jornada [body] {})

(defn terminar-jornada [body] {})

(defn curar [body] {})

(defn garantir-vantagem [body]
  (let [rolagem (:rolagem body)]
    {}))

(defn acao []
  (let [;;avaliar
        ;; {consequencia: { efeito: "string", movimentos: ["string"], narrativa: "string" }, progresso: 0 }
        ]))