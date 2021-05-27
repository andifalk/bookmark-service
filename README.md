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
  * Security Architecture Tests (ArchUnit)
  * Input Validation Tests
  * Broken Authentication Tests
* Integration Test Layer    
  * Security Misconfiguration Tests
  * Injection (SQL Injection) Tests
  * Broken Access Control Tests
* UI/Workflow Layer
  * Dynamic Security Tests using OWASP Zap