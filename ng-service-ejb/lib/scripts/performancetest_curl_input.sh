#!/bin/sh

count_per_element=2
count_service=0
while [ "$count_service" -lt $count_per_element ]   
do
    count_env=0
    while [ "$count_env" -lt $count_per_element ]  
    do
        count_host=0
    	while [ "$count_host" -lt $count_per_element ]  
    	do
       		count_instance=0
    		while [ "$count_instance" -lt $count_per_element ]  
    		do
			   service="service_$count_service"
			   env="env_$count_env"
			   host="host_$count_host"
			   instance="instance_$count_instance"
			   
			   x='{"environmentName":"'
			   y='", "hostName":"'
			   a='", "serviceName":"'
	   		   b='", "instanceName":"'
	   		   c='", "script": {"pathStart":"init.d/arctis start", "pathStop":"init.d/arctis stop", "pathRestart":"init.d/arctis restart", "pathStatus":"init.d/arctis status"}}'
	           
	           json_string="$x$env$y$host$a$service$b$instance$c"
	   	 	   
	   	 	   echo "adding instance: "
	   	 	   echo "$json_string"
	   		   curl -H "Content-Type: application/json" -X PUT -d "$json_string" http://localhost:8080/ngs/api/v1/instances
			   
			   count_instance=`expr $count_instance + 1`
		    done 
		    echo
		    count_host=`expr $count_host + 1`
		done
		echo
		echo
		count_env=`expr $count_env + 1`
    done
    echo
    echo
    echo
    count_service=`expr $count_service + 1`
done

exit 0;