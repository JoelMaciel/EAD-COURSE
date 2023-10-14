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


## Clone the repository
git clone https://github.com/JoelMaciel/EAD-COURSE.git

## Navigate to the project directory
cd EAD-COURSE

## Build the project using Maven
./mvnw clean install

## Run the application
./mvnw spring-boot:run


## Request Images


#### Get All Courses

![Captura de tela de 2023-10-14 10-02-30](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/dbc9734d-73f1-4527-8601-77c72faef9ff)

#### Saved Course

![Captura de tela de 2023-10-14 10-04-50](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/3c86cb7e-8753-42d8-b25d-83e858882a33)

#### Request Course Invalid.
![Captura de tela de 2023-10-14 10-06-05](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/2d9367c6-7fb4-4d0c-88c8-a5f9ead0a67b)


#### Update Course

![Catura de tela de 2023-10-14 10-08-37](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/11c652e5-37a0-4283-a8f5-ebd764775208)

#### UpdateRequest Invalid.

![Captura de tela de 2023-10-14 10-11-17](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/c50148fe-25f7-4ace-8743-2c43a041a7db)

#### FindById whit UserId Invalid.

![Captura de tela de 2023-10-14 10-13-22](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/1f4dfc1a-7b43-491d-8a11-244df26f67e4)


#### Delete Course, status code 204

![Captura de tela de 2023-10-14 10-15-04](https://github.com/JoelMaciel/EAD-COURSE/assets/77079093/88bca5cf-a9cc-45d8-aac5-659e389a1b1a)
