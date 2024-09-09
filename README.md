# GeoSnap - Image Uploader with Dropbox Integration
GeoSnap is a full-stack Java application that allows users to upload images with geographic metadata, store them in Dropbox, and view them with their associated coordinates. The application is built using Spring Boot, Thymeleaf for the frontend, and integrates Dropbox for cloud storage of images.
## Features
* **Image Upload** : Upload images along with latitude and longitude metadata.
* **Dropbox Integration** : Images are stored in a Dropbox folder with dynamically generated shared links.
* **View Images** : Display a list of uploaded images with their coordinates.
* **CI/CD Pipeline** : A simple pipeline that builds the Java application and creates a jar file as an artifact.
* **Unit Testing** : Basic unit tests for the image service, with some limitations due to Dropbox access tokens.
## Pre-requisites
  1. **Java 11+**: Ensure Java is installed on your machine.
  2. **Maven**: Required for building and running the application.
  3. **MySQL**: Set up a local MySQL database for the application.
  4. **Dropbox Account**: Create a Dropbox account and generate an access token.
## Setup
### 1. Clone the repository
`git clone https://github.com/natli-think/geoSnap.git` <br>
`cd geoSnap`
### 2. Set Up the MySQL Database and Dropbox
1. Log in to your MySQL server<br>
`mysql -u root -p`
2. Create a new database <br>
`CREATE DATABASE geoSnap;`
3. Update the `src/main/resources/application.properties` file with your MySQL credentials<br>
```
spring.datasource.url=jdbc:mysql://localhost:3306/geoSnap
spring.datasource.username=your-username
spring.datasource.password=your-password
```
4. In the same file, update the dropbox access token <br>
`dropbox.access.token= enter_token_here`
### 3. Build and Run the Application
```
mvn clean install
java -jar target/geoSnap-0.0.1-SNAPSHOT.jar
```
### 4. Access the Application
Go to browser and type <br>
`http://localhost:8080/geoSnap`

## Project Structure
* ImageController.java: Handles HTTP requests related to image uploading and retrieval.
* ImageService.java: Contains the logic for saving images and interacting with Dropbox.
* Image.java: Represents the Image entity with fields for metadata.
* ImageRepository.java: Interface for CRUD operations on the Image entity.
* src/main/resources/static/: Contains static resources like images and CSS files.
* src/main/resources/templates/: Thymeleaf templates for rendering the frontend views.
* .github/workflows/: Contains the CI/CD pipeline configuration.

## Limitations
* Secret Management: Secrets such as the Dropbox access token are currently stored in .env and application.properties. For better security, consider using a cloud-based secret management service or a vault.
* Error Handling: Basic error handling is implemented, but there is scope for improvement to handle edge cases and unexpected scenarios more robustly.
* Testing: Limited test coverage is provided. Currently, a test for ImageService is commented out due to the short-lived Dropbox access token issue.

## Future Scope
* Enhanced Error Handling: Implement more comprehensive error handling across the application.
* Improved Test Coverage: Expand unit and integration tests to cover more scenarios.
* Cloud-Based Secret Management: Move sensitive data like API keys and database credentials to a secure vault or cloud-based secret management service.
* Feature Enhancements: Add features such as image filtering by location, user authentication, and more.

## Additional Features
* Dropbox Integration: Seamlessly store and retrieve images from Dropbox.
* CI/CD Pipeline: A simple pipeline that automates the build process and generates a JAR file as an artifact.
* Unit Test: A basic unit test for the image service is provided but commented out due to Dropbox's short-lived access token limitations.
