# Ecommerce_Backend
[![Maven Version](https://img.shields.io/badge/Maven-4.0.0-brightblue.svg)](https://maven.apache.org/)
[![Spring Boot Version](https://img.shields.io/badge/Spring_Boot-3.3.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java Version](https://img.shields.io/badge/Java-17-red.svg)](https://www.oracle.com/java/)

This repository is for creating to e-commerce platform, that with a any type things, basically it a template e-commerce platform

## Table of Contents

- [Branch naming Convention](#branch-naming-conventions)
- [Backend Setup and Dependencies](#backend-setup-and-dependencies)
- [Commit Message Structure](#commit-message-structure)
- [Environment](#environments)
- [Documentation Swagger](#documentation-swagger)

## Branch Naming Conventions

In our project, we adhere to well-defined branch naming conventions to ensure a consistent and organized workflow. Our branch names are structured to provide clear information about their purpose and context.

[type]-[hu-name]-[plane]

### Naming Format

Our branch names follow the format:

- **`type`**: Represents the type of branch. We use `FT` for Feature branches.

    - **FT**: For developing new features.
    - **FX**: For bug fixes or issue resolution.
    - **DOC**: For changes or updates to the project documentation.
    - **RF**: For refactoring code without changing its functionality.
    - **TS**: For creating or updating unit/integration tests.
    - **CI**: For changes related to CI/CD pipelines.
    - **HF**: For urgent fixes applied directly to production.
    - **CR**: For non-functional tasks like dependency updates or code cleanup.
    - **REL**: For preparing code for a new version release.
    - **SP**: For research or exploration of a potential solution before implementation.
    - **BR**: For creating or validating bug reports.
    - **MG**: For merging changes from different branches or resolving conflicts.
    - **ST**: For visual or design-related updates.
    - **CFG**: For configuration or setup tasks.


- **`hu-name`**: Stands for the Hypothetical User Story name, which provides context for the branch.

- **`plane`**: Indicates the plane number associated with the task or issue.



### Examples

- **Feature Branch Example:**

  - Branch Name: `FT-ECOMM-38`
  - Purpose: Development of a feature related to task ECOMM-38.


By adhering to these branch naming conventions, we enhance clarity and traceability within our development process.

## Environments

For this project it is using environments `dev`, `stage` and `prod`, copy a data from `application.properties` and create new file with the name `application-dev.properties`

`NOTE:` The file `application-dev.properties` it is change dev for stage or prod example application-stage.properties or application-prod.properties (you can have the 3 files with this names) and the file original application.properties changed value the spring.profiles.active=dev a spring.profile.active=stage or spring.profiles.active=prod

## Backend Setup and Dependencies

To set up and run this project, follow these steps:

1.  **Docker Setup**: Ensure that Docker is installed on your system. Docker will be used to manage the database container for the development environment. If you don't have Docker installed, you can download and install it from the Docker website.

2.  **Clone the Repository**: Clone this project repository to your local machine using Git. You can do this by running the following command in your terminal:
    ```
    git clone https://github.com/VmSoftwareSolution/E-Commerce-Backend.git
    ```
3. **Install JDK 17**
  You must install jdk 17, which to date is the one used in this repository

4. **Install Spring Boot**
 You must install SpringBoot 3.2.4, which to date is the one used in this repository

5. **Database Setup (Development Environment)**

  - Run Database with Docker: In the development environment, the database is managed using Docker. To start the database container, run the following command:

    ```
    docker-compose up -d
    ```

     This command is a one-time operation dedicated to creating the necessary image and container. When you wish to halt the testing, simply terminate the container using the provided command. When needed again, restart the container using the same specific command. This approach ensures a streamlined and convenient database management process.
      
    - View Running Containers: To see a list of running Docker containers, use the following command:

        ```
        docker ps
        ```

    - Start or Stop Containers: To start a stopped container, use:

        ```
        docker start "container-name"
        ```

    - To stop a running container, use:

        ```
        docker stop "container-name"
        ```
6. **Install node_modules**

    ```
    npm install
    ```
7. **Run the project**
    To run the Spring Boot project, use the following Maven command:

    ```
    mvn spring-boot:run
    ```

## Commit Message Structure
Each commit message should follow a standardized structure to ensure consistency, clarity, and proper tracking of changes in the repository. The message structure consists of two main parts: Type and Subject.


- **`Type`**: specifies the category of change made in the commit. It is written in uppercase letters, followed by a colon and a space. Each type corresponds to a different kind of modification or action. The following types are used in our project:

  - **FEATURE**: For developing new features.
  - **FIX**: For bug fixes or issue resolution.
  - **DOCS**: For changes or updates to the project documentation.
  - **REFACTOR**: For refactoring code without changing its functionality.
  - **TEST**: For creating or updating unit/integration tests.
  - **CI**: For changes related to CI/CD pipelines.
  - **HOTFIX**: For urgent fixes applied directly to production.
  - **CHORE**: For non-functional tasks like dependency updates or code cleanup.
  - **RELEASE**: For preparing code for a new version release.
  - **SP**: For research or exploration of a potential solution before implementation.
  - **BUGREPORT**: For creating or validating bug reports.
  - **MERGE**: For merging changes from different branches or resolving conflicts.
  - **STYLES**: For visual or design-related updates.
  - **CONFIGURATION**: For configuration or setup tasks.

- **`Subject`**: The subject is a concise description of the change made in the commit. It provides a brief summary of what was done, making it easy to understand the purpose of the commit. Here are the guidelines for writing the subject:

- The subject should be in lowercase, unless it is a proper noun or acronym (e.g., Java, API).
- The subject should start with a verb in the imperative mood (e.g., Add, Fix, Update, etc.).
- The subject should be brief and specific, ideally 50 characters or fewer.
- Avoid ending the subject with a period.

### Examples of Correct Commit Messages

Here are some examples of correctly formatted commit messages:

- **`Feature-related Commit`**: (FEATURE): Add user authentication

- **`Bug Fix Commit`**: (FIX): Resolve issue with database connection

- **`Documentation Commit`**: (DOCS): Update API documentation

- **`Refactoring Commit`**: (REFACTOR): Simplify data processing logic

- **`Hotfix Commit`**: (HOTFIX): Fix crash on login screen

## Documentation Swagger
The project includes an auto-generated API documentation using Swagger. Swagger provides a comprehensive interface for exploring and testing the available endpoints, their inputs, and responses. This documentation is useful for developers and testers to understand the API structure and ensure proper integration.

To access the Swagger UI, start the application and navigate to the following URL in your browser:

```
http://localhost:8080/swagger-ui/index.html#/
```