package eu.extremexp.dms.utils;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Graph node with id and type (e.g., start, task, data, end).
 */
public record JStep(String id, String type, Integer executionOrder, JsonNode data) {
    public JStep {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Step id must not be blank");
        }
    }
}
