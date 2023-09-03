import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.remove
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.google.protobuf") version "0.9.1"
    id("maven-publish")
}

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

val protobufVersion = "3.22.2"
val grpcVersion = "1.53.0"
val grpcKotlinVersion = "1.3.0"

dependencies {
    implementation("io.grpc:grpc-protobuf-lite:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("com.google.protobuf:protobuf-kotlin-lite:$protobufVersion")
    implementation("com.google.protobuf:protobuf-javalite:$protobufVersion")
}

publishing {
    publications {
        create(project.name, MavenPublication::class.java) {
            groupId = "com.github.trdelnk"
            artifactId = "lite_google_descriptor"
            version = "0.0.1"
            from(project.components["java"])
        }
    }
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:$protobufVersion" }
    plugins {
        id("java") { artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion" }
        id("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion" }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                remove(id = "java")
                id(id = "kotlin") {
                    option("lite")
                }
            }
            task.plugins {
                id(id = "java") {
                    option("lite")
                }
                id(id = "grpc") {
                    option("lite")
                }
                id(id = "grpckt") {
                    option("lite")
                }
            }
        }
    }
}
