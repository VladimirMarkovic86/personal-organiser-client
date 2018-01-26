(ns personal-organiser-client.html-generator
  "Namespace for generating html pages"
  (:require	[clojure.java.io :refer [resource]]))

(defmacro deftmpl
	"Read template from file in resources/"
	[symbol-name html-name]
	(let	[content	(slurp	(resource	html-name))]
		`(def	~symbol-name	~content))
	)

