package io.github.yashchenkon.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import junit.framework.AssertionFailedError;
import org.apache.commons.io.IOUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * JsonContentVerifier is a rule to check JSON returned by services. It compares
 * JSON to files which are placed into classpath. The path to the file builds
 * automatically based on the test class name and test method name.
 *
 * @author Mykola Yashchenko
 */
public class JsonContentVerifier extends TestWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonContentVerifier.class);

    /**
     * default JSON {@link ObjectMapper}
     */
    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    /**
     * default file name {@value} to use for {@link #assertJson(String)}
     */
    private static final String DEFAULT_FILE_NAME = "response";

    /**
     * template of the path to the file
     */
    private static final String PATH = "json/%s/%s/%s.json";

    /**
     * JSON {@link ObjectMapper} to deserialize JSON into {@link JsonNode}
     */
    private ObjectMapper objectMapper = DEFAULT_MAPPER;

    /**
     * current test method name
     */
    private String methodName;

    /**
     * current test class
     */
    private Class<?> testClass;

    /**
     * Creates instance of rule with custom {@link ObjectMapper}
     * @param objectMapper custom {@link ObjectMapper}
     * @return instance of the rule
     */
    public static JsonContentVerifier from(ObjectMapper objectMapper) {
        JsonContentVerifier jsonContentVerifier = new JsonContentVerifier();
        jsonContentVerifier.objectMapper = objectMapper;
        return jsonContentVerifier;
    }

    /**
     * sets test class and method name to build path to file
     *
     * @param description description of the current test method
     */
    @Override
    protected void starting(Description description) {
        this.testClass = description.getTestClass();
        this.methodName = description.getMethodName();
    }

    /**
     * compare JSON to the content of the default file
     *
     * @param actualJson JSON to compare
     * @throws IOException          if an I/O error occurs
     * @throws NullPointerException if the file doesn't exist
     * @throws AssertionFailedError if actualJson is not equal to the content of the default file
     */
    public void assertJson(String actualJson) throws IOException {
        assertJson(actualJson, DEFAULT_FILE_NAME);
    }

    /**
     * compare JSON to the content of the specified file
     *
     * @param actualJson        JSON to compare
     * @param expectedFileName  file name from which to load expected content
     * @throws IOException          if an I/O error occurs
     * @throws NullPointerException if the file doesn't exist
     */
    public void assertJson(String actualJson, String expectedFileName) throws IOException {
        String path = String.format(PATH, testClass.getSimpleName(), methodName, expectedFileName);
        try (InputStream resourceAsStream = testClass.getClassLoader().getResourceAsStream(path)) {
            String expectedJson = IOUtils.toString(resourceAsStream, Charset.defaultCharset());
            JsonNode actual = objectMapper.readTree(actualJson);
            JsonNode expected = objectMapper.readTree(expectedJson);

            LOGGER.info("EXPECTED JSON: " + expectedJson);
            LOGGER.info("ACTUAL JSON: " + actualJson);

            String diff = JsonDiff.asJson(actual, expected).toString();
            if (!diff.equals("[]")) {
                throw new AssertionFailedError("Json objects are not equal: " + diff);
            }
        }
    }
}
