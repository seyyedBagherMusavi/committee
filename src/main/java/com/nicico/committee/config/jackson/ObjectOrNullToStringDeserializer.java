package com.nicico.companies.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * A custom Jackson deserializer that converts any non-string JSON value to null.
 * If the incoming value is a JSON string, it is returned as is.
 * If the incoming value is a JSON object (e.g., `{...}`), array, number, or boolean, it is deserialized as null.
 * This is useful for fields that may be sent as a structured object when they should be a simple string.
 */
public class ObjectOrNullToStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Check the type of the current JSON token
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            // If it's a string, return its text value
            return p.getText();
        }

        // If the token represents a structured type (like an object or array), we must skip its children
        // to ensure the parser continues correctly with the next field.
        if (p.currentToken() == JsonToken.START_OBJECT || p.currentToken() == JsonToken.START_ARRAY) {
            p.skipChildren();
        }

        // For any non-string value (object, array, number, boolean, null), return null
        return null;
    }
}
