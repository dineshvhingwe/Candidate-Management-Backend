# Candidate-Management-Backend
Spring Boot based backend to manage candidates.




# Dockerization Guide

$ sudo ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=<IMAGE_NAME>
   ==> Directly create image by spring boot maven plugin (build-image) phase
$ sudo docker run -it -p 8080:8080 <IMAGE_NAME>
   ==> Run the image by mapping the ports 80 to 8080
$ sudo docker ps
   ==> check for container status
