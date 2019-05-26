# Interview Project
This project including four submodules:
* merge - merge two sorted collection
* concurrency - a queue support multi-writer and multi-reader
* drawing - draw shape with pre/post processing
* dbproject - a website to let customer subscribe service

## Prerequisites
* [jdk 1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - jdk version 1.8
* [gradle 5.4.1](https://gradle.org/next-steps/?version=5.4.1&format=all) - gradle used for build
* [nodejs & npm](https://nodejs.org/en/) - optional, npm for parser reactjs's jsx file

## Build
Use below command to build project, this command will try to download all dependencies from 
maven central, so it will take a while depends on your network.
```
gradle clean assemble
```
## Run Test
After build step succeed, run below command to run all unit tests
```
gradle build
```
test results can be found at <module>/build/reports/tests/test/index.html.
for example, you can find concurrency module's test report at:
```
concurrency/build/reports/tests/test/index.html
```
Open this html file at browser, click "ConcurrentQueueTest" and then switch to "Standard output
", test's output can be found at here.

## DB Project
To start website, use below gradle tasks:
```
cd dbproject
gradle bootRun
```
This task will start tomcat and listen at [localhost:8001](http://localhost:8001)

### Enable jsx parser
If want to enable jsx parser at compiling stage instead of online browser,
which will improve performance, follow below steps:
* edit dbproject/build.gradle, uncomment plugin "id 'net.eikehirsch.react' version '0.4.1'"
and "task compileJsx"
* edit dbproject/src/main/resources/index.html, remove type="text/jsx"
```
  <script src="js/subscribe.js"></script>
  <script src="js/index.js" ></script>
  <script src="js/customer.js"></script>
``` 
and then run gradle bootRun again.
