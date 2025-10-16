package eu.extremexp.dms.utils;

/**
 * Graph edge with identifiers and simple metadata.
 */
public record JEdge(String id, String type, String sourceId, String targetId) {
    public JEdge {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Edge id must not be blank");
        }
        if (sourceId == null || sourceId.isBlank()) {
            throw new IllegalArgumentException("Edge sourceId must not be blank");
        }
        if (targetId == null || targetId.isBlank()) {
            throw new IllegalArgumentException("Edge targetId must not be blank");
        }
    }
}
