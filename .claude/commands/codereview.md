You are now performing a code review for the `bookmark-service` project.
Your task is to improve the code quality, architecture, security, maintainability, and documentation based on the provided guidelines.

Perform the code review for $ARGUMENTS the entire codebase, including all Java files, configuration files, and documentation.

Follow these guidelines during your review:

1. General Code Review Features:
    - Analyze codebase structure and architecture patterns
    - Trace execution flows to understand code behavior
    - Identify deprecated API usage and suggest modern practices
    - Review for (potential) bugs, performance issues, and maintainability
    - Review tests for better coverage and quality
    - Ensure code follows best practices and conventions/costs

2. Architecture and Design:
    - Evaluate the overall architecture for scalability and maintainability
    - Check for proper separation of concerns and modularity
    - Assess the use of design patterns and best practices
    - Assess the use of Java idioms and features
    - Ensure the code is easy to understand and follows clean code principles
    - Review the use of dependency injection and inversion of control
    - Ensure best practices of Spring Web, Spring Data JPA and Spring Security are followed
    - Check for proper error handling and logging practices
    - Review the use of configuration management and externalized properties

3. Security and Compliance:
    - Check for secure coding practices and vulnerability management
    - Ensure sensitive data is handled securely (e.g., encryption, masking, logging)
    - Review authentication and authorization mechanisms
    - Ensure compliance with security policies and standards
    - Check for proper input validation and sanitization
    - Review the use of secure coding libraries and frameworks
    - Check for usage of prepared statements to mitigate SQL injection vulnerabilities
    - Review the use of third-party libraries for known vulnerabilities
    - Ensure secure communication protocols are used (e.g., HTTPS, TLS)
    - Check for proper certificate and keystore configurations
    - Review the use of security annotations (e.g., `@Secured`, `@PreAuthorize`, etc.)
    - Ensure proper use of security headers and CORS configurations
    - Evaluate the use of security frameworks (e.g., Spring Security) and their configurations
    - Review the use of OAuth2 and OpenID Connect configurations and token validation

4. Testing and Quality Assurance:
    - Review the test coverage and quality of unit, integration, and end-to-end tests
    - Ensure tests follow best practices and conventions
    - Check for proper use of mocking and stubbing in tests
    - Review the use of test frameworks and libraries
    - Ensure tests are maintainable and easy to understand
    - Evaluate the use of test data management and test environment setup
    - Check for proper error handling and assertions in tests
    - Ensure tests are isolated and do not depend on external systems
    - Review the use of test annotations and categories
    - Ensure proper use of testing support libraries (e.g., Spring, Testcontainers, Mockito)
    - Evaluate the use of custom test annotations for categorization (e.g., `@UnitTest`, `@IntegrationTest`, etc.)
    - Check for proper use of test profiles and configurations
    - Ensure test data is not leaking production data and personal information
    - Review the use of test doubles (e.g., mocks, stubs, fakes) and their configurations
    - Ensure tests are run in a clean environment and do not leave residual data
    - Check for proper use of assertions and verifications in tests

5. Documentation and Comments:
    - Ensure the code follows the project's documentation standards
    - Ensure code is well-commented and self-explanatory
    - Review the project's documentation for completeness and clarity
    - Check for consistency in documentation style and format
    - Ensure API documentation is up-to-date and accurate
    - Review README, SECURITY.md, CLAUDE.md, and other relevant documentation files for clarity and completeness

Do not make any changes to the codebase directly. Instead, provide detailed feedback and suggestions for improvements based on the above guidelines in markdown
format suitable as a GitHub pull request comment.
