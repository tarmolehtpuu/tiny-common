# tiny-common ![Static Badge](https://img.shields.io/badge/version-0.0.1-blue) ![Endpoint Badge](https://img.shields.io/endpoint?url=https%3A%2F%2Fgist.githubusercontent.com%2Ftarmolehtpuu%2Ffa662c13bf696d7c12a097e2bcca7e12%2Fraw%2F9447d7199e236eb4dc7ca71e0f1dee970b78d7c2%2Ftiny-common-junit-tests.json) ![Endpoint Badge](https://img.shields.io/endpoint?url=https%3A%2F%2Fgist.githubusercontent.com%2Ftarmolehtpuu%2Ffa662c13bf696d7c12a097e2bcca7e12%2Fraw%2F9447d7199e236eb4dc7ca71e0f1dee970b78d7c2%2Ftiny-common-jacoco-coverage.json)

Various small helper classes used across projects.


## Import

### Maven

#### ~/.m2/settings.xml
```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>GITHUB_USERNAME</username>
            <password>GITHUB_TOKEN</password>
        </server>
    </servers>
</settings>
```

#### pom.xml

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/tarmolehtpuu/tiny-common</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>ee.moo</groupId>
        <artifactId>tiny-common</artifactId>
        <version>0.0.1</version>
    </dependency>
</dependencies>
```

### Gradle

```kotlin
repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/tarmolehtpuu/tiny-common")
        credentials {
            username = project.findProperty("github.user") as String? ?: System.getenv("GITHUB_USER")
            password = project.findProperty("github.token") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation("ee.moo:tiny-common:0.0.1")
}
```
