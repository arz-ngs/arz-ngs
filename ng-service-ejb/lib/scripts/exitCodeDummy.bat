@echo off
REM counts the arguments to check if we can proceed with the following code.
set argC=0
for %%x in (%*) do Set /A argC+=1

if %argC% LSS 1 (
	GOTO :EOF
)

if %1==status (
	echo Frage den Status fuer Serviceinstance ab... 
) 
if %1==start (
	echo Starte Serviceinstance... 
)
if %1==stop (
	echo Stoppe Serviceinstance... 
)
if %1==restart (
	echo Starte Serviceinstance neu... 
)

if %argC% LSS 3 (
	GOTO :EOF
)

if %2==predict (
	if %1==status (
		if %3==0 (
			echo Serviceinstance ist gestartet.
			exit 0
		)
		if %3==3 (
			echo Serviceinstance ist gestoppt. 1>&2
			exit 3
		)
		if %3==17 (
			echo Serviceinstance ist gestoppt. 1>&2
			exit 17
		)
		if %3==8 (
			echo Serviceinstance wird gestartet bzw. gestoppt. 1>&2
			exit 8
		)
		exit %3
	) else (
		if %3==0 (
			echo Aktion erfolgreich ausgefuehrt.
			exit 0
		)
		if %3==1 (
			echo Fehler beim Ausfuehren der Aktion. 1>&2
			exit 1 
		)
		if %3==2 (
			echo Aktion nicht bekannt. 1>&2
			exit 2 
		)
		if %3 GEQ 3 (
			echo Unbekannter Fehler! 1>&2
			exit %3
		)
		exit %3
	)	
)
