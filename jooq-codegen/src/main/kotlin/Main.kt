package ru.wolfofbad

import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target

fun main() {
    val target = Target()
        .withPackageName("ru.wolfofbad.authorization.domain.jooq.generated")
        .withDirectory("authorization/src/main/kotlin")

    generateCode(target)
}

fun generateCode(target: Target) {
    val database = Database()
        .withName("org.jooq.meta.extensions.liquibase.LiquibaseDatabase")
        .withProperties(
            Property().withKey("rootPath").withValue("migrations"),
            Property().withKey("scripts").withValue("master.yml")
        );

    val options = Generate()
        .withGeneratedAnnotation(true)
        .withGeneratedAnnotationDate(false)
        .withNullableAnnotation(true)
        .withNullableAnnotationType("org.jetbrains.annotations.Nullable")
        .withNonnullAnnotation(true)
        .withNonnullAnnotationType("org.jetbrains.annotations.NotNull")
        .withJpaAnnotations(false)
        .withValidationAnnotations(true)
        .withSpringAnnotations(true)
        .withConstructorPropertiesAnnotation(true)
        .withConstructorPropertiesAnnotationOnPojos(true)
        .withConstructorPropertiesAnnotationOnRecords(true)
        .withFluentSetters(false)
        .withDaos(false)
        .withPojos(true);

    val configuration = Configuration()
        .withGenerator(
            Generator()
                .withName("org.jooq.codegen.KotlinGenerator")
                .withDatabase(database)
                .withGenerate(options)
                .withTarget(target)
        )

    GenerationTool.generate(configuration)
}
