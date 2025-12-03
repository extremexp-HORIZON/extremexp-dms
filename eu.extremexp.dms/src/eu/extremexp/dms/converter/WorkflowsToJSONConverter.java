package eu.extremexp.dms.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.extremexp.dms.utils.JCompositeWorkflow;
import eu.extremexp.dms.utils.JEdge;
import eu.extremexp.dms.utils.JNode;
import eu.extremexp.dsl.xDSL.AssembledWorkflow;
import eu.extremexp.dsl.xDSL.CompositeWorkflow;
import eu.extremexp.dsl.xDSL.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WorkflowsToJSONConverter {
//
//    private final CompositeWorkflow compositeWorkflow;
//    private final List<AssembledWorkflow> assembledWorkflows;
//    private final ObjectMapper objectMapper;
//    private final Map<String, String> nodeIdMap; // Maps node name to generated ID
//    private int xPosition = 100;
//    private int yPosition = 100;
//
//    public WorkflowsToJSONConverter(CompositeWorkflow compositeWorkflow, List<AssembledWorkflow> assembledWorkflows) {
//        this.compositeWorkflow = compositeWorkflow;
//        this.assembledWorkflows = assembledWorkflows;
//        this.objectMapper = new ObjectMapper();
//        this.nodeIdMap = new HashMap<>();
//    }
//
//    /**
//     * Convert the workflows to JSON format matching the structure of med.json
//     */
//    public ObjectNode convertToJSON() throws JsonProcessingException {
//        // Use JCompositeWorkflow to extract nodes and edges
//        JCompositeWorkflow jCompositeWorkflow = new JCompositeWorkflow(compositeWorkflow);
//        jCompositeWorkflow.populateNodes(assembledWorkflows);
//        jCompositeWorkflow.populateEdges();
//
//        ObjectNode result = objectMapper.createObjectNode();
//        ArrayNode nodesArray = objectMapper.createArrayNode();
//        ArrayNode edgesArray = objectMapper.createArrayNode();
//
//        // Convert JNodes to JSON nodes
//        for (JNode jNode : jCompositeWorkflow.jNodes.values()) {
//            ObjectNode nodeJson = createNodeFromJNode(jNode);
//            nodesArray.add(nodeJson);
//        }
//
//        // Convert JEdges to JSON edges
//        for (JEdge jEdge : jCompositeWorkflow.jEdges.values()) {
//            ObjectNode edgeJson = createEdgeFromJEdge(jEdge);
//            edgesArray.add(edgeJson);
//        }
//
//        result.set("nodes", nodesArray);
//        result.set("edges", edgesArray);
//
//        return result;
//    }
//
//    private ObjectNode createNodeFromJNode(JNode jNode) {
//        ObjectNode node = objectMapper.createObjectNode();
//
//        String nodeId;
//        switch (jNode.type()) {
//            case "start":
//                nodeId = "start-" + generateRandomId();
//                node.put("id", nodeId);
//                node.put("type", "start");
//                node.put("width", 31);
//                node.put("height", 36);
//                node.put("dragging", false);
//                node.put("selected", false);
//                node.set("data", objectMapper.createObjectNode());
//                setPosition(node, -173.5, -81);
//                break;
//
//            case "end":
//                nodeId = "end-" + generateRandomId();
//                node.put("id", nodeId);
//                node.put("type", "end");
//                node.put("width", 32);
//                node.put("height", 37);
//                node.put("dragging", false);
//                node.put("selected", false);
//                node.set("data", objectMapper.createObjectNode());
//                setPosition(node, 514.5, 362.75);
//                break;
//
//            case "task":
//                nodeId = "task-" + generateRandomId();
//                node.put("id", nodeId);
//                node.put("type", "task");
//                node.put("width", 102);
//                node.put("height", 44);
//                node.put("dragging", false);
//                node.put("selected", false);
//
//                // Create task data with variants
//                ObjectNode taskData = objectMapper.createObjectNode();
//                ArrayNode variants = createTaskVariants(jNode);
//                taskData.set("variants", variants);
//
//                if (variants.size() > 0) {
//                    JsonNode firstVariant = variants.get(0);
//                    if (firstVariant.has("id_task")) {
//                        taskData.put("currentVariant", firstVariant.get("id_task").asText());
//                    }
//                }
//
//                node.set("data", taskData);
//                setPosition(node, xPosition, yPosition);
//                xPosition += 150;
//                break;
//
//            case "data":
//                nodeId = "data-" + generateRandomId();
//                node.put("id", nodeId);
//                node.put("type", "data");
//                node.put("width", 102);
//                node.put("height", 77);
//                node.put("dragging", false);
//                node.put("selected", false);
//
//                // Set data fields from data field (the third parameter in JNode record)
//                ObjectNode dataContent = objectMapper.createObjectNode();
//                if (jNode.data() != null) {
//                    if (jNode.data().has("name")) {
//                        dataContent.put("name", jNode.data().get("name").asText());
//                    }
//                    if (jNode.data().has("field")) {
//                        dataContent.put("field", jNode.data().get("field").asText());
//                    }
//                }
//                node.set("data", dataContent);
//                setPosition(node, xPosition, yPosition + 100);
//                xPosition += 150;
//                break;
//
//            default:
//                nodeId = "node-" + generateRandomId();
//                node.put("id", nodeId);
//                node.put("type", jNode.type());
//        }
//
//        nodeIdMap.put(jNode.id(), nodeId);
//        return node;
//    }
//
//    private ArrayNode createTaskVariants(JNode taskNode) {
//        ArrayNode variants = objectMapper.createArrayNode();
//
//        // Find the task in the composite workflow first
//        Task compositeTask = null;
//        for (Task task : compositeWorkflow.getTasks()) {
//            if (task.getName().equals(taskNode.id())) {
//                compositeTask = task;
//                break;
//            }
//        }
//
//        if (compositeTask == null) {
//            return variants;
//        }
//
//        // Collect all variants for this task from assembled workflows
//        int variantNum = 1;
//        for (AssembledWorkflow assembledWorkflow : assembledWorkflows) {
//            for (eu.extremexp.dsl.xDSL.TaskConfiguration taskConfig : assembledWorkflow.getTaskConfigurations()) {
//                // Match by task object reference, not name
//                if (taskConfig.getTask() == compositeTask) {
//                    ObjectNode variant = objectMapper.createObjectNode();
//                    variant.put("id_task", "variant-" + variantNum + "-" + generateRandomId());
//                    variant.put("name", taskNode.id());
//                    variant.put("variant", variantNum);
//                    variantNum++;
//
//                    variant.put("description",
//                        taskConfig.getTaskConfiguration() != null &&
//                        taskConfig.getTaskConfiguration().getDescription() != null
//                            ? taskConfig.getTaskConfiguration().getDescription()
//                            : "no description");
//
//                    // Get implementation reference
//                    String implRef = "";
//                    if (taskConfig.getTaskConfiguration() != null &&
//                        taskConfig.getTaskConfiguration().getPrimitiveImplementation() != null) {
//                        implRef = taskConfig.getTaskConfiguration().getPrimitiveImplementation();
//                    }
//                    variant.put("implementationRef", implRef);
//                    variant.put("isAbstract", true);
//
//                    // Check if this is a composite task (has nested workflow)
//                    boolean isComposite = taskConfig.getTaskConfiguration() != null &&
//                                         taskConfig.getTaskConfiguration().getCompositeImplementation() != null;
//                    variant.put("is_composite", isComposite);
//
//                    // Add parameters if present
//                    ArrayNode parameters = objectMapper.createArrayNode();
//                    if (taskConfig.getTaskConfiguration() != null &&
//                        taskConfig.getTaskConfiguration().getParams() != null) {
//                        for (eu.extremexp.dsl.xDSL.Param param : taskConfig.getTaskConfiguration().getParams()) {
//                            ObjectNode paramNode = objectMapper.createObjectNode();
//                            paramNode.put("id", "parameter-" + generateRandomId());
//                            paramNode.put("name", param.getName());
//                            paramNode.put("abstract", false);
//
//                            // Determine type and value
//                            if (param.isAssigned() && param.getValue() != null) {
//                                String primitiveValue = param.getValue().getPrimitiveValue();
//                                // Remove quotes if string
//                                if (primitiveValue.startsWith("\"") && primitiveValue.endsWith("\"")) {
//                                    primitiveValue = primitiveValue.substring(1, primitiveValue.length() - 1);
//                                }
//
//                                try {
//                                    int intVal = Integer.parseInt(primitiveValue);
//                                    paramNode.put("type", "integer");
//                                    ArrayNode values = objectMapper.createArrayNode();
//                                    values.add(intVal);
//                                    paramNode.set("values", values);
//                                } catch (NumberFormatException e1) {
//                                    try {
//                                        double doubleVal = Double.parseDouble(primitiveValue);
//                                        paramNode.put("type", "real");
//                                        ArrayNode values = objectMapper.createArrayNode();
//                                        values.add(doubleVal);
//                                        paramNode.set("values", values);
//                                    } catch (NumberFormatException e2) {
//                                        paramNode.put("type", "string");
//                                        ArrayNode values = objectMapper.createArrayNode();
//                                        values.add(primitiveValue);
//                                        paramNode.set("values", values);
//                                    }
//                                }
//                            } else {
//                                // Parameter declared but not assigned
//                                paramNode.put("type", "integer");
//                                paramNode.set("values", objectMapper.createArrayNode());
//                            }
//
//                            parameters.add(paramNode);
//                        }
//                    }
//                    variant.set("parameters", parameters);
//
//                    // Add graphical_model (for composite tasks, recursively build; otherwise empty)
//                    if (isComposite && taskConfig.getTaskConfiguration().getCompositeImplementation() != null) {
//                        // Recursively build graphical model for composite task
//                        CompositeWorkflow nestedWorkflow = taskConfig.getTaskConfiguration().getCompositeImplementation();
//                        ObjectNode graphicalModel = buildNestedGraphicalModel(nestedWorkflow);
//                        variant.set("graphical_model", graphicalModel);
//                    } else {
//                        ObjectNode graphicalModel = objectMapper.createObjectNode();
//                        graphicalModel.set("nodes", objectMapper.createArrayNode());
//                        graphicalModel.set("edges", objectMapper.createArrayNode());
//                        variant.set("graphical_model", graphicalModel);
//                    }
//
//                    variants.add(variant);
//                }
//            }
//        }
//
//        return variants;
//    }
//
//    private ObjectNode createEdgeFromJEdge(JEdge jEdge) {
//        ObjectNode edge = objectMapper.createObjectNode();
//
//        edge.put("id", generateRandomId());
//        edge.put("type", jEdge.type());
//
//        String sourceId = nodeIdMap.get(jEdge.sourceId());
//        String targetId = nodeIdMap.get(jEdge.targetId());
//
//        edge.put("source", sourceId);
//        edge.put("target", targetId);
//
//        if (jEdge.type().equals("regular")) {
//            edge.put("sourceHandle", "s-bottom");
//            edge.put("targetHandle", "t-top");
//        } else if (jEdge.type().equals("dataflow")) {
//            edge.put("animated", true);
//            edge.put("sourceHandle", "s-bottom");
//            edge.put("targetHandle", "t-top");
//        }
//
//        // Add style
//        ObjectNode style = objectMapper.createObjectNode();
//        style.put("stroke", "#000");
//        style.put("strokeWidth", 1.5);
//        edge.set("style", style);
//
//        // Add markerEnd
//        ObjectNode markerEnd = objectMapper.createObjectNode();
//        markerEnd.put("type", "arrow");
//        markerEnd.put("color", "#000");
//        markerEnd.put("width", 20);
//        markerEnd.put("height", 20);
//        edge.set("markerEnd", markerEnd);
//
//        edge.set("data", objectMapper.createObjectNode());
//
//        return edge;
//    }
//
//    private void setPosition(ObjectNode node, double x, double y) {
//        ObjectNode position = objectMapper.createObjectNode();
//        position.put("x", x);
//        position.put("y", y);
//        node.set("position", position);
//        node.set("positionAbsolute", position);
//    }
//
//    private String generateRandomId() {
//        return UUID.randomUUID().toString().replace("-", "").substring(0, 21);
//    }
}
