package com.bhf.fieldvalidation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bht.md.messages.MarketState;
import com.bht.md.messages.QuoteCondition;
import com.bht.md.messages.QuoteSide;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

/**
 * If you are verifying/validating the fields which you are encoding
 * as part of your encode chain then you can test that validation
 * separately. You may or may not choose to use validators depending on
 * your use case, latency budget etc. There are two sample
 * implementation for a validator located in this package.
 *
 * By creating a seperate test for each field, where all fields are valid
 * except the one being tested, we can exercise the boundaries
 * of the validation process.
 */
public class FieldValidationTestStyles {

    /** Test the validation of the time field done by
     * a validator which offers no feedback on the incorrect field
     */
    @Test
    @Tags({@Tag("FieldValueValidation"), @Tag("SingleSidedQuote")})
    public void testNestedValidationInvalidTime() {

        CombinedFieldValidatorStyle validator = new CombinedFieldValidatorStyle();

        // choose your favourite value injection methodology

        final long time = 123455;
        final short symb = 1;
        final QuoteCondition quoteCondition = QuoteCondition.Direct;
        final MarketState marketState = MarketState.CONTINUOUS_TRADING_MODE;
        final long price = 123;
        final long qty = 55;
        final QuoteSide quoteSide = QuoteSide.Ask;

        assertTrue(validator.isValid(time, symb, marketState, quoteCondition, price, qty, quoteSide));
        long invalidTime = -1;
        assertFalse(validator.isValid(invalidTime, symb, marketState, quoteCondition, price, qty, quoteSide));
    }

    /** Test the validation of the time field done by
     * a validator which offers feedback on the incorrect field*/
    @Test
    @Tags({@Tag("FieldValueValidation"), @Tag("SingleSidedQuote")})
    public void testFieldSpecificValidationInvalidTime() {
        FieldSpecificValidatorStyle validator = new FieldSpecificValidatorStyle();
        assertTrue(validator.isTimeValid(1000));
        assertFalse(validator.isTimeValid(-1000));
    }
}
