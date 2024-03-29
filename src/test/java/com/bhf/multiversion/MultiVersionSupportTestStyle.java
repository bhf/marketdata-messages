package com.bhf.multiversion;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

/**
 * Sometimes you may need to support multiple versions of a message.
 *
 * <p>We need to check that the old decoder can handle decoding messages generated by encoders from
 * the new version, which should only have the addition of new optional fields.
 *
 * We also need to check that the new decoder can handle decoding messages generated by encoders from
 * the old version, which does not have the new additional optional fields.
 *
 * <p>Note: You can also choose not to handle messages from a schema that your app is not explicitly setup
 * for. This is typically an explicit check in the isValid() method of something like
 * AbstractSBEDecodingHandler
 *
 * There are a few potential approaches:
 *
 * 1. The new schema has to have a different package space e.g. version number in the package name. This
 * is the simplest to implement and to unit test, but could involve changing a lot of code
 * across an organisation (with different teams on different release cycles)
 * and therefore entail release risk.
 *
 * 2. Use a classloader to load the different versions of the encoding/decoding classes. This is
 * challenging in the general case because of potential changes to sub-decoders or the addition
 * of new sub-decoders and becomes very cumbersome to implement and is inherently fragile and risk prone
 *
 * 3. Create binary samples of the new message and old message. Test the old and new decoder in
 * seperate projects so neither project has both dependencies, against the known binary samples. You're still
 * following the process of decoding an old and new message, using an old and a new decoder.
 * After the release is complete and all apps are on the new schema you can delete the projects
 * created for testing - the only real dependency these projects should differ on is
 * the generated messages. This may be more work on the build process side,
 * but has the advantage of being relatively simple, no custom
 * classloading and no change to the package names (so less release risk).
 */
public interface MultiVersionSupportTestStyle {

    /**
     * Decode the new message using the old decoder
     */
    @Test
    @Tags({@Tag("Multiversion"), @Tag("NewMessageOldDecoder")})
    public void decodeNewMessageOldDecoder();

    /**
     * Decode the old message using the new decoder
     */
    @Test
    @Tags({@Tag("Multiversion"), @Tag("OldMessageNewDecoder")})
    public void decodeOldMessageNewDecoder();

    /**
     * Explicitly test the new version header
     */
    @Test
    @Tags({@Tag("Multiversion"), @Tag("NewMessageHeader")})
    public void testNewHeader();

    /**
     * Explicitly test the old version header
     */
    @Tags({@Tag("Multiversion"), @Tag("OldMessageHeader")})
    @Test
    public void testOldHeader();

    /**
     * Decode the new message using the new decoder - ideally
     * you'd just use your other unit test styles but I've
     * put this here for thoroughness
     */
    @Test
    public void decodeNewMessageNewDecoder();

    /**
     * Decode the old message using the old decoder - ideally
     * you'd just use your other unit test styles but I've
     * put this here for thoroughness
     */
    @Test
    public void decodeOldMessageOldDecoder();
}
