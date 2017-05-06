# JSON verifier

The rules which help to verify JSON returned by REST services (Spring, JAX-RS, etc).

## Code Example

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserQueryServiceApplication.class)
public class UserControllerTest {

    // ...

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

## Motivation

During writing the tests for REST services I saw that there are a lot of hardcoded JSON into test classes. 
I think that it's very bad practice and we should avoid hard coding. The better way is to save expected JSON content 
into the files from a classpath. Besides, we can automatically generate a path to this files based on a test 
class name and on a method name.

## Installation

Add following dependency to pom.xml
```xml
<dependency>
    <groupId>io.github.yashchenkon</groupId>
    <artifactId>json-verifier</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
## Tests

Will be added later.

## Contributors

Please, create pull requests in case of any fixes.

## License

A short snippet describing the license (MIT, Apache, etc.)