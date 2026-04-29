package example.testGenerate.test.common;

import javax.annotation.processing.Generated;

@Generated("Yojo")
public interface InterfaceWithMethodsWithoutDescription {

    void someOne(String someString, SomeObjectInnerSchema schema);

    SomeObjectInnerSchema anotherOne(String someString, SomeObjectInnerSchema schema);

}