(ns oseias-master.core.service.dados)

(defn checar [rolagem]
  (let [[acao desafio-a desafio-b] rolagem]
    (if (= desafio-a desafio-b)
      (cond (> acao desafio-a) (keyword :sucesso-critico)
            (<= acao desafio-a) (keyword :falha-critica))
      (cond (and (> acao desafio-a) (> acao desafio-b)) :sucesso-decisivo
            (and (<= acao desafio-a) (<= acao desafio-b)) :falha
            :else :sucesso-parcial)))
  )
(defn rolar-desafio [bonus]
  (let [acao      (+ (rand-int 6) bonus)
        desafio-a (rand-int 10)
        desafio-b (rand-int 10)] [acao desafio-a desafio-b]))

(defn rolar-sim-nao [] (if (= (rand-int 1) 1) :sim :nao))