package ru.yojo.codegen.mapper;

import org.springframework.stereotype.Component;
import ru.yojo.codegen.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static ru.yojo.codegen.constants.ConstantsEnum.*;
import static ru.yojo.codegen.util.MapperUtil.castObjectToMap;
import static ru.yojo.codegen.util.MapperUtil.getStringValueIfExistOrElseNull;

@Component
public class MessageMapper {

    public List<Message> mapMessagesToObjects(Map<String, Object> messages, LombokProperties lombokProperties) {
        List<Message> messageList = new ArrayList<>();
        messages.forEach((messageName, messageValues) -> {
            Map<String, Object> messageMap = castObjectToMap(messageValues);
                Message message = new Message();
                message.setMessageName(capitalize(messageName));
                message.setLombokProperties(lombokProperties);
                message.setMessageProperties(getProperties(messageMap));
                messageList.add(message);
        });
        return messageList;
    }

    private MessageProperties getProperties(Map<String, Object> messageMap) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setName(getStringValueIfExistOrElseNull(NAME, messageMap));
        messageProperties.setTitle(getStringValueIfExistOrElseNull(TITLE, messageMap));
        messageProperties.setSummary(getStringValueIfExistOrElseNull(SUMMARY, messageMap));
        messageProperties.setPayload(getPayload(castObjectToMap(messageMap.get(PAYLOAD.getValue()))));

        return messageProperties;
    }

    @SuppressWarnings("all")
    private MessagePayload getPayload(Map<String, Object> payload) {
        return new MessagePayload(capitalize(getStringValueIfExistOrElseNull(REFERENCE, payload).replaceAll(".+/", "")));
    }
}
