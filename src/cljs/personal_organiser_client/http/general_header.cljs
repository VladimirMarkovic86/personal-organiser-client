(ns personal-organiser-client.http.general-header)

(defn cache-control 
	"Cache control general-header field is used to specify directives that MUST be obeyed by all caching mechanisms along the request/response chain.
	
	example: cache-directive
								- cache-request-directive
									-	\"no-cache\"
									-	\"no-store\"
									-	...
								-	cache-response-directive
									-	\"public\"
									-	\"private\"
									-	...
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9"
	[]
	"Cache-Control")

(defn connection
	"Connection general-header field string key
	
	example: 	keep-alive
						close
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.10"
	[]
	"Connection")

(defn date
	"Date general-header field represents the date and time at which the message was originated.
	
	example:	Tue, 15 Nov 1994 08:12:31 GMT
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.18"
	[]
	"Date")

(defn pragma
	"Pragma general-header field is used to include implementation specific directives that might apply to any recipient along the request/response chain. All pragma directives specify optional behavior from the viewpoint of the protocol; however, some systems MAY require that behavior to be consistent with the directives.
	
	example:	no-cache
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.32"
	[]
	"Pragma")

(defn trailer
	"Trailer general-header field value indicates that the given set of header fields is present in the trailer of a message encoded with chunked transfer-coding.
	
	example:	<field-name>
						- except:	Transfer-Encoding, Content-Length, Trailer
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.40"
	[]
	"Trailer")

(defn transfer-encoding
	"Transfer encoding general-header field indicates what (if any) type of transformation has been applied to the message body in order to safely transfer it between the sender and the recipient. This differs from the content-coding in that the transfer-coding is a property of the message, not of the entity. If multiple encodings have been applied to an entity, the transfer-codings MUST be listed in the order in which they were applied.
	
	example:	<transfer-coding>
							-chunked
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.41"
	[]
	"Transfer-Encoding")

(defn upgrade
	"Upgrade general-header field allows the client to specify what additional communication protocols it supports and would like to use if server finds it appropriate to switch protocols. The server MUST use the Upgrade header field within a 101 (Switching Protocols) response to indicate which protocol(s) are being switched.
	
	example:	HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11
						<product>
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.42"
	[]
	"Upgrade")

(defn via
	"Via general-header field MUST be used by gateways and proxies to indicate the intermediate protocols and recipients between the user agent and the server on request, and between the origin server and the client on responses.
	
	example:	1.0 fred, 1.1 nowhere.com (Apache/1.1)
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.45"
	[]
	"Via")

(defn warning
	"Warning general-header field is used to carry additional information about the status or transformation of a message which might not be reflected in the message. This information is typically used to warn about a possible lack of semantic transparency from caching operations or transformations applied to the entity body of the message.
	
	example:	<warn-code> SP <warn-agent> SP <warn-text>
																				[SP <warn-date>]
	
	https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.46"
	[]
	"Warning")

(defn keep-alive
	"Keep alive general-header field allows the sender to hint about how the connection and may be used to set a timeout and a maximum amount of request. The Connection header needs to be set to \"keep-alive\" for this header to have any meaning. Also, Connection and Keep-alive are ignored in HTTP/2; connection management is handled by other mechanisms there. This feature is non-standard and is not on a standards track. Do not use it on production sites facing the Web: it will not work for every user. There may also be large incompatibilities between implementations and the behavior may change in the future.
	
	example:	<parameters>
						A comma-separated list of parameters, each consisting of an identifier and a
						value separated by the equal sign ('='). The following identifiers are possible:
						timeout and max
	
	https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Keep-Alive"
	[]
	"Keep-Alive")

