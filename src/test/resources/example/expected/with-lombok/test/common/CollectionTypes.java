package example.testGenerate.test.common;

import example.testGenerate.test.common.CollectionTypesInnerSchema;
import example.testGenerate.test.common.CollectionTypesListWithInnerSchemaAndExistingObject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import testGenerate.ClassForExtending;

/**
* Here was located all supported collections
*/
@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true)
@AllArgsConstructor
public class CollectionTypes {


    private List<LocalDate> listOfDate;

    /**
     * This is standalone uri
     */
    private List<URI> listOfStandAloneUri;

    /**
     * This is standalone date
     */
    private List<LocalDate> listOfStandAloneDate;

    private List<LocalDateTime> listOfLocalDateTime;

    private List<OffsetDateTime> listOfOffsetDateTime;

    private List<Integer> listOfInteger;

    private List<Byte> listOfByte;

    private List<Double> listOfDouble;

    private List<Float> listOfFloat;

    private List<BigInteger> listOfBigInteger;

    private List<BigDecimal> listOfBigDecimal;

    private List<Long> listOfLongsWithRealization = new ArrayList<>();

    @Valid
    private List<CollectionTypesListWithInnerSchemaAndExistingObject> listWithInnerSchemaAndExistingObject;

    @Valid
    private List<ClassForExtending> listOfExistingObject;

    @Valid
    private Set<ClassForExtending> setOfExistingObject;

    @Valid
    private List<CollectionTypesInnerSchema> innerSchema;

    private Set<LocalDate> setOfDate;

    private Set<LocalDateTime> setOfLocalDateTime;

    private Set<OffsetDateTime> setOfOffsetDateTime;

    private Set<Integer> setOfInteger;

    private Set<Byte> setOfByte;

    private Set<Double> setOfDouble;

    private Set<Float> setOfFloat;

    private Set<BigInteger> setOfBigInteger;

    private Set<BigDecimal> setOfBigDecimal;

    private Set<Long> setOfLongsWithRealization = new HashSet<>();
}