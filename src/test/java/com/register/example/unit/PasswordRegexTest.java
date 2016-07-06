package com.register.example.unit;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Java6Assertions.assertThat;


@ActiveProfiles("test")
public class PasswordRegexTest {
    @Test
    public void checkValidation() throws Exception {
        String regex = "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{3,}$";

        assertThat("zaq1@WSX".matches(regex)).isTrue();
        assertThat("zaqWS".matches(regex)).isFalse();
        assertThat("1111111111".matches(regex)).isFalse();
        assertThat("!!!!!!!!!!!".matches(regex)).isFalse();
        assertThat("        zaq1@WSX".matches(regex)).isFalse();
        assertThat("aaaaaaaaaaaa".matches(regex)).isFalse();
        assertThat("m@łpa1_\\".matches(regex)).isFalse();
        assertThat("Da✡mian1993".matches(regex)).isTrue();
        assertThat("\\u2721_\\bCoś_".matches(regex)).isTrue();
        assertThat(".*\\".matches(regex)).isFalse();

    }

}
