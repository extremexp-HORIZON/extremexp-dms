package eu.extremexp.dms.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.extremexp.dms.GraphicalJSONExperimentModel;
import eu.extremexp.dms.GraphicalJSONModel;
import eu.extremexp.dms.GraphicalJSONWorkflowModel;
import eu.extremexp.dms.XDSLModelIO;

import java.io.IOException;
import java.util.*;

public class ExperimentJSONParser {
    /**
     * Immutable pair of source/target nodes for an edge.
     */

    private final List<WorkflowJSONParser> workflowJSONParsers;

    private final List<JStep> jSteps;

    /**
     * Parse a JSON workflow string with top-level arrays: "nodes" and "edges".
     * Expected edge fields: id, type, source, target.
     * Expected node fields: id, type (others ignored).
     *
     * @param json JSON content as a String
     * @throws IllegalArgumentException if parsing fails or content is empty
     */
    public ExperimentJSONParser(String json) {
        Objects.requireNonNull(json, "json");

        workflowJSONParsers = new ArrayList<>();

        jSteps = new ArrayList<>();


        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);

            // Build node list and index by id for quick lookup
            Map<String, JNode> idToNode = new HashMap<>();
            JsonNode experiment = root.path("experiment");
            for (var step: experiment.get("steps")){
               jSteps.add(new JStep(
                                   step.get("id").asText(),
                                   step.get("type").asText(),
                                   step.get("executionOrder").asInt(),
                                   step)
               );
            }

            GraphicalJSONModel graphicalJSONModel = new GraphicalJSONModel();

            JsonNode workflows = root.path("workflows");
            for (var wjson : workflows){
                System.out.println(wjson.get("graphical_model"));
                WorkflowJSONParser wjs = new WorkflowJSONParser(wjson.get("graphical_model").toString());
                var graphicalJSONxDSLModelIO = new GraphicalJSONWorkflowModel(wjs.getNodes(), wjs.getEdges(), wjson.get("name").asText());
                workflowJSONParsers.add(wjs);
                graphicalJSONModel.addWorkflows(graphicalJSONxDSLModelIO);
            }

            GraphicalJSONExperimentModel graphicalJSONExperimentModel = new GraphicalJSONExperimentModel(jSteps, experiment.get("name").asText());

            graphicalJSONModel.addExperiment(graphicalJSONExperimentModel);

            XDSLModelIO xdslModelIO = new XDSLModelIO(graphicalJSONModel);
            System.out.println(xdslModelIO.formattedSerialize());

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


    public List<JStep> getSteps() {
        return List.copyOf(this.jSteps);
    }

}
