package com.example.bookmark.security.architecture.security;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

/**
 * V1.4 Access Control Architectural Requirements
 */
@SuppressWarnings("unused")
@AnalyzeClasses(packages = "com.example.bookmark.service",
        importOptions = {ImportOption.DoNotIncludeTests.class})
public class AuthorizedServiceArchitectureTests {

    @ArchTest
    static final ArchRule all_public_service_operations_check_authorization =
            methods().that().arePublic()
                    .and().areDeclaredInClassesThat()
                    .resideInAPackage("com.example.bookmark.service..")
                    .and().areDeclaredInClassesThat()
                    .haveSimpleNameEndingWith("Service").and().areDeclaredInClassesThat().haveSimpleNameNotContaining("UserDetails")
                    .and().areDeclaredInClassesThat()
                    .areAnnotatedWith(Service.class)
                    .should().beAnnotatedWith(PreAuthorize.class).orShould().beDeclaredInClassesThat().areAnnotatedWith(PreAuthorize.class);
}
