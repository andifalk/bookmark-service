# Bookmark Service Application

An insecure spring boot based java service providing an API to store and retrieve browser bookmarks for different users.

* __main__ branch: The insecure variant of this application with lots of security tests covering several topics of the OWASP Top 10 (2021), and the OWASP ASVS 4.x (only secured via basic authentication and form based login without any authorization checks)

* __secure__ branch: The secure variant (all security tests should be green). Still using basic authentication and form based login, but including authorization checks.

## REST API

This application provides a basic bookmark administration (like in your web browsers).

You may ask for bookmarks of user Bruce Wayne like this:

```
http :9090/api/bookmarks?userid=c9caa4d1-5ad7-4dd1-8bd1-91b8bc5b9a48 --auth bruce.wayne@example.com:wayne
```

You could also just try to access bookmarks of another user (which is actually broken authz):

```
http :9090/api/bookmarks?userid=c9caa4d1-5ad7-4dd1-8bd1-91b8bc5b9a48 --auth bruce.banner@example.com:banner
```

Or just ask for the complete list of users (which you usually should not provide to all users!!!)

```
http :9090/api/users --auth bruce.wayne@example.com:wayne
```

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

### Static application security testing

#### SemGrep

To check the project with the OSS semgrep scanner just perform this command inside the project root folder:

```shell
semgrep scan --config auto
```
  
#### SonarQube

To use SonarQube for security analysis the easiest way is the provided docker container.
Just follow the [Getting Started Guide](https://docs.sonarqube.org/latest/setup/get-started-2-minutes/) using the described way using a docker container. Then continue the same guide with _Analyzing a Project_.

After you have configured the project in SonarQube you can trigger the project analysis by issuing the following command:

```
./mvnw sonar:sonar -Dsonar.projectKey=PROJECT_KEY 
                             -Dsonar.projectName='PROJECT_NAME' 
                             -Dsonar.host.url=http://localhost:9000 
                             -Dsonar.token=THE_GENERATED_TOKEN  
```

Please replace _PROJECT_KEY_ and _PROJECT_TOKEN_ with your own values.

## Architecture documentation

This project includes architecture documentation based on Arc42 and is completely created by AI (OpenAI Codex Agent) using the following prompt:

```bash
› you are an architect and have to create the architecture documentation for this project. You find already the arc42 templates in folder 'src/docs/asciidoc'. The
asciidoc files each contain help sections. Please fill in the corresponding contents in the files for this project. Also generate corresponding diagrams in plantuml
format.


• I’ll draft the full arc42 content for this codebase and replace the template help text with project-specific documentation, then add matching PlantUML diagrams in
src/docs/asciidoc. I’m first scanning the code and current docs structure to derive the actual architecture.

• Explored
└ List main
List asciidoc
List ls -la

```