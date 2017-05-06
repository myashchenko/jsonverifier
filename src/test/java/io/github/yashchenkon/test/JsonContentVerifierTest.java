package io.github.yashchenkon.test;

import org.junit.Rule;
import org.junit.Test;

/**
 * @author Mykola Yashchenko
 */
public class JsonContentVerifierTest {

    @Rule
    public JsonContentVerifier jsonContentVerifier = new JsonContentVerifier();

    @Test
    public void shouldThrowExceptionWhenFileDoesNotExist() {

    }

    @Test
    public void shouldVerifyEqualObjects() {

    }
}
