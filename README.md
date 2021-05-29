# Overview
This was an experimental project for me to play with Javalin and Kotlin.

Toy microservice that models inventory information.

FOCUS of this was expanding on https://bitbucket.org/honstain/javalin-inventory-service/src/master/ to include HTTP2
* https://github.com/tipsy/javalin-http2-example
* https://github.com/wmacgyver/javalin-http2-example
* https://github.com/tipsy/javalin/issues/151#issuecomment-375841898

**IMPORTANT VERSIONS:**
* Kotlin 1.4.32
* Javalin 3.13.6
* Jetty 9.4.39.v20210325

## References
* Kotlin https://kotlinlang.org/docs/home.html
* Javalin https://javalin.io/
    * A simple web framework for Java and Kotlin, inspired by Sinatra, uses Jetty

* Junit 5 https://junit.org/junit5/docs/current/user-guide/
    * https://github.com/tipsy/javalin-testing-example
* I referenced this blog post pretty extensively https://phauer.com/2018/best-practices-unit-testing-kotlin/

* Mockito-kotlin - switch to this from MockK, I really struggled with the documentation on MockK and couldn't seem
to effectively debug the issues I was having trying to mock Javalin stuff.
    * https://github.com/mockito/mockito-kotlin
    
* Valiktor - library for validating inputs to my rest endpoints, I tried to use the built in stuff from the Javalin
docs but I couldn't test it (on POST bodies) and I didn't see a clear path to more customization of the
error messages.
    * https://github.com/valiktor/valiktor
    * https://blog.jdriven.com/2019/10/customising-form-validation-in-javalin-using-valiktor/

### Monitoring
* Micrometer https://javalin.io/plugins/micrometer
    * https://micrometer.io/docs

### Compile and Package References
* This guide was super important and the key to me getting a Jar constructed that I could use: https://kotlinlang.org/docs/maven.html#self-contained-jar-file
    * Prior to find this, I went down a lot of dead ends
    * Important for creating the self-contained Jar https://maven.apache.org/plugins/maven-assembly-plugin/usage.html
* Nice reference on the difference between some of the maven plugin options https://medium.com/@randilfernando/when-to-use-maven-jar-maven-assembly-or-maven-shade-ffc3f76ba7a6
* Maven + Javalin https://javalin.io/tutorials/maven-setup
    * https://javalin.io/tutorials/docker

* Inspecting the Jar
```text
jar tf target/javalin-inventory-service-1.0-SNAPSHOT.jar
unzip -l target/javalin-inventory-service-1.0-SNAPSHOT.jar
```

# Run Locally

## Build
```text
mvn clean package
```

Outcome:
```text
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.google.inject.internal.cglib.core.$ReflectUtils$1 (file:/usr/share/maven/lib/guice.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)
WARNING: Please consider reporting this to the maintainers of com.google.inject.internal.cglib.core.$ReflectUtils$1
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
[INFO] Scanning for projects...
[INFO] 
[INFO] ----------< com.mycompany.product:javalin-inventory-service >-----------
[INFO] Building javalin-inventory-service 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:3.1.0:clean (default-clean) @ javalin-inventory-service ---
[INFO] Deleting /home/dev/Desktop/javalin-inventory-service/target
[INFO] 
[INFO] --- maven-resources-plugin:3.0.2:resources (default-resources) @ javalin-inventory-service ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /home/dev/Desktop/javalin-inventory-service/src/main/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ javalin-inventory-service ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- kotlin-maven-plugin:1.4.32:compile (compile) @ javalin-inventory-service ---
[WARNING] Using experimental Kotlin incremental compilation
[INFO] Non-incremental compilation will be performed: NO_BUILD_HISTORY
[INFO] Kotlin compile iteration: /home/dev/Desktop/javalin-inventory-service/src/main/kotlin/com/mycompany/inventory/App.kt, /home/dev/Desktop/javalin-inventory-service/src/main/kotlin/com/mycompany/inventory/Inventory.kt, /home/dev/Desktop/javalin-inventory-service/src/main/kotlin/com/mycompany/inventory/InventoryController.kt, /home/dev/Desktop/javalin-inventory-service/src/main/kotlin/com/mycompany/inventory/JavalinApp.kt
[INFO] Exit code: OK
[INFO] Compiled 4 Kotlin files using incremental compiler
[INFO] 
[INFO] --- maven-resources-plugin:3.0.2:testResources (default-testResources) @ javalin-inventory-service ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ javalin-inventory-service ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- kotlin-maven-plugin:1.4.32:test-compile (test-compile) @ javalin-inventory-service ---
[WARNING] Using experimental Kotlin incremental compilation
[INFO] Non-incremental compilation will be performed: NO_BUILD_HISTORY
[INFO] Kotlin compile iteration: /home/dev/Desktop/javalin-inventory-service/src/test/kotlin/com/mycompany/inventory/InventoryControllerTest.kt, /home/dev/Desktop/javalin-inventory-service/src/test/kotlin/com/mycompany/inventory/JavalinAppIntegrationTest.kt
[INFO] Exit code: OK
[INFO] Compiled 2 Kotlin files using incremental compiler
[INFO] 
[INFO] --- maven-surefire-plugin:2.22.1:test (default-test) @ javalin-inventory-service ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.mycompany.inventory.InventoryControllerTest
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.938 s - in com.mycompany.inventory.InventoryControllerTest
[INFO] Running com.mycompany.inventory.JavalinAppIntegrationTest
[main] INFO org.eclipse.jetty.util.log - Logging initialized @2613ms to org.eclipse.jetty.util.log.Slf4jLog
[main] INFO io.javalin.Javalin - 
           __                      __ _
          / /____ _ _   __ ____ _ / /(_)____
     __  / // __ `/| | / // __ `// // // __ \
    / /_/ // /_/ / | |/ // /_/ // // // / / /
    \____/ \__,_/  |___/ \__,_//_//_//_/ /_/

        https://javalin.io/documentation

[main] INFO io.javalin.Javalin - Starting Javalin ...
[main] INFO org.eclipse.jetty.server.Server - jetty-9.4.39.v20210325; built: 2021-03-25T14:42:11.471Z; git: 9fc7ca5a922f2a37b84ec9dbc26a5168cee7e667; jvm 11.0.11+9-Ubuntu-0ubuntu2.20.04
[main] INFO org.eclipse.jetty.server.Server - Started @2847ms
[main] INFO io.javalin.Javalin - Listening on http://localhost:1237/
[main] INFO io.javalin.Javalin - Javalin started in 147ms \o/
[qtp1362842287-24] ERROR io.javalin.Javalin - 200 http://localhost:1237/inventory/1 time:53.874775
[main] INFO io.javalin.Javalin - Stopping Javalin ...
[main] INFO io.javalin.Javalin - Javalin has stopped
[main] INFO io.javalin.Javalin - 
           __                      __ _
          / /____ _ _   __ ____ _ / /(_)____
     __  / // __ `/| | / // __ `// // // __ \
    / /_/ // /_/ / | |/ // /_/ // // // / / /
    \____/ \__,_/  |___/ \__,_//_//_//_/ /_/

        https://javalin.io/documentation

[main] INFO io.javalin.Javalin - Starting Javalin ...
[main] INFO org.eclipse.jetty.server.Server - jetty-9.4.39.v20210325; built: 2021-03-25T14:42:11.471Z; git: 9fc7ca5a922f2a37b84ec9dbc26a5168cee7e667; jvm 11.0.11+9-Ubuntu-0ubuntu2.20.04
[main] INFO org.eclipse.jetty.server.Server - Started @3496ms
[main] INFO io.javalin.Javalin - Listening on http://localhost:1236/
[main] INFO io.javalin.Javalin - Javalin started in 12ms \o/
[qtp990722659-38] ERROR io.javalin.Javalin - 201 http://localhost:1236/inventory/ time:0.809984
[main] INFO io.javalin.Javalin - Stopping Javalin ...
[main] INFO io.javalin.Javalin - Javalin has stopped
[main] INFO io.javalin.Javalin - 
           __                      __ _
          / /____ _ _   __ ____ _ / /(_)____
     __  / // __ `/| | / // __ `// // // __ \
    / /_/ // /_/ / | |/ // /_/ // // // / / /
    \____/ \__,_/  |___/ \__,_//_//_//_/ /_/

        https://javalin.io/documentation

[main] INFO io.javalin.Javalin - Starting Javalin ...
[main] INFO org.eclipse.jetty.server.Server - jetty-9.4.39.v20210325; built: 2021-03-25T14:42:11.471Z; git: 9fc7ca5a922f2a37b84ec9dbc26a5168cee7e667; jvm 11.0.11+9-Ubuntu-0ubuntu2.20.04
[main] INFO org.eclipse.jetty.server.Server - Started @3537ms
[main] INFO io.javalin.Javalin - Listening on http://localhost:1234/
[main] INFO io.javalin.Javalin - Javalin started in 11ms \o/
[qtp1351275110-51] ERROR io.javalin.Javalin - 200 http://localhost:1234/inventory time:2.61979
[main] INFO io.javalin.Javalin - Stopping Javalin ...
[main] INFO io.javalin.Javalin - Javalin has stopped
[main] INFO io.javalin.Javalin - 
           __                      __ _
          / /____ _ _   __ ____ _ / /(_)____
     __  / // __ `/| | / // __ `// // // __ \
    / /_/ // /_/ / | |/ // /_/ // // // / / /
    \____/ \__,_/  |___/ \__,_//_//_//_/ /_/

        https://javalin.io/documentation

[main] INFO io.javalin.Javalin - Starting Javalin ...
[main] INFO org.eclipse.jetty.server.Server - jetty-9.4.39.v20210325; built: 2021-03-25T14:42:11.471Z; git: 9fc7ca5a922f2a37b84ec9dbc26a5168cee7e667; jvm 11.0.11+9-Ubuntu-0ubuntu2.20.04
[main] INFO org.eclipse.jetty.server.Server - Started @3581ms
[main] INFO io.javalin.Javalin - Listening on http://localhost:1235/
[main] INFO io.javalin.Javalin - Javalin started in 14ms \o/
[qtp1684368286-65] ERROR io.javalin.Javalin - 200 http://localhost:1235/inventory/1 time:0.718739
[main] INFO io.javalin.Javalin - Stopping Javalin ...
[main] INFO io.javalin.Javalin - Javalin has stopped
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.063 s - in com.mycompany.inventory.JavalinAppIntegrationTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] 
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ javalin-inventory-service ---
[INFO] Building jar: /home/dev/Desktop/javalin-inventory-service/target/javalin-inventory-service-1.0-SNAPSHOT.jar
[INFO] 
[INFO] --- maven-assembly-plugin:2.6:single (make-assembly) @ javalin-inventory-service ---
[INFO] Building jar: /home/dev/Desktop/javalin-inventory-service/target/javalin-inventory-service-1.0-SNAPSHOT-jar-with-dependencies.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  20.003 s
[INFO] Finished at: 2021-05-29T10:18:52-07:00
[INFO] ------------------------------------------------------------------------

```

## Run Javalin
```text
java -jar target/javalin-inventory-service-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Outcome:
```text
[main] INFO org.eclipse.jetty.util.log - Logging initialized @238ms to org.eclipse.jetty.util.log.Slf4jLog
[main] INFO io.javalin.Javalin - 
           __                      __ _
          / /____ _ _   __ ____ _ / /(_)____
     __  / // __ `/| | / // __ `// // // __ \
    / /_/ // /_/ / | |/ // /_/ // // // / / /
    \____/ \__,_/  |___/ \__,_//_//_//_/ /_/

        https://javalin.io/documentation

[main] INFO io.javalin.Javalin - Starting Javalin ...
[main] INFO org.eclipse.jetty.server.Server - jetty-9.4.39.v20210325; built: 2021-03-25T14:42:11.471Z; git: 9fc7ca5a922f2a37b84ec9dbc26a5168cee7e667; jvm 11.0.11+9-Ubuntu-0ubuntu2.20.04
[main] INFO org.eclipse.jetty.server.Server - Started @629ms
[main] INFO io.javalin.Javalin - Listening on http://localhost:8000/
[main] INFO io.javalin.Javalin - Javalin started in 183ms \o/
```

