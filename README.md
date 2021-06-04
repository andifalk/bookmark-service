# Bookmark Service Application

An insecure spring boot based java service providing an API to store and retrieve browser bookmarks for different users.

* __main__ branch: The insecure variant of this application with lots of security tests covering several topics of the OWASP Top 10 (2017), and the OWASP ASVS 4.x (only secured via basic authentication and form based login without any authorization checks)

* __secure__ branch (planned): The secure variant (all security tests should be green). Still using basic authentication and form based login, but including authorization checks.

* __jwt__ branch (planned): Another secure variant (same as in __secure__ branch but replacing basic authentication/form login with JWT based authentication & authorization)

## Security Tests

The security tests include the following types:

* Unit Test Layer
  * Static Code Analysis using SpotBugs & SonarQube (both using the respective Gradle Plugins)
  * OWASP Dependency Check
  * Security Architecture Tests (using [ArchUnit](https://www.archunit.org/))
  * Input Validation Tests
  * Broken Authentication Tests
* Integration Test Layer    
  * Security Misconfiguration Tests
  * Injection (SQL Injection) Tests
  * Broken Access Control Tests
* UI/Workflow Layer
  * Dynamic Security Tests using OWASP Zap
  
## Using SonaQube

To use SonarQube for security analysis the easiest way is the provided docker container.
Just follow the [Getting Started Guide](https://docs.sonarqube.org/latest/setup/get-started-2-minutes/) using the described way using a docker container. Then continue the same guide with _Analyzing a Project_.

After you have configured the project in SonarQube you can trigger the project analysis by issuing the following command:

```
./gradlew sonarqube \
  -Dsonar.projectKey=[PROJECT_KEY]
  -Dsonar.login=[PROJECT_TOKEN]
```

Please replace _PROJECT_KEY_ and _PROJECT_TOKEN_ with your own values.

