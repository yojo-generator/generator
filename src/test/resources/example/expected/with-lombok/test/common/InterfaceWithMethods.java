package example.testGenerate.test.common;

import javax.annotation.processing.Generated;

@Generated("Yojo")
/**
* It is interface with Methods
*/
public interface InterfaceWithMethods {/**
* This is void method
*/

    void someOne(String someString, SomeObjectInnerSchema schema);
/**
* This is method with return statement
*/

    SomeObjectInnerSchema anotherOne(String someString, SomeObjectInnerSchema schema);

}