package io.github.yashchenkon.test;

import org.apache.commons.io.IOUtils;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * JsonContentLoader is a rule for loading JSON files from the classpath.
 * This rule builds a path to the file based on the invoked test.
 * For example, this test loads JSON from the classpath resource "json/TestClass/test/request.json":
 * <pre>
 *     class TestClass {
 *
 *         &#064;Rule
 *         public JsonContentLoader jsonContentLoader = new JsonContentLoader();
 *
 *         &#064;Test
 *         public void test() {
 *             String json = jsonContentLoader.load("request");
 *         }
 *     }
 * </pre>
 *
 * @author Mykola Yashchenko
 */
public class JsonContentLoader extends TestWatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonContentLoader.class);

    /**
     * default file name {@value} to use for {@link #load()}
     */
    private static final String DEFAULT_FILE_NAME = "request";

    /**
     * template of the path to the file
     */
    private static final String PATH = "json/%s/%s/%s.json";

    /**
     * current test method name
     */
    private String methodName;

    /**
     * current test class
     */
    private Class<?> testClass;

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
     * loads json file from classpath
     * uses {@link #load(String)} with default file name
     *
     * @return loaded json as string
     * @throws IOException          if an I/O error occurs
     * @throws NullPointerException if the file doesn't exist
     */
    public String load() throws IOException {
        return load(DEFAULT_FILE_NAME);
    }

    /**
     * loads json file from classpath
     *
     * @param fileName name of the file without format (e.g "request" not "request.json")
     * @return loaded json as string
     * @throws IOException          if an I/O error occurs
     * @throws NullPointerException if the file doesn't exist
     */
    public String load(String fileName) throws IOException {
        String path = String.format(PATH, testClass.getSimpleName(), methodName, fileName);
        LOGGER.info("Reading file: {}", path);
        try (InputStream resourceAsStream = testClass.getClassLoader().getResourceAsStream(path)) {
            String json = IOUtils.toString(resourceAsStream, Charset.defaultCharset());
            LOGGER.info("Loaded JSON: \n{}", json);
            return json;
        }
    }
}