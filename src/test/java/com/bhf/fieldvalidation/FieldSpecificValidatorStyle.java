package com.bhf.fieldvalidation;

import com.bht.md.messages.QuoteCondition;

/**
 * A sample field value validator for a single sided quote where
 * we explicitly validate each field in order
 *
 * This style of validation means you get feedback on whether a specific
 * field is valid or not.
 *
 * This may be more appropriate for use cases where you are validating
 * fields and encoding them in a rolling process i.e. recieve a field value,
 * validate it, encode it into the correct SBE field, repeat.
 *
 * This is different to the case where you have all your field values in advance and you're
 * following a process that looks more like: assign all field values into local
 * variables, use the local variables to encode the SBE message in a single call to
 * an encode helper which handles encoding the message in the correct order
 */
public class FieldSpecificValidatorStyle {

    private static final short MAX_SYMB_VALUE = 254;

    public boolean isTimeValid(long time) {
        return time > 0;
    }

    public boolean isQuoteConditionValid(QuoteCondition quoteCondition) {
        return null != quoteCondition;
    }
}
