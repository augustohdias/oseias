(ns oseias-master.core.service.destino)

;; Pagar o Preço: /destino/pagar
(defn pagar-preco [body] 
  (let [contexto (keyword (:contexto body))
        combate? (= contexto :Combate)]
    {:consequencia {:execute [:efeito (if combate? "Sofra o dano inimigo" "Consequência narrativa negativa")]}}))