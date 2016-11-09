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
	   		    c='", "script": {"pathStart":"'
				e='", "pathStop":"'
				f='", "pathRestart":"'
				g='", "pathStatus":"'
				h='"}}"'
			   
			    SCRIPT=$(readlink -f "$0")
				# Absolute path this script is in, thus /home/user/bin
				SCRIPTPATH=$(dirname "$SCRIPT")
				
				string_to_replace_d=D:
				SCRIPTPATH="${SCRIPTPATH/\/d/$string_to_replace_d}"
				string_to_replace_c=C:
				SCRIPTPATH="${SCRIPTPATH/\/c/$string_to_replace_c}"
				SCRIPTPATH="$SCRIPTPATH/exitCodeDummy.bat"
				
				predict="predict 0"
				start_path="$SCRIPTPATH start $predict"
				stop_path="$SCRIPTPATH stop $predict"
				restart_path="$SCRIPTPATH restart $predict"
				status_path="$SCRIPTPATH status $predict"
				
				json_string="$x$env$y$host$a$service$b$instance$c$start_path$e$stop_path$f$restart_path$g$status_path$h" 
				echo "adding instance: "
				echo "$json_string"
	   		    curl -u admin:admin1234 -H "Content-Type: application/json" -X PUT -d "$json_string" http://localhost:8080/ngs/api/v1/instances
				
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