(ns oseias-master.core.service.avaliacoes)

(defn avaliar-rolagem [rolagem]
  (let [valor-acao (+ (:d6 rolagem) (:bonus rolagem))]
    (cond
      (and (> valor-acao (:d10a rolagem))
           (> valor-acao (:d10b rolagem))) "Sucesso Decisivo"
      (or (> valor-acao (:d10a rolagem))
          (> valor-acao (:d10b rolagem))) "Sucesso Parcial"
      :else "Falha")))

(defn avaliar-progresso [progresso rolagem]
  (let [valor-acao progresso]
    (cond
      (and (> valor-acao (:d10a rolagem))
           (> valor-acao (:d10b rolagem))) "Sucesso Decisivo"
      (or (> valor-acao (:d10a rolagem))
          (> valor-acao (:d10b rolagem))) "Sucesso Parcial"
      :else "Falha")))

(defn calcular-resultado [rolagem avaliacao]
  {:avaliacao avaliacao
   :critico (= (:d10a rolagem) (:d10b rolagem))})