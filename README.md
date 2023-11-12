# MICROSERVICE COURSE

[![NPM](https://img.shields.io/npm/l/react)](https://github.com/JoelMaciel/Product-Catalog/blob/readm/LICENCE)

## About the Project

**Microservice Course** is a service designed to manage courses, modules, and lessons. Administrators can register users in the desired courses through this service.

> **Note:** The project is still under construction. This microservice is implemented synchronously. An asynchronous implementation will be introduced in another branch in the future.

## Technology Stack

- **Core:** Java with Spring Framework
- **Service Discovery:** Eureka
- API Gateway
- **Message Broker:** RabbitMQ
- **Documentation & Standards:** OpenAPI, Swagger, HATEOAS
- **Security:** Spring Security, JWT
- **Data Persistence:** Spring Data JPA with MySQL
- **Database Versioning:** Flyway

## Getting Started

### Prerequisites

- Java Development Kit (JDK 11)
- MySQL Database Server
- RabbitMQ Server

### Clone the Repository
git clone https://github.com/JoelMaciel/EAD-COURSE.git

### Navigate to the Project Directory
cd EAD-COURSE
### Build the Project using Maven
./mvnw clean install
### Run the Application
./mvnw spring-boot:run

## Request Images

#### Microservice Course registered in Service Discovery Eureka and queued in RabbitMQ

![Captura de tela de 2023-11-12 20-41-16](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/a2dd17f6-f001-4350-9698-779858d7b7c9)
![Captura de tela de 2023-11-12 20-41-51](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/22b1d265-8b0b-4adf-8dca-b07229710965)



#### Get All Courses
![Captura de tela de 2023-10-14 10-02-30](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/ca057d29-2883-48f8-9e71-ceb7babd3312)


#### Saved Course Successfully
![Captura de tela de 2023-10-14 10-04-50](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/97069bdf-4bd8-4979-822a-37978002cd1f)


#### Saved Course whit CourseRequest Invalid.
![Captura de tela de 2023-10-14 10-06-05](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/e08bcaee-1ecc-4a6b-82ea-4c833fedc32a)


#### Update Course
![Captura de tela de 2023-10-14 10-08-37](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/df5eed60-386a-4e80-9d54-185420857816)


#### Update Course whit CourseRequest Invalid.
![Captura de tela de 2023-10-14 10-11-17](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/971edd4f-1045-4c66-aacb-a1a91fc77b5b)


#### FindById whih courseId Invalid.
![Captura de tela de 2023-10-14 10-13-22](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/27f82434-744f-4c09-bbf0-0261308451ea)


#### Delete Course whit Status Code 204.
![Captura de tela de 2023-10-14 10-15-04](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/05c07697-d9fb-4a40-8301-666169fac68e)


#### Integration Tests
![Captura de tela de 2023-10-14 10-21-49](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/61939ecd-2ae5-4dcd-9446-5f4e909940c5)

#### Unit Tests

![Captura de tela de 2023-10-14 10-28-54](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/df894930-f443-42fa-9fde-3627c77bf0a7)


