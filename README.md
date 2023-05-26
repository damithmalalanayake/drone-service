# drone-service

###Assumptions
-Drone Status will be in IDLE mode at the initial point.  
-Drone Status will be changed to LOADED mode after loading medications to the drone.

###Design
JDK - Java 17  
DB - H2 in-memory database  
Data - preloaded data through data.sql file

###Build
-mvn clean install -Dspring.profiles.active=test

###Run
-mvn spring-boot:run  
-swagger is enabled http://localhost:8002/swagger-ui

###Questions

-Where we should use LOADING state? 

###Missed Requirements

-save image for medication (just added image path for now)  
-periodic task to output battery levels into a file
