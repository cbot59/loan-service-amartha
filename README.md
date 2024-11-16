# Loan Service Amartha

Problem example on take home test of Amartha's hiring process.

## Getting Started

These instructions will give you a copy of the project up and running on
your local machine for development and testing purposes.

### Prerequisites

Requirements for the software and other tools to build and test

- [Maven](https://maven.apache.org/download.cgi?.)
- [Java 21](https://adoptium.net/download/)

### Installing

Execute run via `spring-boot-maven` plugin

    mvn spring-boot:run

The application successfully run when the logs show something like

```
2024-11-16T13:05:36.002+07:00  INFO 87182 --- [loan-service-amartha] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2024-11-16T13:05:36.002+07:00  INFO 87182 --- [loan-service-amartha] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 455 ms
2024-11-16T13:05:36.215+07:00  INFO 87182 --- [loan-service-amartha] [           main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint beneath base path '/actuator'
2024-11-16T13:05:36.245+07:00  INFO 87182 --- [loan-service-amartha] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path '/'
2024-11-16T13:05:36.252+07:00  INFO 87182 --- [loan-service-amartha] [           main] c.j.l.LoanServiceAmarthaApplication      : Started LoanServiceAmarthaApplication in 0.872 seconds (process running for 1.007)
```

## Running the tests

Execute test run via maven

    mvn verify

## API specs

API specification can be seen on `{{baseUrl}}/swagger-ui/index.html#/` once the application is running

## Built With

- [Java](https://www.java.com/en/)
- [Spring Boot](https://spring.io/projects/spring-boot)

## Authors

- **Rivaldi**
