(ns openclknjiga-001.core
  (:require [midje.sweet :refer :all]
            [clojure.java.io :as io]
            [clojure.core.async :refer [chan <!!]]
            [uncomplicate.clojurecl
             [core :refer :all]
             [info :refer :all]
             [legacy :refer :all]
             [constants :refer :all]
             [toolbox :refer :all]
             [utils :refer :all]]
            [vertigo
             [bytes :refer [direct-buffer byte-seq]]
             [structs :refer [wrap-byte-seq int8]]])
  (:import [org.jocl CL]))

(let [notifications (chan)
      follow (register notifications)work-sizes (work-size [1])
      host-msg (direct-buffer 16)
      program-source 
      "__kernel void hello_kernel(__global char16 *msg) {\n    *msg = (char16)('H', 'e', 'l', 'l', 'o', ' ',
   'k', 'e', 'r', 'n', 'e', 'l', '!', '!', '!', '\\0');\n}\n"
      ]
  (try
    (with-release [num-platforms (num-platforms)
                   ;platform (*platform*)
                   platformsone (first (platforms))
                   versionone (version (first (platforms)))
                   icd-suffix-khrone (icd-suffix-khr (first (platforms)))
                   extensionsone (extensions (first (platforms)))
                   name-infoone (name-info (first (platforms)))
                   profileone (profile (first (platforms)))
                   vendorone (vendor (first (platforms)))
                   ;platformone-info (platform-info)               
                   
                   platformstwo (second (platforms))
                   versiontwo (version (second (platforms)))
                   icd-suffix-khrtwo (icd-suffix-khr (second (platforms))) 
                   name-infotwo (name-info (second (platforms)))
                   profiletwo (profile (second (platforms)))
                   vendortwo (vendor (second (platforms)))                   
                   ;platformtwo-info (platform-info [1])
                   
                   
                   
                   
                   
                   devs (devices (first (platforms)))
                   dev (first devs)
                   ;dev (first (devices (first (platforms))))
                   ctx (context [dev])
                   cqueue (command-queue-1 ctx dev)
                   cl-msg (cl-buffer ctx 16 :write-only)
                   prog (build-program! (program-with-source ctx [program-source]))
                   hello-kernel (kernel prog "hello_kernel")

                   ;read-complete (event)
                   ]

      (println "PLATFORME ----------------------------------------------------------")
      (println "num-platforms: " num-platforms) 
      (println "PLATFORMA 111111111111----------------------------------------------")      
      (println "platformsone: " platformsone)
      (println "versionone: " versionone)
      (println "icd-suffix-khrone: " icd-suffix-khrone)      
      (println "extensionsone: " extensionsone)
      (println "name-infoone: " name-infoone)      
      (println "profileone: " profileone)      
      (println "vendorone: " vendorone)
      
      (println "PLATFORMA 222222222222----------------------------------------------")         
      (println "platformstwo: " platformstwo) 
      (println "versiontwo: " versiontwo) 
      (println "icd-suffix-khrtwo: " icd-suffix-khrtwo)
      (println "extensionstwo: " extensionsone)
      (println "name-infotwo: " name-infotwo)      
      (println "profiletwo: " profiletwo)      
      (println "vendortwo: " vendortwo)
      (println "DOVDE ----------------------------------------------------- PLATFORME")







      
      (println "dev1: " dev)
      (println "ctx1: " ctx)
      (println "cqueue1: " cqueue) 
      (println "prog1: " prog)
      (println "hello-kernel1: " hello-kernel)
      
      ;(println "dev1: " info dev)
      ;(println "ctx1: " info ctx)
      ;(println "cqueue1: " info cqueue) 
      ;(println "prog1: " info prog)
      ;(println "hello-kernel1: " info hello-kernel)
      
      
      ;(println "read-complete1: " info read-complete)

      ;(set-args! hello-kernel cl-msg)
      ;(enq-nd! cqueue  hello-kernel work-sizes)
      ;(enq-read! cqueue cl-msg host-msg)
      ;(apply str (map char
      ;               (wrap-byte-seq int8 (byte-seq host-msg))))
      )
    (catch Exception e (println "Greska 11111111: " (.getMessage e)))
  )
  )