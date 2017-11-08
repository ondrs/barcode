(ns ondrs.barcode-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [ondrs.barcode :refer :all])
  (:import (net.sourceforge.barbecue Barcode)))


(def temp-dir
  (let [dir (str (System/getProperty "java.io.tmpdir") "/barcode-test")]
    (.mkdir (io/file dir))
    dir))

(def o-stream (io/output-stream (str temp-dir "/stream")))

(deftest image-format-test
  (testing "valid extension"
    (is (=
          "jpg"
          (image-format "sas/sas.dsd/dsad.img.jpg")))
    (is (=
          "jpeg"
          (image-format "sas/sas.dsd/dsad.img.jpEG"))))
  (testing "invvalid extension"
    (is (thrown?
          IllegalArgumentException
          (image-format "sas/sas.dsd/dsad.img.txt")))))


(deftest generate-test
  (testing "valid barcode"
    (is (instance? Barcode
                   (generate :code128 "code128"))))
  (testing "invalid barcode"
    (is (thrown? IllegalArgumentException
                 (generate :nope "code128")))))


(deftest write!-test
  (testing "writing to an image file"
    (is (true?
          (-> (generate :code128 "code")
              (write! (str temp-dir "code.jpg"))))))
  (testing "writing to a wrong file"
    (is (thrown? IllegalArgumentException
                 (-> (generate :2of7 "123")
                     (write! (str temp-dir "code.tex"))))))
  (testing "writing to an output stream"
    (is (true?
          (-> (generate :code128 "code")
              (write! o-stream :jpg)))))
  (testing "writing to an output stream with wrong file format"
    (is (thrown? IllegalArgumentException
          (-> (generate :code128 "code")
              (write! o-stream :black))))))