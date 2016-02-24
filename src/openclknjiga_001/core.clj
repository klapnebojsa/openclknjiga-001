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
(facts
  (let [notifications (chan)
        follow (register notifications)work-sizes (work-size [1])
        host-msg (direct-buffer 16)
        program-source 
        "__kernel void hello_kernel(__global char16 *msg) {\n    *msg = (char16)('H', 'e', 'l', 'l', 'o', ' ',
   'k', 'e', 'r', 'n', 'e', 'l', '!', '!', '!', '\\0');\n}\n"
        ;pr-sor (slurp (io/reader "examples/double-test.cl"))
        pr-sor (slurp (io/reader "examples/exam002.clj"))
      ]
    
 
  (try     ;"PLATFORME ----------------------------------------------------------" 
    (with-release [num-platforms (num-platforms)
                   ;"-----Platforma 1"
                   platformsone (first (platforms))
                   versionone (version platformsone)
                   icd-suffix-khrone (icd-suffix-khr platformsone)
                   extensionsone (extensions platformsone)
                   name-infoone (name-info platformsone)
                   profileone (profile platformsone)
                   vendorone (vendor platformsone)          
                  ;"-----Platforma 2"         
                   platformstwo (second (platforms))
                   versiontwo (version platformstwo)
                   icd-suffix-khrtwo (icd-suffix-khr platformstwo) 
                   extensionstwo (extensions platformstwo)                   
                   name-infotwo (name-info platformstwo)
                     profiletwo (profile platformstwo)
                     vendortwo (vendor platformstwo)                   

            ;"DEVICES------------------------------------------------------------"
                   ;"--------Devices u platformi 1"
                   devsone (devices platformsone)    ;problem zato sto je izlaz vektor
                   devsone1 (first (devices platformsone))
                   devsone2 (second (devices platformsone))                   
                   devsonegpu (first (devices platformsone :gpu))  ;mora first zato sto je bez njega vrednost devsonegpu u obliku vektora
                                                                   ;a komanda name-info ne moze da cita vektor vec samo clan vektora (ovde je to prvi i jedini)
                   devsonecpu (first (devices platformsone :cpu))
                   devsoneacc (first (devices platformsone :accelerator))
                   devicesone* (devices* platformsone 16)
                   name-infodevonegpu (name-info devsonegpu)
                   opencl-c-ver-devonegpu (opencl-c-version devsonegpu)  ;sva moguca polja na https://crossclj.info/ns/uncomplicate/clojurecl/0.3.0/uncomplicate.clojurecl.info.html#_info~cl_device_id~Info
                   id-infodevonegpu (extensions devsonegpu)    ; ...      u sekciji info                   
                   name-infodevonecpu (name-info devsonecpu)
                   ;name-infodevoneacc (name-info devsoneacc)    ;puca/ne radi za nil
                   
                   ;"--------Devices u platformi 2"                   
                   devstwo (first (devices platformstwo))
                   devstwogpu (first (devices platformstwo :gpu))
                     devstwocpu (first (devices platformstwo :cpu))
                     devstwoacc (first (devices platformstwo :accelerator))                   
                   devicestwo* (devices* platformstwo 8)                   
                   name-infodevtwogpu (name-info devstwogpu)
                   
            ;"CONTEXTS------------------------------------------------------------"  
                  ;"---Context 1"               
                   ;ctxone (context (devices (first (platforms))))   ;devsone-izlaz je vektor pa zato ponavaljamo celu komandu a ne uzimamo promenjivu           
                   ;ctxone (context devsone)  ;preko prethodih ucitanih devices. Tada context ima dva uredjaja
                           ;Greska koja nastaje:
                           ;     Stack dump:
                           ;     0.	CTHeader.h:1050:91: current parser token ';'
                     
                   ;U context je  dodat samo jedan uredjaj tj procesor. U prethodnom redu su dodata dva uredjaja ali nece da radi (puca)
                   ;nece da radi sa integrisanom grafickom kartom a neintegrisana grafika pripada drugoj platform tako da nemoze da se kombinuje sa procesorom
                   ;samo neintegrisana grafika nece da se smesti u context - izbacuje gresku
                   ctxone (context [devsone1])
                   
                   infoctxone (info ctxone)
                   reference-countone (reference-count ctxone)
                   propertiesone (properties ctxone)
                  ;"--------Devices u context 1"            
                   devices-in-context (devices-in-context ctxone) 
                   num-devices-in-context (num-devices-in-context ctxone)
                   ;iz konteksta uzimamo uredjaje, a ne iz devices, koji su tu jer smo ih ponovo definisali
                   devctxone1 (first devices-in-context)
                   ;devctxone2 (second devices-in-context)
                   name-infodevone1 (name-info devctxone1)
                   ;name-infodevone2 (name-info devctxone2)                   
                  ;"---Context 2"               
                   ;ctxtwo (context (devices (second (platforms))))
                   ;infoctxtwo (info ctxtwo)
                  ;"--------Devices u context 2"                     
                  
             ;"PROGRAM--------------------------------------------------------------"             
                   ;cqueue (command-queue-1 ctxone devsone1)
                   ;cl-msg (cl-buffer ctxone 16 :write-only)                   
                   prog (build-program! (program-with-source ctxone [program-source]))
                   infoprog (info prog)
                   infoprog1 (program-context prog)                   
                   infoprog2 (program-num-devices prog)                   
                   infoprog3 (reference-count prog)                   
                   infoprog4 (program-devices prog)                                    
                   infoprog6 (binary-sizes prog)
                   infoprog7 (binaries prog)                   
                   infoprog8 (program-num-kernels prog)                   
                   infoprog9 (kernel-names prog)                   
                   ;hello-kernel (kernel prog "hello_kernel")
                   ;read-complete (event)

                   
                   ;devs (devices (first (platforms)))
                             ;dev (first devs)
                             ;;dev (first (devices (first (platforms))))
                             ;ctx (context [dev])
                             ;cqueue (command-queue-1 ctx dev)
                             ;cl-msg (cl-buffer ctx 16 :write-only)
                             ;prog (build-program! (program-with-source ctx [program-source]))
                             ;hello-kernel (kernel prog "hello_kernel")

                     ;read-complete (event)
                     ]
        (println "----------------------------------------------------------PLATFORME ")
        (println "num-platforms: " num-platforms) 
        (println "---------------PLATFORMA 111111111111-------------------------------")      
        (println "platformsone: " platformsone)
        (println "versionone: " versionone)
        (println "icd-suffix-khrone: " icd-suffix-khrone)      
        (println "extensionsone: " extensionsone)
        (println "name-infoone: " name-infoone)      
        (println "profileone: " profileone)      
        (println "vendorone: " vendorone)
        (println "")
      
        (println "---------------PLATFORMA 222222222222-------------------------------")         
        (println "platformstwo: " platformstwo) 
        (println "versiontwo: " versiontwo) 
        (println "icd-suffix-khrtwo: " icd-suffix-khrtwo)
        (println "extensionstwo: " extensionstwo)
        (println "name-infotwo: " name-infotwo)      
        (println "profiletwo: " profiletwo)      
        (println "vendortwo: " vendortwo)
        (println "DOVDE ----------------------------------------------------- PLATFORME")
        (println "") 
        (println "")
      
        (println "------------------------------------------------------------DEVICES")  
        (println "---------------DEVICE U PLATFORM 1111111111111111 ------------------")
        (println "devsone: " devsone)
        (println "devsone1: " devsone1)
        (println "devsone2: " devsone2)      
        (println "devsonegpu: " devsonegpu)
        (println "devsonecpu: " devsonecpu)
        (println "devsoneacc: " devsoneacc)
        (println "devicesone*: " devicesone*)
        (println "-----------Device 1")      
        (println "name-infodevonegpu: " name-infodevonegpu)
        (println "opencl-c-ver-devonegpu: " opencl-c-ver-devonegpu)
        (println "id-infodevonegpu: " id-infodevonegpu) 
        (println "       ...")
        (println "-----------Device 2")
        (println "name-infodevonecpu: " name-infodevonecpu)
        (println "       ...")
      
        (println "")
        (println "---------------DEVICE U PLATFORM 2222222222222222 --------------------")
        (println "devstwo: " devstwo)
        (println "devstwogpu: " devstwogpu)
        (println "devstwocpu: " devstwocpu)
        (println "devstwoacc: " devstwoacc)
        (println "devicestwo*: " devicestwo*)
        (println "name-infodevtwogpu: " name-infodevtwogpu)      
        (println "DOVDE ----------------------------------------------------- DEVICES")

        (println "")
        (println "------------------------------------------------------------CONTEXT") 
        (println "ctxone: " ctxone)
        (println "infoctxone: " infoctxone)
        (println "reference-countone: " reference-countone)
        (println "propertiesone: " propertiesone)       
        (println "---------------DEVICE U CONTEXT 1111111111111111 ------------------")
        (println "devices-in-context: " devices-in-context)
        (println "num-devices-in-context: " num-devices-in-context)      
        (println "devctxone1: " devctxone1)
        ;(println "devctxone2: " devctxone2)      
        (println "name-infodevone1: " name-infodevone1) 
        ;(println "name-infodevone2: " name-infodevone2) 
        
        (println "---------------DEVICE U CONTEXT 2222222222222222 ------------------")
        ;(println "ctxtwo: " ctxtwo)
        ;(println "infoctxtwo: " infoctxtwo)     
        (println "DOVDE ----------------------------------------------------- CONTEXT")

        
        (println "----------------------------------------------------------- PROGRAM")
        (println "infoprog: " infoprog)
        (println "infoprog1: " infoprog1) 
        (println "infoprog2: " infoprog2)
        (println "infoprog3: " infoprog3) 
        (println "infoprog4: " infoprog4) 
        (println "infoprog6: " infoprog6)
        (println "infoprog7: " infoprog7) 
        (println "infoprog8: " infoprog8)
        (println "infoprog9: " infoprog9)
        (println "DOVDE ----------------------------------------------------- PROGRAM")

        
        ;(println pr-sor)
        
        ;(println "dev1: " dev)
        ;(println "ctx1: " ctx)
        ;(println "cqueue1: " cqueue) 
        ;(println "prog1: " prog)
        ;(println "hello-kernel1: " hello-kernel)
        
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
        (release platforms)
        (release devices)
        (release ctxone)
        (release prog)
        
        ;(release-seq (platforms)) 
        ;(Releaseable (platforms))      
        
      )
    (catch Exception e (println "Greska 11111111: " (.getMessage e)))
  )
  )
)