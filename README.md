# RFC Browser (Example for refactoring Java App)

This application is an example for live coding refactor.

- Separate packages
- Remove unused code
- Create service layer
- Clean controller code

In oder to get the files to test this application you can go to the following [link](https://www.rfc-editor.org/retrieve/bulk/) and download.

## Technologies

- Maven 3.6.3
- Spring Boot 2.6.7
- Java 17
- VueJS
- Axios


## How to run this project

Setup `RFC_DIR_PATH` env variable pointing to your `RFC` downloaded sources and then run the following command.

```bash
./mvnw spring-boot:run
```

After running this project you visit the url http://localhost:8080