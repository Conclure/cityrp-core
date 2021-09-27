plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version("7.0.0") apply(false)
}

group = "me.conclure.cityrp"
version = "1.1-SNAPSHOT"

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")

    java.toolchain.languageVersion.set(JavaLanguageVersion.of(16))

    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        }
        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }

        flatDir {
            dirs(
                File(rootProject.rootDir,"libs"),
                File(rootProject.rootDir,"libs/cache"),
                File(rootProject.rootDir,"libs/plugins")
            )
        }
    }

    dependencies {
        implementation("org.spongepowered:configurate-jackson:4.1.2")
        implementation("net.kyori:adventure-platform-bukkit:4.0.0")
        implementation("com.github.ben-manes.caffeine:caffeine:3.0.4")
        implementation("org.jspecify:jspecify:0.2.0")
    }
}
/*
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")
    compileOnly("me.clip:placeholderapi:2.10.10")
    compileOnly(name= "patched_1.17.1")
    compileOnly(name= "HolographicDisplays")
    compileOnly(name= "RealisticSeasons-4.2")
    testCompileOnly(name= "patched_1.17.1")
    compileOnly("org.jspecify:jspecify:0.2.0")
    implementation("com.github.ben-manes.caffeine:caffeine:3.0.4")
    compileOnly("org.spigotmc:plugin-annotations:1.2.3-SNAPSHOT")
    annotationProcessor("org.spigotmc:plugin-annotations:1.2.3-SNAPSHOT")
    implementation("org.spongepowered:configurate-jackson:4.1.2")
    implementation("net.kyori:adventure-platform-bukkit:4.0.0")
}
*/
/*
test {
    useJUnitPlatform()
}
/*
 */
/*
shadowJar.destinationDir(file("./libs/plugins"))
*/