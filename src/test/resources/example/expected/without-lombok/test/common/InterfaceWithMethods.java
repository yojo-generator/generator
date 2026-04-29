package example.testGenerate.test.common;

import javax.annotation.processing.Generated;

/**
* It is interface with Methods
*/
@Generated("Yojo")
public interface InterfaceWithMethods {

    /**
     * This is void method
     */
    void someOne(String someString, SomeObjectInnerSchema schema);

    /**
     * This is method with return statement
     */
    SomeObjectInnerSchema anotherOne(String someString, SomeObjectInnerSchema schema);

}