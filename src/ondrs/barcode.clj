(ns ondrs.barcode
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:import (net.sourceforge.barbecue BarcodeImageHandler BarcodeFactory Barcode)
           (javax.imageio ImageIO)
           (java.io OutputStream File)))


(defn generate
  [type ^String code & args]
  (let [args (apply hash-map args)]
    (condp = type
      :2of7 (BarcodeFactory/create2of7 code)
      :3of9 (BarcodeFactory/create3of9 code (:require-checksum args))
      :codabar (BarcodeFactory/createCodabar code)
      :code128 (BarcodeFactory/createCode128 code)
      :code128A (BarcodeFactory/createCode128A code)
      :code128B (BarcodeFactory/createCode128B code)
      :code128C (BarcodeFactory/createCode128C code)
      :code39 (BarcodeFactory/createCode39 code (:require-checksum args))
      :ean128 (BarcodeFactory/createEAN128 code)
      :global-trade-item-number (BarcodeFactory/createGlobalTradeItemNumber code)
      :monarch (BarcodeFactory/createMonarch code)
      :nw7 (BarcodeFactory/createNW7 code)
      :pdf417 (BarcodeFactory/createPDF417 code)
      :scc14-shipping-code (BarcodeFactory/createSCC14ShippingCode code)
      :shipment-id-num (BarcodeFactory/createShipmentIdentificationNumber code)
      :sscc18 (BarcodeFactory/createSSCC18 code)
      :ucc128 (BarcodeFactory/createUCC128 code (:app-id args))
      :usd3 (BarcodeFactory/createUSD3 code (:require-checksum args))
      :usd4 (BarcodeFactory/createUSD4 code)
      :usps (BarcodeFactory/createUSPS code))))


(defn image-format
  "Gets the file extension and validates it"
  [^String file]
  (let [ext (re-find #"\.(?i)(jpe?g|gif|png)$" file)]
    (if-not ext
      (throw (IllegalArgumentException. "Output file is not supported image type. Use jpg, gif or png"))
      (-> ext last string/lower-case))))


(defn- ^Boolean writer!-impl
  "General wrapper implementation around ImageIO/write"
  [^Barcode barcode ^String format output]
  (if-not (ImageIO/write (BarcodeImageHandler/getImage barcode)
                         format
                         output)
    (throw (Exception. (str "Unable to write the barcode into " into)))
    true))


(defn ^Boolean write!
  "Writes the generated barcode into a file our into an output stream"
  ([^Barcode barcode output-file]
   (let [file (if-not
                (instance? File output-file)
                (io/file output-file))]
     (writer!-impl barcode
                   (image-format (.getName file))
                   file)))
  ([^Barcode barcode ^OutputStream output-stream format]
   (writer!-impl barcode
                 (condp = format
                     :jpg "jpg"
                     :gif "gif"
                     :png "png")
                 output-stream)))
