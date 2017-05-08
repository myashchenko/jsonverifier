package io.github.yashchenkon.test;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Mykola Yashchenko
 */
public class JsonContentVerifierTest {

    private static final String actualJson = "{\n" +
            "  \"string\" : \"test\",\n" +
            "  \"number\" : 1,\n" +
            "  \"boolean\" : true\n" +
            "}";

    @Rule
    public JsonContentVerifier jsonContentVerifier = new JsonContentVerifier();

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenFileDoesNotExist() throws IOException {
        jsonContentVerifier.assertJson(actualJson);
    }

    @Test
    public void shouldVerifyEqualObjects() throws IOException {
        jsonContentVerifier.assertJson(actualJson, "fileName");
    }
}
