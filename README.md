# Numbers Server & Client
This project contains a Server and a Client classes.

* The Server accepts a max number (3) of connections and responds with the SECONDS int value of the current time. The connection is closed with a delay of 5 seconds, this is to demonstrate the server can only accept a limited number of connections at a time.
* The Client sends a number of requests (max number of server requests plus one), receives a response from the server and prints the value to the console.

The output from the client shows the first 3 responses are received within milliseconds from each other; the fourth response is received with a delay of 5 seconds. This shows that the server only proceses the fourth request after releasing one of the connections it initially received.

```
#########################################################
# Submitting 4 requests to the server on localhost:8087 #
#########################################################
14
14
14
19
```

## Requirements
Mac OSX 10.0+
Java 1.7 SDK
Maven 3.1+

## Build & Run
To run the project:
1. Go to the folder where the project resides
2. Run ```mvn package```
3. Run ```./run.sh``` bash script
