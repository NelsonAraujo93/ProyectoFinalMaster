package com.example.securityservice.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@Transactional
public class BlacklistedTokenTest {

    @Test
    public void testGettersAndSetters() {
        BlacklistedToken blacklistedToken = new BlacklistedToken();

        // Set values
        blacklistedToken.setId(1L);
        blacklistedToken.setToken("sampleToken");

        // Verify values
        assertEquals(1L, blacklistedToken.getId());
        assertEquals("sampleToken", blacklistedToken.getToken());
    }
}
