/* Generated SBE (Simple Binary Encoding) message codec. */
package com.bht.md.messages;

@SuppressWarnings("all")
public enum QuoteCondition {
    Implied((short) 0),

    Direct((short) 1),

    NonFirm((short) 2),

    /** To be used to represent not present or null. */
    NULL_VAL((short) 255);

    private final short value;

    QuoteCondition(final short value) {
        this.value = value;
    }

    /**
     * The raw encoded value in the Java type representation.
     *
     * @return the raw value encoded.
     */
    public short value() {
        return value;
    }

    /**
     * Lookup the enum value representing the value.
     *
     * @param value encoded to be looked up.
     * @return the enum value representing the value.
     */
    public static QuoteCondition get(final short value) {
        switch (value) {
            case 0:
                return Implied;
            case 1:
                return Direct;
            case 2:
                return NonFirm;
            case 255:
                return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
