dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")

    api("org.spongepowered:configurate-jackson:4.1.2")
    api("net.kyori:adventure-platform-bukkit:4.0.0")
    api("com.github.ben-manes.caffeine:caffeine:3.0.4")
    compileOnlyApi("org.jspecify:jspecify:0.2.0")
}

tasks.test {
    useJUnitPlatform()
}