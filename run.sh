#!/usr/bin/env bash
#This script calls the Server and Client classes in different Terminal  windows so you can see the communication as it happens.
DIR=`pwd`
echo $DIR
osascript -e "tell application \"Terminal\" to do script with command \"java -cp $DIR/target/nrcc-1.0-SNAPSHOT.jar com.nr.cc.server.NumbersServer\""
osascript -e "tell application \"Terminal\" to do script with command \"java -cp $DIR/target/nrcc-1.0-SNAPSHOT.jar com.nr.cc.client.NumbersClient\""