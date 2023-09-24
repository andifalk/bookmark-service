package com.example.bookmark.security.architecture.security;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.*;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;

/**
 * 1.5.3 Verify that input validation is enforced on a trusted service layer.
 */
@SuppressWarnings("unused")
@AnalyzeClasses(packages = {"com.example.bookmark.api", "com.example.bookmark.data", "com.example.bookmark.service"},
        importOptions = {ImportOption.DoNotIncludeTests.class})
public class InputValidationArchitectureTests {

    @ArchTest
    static final ArchRule all_entity_fields_should_be_validated_by_constraints =
            fields().that().areDeclaredInClassesThat()
                    .resideInAPackage("com.example.bookmark.data..")
                    .and().areDeclaredInClassesThat()
                    .haveSimpleNameEndingWith("Entity")
                    .and().areDeclaredInClassesThat()
                    .areAnnotatedWith(Entity.class)
                    .should().beAnnotatedWith(Id.class)
                    .orShould().beAnnotatedWith(NotNull.class)
                    .orShould().beAnnotatedWith(NotEmpty.class)
                    .orShould().beAnnotatedWith(NotBlank.class)
                    .orShould().beAnnotatedWith(Size.class)
                    .orShould().beAnnotatedWith(Email.class)
                    .orShould().beAnnotatedWith(Pattern.class)
                    .orShould().beAnnotatedWith(Min.class)
                    .orShould().beAnnotatedWith(Max.class);

    @ArchTest
    static final ArchRule controller_should_be_validated =
            classes().that()
                    .resideInAPackage("com.example.bookmark.api..")
                    .and()
                    .haveSimpleNameEndingWith("RestController")
                    .and().haveSimpleNameNotEndingWith("MainRestController")
                    .and()
                    .areAnnotatedWith(RestController.class)
                    .should().beAnnotatedWith(Validated.class);
}
