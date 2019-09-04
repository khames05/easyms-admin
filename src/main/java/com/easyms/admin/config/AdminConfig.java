package com.easyms.admin.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.codecentric.boot.admin.server.domain.values.Registration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Configuration
public class AdminConfig {

    @Bean
    public ObjectMapper mapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(Registration.class, new JsonDeserializer<Registration>() {
            @Override
            public Registration deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
                JsonNode node = jsonParser.getCodec().readTree(jsonParser);

                return Registration.builder()
                        .name(node.get("name").asText())
                        .managementUrl(node.get("managementUrl").asText())
                        .healthUrl(node.get("healthUrl").asText())
                        .serviceUrl(node.get("serviceUrl").asText())
                        .metadata(calculateMetadataMap(node.get("metadata")))
                        .build();
            }

            private Map<String, String> calculateMetadataMap(JsonNode metadataNode) {
                Iterator<String> metadataFieldNamesIterator = metadataNode.fieldNames();
                Stream<String> metadataFieldNamesStream = StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(metadataFieldNamesIterator, Spliterator.ORDERED), false);

                return metadataFieldNamesStream.collect(Collectors.toMap(Function.identity(),
                        e -> metadataNode.get(e).asText()));
            }
        });
        objectMapper.registerModule(module);

        return objectMapper;
    }
}
