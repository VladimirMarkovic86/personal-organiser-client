(ns personal-organiser-client.http.response-header)

(defn accept-ranges
	"Accept ranges response-header field allows server to indicate its acceptance of range for a resource:
	
	example: bytes
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.5"
	[]
	"Accept-Ranges")

(defn age
	"Age response-header field conveys the sender's estimate of the amount of time since the response (or its revalidation) was generated at the origin server
	
	example:	30
						meaning 30s
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.6"
	[]
	"Age")

(defn etag
	"ETag response-header field provides the current value of the entity tag for the requested variant. The entity tag MAY be used for comparison with other entities from the same resource.
	
	example:	<entity-tag>
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.19"
	[]
	"ETag")

(defn location
	"Location response-header field is used to redirect the recipient to a location other than the Request-URI for completion of the request or identification of a new resource.
	
	example:	absoluteURI
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.30"
	[]
	"Location")

(defn proxy-authenticate
	"Proxy authenticate response-header field MUST be included as part of a 407 (Proxy Authenticate Required) response. The field value consists of a challenge that indicates the authentication scheme and parameters applicable to the proxy for this Request-URI.
	
	example:	<challenge>
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.33"
	[]
	"Proxy-Authenticate")

(defn retry-after
	"Retry after response-header field can be used with a 503 (Service Unavailable) response to indicate how long the service is expected to be unavailable to the requesting client. This field MAY also be used with any 3xx (Redirection) response to indicate the minimum time the user-agent is asked to wait before issuing the redirect request. The value of this field can be either an HTTP-date or an integer number of seconds (in decimal) after the time of the response.
	
	example:	Fri, 31 Dec 1999 23:59:59 GMT
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.37"
	[]
	"Retry-After")

(defn server
	"Server response-header field contains information about the software used by the origin server to handle the request.The field can contain multiple product tokens and comments identifying the server and any significant subproducts. The product tokens are listed in order of their significance for identifying the application.
	
	example:	CERN/3.0 libwww/2.17
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.38"
	[]
	"Server")

(defn vary
	"Vary response-header field value indicates the set of request-header fields that fully determines, while the response to reply to a subsequent request without revalidation. For uncacheable or stale responses, the Vary field value advises the user agent about the criteria that were used to select the representation.
	
	example:	( * | field-name )
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.44"
	[]
	"Vary")

(defn www-authenticate
	"WWW authenticate response-header field MUST be included in 401 (Unauthorized) response messages. The field value consists of at least one challange that indicates the authentication scheme(s) and parameters applicable to the Request-URI.
	
	example:	<challange>
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.47"
	[]
	"WWW-Authenticate")

(defn access-control-allow-origin
	"Access control allow origin CORS response-header field indicates whether the response can be shared, via returning the literal value of the 'Origin' request header (which can be 'null') or '*' in a response.
	
	example:	www.domain.com
						( origin-or-null | wildcard )
	
	https://fetch.spec.whatwg.org/#http-responses"
	[]
	"Access-Control-Allow-Origin")

(defn access-control-allow-credentials
	"Access control allow credentials CORS response-header field indicates whether the response can be shared when request's credentials mode is \"include\".
	
	example:	%x74.72.75.65 ; \"true\", case-sensitive
	
	https://fetch.spec.whatwg.org/#http-responses"
	[]
	"Access-Control-Allow-Credentials")

(defn access-control-allow-methods
	"Access control allow methods CORS response-header field indicates which methods are supported by the response's url for the purposes of the CORS protocol.
	
	example:	GET, POST, PUT...
	
	https://fetch.spec.whatwg.org/#http-responses"
	[]
	"Access-Control-Allow-Methods")

(defn access-control-allow-headers
	"Access control allow headers CORS response-header field indicates which headers are supported by the response's url for the purposes of CORS protocol.
	
	https://fetch.spec.whatwg.org/#http-responses"
	[]
	"Access-Control-Allow-Headers")

(defn access-control-max-age
	"Access control max age CORS response-header field indicates how long the information provided by the 'Access-Control-Allow-Methods' and 'Access-Control-Allow-Headers' headers can be cached.
	
	example:	DIGITS
						-	delta-seconds
	
	https://fetch.spec.whatwg.org/#http-responses"
	[]
	"Access-Control-Max-Age")

(defn access-control-expose-headers
	"Access control expose headers CORS response-header field indicates which headers can be exposed as part of the response by listing their names.
	
	example:	( <general-header> | <response-header> | <entity-header> )
	
	https://fetch.spec.whatwg.org/#http-responses"
	[]
	"Access-Control-Expose-Headers")

(defn content-disposition
	"Content disposition response-header field indicates if the content is expected to be displayed inline in the browser, that is, as a Web page or as part of a Web page, or as an attachment, that is downloaded and saved locally.
	
	example:	( inline | attachment | attachment; filename=\"filename.jpg\" | 
							form-data | form-data; name=\"fieldName\" |
							form-data; name=\"fieldName\"; filename=\"filename.jpg\" )
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Disposition"
	[]
	"Content-Disposition")

(defn content-security-policy
	"Content security policy response-header field allows web site administrators to control resources the user agent is allowed to load for a given page. With a few exceptions, policies mostly involve specifying server origins and script endpoints.
	
	example:	<policy-directive>; <policy-directive>
						<fetch-directive>
						<document-directive>
						<navigation-directive>
						<reporting-directive>
						<other-directive>
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy"
	[]
	"Content-Security-Policy")

(defn content-security-policy-report-only
	"Content security policy report only response-header field allows web developers to experiment with policies by monitoring (but not enforcing) their effects. These violation reports consist of JSON documents sent via an HTTP POST request to the specified URI.
	
	example:	<policy-directive>; <policy-directive>
						report-uri should be used with this header, otherwise this header will be an
						expensive no-op machine
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy-Report-Only"
	[]
	"Content-Security-Policy-Report-Only")

(defn large-allocation
	"The non-standard Large-Alocation response-header field tells the browser that the page being loaded is going to want to perform a large allocation. It is currently only implemented in Firefox, but is harmless to send to every browser.
	
	WebAssembly or asm.js applications can use large contiguous blocks of allocated memory. For complex games, for example, these allocations can be quite large, sometimes as large as 1GB. The Large-Allocation tells the browser that the web content in the to-be-loaded page is going to want to perform a large contiguous memory allocated and the browser can react to this header by starting dedicated process for the to-be-loaded document, for example.
	
	example:	0
						<megabytes>
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Large-Allocation"
	[]
	"Large-Alocation")

(defn referrer-policy
	"Referrer policy response-header field governs which referrer information, sent in the Referer header, should be icluded with requests made.
	
	example:	( no-referrer | no-referrer-when-downgrade | origin | ...)
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Referrer-Policy"
	[]
	"Referrer-Policy")

(defn set-cookie
	"Set cookie response-header field is used to send cookies from the server to the user agent.
	
	example:	<cookie-name>=<cookie-value>	[; Expires=<date>]
																					[; Max-age=<non-zero-digit>]
																					[; Domain=<domain-value>]
																					[; Path=<path-value>]
																					[; Secure]
																					[; HttpOnly]
																					[; SameSite=Strict]
																					[; SameSite=Lax]
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie"
	[]
	"Set-Cookie")

(defn set-cookie2
	"Set cookie 2 response-header field is used to send cookies from the server to the user agent, but has been depricated by the specification. Use Set-Cookie instead. This feature is obsolete. Although it may still work in some browsers, its use is discouraged since it could be removed at any time. Try to avoid using it.
	
	example:	<cookie-name>=<cookie-value>	[; Comment=<value>]
																					[; CommentURL=<http-url>]
																					[; Discard]
																					[; Domain=<domain-value>]
																					[; Max-Age=<non-zero-digit>]
																					[; Path=<path-value>]
																					[; Port=<port-number>]
																					[; Secure]
																					[; Version=<version-number>]
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie2"
	[]
	"Set-Cookie2")

(defn source-map
	"SourceMap response-header field links generated code to a source map, enabling the browser to reconstruct the original source and present the reconstructed original in the debugger.
	
	example:	<url>
						X-SourceMap
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/SourceMap"
	[]
	"Source-Map")

(defn strict-transport-security
	"Strict transport security response-header field (often abbreviated as HSTS) lets a web site tell browsers that it should only be accessed using HTTPS, instead of using HTTP.
	
	example:	max-age=<expire-time>	[; includeSubDomains]
																	[; preload]
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Strict-Transport-Security"
	[]
	"Strict-Transport-Security")

(defn timing-allow-origin
	"Timing allow origin response-header field specifies origins that are allowed to see values of attributes retrieved via features of the Resource Timing API, which would otherwise be reported as zero due to cross-origin restrictions.
	
	example:	https://developer.mozilla.org
						( * | <origin>	[, <origin>])
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Timing-Allow-Origin"
	[]
	"Timing-Allow-Origin")

(defn tk
	"Tk response-header field indicates the tracking status that applied to the corresponding request.
	
	example:	! (under construction)
						? (dynamic)
						G (gateway or multiple parties)
						N (not tracking)
						T (tracking)
						C (tracking with consent)
						P (potential consent)
						D (disregarding DNT)
						U (updated)
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Tk"
	[]
	"Tk")

(defn x-content-type-options
	"X content type options response-header field is a marker used by the server to indicate that the MIME types advertised in the Content-Type headers should not be changed and be followed. This allows to opt-out of MIME type sniffing, or, in other words, it is a way to say that the webmasters knew what they were doing.
	
	example:	nosniff
							Blocks a request if the requested type is
								-	\"style\" and the MIME type is not \"text/css\", or
								-	\"script\" and the MIME type is not a JavaScript MIME type
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options"
	[]
	"X-Content-Type-Options")

(defn x-dns-prefetch-control
	"X DNS prefetch control response-header field controls DNS prefetching, a feature by which browsers proactively perform domain name resolution on both links that the user may choose to follow as well as URLs for items referenced by the document, including images, CSS, JavaScript, and so forth.
	
	example:	( on | off )
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-DNS-Prefetch-Control"
	[]
	"X-DNS-Prefetch-Control")

(defn x-frame-options
	"X frame options response-header field can be used to indicate whether or not a browser should be allowed to render a page in a <frame>, <iframe> or <object>. Sites can use this to avoid clickjacking attacks, by ensuring that their content is not embedded into other sites. The added security is only provided if the user accessing the document is using a browser supporting X-Frame-Options.
	
	example:	( DENY | SAMEORIGIN | ALLOW-FROM http://example.com )
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Frame-Options"
	[]
	"X-Frame-Options")

(defn x-xss-protection
	"X XSS protection response-header field is a feature of Internet Explorer, Chrome and Safari that stops pages from loading when they detect reflected cross-site scripting (XSS) attacks. Although these protections are largely unnecessary in modern browsers when sites implement a strong Content-Security-Policy that disables the use of inline JavaScript ('unsafe-inline'), they can still provide protections for users of older web browsers that don't yet support CSP.
	
	example:	( 0 | 1 [; mode=block] [; report=<reporting-uri>])
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection"
	[]
	"X-XSS-Protection")

