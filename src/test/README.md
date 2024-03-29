# Unit Testing #

No testing pipeline is infallible, so we have to try and create multiple layers of safety nets for ourselves.

The below describes various approaches to the testing of encoding and decoding messages, and the package structure of this test folder

Please bear in mind that different people and organisations use unit tests in different ways - not all unit tests necessarilly need to be part of your build pipeline, but can also just be unit tests which are useful for developers to run locally.

## Package Description ##

All discussions below are regarding classes you can find in com.bhf package of this test folder, the classes in com.bht are autogenerated from a tool I wrote and are mainly scaffold.

### encodervalidation ###


**EncoderCallOrderingTestStyles**


Testing encoders for correct call ordering using Mockito. Alternatives to mockito are using anonymous classes with a collection for recording the ordering

The problem with this comes if you write the unit test in the same way that you write the encode helper i.e. both have the same ordering, but the ordering is incorrect. This is where the standard tests from ExploringEncoderUnitTestStyles come in useful. Other strategies for solving this risk are to have one person write the unit tests and another person the encoder.

Alternatively if the encoder, encode helper and unit test are all autogenerated, there is still the risk that the way the codegenerator is interpreting the fields and messages is wrong. The standard value check type tests (like those in ExploringEncoderUnitTestStyles) may help in this case also, but if those are also autogenerated, then they may also suffer from the same biases or issues.

A third solution is to conduct the tests against known samples of raw binary which represent known SBE messages. See "ExploringRawMessageTestStyles".

**EncoderValueUnitTestStyles** 

Testing encoders based on different values using 4 strategies:

1. Hard coded values
2. Randomly generated values
3. Extreme values
4. Against a known fixed amount of argument combinations

Cases 2 and 3 can suffer from examples where the SBE type doesn't match the Java type range, but this can be solved using explicit fields for max and min
values somewhere centrally. It is also not clear whether this kind of check should be done at this level e.g. if as part of your encoding chain you have
validation steps on the values of the fields being encoded then it would be better to test that field ranges are validated in a seperate group of tests
which test the validation process explicitly

Case 4 offers more value range coverage vs Case 1 (hard coded values) and may be preferable to random values (deterministic values being tested against)

### fieldvalidation ###

**CombinedFieldValidatorStyle** 

A sample field value validator for a single sided quote with 2 different styles of validating. The first one should inline but just incase, there is a final nested version which should be eligible for aggressive inlining. The generated bytecode of the second version is also half the size, but still bigger than 35 bytes so could be further split down if needed. This also depends on your MaxInlineSize, whether the method becomes hot etc

This style of validation does not offer any feedback in terms of which field is not valid. It's more appropriate if you don't care why validation failed, it just did. It also requires you have all field values you want to check in advance of doing the validation.

**FieldSpecificValidatorStyle**

A sample field value validator for a single sided quote where we explicitly validate each field in order

This style of validation means you get feedback on whether a specific field is valid or not.

This may be more appropriate for use cases where you are validating fields and encoding them in a rolling process i.e. recieve a field value,
validate it, encode it into the correct SBE field, repeat.

This is different to the case where you have all your field values in advance and you're following a process that looks more like: assign all field values into local variables, use the local variables to encode the SBE message in a single call to an encode helper which handles encoding the message in the correct order

### FieldValidationTestStyles ###
If you are verifying/validating the fields which you are encoding as part of your encode chain then you can test that validation separately. You may or may not choose to use validators depending on your use case, latency budget etc. There are two sample implementation for a validator located in this package.

By creating a seperate test for each field, where all fields are valid except the one being tested, we can exercise the boundaries of the validation process.

### multiversion ###

Sometimes you may need to support multiple versions of a message.
We need to check that the old decoder can handle decoding messages generated by encoders from
the new version, which should only have the addition of new optional fields.

We also need to check that the new decoder can handle decoding messages generated by encoders from
the old version, which does not have the new additional optional fields.

Note: You can also choose not to handle messages from a schema that your app is not explicitly setup
for. This is typically an explicit check in the isValid() method of something like
AbstractSBEDecodingHandler

There are a few potential approaches:

1. The new schema has to have a different package space e.g. version number in the package name. This
   is the simplest to implement and to unit test, but could involve changing a lot of code
   across an organisation (with different teams on different release cycles)
   and therefore entail release risk.

2. Use a classloader to load the different versions of the encoding/decoding classes. This is
   challenging in the general case because of potential changes to sub-decoders or the addition
   of new sub-decoders and becomes very cumbersome to implement and is inherently fragile and risk prone

3. Create binary samples of the new message and old message. Test the old and new decoder in
   seperate projects so neither project has both dependencies, against the known binary samples. You're still
   following the process of decoding an old and new message, using an old and a new decoder.
   After the release is complete and all apps are on the new schema you can delete the projects
   created for testing - the only real dependency these projects should differ on is
   the generated messages. This may be more work on the build process side,
   but has the advantage of being relatively simple, no custom
   classloading and no change to the package names (so less release risk).

Two interfaces are suggested, allowing for a separation of concerns between testing the version
mismatch case and the version match case
 
