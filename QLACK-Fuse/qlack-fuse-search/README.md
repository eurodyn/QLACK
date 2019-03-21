# QLACK Search module
 
 This module is used to integrate elasticsearch to your application
 
## Integration

## Docker Elasticsearch 

In a cmd enter the following :

`docker run --name=A_NAME_OF_YOUR_FLAVOR -p 9400:9200  -p 9401:9300 -e "http.host=0.0.0.0" -e "transport.host=0.0.0.0"  -e "xpack.security.enabled=false" -d docker.elastic.co/elasticsearch/elasticsearch:6.4.2`

## Spring boot Elasticsearch integration

### Add qlack-fuse-search at your pom.xml:
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

### Add the required properties at the application.properties file:
```properties
################################################################################
# Elasticsearch configuration
################################################################################
## Qlack uses 2 different Elasticsearch clients:
#
# RestHighLevelClient ES client
qlack.fuse.search.es_hosts=http:localhost:9400

# Repo ES client (org.elasticsearch.client.Client)
qlack.fuse.search.host.name=localhost
qlack.fuse.search.host.port=9401

#>`docker-cluster` is the docker default cluster.name, it will be different in any other Elasticsearch environment.
qlack.fuse.search.cluster.name= docker-cluster

spring.main.allow-bean-definition-overriding=true
```

### Add the packages in the Spring boot application main class declaration:

```java
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
// ..


@SpringBootApplication
@EnableAsync
@EnableJpaRepositories({
    "com.eurodyn.qlack.fuse.search",
    // ..

})
@EnableElasticsearchRepositories({
// + The location of ElasticsearchRepositories in your app: 
    "domain.appName.repository.es" 
})
@EnableCaching
@ComponentScan({
  "com.eurodyn.qlack.fuse.search",
    //..
})

```

### Add the ES annotations to each document (annotated class):
```java

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;
//..

@Document(indexName = "animal")
public class Animal  {

 
  @Field(type = Text, index = true)
  private String id;

 // Searchable with the french analyzer and Retrievable
  @Field(type = Text, index = true, searchAnalyzer="french", analyzer = "french")
  private String name;

  // Retrievable but not searchable
  @Field(type = Text, index = false)
  private String type;

 // Searchable and Retrievable 
  @Field(type = Text, index = true)
  private int age;

//...
}
```

### JSON index mapping example: 
```json
{
   "animals":{
      "dynamic": false,
      "properties":{
         "id":{
            "type":"text",
            "fields":{
               "keyword":{
                  "type":"keyword"
               }
            }
         },
         "name":{
            "type":"text",
            "fields":{
               "keyword":{
                  "type":"keyword"
               }
            }
         },
         "type":{
            "type":"text",
            "fields":{
               "keyword":{
                  "type":"keyword"
               }
            }
         },
         "age":{
            "type":"long"
         }
      }
   }
}
```

### Create unique repositories, for each document class, which extend the ElasticsearchRepository.

```java

package domain.appName.repository.es;

//...

public interface ApplicationAnimalsRepository extends ElasticsearchRepository<Animal, String> {

    // custom query declaration for the field 
    // int Age  of Animal class
    //no further implemetation nedeed
    List<Animal>  findByAge(int age);
    
```

## Index operations

### Indices can be created using either a  JSON file mapping  or an annotated class.

#### Index creation from json mapping example:
```java

import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.service.AdminService;
//..

private AdminService adminService;

    @Autowired
    public AdminServiceTest(AdminService adminService) {
        this.adminService = adminService;
    }
    
    public void createIndexFromJsonMapping() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest();
        createIndexRequest.setName("animals");
        createIndexRequest.setType("mammals");

        Path resourceDirectory = Paths.get("src/main/resources/animal.json");

        try {
          String mapping = new String(Files.readAllBytes(resourceDirectory), "UTF-8");
          createIndexRequest.setIndexMapping(mapping);
        } catch (IOException e) {
          e.printStackTrace();
        }
        
        adminService.createIndex(createIndexRequest):
    }
```

#### Index creation from annotated class example:
```java

import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.service.AdminService;
//..

private AdminService adminService;

    @Autowired
    public AdminServiceTest(AdminService adminService) {
        this.adminService = adminService;
    }
    
    public void createIndexFromAnnotatedClass() {
        adminService.createIndex(Animal.class):
    }
```

### Indices can be deleted either by name or providing an annotated class.
#### Delete index by name example: 
 ```java
 
 import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
 import com.eurodyn.qlack.fuse.search.service.AdminService;
 //..
 
 private AdminService adminService;
 
     @Autowired
     public AdminServiceTest(AdminService adminService) {
         this.adminService = adminService;
     }
     
     public void deleteIndexByName(){
         adminService.deleteIndex("animals"):
     }
 }
```

#### Delete index by class example: 
 ```java
 
 import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
 import com.eurodyn.qlack.fuse.search.service.AdminService;
 //..
 
 private AdminService adminService;
 
     @Autowired
     public AdminServiceTest(AdminService adminService) {
         this.adminService = adminService;
     }

     public void deleteIndexByClass(){
         adminService.deleteIndex(Animal.class);
     }
 }
```

### Indexing documents is done either by using ES repository or using Qlack's indexing service.
#### Index document using repository example:
 ```java
 
 import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
 import com.eurodyn.qlack.fuse.search.service.AdminService;
 //..
 
 private ApplicationAnimalsRepository applicationAnimalsRepository;
 
     @Autowired
     public AdminServiceTest(ApplicationAnimalsRepository applicationAnimalsRepository) {
         this.applicationAnimalsRepository = applicationAnimalsRepository;
     }

     public void indexDocuemntUsingRepository(){
           List<Animal> animals = getAllAnimals();
           applicationAnimalsRepository.saveAll(animals);
     }
 }
 ```

#### Index document using Qlack's indexing service example:
  ```java
  
  import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
  import com.eurodyn.qlack.fuse.search.service.IndexingService;
  //..
  
  private IndexingService indexingService;
  
      @Autowired
      public IndexingServiceTest(IndexingService indexingService;) {
          this.indexingService = indexingService;
      }
 
      public void indexDocuemntUsingService(){
          Animal animal = new Animal("some_id", "Rex", "Dog", "5");
          IndexingDTO indexingDTO = new IndexingDTO();
          indexingDTO.setSourceObject(animal);
          indexingDTO.setIndex("animals");
          indexingDTO.setType("mammals");
          indexingDTO.setId(animal.getId());
          indexingService.indexDocument(indexingDTO);
      }
  }
  ```
  
## Searching 

Qlack provides a variety of search queries than can be used to query documents. 
Moreover elasticsearch repositories can be enhanced with findBy{Attribute} methods to implement wanted search functionality.

### Searching using Qlack's SearchService

```java
//**
    import com.eurodyn.qlack.fuse.search.dto.SearchResultDTO;
    import com.eurodyn.qlack.fuse.search.dto.queries.QueryRange;
    import com.eurodyn.qlack.fuse.search.service.SearchService;
//**

     private SearchService searchService;


     public void searchQueryRange() {
        System.out.println("******************");
        System.out.println("Testing query range");
        QueryRange queryRange = new QueryRange() {};
        queryRange.setTerm("age", 28,30);
        queryRange.setIndex("animals");
    
        SearchResultDTO searchResultDTO = searchService.search(queryRange);
        System.out.println(searchResultDTO.getHits());
    }
```

### Searching using ES repository methods

```java
//**
    import domain.appName.repository.es.ApplicationAnimalRepository;
//**

     private ApplicationAnimalsRepository applicationAnimalRepository;


     public void searchByAge() {
        List<Animal> animals = applicationAnimalRepository.findByAge(5);
    }
```