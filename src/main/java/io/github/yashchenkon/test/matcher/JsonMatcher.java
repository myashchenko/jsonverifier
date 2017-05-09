package io.github.yashchenkon.test.matcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.io.IOException;
import java.util.Objects;

/**
 * JsonMatcher is a class for verifying equality of JSON.
 * <pre>
 *     class TestClass {
 *
 *         &#064;Rule
 *         public JsonContentLoader jsonContentLoader = new JsonContentLoader();
 *
 *         &#064;Test
 *         public void test() {
 *             String actualJson = ...
 *             String expectedJson = jsonContentLoader.load("request");
 *             assertThat(actualJson, JsonMatcher.json(expectedJson));
 *         }
 *     }
 * </pre>
 *
 * @author Mykola Yashchenko
 */
public class JsonMatcher extends BaseMatcher<String> {

    /**
     * default JSON {@link ObjectMapper}
     */
    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    /**
     * JSON {@link ObjectMapper} to deserialize JSON into {@link JsonNode}
     */
    private ObjectMapper objectMapper;

    /**
     * expected JSON content
     */
    private String expectedJson;

    /**
     * private constructor
     * @param expectedJson expected JSON content
     */
    private JsonMatcher(String expectedJson, ObjectMapper objectMapper) {
        Objects.requireNonNull(objectMapper);
        Objects.requireNonNull(expectedJson);
        this.expectedJson = expectedJson;
        this.objectMapper = objectMapper;
    }

    /**
     * Generates a description of the object.
     *
     * @param description
     *     The description to be built or appended to.
     */
    @Override
    public void describeTo(Description description) {
        description.appendValue(expectedJson);
    }

    /**
     * Evaluates the matcher for argument <var>actualJson</var>.
     *
     * @param actualJson the JSON content against which the matcher is evaluated.
     * @return <code>true</code> if <var>actualJson</var> matches <var>expectedJson</var>, otherwise <code>false</code>.
     */
    @Override
    public boolean matches(Object actualJson) {
        try {
            JsonNode actual = objectMapper.readTree(actualJson.toString());
            JsonNode expected = objectMapper.readTree(expectedJson);
            String diff = JsonDiff.asJson(actual, expected).toString();
            return diff.equals("[]");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a matcher that matches when the examined JSON content is equal to the specified <code>JSON</code>.
     */
    public static Matcher<String> json(String actualJson) {
        return new JsonMatcher(actualJson, DEFAULT_MAPPER);
    }

    /**
     * Creates a matcher that matches when the examined JSON content is equal to the specified
     * <code>JSON</code>. ObjectMapper as additional parameter in case of custom
     * deserialization.
     *
     * For example:
     * <pre>
     * assertThat("{\"test"\ : \"test\"}", json("{\"test"\ : \"test\"}"));
     * </pre>
     */
    public static Matcher<String> json(String actualJson, ObjectMapper objectMapper) {
        return new JsonMatcher(actualJson, objectMapper);
    }
}
