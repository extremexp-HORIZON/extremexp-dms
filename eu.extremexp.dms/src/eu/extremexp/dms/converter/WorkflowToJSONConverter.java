package eu.extremexp.dms.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.extremexp.dsl.xDSL.*;

import java.util.*;

/**
 * Converts DSL Workflow objects to JSON format
 */
public class WorkflowToJSONConverter {
//
//    private final ObjectMapper objectMapper;
//    private Map<String, String> objectToNodeId;
//    private int nodeCounter;
//
//    public WorkflowToJSONConverter() {
//        this.objectMapper = new ObjectMapper();
//        this.objectToNodeId = new HashMap<>();
//        this.nodeCounter = 0;
//    }
//
//    /**
//     * Convert a Workflow to JSON format
//     *
//     * @param workflow The workflow to convert
//     * @return JSON string representation
//     */
//    public String convertToJSON(Workflow workflow) {
//        objectToNodeId.clear();
//        nodeCounter = 0;
//
//        ObjectNode result = objectMapper.createObjectNode();
//        ArrayNode nodesArray = objectMapper.createArrayNode();
//        ArrayNode edgesArray = objectMapper.createArrayNode();
//
//        if (workflow instanceof CompositeWorkflow) {
//            CompositeWorkflow compositeWorkflow = (CompositeWorkflow) workflow;
//
//            // Process tasks
//            for (Task task : compositeWorkflow.getTasks()) {
//                ObjectNode taskNode = createTaskNode(task, compositeWorkflow);
//                nodesArray.add(taskNode);
//            }
//
//            // Process input data
//            for (InputData inputData : compositeWorkflow.getInputs()) {
//                ObjectNode dataNode = createInputDataNode(inputData, compositeWorkflow);
//                nodesArray.add(dataNode);
//            }
//
//            // Process output data
//            for (OutputData outputData : compositeWorkflow.getOutputs()) {
//                ObjectNode dataNode = createOutputDataNode(outputData, compositeWorkflow);
//                nodesArray.add(dataNode);
//            }
//
//            // Process links (control flow)
//            for (Link link : compositeWorkflow.getLinks()) {
//                if (link instanceof ChainLink) {
//                    ChainLink chainLink = (ChainLink) link;
//                    processChainLink(chainLink, edgesArray, nodesArray);
//                }
//            }
//
//            // Process data links
//            for (DataLink dataLink : compositeWorkflow.getDataLinks()) {
//                ObjectNode edgeNode = createDataLinkEdge(dataLink);
//                if (edgeNode != null) {
//                    edgesArray.add(edgeNode);
//                }
//            }
//        }
//
//        result.set("nodes", nodesArray);
//        result.set("edges", edgesArray);
//
//        return result.toString();
//    }
//
//    private ObjectNode createTaskNode(Task task, CompositeWorkflow workflow) {
//        String nodeId = "task-" + generateRandomId();
//        objectToNodeId.put(task.getName(), nodeId);
//
//        ObjectNode node = objectMapper.createObjectNode();
//        node.put("id", nodeId);
//        node.put("type", "task");
//        node.put("dragging", false);
//        node.put("selected", false);
//        node.put("height", 44);
//        node.put("width", 100);
//
//        // Position (can be adjusted)
//        ObjectNode position = objectMapper.createObjectNode();
//        position.put("x", 100 + (nodeCounter * 150));
//        position.put("y", 100);
//        node.set("position", position);
//        node.set("positionAbsolute", position);
//
//        // Task data with variants
//        ObjectNode data = objectMapper.createObjectNode();
//        ArrayNode variants = objectMapper.createArrayNode();
//
//        // Create variant from task configuration
//        ObjectNode variant = createTaskVariant(task, workflow);
//        variants.add(variant);
//
//        data.set("variants", variants);
//        data.put("currentVariant", variant.get("id_task").asText());
//        node.set("data", data);
//
//        nodeCounter++;
//        return node;
//    }
//
//    private ObjectNode createTaskVariant(Task task, CompositeWorkflow workflow) {
//        ObjectNode variant = objectMapper.createObjectNode();
//        String variantId = "variant-1-" + generateRandomId();
//
//        variant.put("id_task", variantId);
//        variant.put("name", task.getName());
//        variant.put("variant", 1);
//        variant.put("isAbstract", task.isAbstract());
//        variant.put("is_composite", false);
//        variant.put("description", "");
//
//        // Find task configurations
//        ArrayNode parameters = objectMapper.createArrayNode();
//        for (TaskConfiguration taskConfig : findTaskConfigurations(task, workflow)) {
//            for (Param param : taskConfig.getTaskConfiguration().getParams()) {
//                ObjectNode paramNode = objectMapper.createObjectNode();
//                paramNode.put("id", "parameter-" + generateRandomId());
//                paramNode.put("name", param.getName());
//                paramNode.put("type", getParamType(param));
//                paramNode.put("abstract", false);
//                paramNode.set("values", objectMapper.createArrayNode());
//                parameters.add(paramNode);
//            }
//        }
//        variant.set("parameters", parameters);
//
//        // Implementation reference
//        String implRef = "";
//        for (TaskConfiguration taskConfig : findTaskConfigurations(task, workflow)) {
//            if (taskConfig.getImplementationRef() != null) {
//                implRef = taskConfig.getImplementationRef();
//                break;
//            }
//        }
//        variant.put("implementationRef", implRef);
//
//        // Empty graphical model
//        ObjectNode graphicalModel = objectMapper.createObjectNode();
//        graphicalModel.set("nodes", objectMapper.createArrayNode());
//        graphicalModel.set("edges", objectMapper.createArrayNode());
//        variant.set("graphical_model", graphicalModel);
//
//        return variant;
//    }
//
//    private List<TaskConfiguration> findTaskConfigurations(Task task, CompositeWorkflow workflow) {
//        List<TaskConfiguration> configs = new ArrayList<>();
//        // Search in assembled workflows
//        for (TaskConfiguration config : getAllTaskConfigurations(workflow)) {
//            if (config.getTask() != null && config.getTask().getName().equals(task.getName())) {
//                configs.add(config);
//            }
//        }
//        return configs;
//    }
//
//    private List<TaskConfiguration> getAllTaskConfigurations(CompositeWorkflow workflow) {
//        List<TaskConfiguration> allConfigs = new ArrayList<>();
//        // This would need to traverse assembled workflows from root
//        // For now, return empty list - would need Root reference
//        return allConfigs;
//    }
//
//    private String getParamType(Param param) {
//        if (param.getValue() != null) {
//            Value value = param.getValue();
//            if (value.getInt() != 0) return "integer";
//            if (value.getReal() != 0.0) return "real";
//            if (value.getString() != null) return "string";
//            if (value.isBool()) return "boolean";
//        }
//        return "string";
//    }
//
//    private ObjectNode createInputDataNode(InputData inputData, CompositeWorkflow workflow) {
//        String nodeId = "data-" + generateRandomId();
//        objectToNodeId.put(inputData.getName(), nodeId);
//
//        ObjectNode node = objectMapper.createObjectNode();
//        node.put("id", nodeId);
//        node.put("type", "data");
//        node.put("dragging", false);
//        node.put("selected", false);
//        node.put("height", 77);
//        node.put("width", 100);
//
//        ObjectNode position = objectMapper.createObjectNode();
//        position.put("x", 50);
//        position.put("y", 50 + (nodeCounter * 100));
//        node.set("position", position);
//        node.set("positionAbsolute", position);
//
//        ObjectNode data = objectMapper.createObjectNode();
//        data.put("name", inputData.getName());
//
//        // Find data configuration
//        for (DataConfiguration dataConfig : workflow.getDataConfigurations()) {
//            if (dataConfig.getData() != null && dataConfig.getData().getName().equals(inputData.getName())) {
//                if (dataConfig.getDataConfiguration() != null &&
//                    dataConfig.getDataConfiguration().getPath() != null) {
//                    data.put("field", dataConfig.getDataConfiguration().getPath());
//                }
//                break;
//            }
//        }
//
//        node.set("data", data);
//        nodeCounter++;
//        return node;
//    }
//
//    private ObjectNode createOutputDataNode(OutputData outputData, CompositeWorkflow workflow) {
//        String nodeId = "data-" + generateRandomId();
//        objectToNodeId.put(outputData.getName(), nodeId);
//
//        ObjectNode node = objectMapper.createObjectNode();
//        node.put("id", nodeId);
//        node.put("type", "data");
//        node.put("dragging", false);
//        node.put("selected", false);
//        node.put("height", 77);
//        node.put("width", 100);
//
//        ObjectNode position = objectMapper.createObjectNode();
//        position.put("x", 400);
//        position.put("y", 50 + (nodeCounter * 100));
//        node.set("position", position);
//        node.set("positionAbsolute", position);
//
//        ObjectNode data = objectMapper.createObjectNode();
//        data.put("name", outputData.getName());
//
//        // Find data configuration
//        for (DataConfiguration dataConfig : workflow.getDataConfigurations()) {
//            if (dataConfig.getData() != null && dataConfig.getData().getName().equals(outputData.getName())) {
//                if (dataConfig.getDataConfiguration() != null &&
//                    dataConfig.getDataConfiguration().getPath() != null) {
//                    data.put("field", dataConfig.getDataConfiguration().getPath());
//                }
//                break;
//            }
//        }
//
//        node.set("data", data);
//        nodeCounter++;
//        return node;
//    }
//
//    private void processChainLink(ChainLink chainLink, ArrayNode edgesArray, ArrayNode nodesArray) {
//        // Add start node if not exists
//        if (chainLink.getStart() != null) {
//            Event startEvent = chainLink.getStart();
//            if (startEvent.getEvent() == EventValue.START) {
//                String startId = ensureStartNode(nodesArray);
//
//                // Create edge from start to first task
//                if (!chainLink.getNodes().isEmpty()) {
//                    Task firstTask = chainLink.getNodes().get(0);
//                    String targetId = objectToNodeId.get(firstTask.getName());
//                    if (targetId != null) {
//                        edgesArray.add(createRegularEdge(startId, targetId));
//                    }
//                }
//            }
//        }
//
//        // Process chain of tasks
//        for (int i = 0; i < chainLink.getNodes().size() - 1; i++) {
//            Task sourceTask = chainLink.getNodes().get(i);
//            Task targetTask = chainLink.getNodes().get(i + 1);
//
//            String sourceId = objectToNodeId.get(sourceTask.getName());
//            String targetId = objectToNodeId.get(targetTask.getName());
//
//            if (sourceId != null && targetId != null) {
//                edgesArray.add(createRegularEdge(sourceId, targetId));
//            }
//        }
//
//        // Add end node if not exists
//        if (chainLink.getEnd() != null) {
//            Event endEvent = chainLink.getEnd();
//            if (endEvent.getEvent() == EventValue.END) {
//                String endId = ensureEndNode(nodesArray);
//
//                // Create edge from last task to end
//                if (!chainLink.getNodes().isEmpty()) {
//                    Task lastTask = chainLink.getNodes().get(chainLink.getNodes().size() - 1);
//                    String sourceId = objectToNodeId.get(lastTask.getName());
//                    if (sourceId != null) {
//                        edgesArray.add(createRegularEdge(sourceId, endId));
//                    }
//                }
//            }
//        }
//    }
//
//    private String ensureStartNode(ArrayNode nodesArray) {
//        String startId = objectToNodeId.get("START");
//        if (startId == null) {
//            startId = "start-" + generateRandomId();
//            objectToNodeId.put("START", startId);
//
//            ObjectNode startNode = objectMapper.createObjectNode();
//            startNode.put("id", startId);
//            startNode.put("type", "start");
//            startNode.put("height", 31);
//            startNode.put("width", 31);
//            startNode.put("dragging", false);
//            startNode.put("selected", false);
//
//            ObjectNode position = objectMapper.createObjectNode();
//            position.put("x", 200);
//            position.put("y", -50);
//            startNode.set("position", position);
//            startNode.set("positionAbsolute", position);
//            startNode.set("data", objectMapper.createObjectNode());
//
//            nodesArray.add(startNode);
//        }
//        return startId;
//    }
//
//    private String ensureEndNode(ArrayNode nodesArray) {
//        String endId = objectToNodeId.get("END");
//        if (endId == null) {
//            endId = "end-" + generateRandomId();
//            objectToNodeId.put("END", endId);
//
//            ObjectNode endNode = objectMapper.createObjectNode();
//            endNode.put("id", endId);
//            endNode.put("type", "end");
//            endNode.put("height", 32);
//            endNode.put("width", 32);
//            endNode.put("dragging", false);
//            endNode.put("selected", false);
//
//            ObjectNode position = objectMapper.createObjectNode();
//            position.put("x", 200);
//            position.put("y", 400);
//            endNode.set("position", position);
//            endNode.set("positionAbsolute", position);
//            endNode.set("data", objectMapper.createObjectNode());
//
//            nodesArray.add(endNode);
//        }
//        return endId;
//    }
//
//    private ObjectNode createRegularEdge(String sourceId, String targetId) {
//        ObjectNode edge = objectMapper.createObjectNode();
//        edge.put("id", generateRandomId());
//        edge.put("type", "regular");
//        edge.put("source", sourceId);
//        edge.put("target", targetId);
//        edge.put("sourceHandle", "s-bottom");
//        edge.put("targetHandle", "t-top");
//
//        ObjectNode style = objectMapper.createObjectNode();
//        style.put("stroke", "#000");
//        style.put("strokeWidth", 1.5);
//        edge.set("style", style);
//
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
//    private ObjectNode createDataLinkEdge(DataLink dataLink) {
//        String sourceId = null;
//        String targetId = null;
//
//        // Determine source
//        if (dataLink.getInputData() != null) {
//            sourceId = objectToNodeId.get(dataLink.getInputData().getName());
//        } else if (dataLink.getSourceTask() != null) {
//            sourceId = objectToNodeId.get(dataLink.getSourceTask().getName());
//        }
//
//        // Determine target
//        if (dataLink.getOutputData() != null) {
//            targetId = objectToNodeId.get(dataLink.getOutputData().getName());
//        } else if (dataLink.getTargetTask() != null) {
//            targetId = objectToNodeId.get(dataLink.getTargetTask().getName());
//        }
//
//        if (sourceId == null || targetId == null) {
//            return null;
//        }
//
//        ObjectNode edge = objectMapper.createObjectNode();
//        edge.put("id", generateRandomId());
//        edge.put("type", "dataflow");
//        edge.put("source", sourceId);
//        edge.put("target", targetId);
//        edge.put("animated", true);
//
//        ObjectNode style = objectMapper.createObjectNode();
//        style.put("stroke", "#000");
//        style.put("strokeWidth", 1.5);
//        edge.set("style", style);
//
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
//    private String generateRandomId() {
//        return UUID.randomUUID().toString().substring(0, 21);
//    }
}
