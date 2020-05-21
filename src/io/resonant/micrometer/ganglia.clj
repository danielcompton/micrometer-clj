(ns io.resonant.micrometer.ganglia
  (:require
    [io.resonant.micrometer :refer [create-registry]])
  (:import (io.micrometer.ganglia GangliaMeterRegistry GangliaConfig)
           (java.util.concurrent TimeUnit)
           (info.ganglia.gmetric4j.gmetric GMetric$UDPAddressingMode)
           (io.micrometer.core.instrument.step StepRegistryConfig)
           (java.time Duration)
           (io.micrometer.core.instrument Clock)))

(defmethod create-registry :ganglia [cfg]
  (GangliaMeterRegistry.
    (reify
      GangliaConfig
      (get [_ _] nil)
      (durationUnits [_] (TimeUnit/valueOf (.toUpperCase (name (get :duration-units cfg :MILLISECONDS)))))
      (addressingMode [_] (GMetric$UDPAddressingMode/valueOf (.toUpperCase (name (get :addressing-mode cfg :MULTICAST)))))
      (ttl [_] (:ttl cfg 1))
      (host [_] (:host cfg "localhost"))
      (port [_] (:port cfg 8649))
      StepRegistryConfig
      (step [_] (Duration/ofMillis (:step cfg 60000)))
      (enabled [_] (:enabled? cfg true))
      (numThreads [_] (:num-threads cfg 2))
      (connectTimeout [_] (Duration/ofMillis (:connect-timeout cfg 1000)))
      (readTimeout [_] (Duration/ofMillis (:read-timeout cfg 10000))))
    (Clock/SYSTEM)))
