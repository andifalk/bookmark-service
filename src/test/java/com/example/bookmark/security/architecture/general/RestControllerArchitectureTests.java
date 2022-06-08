package com.example.bookmark.security.architecture.general;

import com.example.bookmark.security.annotation.ArchitectureTest;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.web.bind.annotation.*;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

@SuppressWarnings("unused")
@ArchitectureTest
@AnalyzeClasses(packages = "com.example.bookmark.api",
        importOptions = {ImportOption.DoNotIncludeTests.class})
public class RestControllerArchitectureTests {

    @ArchTest
    static final ArchRule layer_dependencies_are_respected =
            methods().that().arePublic().or().arePackagePrivate()
                    .and().areDeclaredInClassesThat()
                    .resideInAPackage("com.example.bookmark.api..")
                    .and().areDeclaredInClassesThat()
                    .haveSimpleNameEndingWith("RestController")
                    .and().areDeclaredInClassesThat()
                    .areAnnotatedWith(RestController.class)
                    .should().beAnnotatedWith(RequestMapping.class)
                    .orShould().beAnnotatedWith(GetMapping.class)
                    .orShould().beAnnotatedWith(PostMapping.class)
                    .orShould().beAnnotatedWith(PutMapping.class)
                    .orShould().beAnnotatedWith(DeleteMapping.class);
}
