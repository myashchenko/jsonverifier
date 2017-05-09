package io.github.yashchenkon.test.matcher;

import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * @author Mykola Yashchenko
 */
public class JsonMatcherTest {

    private static final String JSON = "{\n" +
            "  \"string\" : \"test\",\n" +
            "  \"number\" : 1,\n" +
            "  \"boolean\" : true\n" +
            "}";

    @Test(expected = NullPointerException.class)
    public void shouldNotVerifyNullObjects() {
        assertThat(null, JsonMatcher.json(null));
    }

    @Test
    public void shouldVerifyEmptyArrayAndObject() {
        assertThat("{}", JsonMatcher.json("{}"));
        assertThat("[]", JsonMatcher.json("[]"));
    }

    @Test
    public void shouldVerifyEqualityOfAnyObjects() {
        assertThat(JSON, JsonMatcher.json(JSON));
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowAssertionErrorIfObjectsAreNotEqual() {
        assertThat("{}", JsonMatcher.json("[]"));
    }
}
