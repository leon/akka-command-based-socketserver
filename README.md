# Akka Command based Socket Server

I played around a bit with akka IO and thought I'd share how I setup a simple 4 char command socket server

There are a couple of commands that show how you use the iteratees to convert from the socket input.
and also how you can respond to the commands differently.

 * EXIT
   Closes connection to socket server

 * ECHO Hello World
   Echoes everything after ECHO back to the socket

 * DATE 2012-01-01
   Parses date input YYYY-mm-dd to a jodatime and outputs to socket in YY/MM/DD format

 * RAND 5 of 10
   Generates a 5 column, 10 row grid with numbers between 0 and 9

## To run
Open two terminals

In one type

    sbt run

this will start the socket server and output that it's listening on a specific port

In the other type

    telnet 127.0.0.1 12345

Change 12345 to the port that the server is listening too.

If everything works you should now be connected to the socket server and can try out the different commands.
