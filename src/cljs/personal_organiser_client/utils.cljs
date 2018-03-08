(ns personal-organiser-client.utils)

(defn round-up
  "Round up divided numbers"
  [number1
   number2]
  (if (= 0 (mod number1
                number2))
   (int (/ number1
           number2))
   (inc 
    (int (/ number1
            number2))
    ))
  )

(defn find-index-to-remove
  ""
  [itr
   index-to-remove
   current-index]
  (if (< current-index (count index-to-remove))
   (if (= itr (index-to-remove current-index))
    current-index
    (recur itr
           index-to-remove
           (inc current-index))
    )
   false))

(defn remove-index-from-vector
  ""
  [data-vector
   index]
  (let [removed-index  (reduce (fn [acc
                                    elem]
                                (let [itr  (:itr acc)
                                      index-to-remove  (:index-to-remove acc)
                                      result  (:result acc)
                                      compared-index  (find-index-to-remove itr
                                                                            index-to-remove
                                                                            0)]
                                 (if compared-index
                                  (if (< (count index-to-remove) 2)
                                   {:itr (inc itr)
                                    :index-to-remove []
                                    :result result}
                                   {:itr (inc itr)
                                    :index-to-remove (remove-index-from-vector
                                                      index-to-remove
                                                      compared-index)
                                    :result result})
                                  {:itr (inc itr)
                                   :index-to-remove index-to-remove
                                   :result (conj result elem)})
                                 ))
                               {:itr 0
                                :index-to-remove (if (vector? index)
                                                  index
                                                  [index])
                                :result []}
                               data-vector)]
   (:result removed-index))
  )

(defn replace-in-vector-on-index
  ""
  [data-vector
   element
   index]
  (let [replaced-elements  (reduce (fn [acc
                                        elem]
                                    (let [itr  (:itr acc)
                                          replace-on-index  (:replace-on-index acc)
                                          replace-element  (:replace-element acc)
                                          result  (:result acc)
                                          compared-index  (find-index-to-remove
                                                           itr
                                                           replace-on-index
                                                           0)]
                                     (if compared-index
                                      {:itr  (inc itr)
                                       :replace-on-index  (remove-index-from-vector
                                                           replace-on-index
                                                           compared-index)
                                       :replace-element  (remove-index-from-vector
                                                          replace-element
                                                          compared-index)
                                       :result (conj result
                                                     (replace-element
                                                      compared-index))}
                                      {:itr (inc itr)
                                       :replace-on-index replace-on-index
                                       :replace-element replace-element
                                       :result (conj result
                                                     elem)})
                                     ))
                                   {:itr 0
                                    :replace-on-index (if (vector? index)
                                                          index
                                                          [index])
                                    :replace-element (if (vector? element)
                                                         element
                                                         [element])
                                    :result []}
                                   data-vector)]
   (:result replaced-elements))
  )

(defn insert-in-vector-on-index
  ""
  [data-vector
   element
   index]
  (let [inserted-elements  (reduce (fn [acc
                                        elem]
                                    (let [itr  (:itr acc)
                                          insert-on-index  (:insert-on-index acc)
                                          insert-element  (:insert-element acc)
                                          result  (:result acc)]
                                     (if (= itr insert-on-index)
                                      {:itr  (inc itr)
                                       :insert-on-index  -1
                                       :insert-element  []
                                       :result (reduce conj
                                                       result
                                                       insert-element)}
                                      {:itr  (inc itr)
                                       :insert-on-index  insert-on-index
                                       :insert-element  insert-element
                                       :result (conj result
                                                     elem)}))
                                    )
                                   {:itr 0
                                    :insert-on-index index
                                    :insert-element (if (vector? element)
                                                        element
                                                        [element])
                                    :result []}
                                   data-vector)]
   (:result inserted-elements))
  )

(defn read-file
 "example:
 file-pathe  public/index.html"
 [file-path]
; (clojure.core/slurp	(resource	file-path))
 )

