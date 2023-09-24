package com.example.bookmark.security.architecture.general;

import com.example.bookmark.security.annotation.ArchitectureTest;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@SuppressWarnings("unused")
@ArchitectureTest
@AnalyzeClasses(packages = {"com.example.bookmark.api", "com.example.bookmark.data", "com.example.bookmark.service"},
        importOptions = {ImportOption.DoNotIncludeTests.class})
public class LayerDependencyArchitectureTests {
    @ArchTest
    static final ArchRule services_should_not_access_controllers =
            noClasses().that().resideInAPackage("..service..")
                    .should().accessClassesThat().resideInAPackage("..api..");

    @ArchTest
    static final ArchRule persistence_should_not_access_services =
            noClasses().that().resideInAPackage("..data..")
                    .should().accessClassesThat().resideInAPackage("..service..");

    @ArchTest
    static final ArchRule services_should_only_be_accessed_by_controllers_or_other_services =
            classes().that().resideInAPackage("..service..")
                    .should().onlyBeAccessed().byAnyPackage("..api..", "..service..");

    @ArchTest
    static final ArchRule services_should_only_access_persistence_or_other_services =
            classes().that().resideInAPackage("..service..")
                    .should().onlyAccessClassesThat().resideInAnyPackage(
                    "..service..", "..data..", "java..",
                    "javax..", "org..");

    // 'dependOn' catches a wider variety of violations, e.g. having fields of type, having method parameters of type, extending type ...

    @ArchTest
    static final ArchRule services_should_not_depend_on_controllers =
            noClasses().that().resideInAPackage("..service..")
                    .should().dependOnClassesThat().resideInAPackage("..api..");

    @ArchTest
    static final ArchRule persistence_should_not_depend_on_services =
            noClasses().that().resideInAPackage("..data..")
                    .should().dependOnClassesThat().resideInAPackage("..service..");

    @ArchTest
    static final ArchRule services_should_only_be_depended_on_by_controllers_or_other_services =
            classes().that().resideInAPackage("..service..")
                    .should().onlyHaveDependentClassesThat().resideInAnyPackage("..api..", "..service..");

    @ArchTest
    static final ArchRule services_should_only_depend_on_persistence_or_other_services =
            classes().that().resideInAPackage("..service..")
                    .should().onlyDependOnClassesThat().resideInAnyPackage("..service..", "..data..", "java..", "javax..", "org..", "com.fasterxml.jackson.annotation..");

}
