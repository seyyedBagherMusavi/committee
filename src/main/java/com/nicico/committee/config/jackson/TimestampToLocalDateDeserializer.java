package com.nicico.committee.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

/**
 * A custom Jackson deserializer for LocalDate that can handle both a Long timestamp (milliseconds)
 * and a standard ISO date string (e.g., "1990-12-01").
 */
public class TimestampToLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();

        // Case 1: The date is sent as a number (timestamp)
        if (token == JsonToken.VALUE_NUMBER_INT) {
            long timestamp = p.getLongValue();
            return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
        }

        // Case 2: The date is sent as a string
        if (token == JsonToken.VALUE_STRING) {
            String dateString = p.getText().trim();
            if (dateString.isEmpty()) {
                return null;
            }
            try {
                // Attempt to parse as a standard ISO date string
                return LocalDate.parse(dateString);
            } catch (DateTimeParseException e) {
                // If parsing fails, you could try other formats or throw an exception
                throw new IOException("Failed to parse date string: " + dateString, e);
            }
        }

        // If the value is neither a number nor a string, return null or handle as an error
        return null;
    }
}
