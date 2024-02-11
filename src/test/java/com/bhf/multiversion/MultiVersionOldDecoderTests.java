package com.bhf.multiversion;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

/**
 * Implementations of this reside in a project which
 * only loads the old version of the decoder and focus on testing
 * only the version mismatch case
 *
 * Allows for seperation of concerns between testing the version
 * mismatch case and the version match case
 */
public interface MultiVersionOldDecoderTests {

    /**
     * Decode the new message using the old decoder
     */
    @Test
    @Tags({@Tag("Multiversion"), @Tag("NewMessageOldDecoder"), @Tag("VersionMismatch")})
    public void decodeNewMessageOldDecoder();

    /**
     * Explicitly test the new version header
     * with the old decoder
     */
    @Test
    @Tags({@Tag("Multiversion"), @Tag("NewHeaderOldDecoder"), @Tag("VersionMismatch")})
    public void testNewHeader();
}
