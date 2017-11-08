# barcode [![Build Status](https://travis-ci.org/ondrs/barcode.svg?branch=master)](https://travis-ci.org/ondrs/barcode)

Dead simple barcode generator for clojure based on Java barbecue library.

## Usage

```clojure
(def barcode (generate :code128 "010123456789012815051231")) ;; creates a Barcode instance

(write! "ean-128.png") ;; and writes it into the file

(write! an-output-stream :png) ;; or you can use an instance of OutputStream directly 
```

That's it!