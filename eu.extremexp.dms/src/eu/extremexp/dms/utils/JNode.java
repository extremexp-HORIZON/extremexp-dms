package eu.extremexp.dms.utils;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Graph node with id and type (e.g., start, task, data, end).
 */
public record JNode(String id, String type, JsonNode data) {
    public JNode {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Node id must not be blank");
        }
    }
}
