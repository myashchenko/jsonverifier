package io.github.yashchenkon.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Mykola Yashchenko
 */
public class JsonContentLoaderTest {

    private static final String expected = "{\n" +
            "  \"string\" : \"test\",\n" +
            "  \"number\" : 1,\n" +
            "  \"boolean\" : true\n" +
            "}";

    @Rule
    public JsonContentLoader jsonContentLoader = new JsonContentLoader();

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenFileDoesNotExist_Default() throws IOException {
        jsonContentLoader.load();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenFileDoesNotExist() throws IOException {
        jsonContentLoader.load("test123");
    }

    @Test
    public void shouldLoadDefaultFile() throws IOException {
        String actual = jsonContentLoader.load();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldLoadFile() throws IOException {
        String actual = jsonContentLoader.load("fileName");

        Assert.assertEquals(expected, actual);
    }
}
