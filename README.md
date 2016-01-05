# dev-test

### required tools

* [Apache Maven](https://maven.apache.org)

### Steps to create executable JAR

* `mvn package`
* A JAR file named `GoEuroTest.jar` would be created under the directory called `target` inside the source directory

### Executing the JAR file

* `java -jar /path/to/GoEuroTest.jar "queryString"`
* The expected CSV named `output.csv` will be created under the current working directory.
