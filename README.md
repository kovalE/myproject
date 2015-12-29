[![Build Status](https://travis-ci.org/kovalE/myproject.svg)](https://travis-ci.org/kovalE/myproject)

## Simple text editor

###To edit and run appplication, you need : ###

* JDK >= 8
* Maven
* Eclipse with maven plugin (or another IDE with maven support)

Make sure that mvn, java and git are in your system PATH and JAVA_HOME is configured correctly.

###Just to run application, you can :###

1. Open command line
2. Enter next commands to run tests and generate .jar file:

   ```sh    
   $ mvn dependency:copy-dependencies
   $ mvn package
   ```
3. Enter `java -jar target/myproject-{version number}.jar` to run application (use proper version number)
