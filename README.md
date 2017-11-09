# barcode [![Build Status](https://travis-ci.org/ondrs/barcode.svg?branch=master)](https://travis-ci.org/ondrs/barcode)

Dead simple barcode generator for clojure based on Java barbecue library.

## Usage

```clojure
(def barcode (generate :code128 "010123456789012815051231")) ;; create a Barcode instance

;; adjust the barcode to your wishes
(doto barcode
  (.setDrawingText false)
  (.setBarHeight 3000)
  (.setBarWidth 200)
  (.setResolution 300))

(write! barcode "ean-128.png") ;; and write it into the file

(write! barcode an-output-stream :png) ;; or you can use an instance of OutputStream 
```

That's it!