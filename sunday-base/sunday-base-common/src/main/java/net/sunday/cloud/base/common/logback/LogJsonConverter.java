package net.sunday.cloud.base.common.logback;


import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import net.sunday.cloud.base.common.util.json.JsonUtils;
import org.slf4j.helpers.MessageFormatter;

import java.util.Objects;
import java.util.stream.Stream;

public class LogJsonConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {

        if (Objects.isNull(event.getArgumentArray())
                || event.getArgumentArray().length == 0) {
            return super.convert(event);
        }

        try {
            Object[] objects = Stream.of(event.getArgumentArray())
                    .map(argument -> {
                        if (argument instanceof String) {
                            // String类型直接打印
                            return argument;
                        } else {
                            try {
                                return JsonUtils.toJsonString(argument);
                            } catch (Exception e) {
                                return super.convert(event);
                            }
                        }
                    }).toArray();
            return MessageFormatter.arrayFormat(event.getMessage(), objects).getMessage();
        } catch (Exception ignore) {
            return super.convert(event);
        }
    }
}
