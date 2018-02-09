(ns personal-organiser-client.http.mime-type)

; https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types

; text -	Represents any document that contains text and is theoretically human readable

(defn text-plain
  ""
  []
  "text/plain")

(defn text-html
  ""
  []
  "text/html")

(defn text-css
  ""
  []
  "text/css")

(defn text-javascript
  ""
  []
  "text/javascript")

; image -	Represents any kind of images. Videos are not included, though animated images (like animated gif) are described with an image type.

(defn image-jpeg
  ""
  []
  "image/jpeg")

(defn image-png
  ""
  []
  "image/png")

(defn image-gif
  ""
  []
  "image/gif")

(defn image-bmp
  ""
  []
  "image/bmp")

(defn image-webp
  ""
  []
  "image/webp")

; audio -	Represents any kind of audio files

(defn audio-mpeg
  ""
  []
  "audio/mpeg")

; An audio file in the OGG container format. Vorbis is the most common audio codec used in such a container.

(defn audio-ogg
  ""
  []
  "audio/ogg")

(defn audio-midi
  ""
  []
  "audio/midi")

; An audio file in the WebM container format. Vorbis and Opus are the most common audio codecs.

(defn audio-webm
  ""
  []
  "audio/webm")

; An audio file in the WAVE container format. The PCM audio codec (WAVE codec "1") is often supported, but other codecs have more limited support (if any).

(defn audio-wave
  ""
  []
  "audio/wave")

(defn audio-wav
  ""
  []
  "audio/wav")

(defn audio-x-wav
  ""
  []
  "audio/x-wav")

(defn audio-x-pn-wav
  ""
  []
  "audio/x-pn-wav")

(defn audio-all
  ""
  []
  "audio/*")

; video -	Represents any kind of video files

(defn video-mp4
  ""
  []
  "video/mp4")

; A video file, possibly with audio, in the WebM container format. VP8 and VP9 are the most common video codecs used within it; Vorbis and Opus the most common audio codecs.

(defn video-webm
  ""
  []
  "video/webm")

; A video file, possibly with audio, in the OGG container format. Theora is the usual video codec used within it; Vorbis is the usual audio codec.

(defn video-ogg
  ""
  []
  "video/ogg")

; application -	Represents any kind of binary data.

; Most web servers send unknown-type resources using the default application/octet-stream MIME type

(defn application-octet-stream
  ""
  []
  "application/octet-stream")

(defn application-pkcs12
  ""
  []
  "application/pkcs12")

(defn application-vnd-mspowerpoint
  ""
  []
  "application/vnd.mspowerpoint")

(defn application-xhtml-xml
  ""
  []
  "application/xhtml+xml")

(defn application-xml
  ""
  []
  "application/xml")

(defn application-pdf
  ""
  []
  "application/pdf")

; An audio or video file using the OGG container format. Theora is the usual video codec used within it; Vorbis is the usual audio codec.

(defn application-ogg
  ""
  []
  "application/ogg")

; The multipart/form-data type can be used when sending the content of a completed HTML Form from the browser to the server. As a multipart document formal, it consists of different parts, delimited by a boundary (a string starting with a double dash '--'). Each part is an entity by itself, with its own HTTP headers, Content-Disposition, and Content-Type for file uploading fields, and the most common (Content-Length is ignored as the boundary line is used as the delimiter).

(defn multipart-form-data
  ""
  []
  "multipart/form-data")

; The multipart/byteranges MIME type is used in the context of sending partial responses back to the browser. When the 206 Partial Content status code is sent, this MIME type is used to indicate that the document is composed of several parts, one for each of the requested range. Like other multipart types, the Content-Type uses the boundary directive to define the boundary string. Each of the different parts have a Content-Type header with the actual type of the document and a Content-Range with the range they represent.

(defn multipart-byteranges
  ""
  []
  "multipart/byteranges")

