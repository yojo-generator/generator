package enumvalues.messages;

import enumvalues.common.PayloadStatus;
import enumvalues.common.PayloadStatusWithDefault;
import javax.annotation.processing.Generated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Generated("Yojo")
@Data
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
@AllArgsConstructor
public class TestMessage {


    private PayloadStatus status;

    private PayloadStatusWithDefault statusWithDefault;
}