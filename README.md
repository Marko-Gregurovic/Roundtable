# Roundtable

This project uses Spring Boot version 2.6.7.

## Building the application
### Prerequisites
To build the native application native image compiler is required.
Instructions can be found on this page: https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/ (10.5.2022.)

Maven installation is required. Version 3.6.3 was used during development.

Docker is also required as it is used by the maven spring native plugin to create the native image. The created image can then be run with docker, podman...

### Creating the binary
The binary can be created by running the following command from the project root:
```
mvn -Pnative -DskipTests package
```
This creates a binary file PROJECT_ROOT/target/native

### Run the binary
The binary can be run from the project root with the following command which also specifies which services to target:
```
./target/native -Dtarget.services=http://service1:8080,http://service2:8080
```
  
### Creating the docker image
Image can be created by running the following command from the project root:
```
mvn spring-boot:build-image
```
  
### Start the container
The created image can then be started with the following command.
```
docker run --rm -p 8080:8080 native:0.0.1-SNAPSHOT -Dtarget.services=http://service1:8080,http://service2:8080
```
