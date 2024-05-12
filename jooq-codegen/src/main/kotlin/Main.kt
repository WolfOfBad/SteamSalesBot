package ru.wolfofbad

import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target

fun main() {
    val authorization = Target()
        .withPackageName("ru.wolfofbad.authorization.domain.jooq.generated")
        .withDirectory("authorization/src/main/kotlin")
    val authorizationPath = "migrations/authorization"
    generateCode(authorization, authorizationPath)

    val link = Target()
        .withPackageName("ru.wolfofbad.links.domain.jooq.generated")
        .withDirectory("links/src/main/kotlin")
    val linkPath = "migrations/link"
    generateCode(link, linkPath)
}

fun generateCode(target: Target, path: String) {
    val database = Database()
        .withName("org.jooq.meta.extensions.liquibase.LiquibaseDatabase")
        .withProperties(
            Property().withKey("rootPath").withValue(path),
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
