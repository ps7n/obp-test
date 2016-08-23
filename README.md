# obp-test

Simple app for testing 'South' side in the OBP API project (https://openbankproject.com).
Currently supports only resources without OAuth authorization.

---------------------------------------------
Quick Start
---------------------------------------------

Before you start you need to have access to a running OBP API instance.

Running the tool:

1. Go to test folder
2. Compile with: 
	mvn clean install
3. Run with:
	java -jar target\test-1.0.0-SNAPSHOT-jar-with-dependencies.jar
4. When asked insert the:
	Resource url you want to test 
	e.g. 
	http://localhost:8080/obp/v2.1.0/banks/bigbank
	
	Message that should be returned to the 'North' side
	e.g.
	{  "id" : "bank",  "short_name" : "bigbank",  "full_name" : "Big Bank",  "logo" : "bigbank.png",  "website" : "www.bigbank.com"}
	
