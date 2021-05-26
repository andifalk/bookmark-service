package com.example.bookmark.security.architecture.security;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = {"com.example.bookmark.api", "com.example.bookmark.data", "com.example.bookmark.service"},
        importOptions = {ImportOption.DoNotIncludeTests.class})
public class InsecurePersistenceArchitectureTests {

    @ArchTest
    static final ArchRule no_api_or_service_should_directly_access_low_level_persistence_services =
            classes().that().resideInAnyPackage("..api..", "..service..")
                    .should().onlyAccessClassesThat().resideOutsideOfPackages(
                    "java.sql..", "javax.persistence..", "org.springframework.data..", "org.hibernate..");

    @ArchTest
    static final ArchRule no_data_layer_should_directly_access_insecure_persistence_services =
            classes().that().resideInAPackage("..data..")
                    .should().onlyAccessClassesThat().resideOutsideOfPackages(
                    "java.sql..", "javax.persistence..", "org.hibernate..");
}
