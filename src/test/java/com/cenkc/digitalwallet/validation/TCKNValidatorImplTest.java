package com.cenkc.digitalwallet.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TCKNValidatorImplTest {

    private TCKNValidatorImpl validator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private TCKN tcknAnnotation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new TCKNValidatorImpl();
        validator.initialize(tcknAnnotation);
    }

    @Nested
    @DisplayName("Valid TCKN tests")
    class ValidTCKNTests {
        @ParameterizedTest
        @DisplayName("Should accept valid TCKNs with 11 digits and starting with non-zero digit")
        @ValueSource(strings = {
                "12345678901",
                "98765432101",
                "10000000000",
                "11111111111",
                "99999999999"
        })
        public void validTCKN_shouldPassValidation(String validTCKN) {
            boolean isValid = validator.isValid(validTCKN, constraintValidatorContext);
            assertTrue(isValid, String.format("TCKN '%s' should be considered valid", validTCKN));
        }
    }

    @Nested
    @DisplayName("Invalid TCKN tests")
    class InvalidTCKNTests {

        // TODO fix 'java.lang.NullPointerException: Cannot invoke "java.lang.CharSequence.length()" because "this.text" is null'
/*
        @ParameterizedTest
        @NullSource
        @Order(1)
        @DisplayName("Should reject null TCKNs")
        public void nullTCKN_shouldFailValidation(String nullTCKN) {
            boolean isValid = validator.isValid(nullTCKN, constraintValidatorContext);
            assertFalse(isValid, "Null TCKN should be rejected");
        }
*/

        @ParameterizedTest
        @EmptySource
        @Order(2)
        @DisplayName("Should reject empty TCKNs")
        public void emptyTCKN_shouldFailValidation(String emptyTCKN) {
            boolean isValid = validator.isValid(emptyTCKN, constraintValidatorContext);
            assertFalse(isValid, "Empty TCKN should be rejected");
        }

        @ParameterizedTest
        @Order(3)
        @DisplayName("Should reject TCKNs with less than 11 digits")
        @ValueSource(strings = {
                "1234567",
                "1000000",
                "9999999"
        })
        public void lessThan11DigitsTCKN_shouldFailValidation(String lessThan11Digits) {
            boolean isValid = validator.isValid(lessThan11Digits, constraintValidatorContext);
            assertFalse(isValid, String.format("TCKN '%s' with less than 11 digits should be rejected", lessThan11Digits));
        }

        @ParameterizedTest
        @Order(4)
        @DisplayName("Should reject TCKNs with more than 11 digits")
        @ValueSource(strings = {
                "1234567890177",
                "1000000000023333",
                "999999999993432423"
        })
        public void moreThan11DigitsTCKN_shouldFailValidation(String moreThan11Digits) {
            boolean isValid = validator.isValid(moreThan11Digits, constraintValidatorContext);
            assertFalse(isValid, String.format("TCKN '%s' with more than 11 digits should be rejected", moreThan11Digits));
        }

        @ParameterizedTest
        @Order(5)
        @DisplayName("Should reject TCKNs that start with zero")
        @ValueSource(strings = {
                "01234567890",
                "00000000000",
                "09999999999"
        })
        public void TCKNWithALeadingZero_shouldFailValidation(String tcknWithALeadingZero) {
            boolean isValid = validator.isValid(tcknWithALeadingZero, constraintValidatorContext);
            assertFalse(isValid, String.format("TCKN '%s' starting with zero should be rejected", tcknWithALeadingZero));
        }

        @ParameterizedTest
        @Order(6)
        @DisplayName("Should reject TCKNs that includes non-numeric char(s)")
        @ValueSource(strings = {
                "1X345678901",
                "9WYF6543210",
                "1A000000000",
                "1111111111Z",
                "9999999999p"
        })
        public void nonNumericTCKN_shouldFailValidation(String nonNumericTCKN) {
            boolean isValid = validator.isValid(nonNumericTCKN, constraintValidatorContext);
            assertFalse(isValid, String.format("TCKN '%s' includes non-numeric char(s) should be rejected", nonNumericTCKN));
        }
    }




    // this should not be necessary when a proper algorithm implemented
    @Test
    @DisplayName("Test TCKN regex pattern directly")
    void testTCKNRegexPattern() {
        // Given
        String validPattern = TCKNValidatorImpl.TCKN_SIMPLE_REGEX;

        // Valid examples
        assertTrue("12345678901".matches(validPattern));
        assertTrue("98765432109".matches(validPattern));

        // Invalid examples
        assertFalse("01234567890".matches(validPattern)); // Leading zero
        assertFalse("1234567890".matches(validPattern));  // Less than 10 digits
        assertFalse("123456789012".matches(validPattern)); // More than 12 digits
        assertFalse("1234567890A".matches(validPattern)); // Contains non-Numeric
    }

}