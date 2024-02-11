package com.bhf.multiversion;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

/**
 * Implementations of this reside in a project that only
 * loads the new version of the decoder and focus on testing
 * only the version mismatch case.
 *
 * Allows for seperation of concerns between testing the version
 * mismatch case and the version match case
 */
public interface MultiVersionNewDecoderTests {
    /**
     * Decode the old message using the new decoder
     */
    @Test
    @Tags({@Tag("Multiversion"), @Tag("OldMessageNewDecoder"), @Tag("VersionMismatch")})
    public void decodeOldMessageNewDecoder();

    /**
     * Explicitly test the old version header
     * with the new decoder
     */
    @Tags({@Tag("Multiversion"), @Tag("OldHeaderNewDecoder"), @Tag("VersionMismatch")})
    @Test
    public void testOldHeader();
}
