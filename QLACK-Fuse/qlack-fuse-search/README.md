# Elasticsearch integration using Qlack

## Docker Elasticsearch 

In a cmd enter the following :

`docker run --name=A_NAME_OF_YOUR_FLAVOR -p 9400:9200  -p 9401:9300 -e "http.host=0.0.0.0" -e "transport.host=0.0.0.0"  -e "xpack.security.enabled=false" -d docker.elastic.co/elasticsearch/elasticsearch:6.4.2`



## Spring boot Elasticsearch integration


### Elasticsearch configuration

Add at application.properties :
```properties
################################################################################
# Elasticsearch configuration
################################################################################

qlack.fuse.search.host.name=localhost
qlack.fuse.search.host.port=9401
qlack.fuse.search.cluster.name=docker-cluster
```
> `docker-cluster` is the docker default cluster.name, it will be different in any other Elasticsearch environment.



### Enable Elasticsearch Spring boot repositories


At Add qlack-fuse-search at your pom.xml:
```xml

    <properties>
<!-- ... -->
    <qlack.version>3.0.0-SNAPSHOT</qlack.version>
  </properties>

<!-- ... -->

    <dependency>
	  <groupId>com.eurodyn.qlack.fuse</groupId>
	  <artifactId>qlack-fuse-search</artifactId>
	  <version>${qlack.version}</version>
	</dependency>

```


At App.java:

```java
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
// ..

@SpringBootApplication

// ...


@EnableElasticsearchRepositories({
       "com.eurodyn.qlack.fuse.search",
       // + The location of ElasticsearchRepositories:
       "ch.admin.blv.etv.repository.es"

})


@SpringBootApplication
@EnableAsync
@EnableJpaRepositories({
    "com.eurodyn.qlack.fuse.search",
    // ..

})
@EnableElasticsearchRepositories({
    "com.eurodyn.qlack.fuse.search",
// + The location of ElasticsearchRepositories:
    "ch.admin.blv.etv.repository.es" 
})
@EnableCaching
@ComponentScan({
  "com.eurodyn.qlack.fuse.search",
    //..
})

```


ElasticsearchRepository declaration example:

```java

package ch.admin.blv.etv.repository.es;

//...

public interface ApplicationAnimalsRepository extends ElasticsearchRepository<Animal, String> {

    // custom query declaration for the field 
    // int Age  of Animal class
    //no further implemetation nedeed
    List<Animal>  findByAge(int age);
    
```


ElasticsearchRepository call example:


```java
    // index creation for the class Animals:
    List<Animal> animals = getAllAnimals();
    
    applicationAnimalsRepository.saveAll(animals);
    //...

    // simple search with like:
    applicationAnimalsRepository.findByNameike(text);

```

