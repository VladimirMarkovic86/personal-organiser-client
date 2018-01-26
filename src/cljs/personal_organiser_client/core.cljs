(ns personal-organiser-client.core
	(:require	[cljsjs.react							:as	react]
						[enfocus.core							:as	ef]
            [enfocus.events						:as	events]
            [personal-organiser-client.ajax	:as	ajx])
	(:require-macros	[enfocus.macros														:as	em]
										[personal-organiser-client.html-generator	:as	hg]))

(defn	success-handler
	"Simple ajax success handler"
	[	xhr
		params-map]
	(let	[	resp			(aget	xhr	"response")
					elements	(.getElementsByClassName	js/document	(:entity	params-map))]
		(.log	js/console	xhr)
		(aset	(aget	elements	0)	"innerHTML"	(str	resp	" - success"))
		))

(defn	error-handler
	"Simple ajax error handler"
	[	xhr
		params-map]
	(let	[	resp			(aget	xhr	"response")
					elements	(.getElementsByClassName	js/document	(:entity	params-map))]
		(.log	js/console	xhr)
		(aset	(aget	elements	0)	"innerHTML"	(str	resp	" - error"))
		))

(hg/deftmpl	login-form	"public/login-form.html")
(hg/deftmpl	template	"public/template.html")
(hg/deftmpl	nav	"public/nav.html")
(hg/deftmpl	form	"public/form.html")
(hg/deftmpl	footer	"public/footer.html")

(em/defaction on-page-load
	[]
	["body"]
		(ef/content	template)
	[".header"]
		(ef/content	nav)
	[".content"]
		(ef/content	form)
	[".footer"]
		(ef/content	footer)
	["#testBtnId"]
		(events/listen	:click
										#(do	(ajx/uni-ajax-call
														{	:url									"https://localhost:8443/clojure/index1"
															:request-method				"POST"
															:success-fn						success-handler
															:error-fn							error-handler
															:request-header-map
																{	"Accept"				"application/json"
																	"Content-Type"	"application/json"}
															:request-property-map
																{	"responseType"	"text/javascript"}
															:entity								"index1"
															})
													(ajx/uni-ajax-call
														{	:url									"https://localhost:8443/clojure/index2"
															:request-method				"POST"
															:success-fn						success-handler
															:error-fn							error-handler
															:request-header-map
																{	"Accept"				"application/json"
																	"Content-Type"	"application/json"}
															:request-property-map
																{	"responseType"	"text/javascript"}
															:entity								"index2"
															}))
										))

(defn	login-success
	"Login success"
	[]
	(.log	js/console	"success"))

(defn	login-error
	"Login error"
	[]
	(.log	js/console	"error"))

(defn	read-login-form
	"Read data from login form"
	[]
	(let	[	email			(.getElementById	js/document	"txtEmailId")
					password	(.getElementById	js/document	"pswLoginId")]
		{	:email			email
			:password		password}))

(em/defaction redirect-to-login
	[]
	["body"]
		(ef/content	login-form)
	["#btnLoginId"]
		(events/listen	:click
										#(ajx/uni-ajax-call
											{	:url									"https://localhost:8443/clojure/login"
												:request-method				"POST"
												:success-fn						login-success
												:error-fn							login-error
												:request-header-map
													{	"Accept"				"application/json"
														"Content-Type"	"application/json"}
												:request-property-map
													{	"responseType"	"application/json"}
												:entity								"{}";(read-login-form)
												}))
	)

(defn	is-logged-in
	"Check if user is logged in"
	[]
	(if	(= 1 2)
		(on-page-load)
		(redirect-to-login))
	)

(set! (.-onload js/window)	is-logged-in)

