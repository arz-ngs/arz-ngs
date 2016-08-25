#!/bin/sh

a=0
while [ "$a" -lt 100 ]    # this is loop1
do
    b=0
    while [ "$b" -lt 100 ]  # this is loop2
    do
       echo "adding instance instance_$b for service $a"
	   service="service_$a"
	   instance="instance_$b"
	   first='{"environmentName":"env1", "hostName":"host1", "serviceName":"'
	   second='", "instanceName":"'
	   third='", "script": {"pathStart":"init.d/arctis start", "pathStop":"init.d/arctis stop", "pathRestart":"init.d/arctis restart", "pathStatus":"init.d/arctis status"}}'
	   json_string="$first$service$second$instance$third"
	   echo "$json_string"
	   curl -H "Content-Type: application/json" -X PUT -d "$json_string" http://localhost:8080/ngs/api/v1/instances
       b=`expr $b + 1`
    done
    echo
    a=`expr $a + 1`
done
