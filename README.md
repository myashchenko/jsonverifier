# JSON verifier

The rule which helps to verify JSON returned by REST services (Spring, JAX-RS, etc).
It allows you to hide paths to the files with JSON completely.
Just put *.json file to json/TestClassName/testMethodName/ directory and the rule will compare response
of the service to this file. Another option is JSON matcher that can be used to compare JSON objects.

## Motivation

During writing the tests for REST services I saw that there are a lot of hardcoded JSON into test classes. 
I think that it's very bad practice and we should avoid hard coding. The better way is to save expected JSON content 
into the files from a classpath. Besides, we can automatically generate a path to this files based on a test 
class name and on a method name.

## Code Example

There are two options:

1) Using rule
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserQueryServiceApplication.class)
public class UserControllerTest {

    @Rule
    public JsonContentVerifier jsonContentVerifier = new JsonContentVerifier();
    
    // ..

    @Test
    public void testGetUserWithoutFriendsWithoutExpandParam() throws Exception {
        String responseBody = mockMvc.perform(get("/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        jsonContentVerifier.assertJson(responseBody);
    }
}
```

2) Using JSON matcher
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserQueryServiceApplication.class)
public class UserControllerTest {

    @Rule
    public JsonContentLoader jsonContentLoader = new JsonContentLoader();
    
    // ..

    @Test
    public void testGetUserWithoutFriendsWithoutExpandParam() throws Exception {
        String responseBody = mockMvc.perform(get("/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        assertThat(responseBody, JsonMatcher.json(jsonContentLoader.load()));
    }
}
```

## Installation

Add following dependency to pom.xml
```xml
<dependency>
    <groupId>io.github.yashchenkon</groupId>
    <artifactId>json-verifier</artifactId>
    <version>1.0.0</version>
</dependency>
```
## Tests

Description will be added later.

## Contributors

Please, create pull requests in case of any fixes.

## License

MIT