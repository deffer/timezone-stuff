timezone-stuff
==============

Example script processing file with dates and coordinates


Compiling
=========

    mvn compile


Running
=======
Application expects two arguments:

* file name
* \[OPTIONAL\] csv file separator 
  
To run usin maven:

    mvn exec:java -Dexec.mainClass="org.convert.App" -Dexec.args="./test.data ,"

This will produce output file:

>./test.data.out

Notes
=====
* I had to create account at GeoNames to access free service that provides time zone information from coordinates.
Expect errors if account limit is reached.
* Not using Spring because there is not enough time, also trying to keep app small.
* Not using log4j or slf4j for the same reason.
* Not enough time for proper error handling and logging.
