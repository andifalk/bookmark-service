package com.example.bookmark.security.architecture.general;

import com.example.bookmark.security.annotation.ArchitectureTest;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

@SuppressWarnings("unused")
@ArchitectureTest
@AnalyzeClasses(packages = "com.example.bookmark.service",
        importOptions = {ImportOption.DoNotIncludeTests.class})
public class TransactionalServiceArchitectureTests {

    @ArchTest
    static final ArchRule all_public_service_operations_are_transactional =
            methods().that().arePublic()
                    .and().areDeclaredInClassesThat()
                    .resideInAPackage("com.example.bookmark.service..")
                    .and().areDeclaredInClassesThat()
                    .haveSimpleNameEndingWith("Service").and().areDeclaredInClassesThat().haveSimpleNameNotContaining("UserDetails")
                    .and().areDeclaredInClassesThat()
                    .areAnnotatedWith(Service.class)
                    .should().beAnnotatedWith(Transactional.class).orShould().beDeclaredInClassesThat().areAnnotatedWith(Transactional.class);
}
