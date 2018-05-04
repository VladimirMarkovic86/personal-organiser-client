(ns personal-organiser-client.http.request-header)

(defn accept
	"Accept request-header field is used to specify media type which are acceptable for the response.
	
	example:	text/plain; q=0.5, text/html,
						text/x-dvi; q=0.8, text/x-c
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.1"
	[]
	"Accept")

(defn accept-charset
	"Accept charset request-header field can be used to indicate what character sets are acceptable for theresponse.
	
	example: iso-8859-5, unicode-1-1;q=0.8
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.2"
	[]
	"Accept-Charset")

(defn accept-encoding
	"Accept-Encoding request-header field is similar to Accept, but restricts the content-codings that are accaptable in the response.
	
	example: compress, gzip
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.3"
	[]
	"Accept-Encoding")

(defn accept-language
	"Accept language request-header field is ismilar to Accept, but restricts to set of natural languages that are preferred as a response to the request.
	
	example: da, en-gb;q=0.8, en;q=0.7
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.4"
	[]
	"Accept-Language")

(defn authorization
	"Authorization request-header field. A user agent that wishes to authenticate itself with a server, usually, but not necessarily after receiving a 401 response, does soby including an Authorization request-header field with the request.
	
	example: credentials
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.8"
	[]
	"Authorization")

(defn expect
	"The expect request-header field is used to indicate that particular server behaviors are required by the client.
	
	example:	100-continue
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.20"
	[]
	"Expect")

(defn from
	"From request-header field, if given, SHOULD contain an Internet e-mail address for the human user who controls the requesting user agent.
	
	example:	webmaster@w3.org
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.22"
	[]
	"From")

(defn host
	"Host request-header field specifies the Internet host and port number of the resource being requested, as obtained from the original URI given by the user or referring resource.
	
	example: <http://www.w3.org/pub/WWW/>
						GET	/pub/WWW/	HTTP/1.1
						Host:	www.w3.org
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.23"
	[]
	"Host")

(defn if-match
	"If match request-header field is used with a method to make it conditional. This behavior is most useful when the client wants to prevent an updating method, such as PUT, from modifying a resource that has changed since the client last retrieved it.
	
	example:	<entity-tag>
							-	server MUST use the strong comparsion function to compare entity tags
									in If-Match
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.24"
	[]
	"If-Match")

(defn if-modified-since
	"If modified since request-header field is used with a method to make it conditional: if the requested variant has not been modified since the time specified in this field, an entity will not be returned from the server; instead, a 304 (not modified) response will be returned without any message-body.
	
	example:	Sat, 29 Oct 1994 19:43:31 GMT
							-HTTP-date
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.25"
	[]
	"If-Modified-Since")

(defn if-none-match
	"If none match request-header field is used with a method to make it conditional. The purpose of this feature is to allow efficient updates of cached information with a minimum amount of transaction overhead. It is also used to prevent a method (e.g. PUT) from inadvertently modifying an existing resource when the client believes that the resource does not exist.
	
	example:	<entity-tag>
							-	server MUST use the strong comparsion function to compare entity tags
									in If-Match
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.26"
	[]
	"If-None-Match")

(defn if-range
	"If range request-header field: if client has partial copy of an entity in its cache, and wishes to have an up-to-date copy of the entire entity in its cache, it could use the Range request-header with a conditional GET (using either or both of If-Unmodified-Since and If-Match.) However, if the condition fails because the entity has been modified, the client would then have to make second request to obtain the entire current entity-body.
	
	example:	<entity-tag> | <HTTP-date>
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.27"
	[]
	"If-Range")

(defn if-unmodified-since
	"If unmodified since request-header field is used with a method to make it conditional. If the requested resource has been modified since the time specified in this field, the server SHOULD perform the requested operation as if the If-Unmodified-Since header were not present.
	
	example:	Sat, 29 Oct 1994 19:43:31 GMT
							-HTTP-date
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.28"
	[]
	"If-Unmodified-Since")

(defn max-forwards
	"Max forwards request-header field provides a mechanism with the TRACE and OPTIONS methods to limit the number of proxies or gateways that can forward the request to the next inbound server. This can be useful when client is attempting to trace a request chain which appears to be failing or looping in mid-chain.
	
	example:	1*DIGIT
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.31"
	[]
	"Max-Forwards")

(defn proxy-authenticate
	"Proxy authenticate request-header field MUST be included as part of a 407 (Proxy Authentication Required) response. The field value consists of a challenge that indicates the authentication scheme and parameters applicable to the proxy for this Request-URI.
	
	example:	<challange>
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.33"
	[]
	"Proxy-Authenticate")

(defn proxy-authorization
	"Proxy authorization request-header field allows the client to identify itself (or its user) to a proxy which requires authentication. This field value consists of credentials containing the authentication information of the user agent for the proxy and/or realm of the resource being requested.
	
	example:	<credentials>
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.34"
	[]
	"Proxy-Authorization")

(defn request-range
	"Range request-header field: Since all HTTP entities are represented in HTTP messages as sequences of bytes, the concept of a byte range is meaningful for any HTTP entity. Byte range specifications in HTTP aply to the sequence of bytes in the entity-body (not necessarily the same as message-body).
	
	example:	0-499
						    9500-
						    <first-byte-pos> - [<last-byte-pos>]
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.35"
	[]
	"Range")

(defn referer
	"Referer request-hedader field allows the client to specify,for the server's benefit, the address (URI) of the resource from which the Request-URI was obtained (the \"referrer\", although the header field is misspelled.) The Referer request-header allows a server to generate lists of back-links to resources for interest, logging, optimized caching, etc. It also allows obsolete or mistyped links to be traced for maintenance. The Referer field MUST NOT be sent if the Request-URI was obtained from a source that does not have its own URI, such as input from the user keyboard.
	
	example:	( <absoluteURI> | <relativeURI> )
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.36"
	[]
	"Referer")

(defn te
	"TE request-header field indicates what extension transfer-codings it is willing to accept in the response and wether or not it is willing to accept trailer fields in a chunked transfer-coding. Its value may consist of the keyword \"trailers\" and/or a comma-separated list of extension transfer-coding names with optional accept parameters.
	
	example:	trailers
						deflate
						trailers, deflate; q=0.5
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.39"
	[]
	"TE")

(defn user-agent
	"User agent request-header field contains information about the user agent originating the request. This is for statistcal purposes, the tracing of protocol violations, and automated recognition of user agent limitations.
	
	example:	CERN-LineMode/2.15 libwww/2.17b3
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.43"
	[]
	"User-Agent")

(defn access-control-request-method
	"Access control request method CORS request-header field indicates which method a future CORS request to the same resource might use.
	
	example:	GET, POST, PUT...
	
	https://fetch.spec.whatwg.org/#http-requests"
	[]
	"Access-Control-Request-Method")

(defn access-control-request-headers
	"Access control request headers CORS request-header field indicates which headers a future CORS request to the same resource might use.
	
	example:	( <general-header> | <request-header> | <entity-header> )
	
	https://fetch.spec.whatwg.org/#http-requests"
	[]
	"Access-Control-Request-Headers")

(defn cookie
	"Cookie request-header field contains stored HTTP cookies previously sent by the server with the Set-Cookie header. The Cookie header is optional and may be omitted if, for example, the browser's privacy settings block cookies.
	
	example:	<cookie-list>
						name=value
						name=value; name2=value2; name3=value3
						PHPSESSID=298zf09hf012fh2; csrftoken=u32t4o3tb3gg43; _gat=1
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cookie"
	[]
	"Cookie")

(defn cookie2
	"Cookie2 request-header field is used to advise the server that the user agent understands \"new-style\" cookies, but nowadays user agents will use the Cookie header instead, not this one. This feature is obsolete. Although it may still work in some browsers, its use is discouraged since it could be removed at any time. Try to avoid using it.
	
	example:	$Version=\"2\"
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Cookie2"
	[]
	"Cookie2")

(defn do-not-track
	"DNT (Do Not Track) request-header field indicates the user's tracking preference. It lets users indicate whether they would prefer privacy rather than personalized content.
	
	example:	( 0 | 1 )
	
	Reading Do Not Track status from JavaScript: navigator.doNotTrack; // \"0\" or \"1\"
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/DNT"
	[]
	"DNT")

(defn forwarded
	"Forwarded request-header field contains information from the client-facing side of proxy servers that is altered or lost when a proxy is involved in the path of the request. The alternative and de-facto standard versions of this header are the X-Forwarded-For, X-Forwarded-Host and X-Forwarded-Proto headers. This header is used for debugging, statistics, and generating location-dependent content and by design it exposes privacy sensitive information, such as the IP address of the client. Therefore the user's privacy must be kept in mind when deploying this header.
	
	example:by=<identifier>; for=<identifier>; host=<host>; proto=<http|https>
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Forwarded"
	[]
	"Forwarded")

(defn origin
	"Origin request-header field indicates where a fetch originates from. It doesn't include any path information, but only the server name. It is sent with CORS requests, as well as with POST requests. It is similar to the Referer header, but, unlike this header, it doesn't disclose the whole path.
	
	example:	<scheme> \"://\" <host> [ \":\" <port> ]
						https://developer.mozilla.org
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Origin"
	[]
	"Origin")

(defn upgrade-insecure-requests
	"Upgrade insecure requests request-header field sends a signal to the server expressing the client's preference for an encrypted and authenticated response, and that it can successfully handle the upgrade-insecure-requests CSP directive.
	
	example:	1
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Upgrade-Insecure-Requests"
	[]
	"Upgrade-Insecure-Requests")

(defn x-forwarded-for
	"X forwarded for request-header field is a de-facto standard header for identifying the originating IP address of a client connecting to a web server through an HTTP proxy or a load balancer.
	
	example:	<client>, <proxy1>, <proxy2>
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Forwarded-For"
	[]
	"X-Forwarded-For")

(defn x-forwarded-host
	"X forwarded host (XFH) request-header is a de-facto standard header for identifying the original host requested by the client in the Host HTTP request header. Host names and ports of reverse proxies (load balancers, CDNs) may differ from the origin server handling the request, in that case the X-Forwarded-Host is useful to determine which Host was originally used.
	
	example:	<host>
						    id42.example-cdn.com
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Forwarded-Host"
	[]
	"X-Forwarded-Host")

(defn x-forwarded-proto
	"X forwarded proto request-header field is a de-facto standard header for identifying the protocol (HTTP or HTTPS) that a client used to connect to your proxy or load balancer. Your server access logs contain the protocol used between the server and the load balancer, but not the protocol used between the client and the load balancer. To determine the protocol used between the client and the load balancer, the X-Forwarded-Proto request header can be used.
	
	example:	( http | https )
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Forwarded-Proto"
	[]
	"X-Forwarded-Proto")

