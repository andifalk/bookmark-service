package com.example.bookmark.security.architecture.general;

import com.example.bookmark.security.annotation.ArchitectureTest;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@SuppressWarnings("unused")
@ArchitectureTest
@AnalyzeClasses(packages = {"com.example.bookmark.api", "com.example.bookmark.data", "com.example.bookmark.service"},
        importOptions = {ImportOption.DoNotIncludeTests.class})
public class LayeredArchitectureTests {

    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()

            .layer("Controllers").definedBy("com.example.bookmark.api..")
            .layer("Services").definedBy("com.example.bookmark.service..")
            .layer("Persistence").definedBy("com.example.bookmark.data..")

            .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
            .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Services");
}
