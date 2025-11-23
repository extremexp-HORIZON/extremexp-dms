package eu.extremexp.dms.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.extremexp.dms.GraphicalJSONModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WorkflowJSONParser {
    /**
     * Immutable pair of source/target nodes for an edge.
     */
    private final List<JNode> JNodes;
    private final List<JEdge> JEdges;

    /**
     * Parse a JSON workflow string with top-level arrays: "nodes" and "edges".
     * Expected edge fields: id, type, source, target.
     * Expected node fields: id, type (others ignored).
     *
     * @param json JSON content as a String
     * @throws IllegalArgumentException if parsing fails or content is empty
     */
    public WorkflowJSONParser(String json) {
        Objects.requireNonNull(json, "json");
        this.JNodes = new ArrayList<>();
        this.JEdges = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);

            // Build node list and index by id for quick lookup
            Map<String, JNode> idToNode = new HashMap<>();
            JsonNode nodesArr = root.path("nodes");
            if (nodesArr.isArray()) {
                for (JsonNode n : nodesArr) {
                    String id = textOrNull(n, "id");
                    if (id == null || id.isBlank()) continue;
                    String type = textOrNull(n, "type");
                    JsonNode data = jsonOrEmpty(n, "data");
                    JNode JNode = new JNode(id, type, data);
                    JNodes.add(JNode);
                    idToNode.put(id, JNode);
                }
            }

            // Build edges map keyed by Edge, value is Endpoints(source, target)
            JsonNode edgesArr = root.path("edges");
            if (edgesArr.isArray()) {
                for (JsonNode e : edgesArr) {
                    String id = textOrNull(e, "id");
                    String type = textOrNull(e, "type");
                    String sourceId = textOrNull(e, "source");
                    String targetId = textOrNull(e, "target");
                    if (id == null || sourceId == null || targetId == null) {
                        // skip malformed edges
                        continue;
                    }
                    JNode source = idToNode.get(sourceId);
                    JNode target = idToNode.get(targetId);
                    if (source == null || target == null) {
                        // If JSON edges reference unknown nodes, skip this edge
                        continue;
                    }
                    JEdge JEdge = new JEdge(id, type, sourceId, targetId);
                    JEdges.add(JEdge);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse JSON content", e);
        }
    }

    private static String textOrNull(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return v != null && !v.isNull() ? v.asText() : null;
    }

    private static JsonNode jsonOrEmpty(JsonNode node, String field) {
        JsonNode v = node.get(field);
        return v != null && !v.isNull() ? v : null;
    }


    public List<JNode> getNodes() {
        return List.copyOf(JNodes);
    }

    public List<JEdge> getEdges() {
        return List.copyOf(JEdges);
    }
}
