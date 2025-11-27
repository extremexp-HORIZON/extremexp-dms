package eu.extremexp.dms.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.extremexp.dsl.xDSL.*;

import java.util.*;

/**
 * Converts DSL Experiment objects to JSON format
 */
public class ExperimentToJSONConverter {
//
//    private final ObjectMapper objectMapper;
//
//    public ExperimentToJSONConverter() {
//        this.objectMapper = new ObjectMapper();
//    }
//
//    /**
//     * Convert an Experiment to JSON format
//     *
//     * @param experiment The experiment to convert
//     * @param root The root object containing workflows
//     * @return JSON string representation
//     */
//    public String convertToJSON(Experiment experiment, Root root) {
//        ObjectNode result = objectMapper.createObjectNode();
//        ObjectNode experimentNode = objectMapper.createObjectNode();
//
//        experimentNode.put("name", experiment.getName());
//
//        // Process experiment steps
//        ArrayNode stepsArray = objectMapper.createArrayNode();
//
//        if (experiment.getControl() != null) {
//            // Extract spaces from experiment
//            int executionOrder = 1;
//            for (Space space : experiment.getSpaces()) {
//                ObjectNode stepNode = createStepFromSpace(space, executionOrder);
//                stepsArray.add(stepNode);
//                executionOrder++;
//            }
//        }
//
//        experimentNode.set("steps", stepsArray);
//        result.set("experiment", experimentNode);
//
//        // Add workflows referenced by the experiment
//        ArrayNode workflowsArray = objectMapper.createArrayNode();
//        Set<String> addedWorkflows = new HashSet<>();
//
//        for (Space space : experiment.getSpaces()) {
//            if (space.getAssembledWorkflow() != null) {
//                AssembledWorkflow assembledWorkflow = space.getAssembledWorkflow();
//                if (assembledWorkflow.getParent() != null) {
//                    CompositeWorkflow parent = assembledWorkflow.getParent();
//                    String workflowId = "workflow-" + parent.getName();
//
//                    if (!addedWorkflows.contains(workflowId)) {
//                        ObjectNode workflowNode = createWorkflowReference(parent, workflowId);
//                        workflowsArray.add(workflowNode);
//                        addedWorkflows.add(workflowId);
//                    }
//                }
//            }
//        }
//
//        result.set("workflows", workflowsArray);
//
//        return result.toString();
//    }
//
//    private ObjectNode createStepFromSpace(Space space, int executionOrder) {
//        ObjectNode step = objectMapper.createObjectNode();
//
//        step.put("id", "step-" + System.currentTimeMillis());
//        step.put("name", "Step " + executionOrder);
//        step.put("executionOrder", executionOrder);
//        step.put("collapsed", false);
//        step.put("status", "idle");
//        step.put("type", "container");
//
//        ArrayNode spacesArray = objectMapper.createArrayNode();
//        ObjectNode spaceNode = createSpaceNode(space);
//        spacesArray.add(spaceNode);
//
//        step.set("spaces", spacesArray);
//
//        return step;
//    }
//
//    private ObjectNode createSpaceNode(Space space) {
//        ObjectNode spaceNode = objectMapper.createObjectNode();
//
//        spaceNode.put("id", "space-" + System.currentTimeMillis());
//        spaceNode.put("name", space.getName() != null ? space.getName() : "New Space");
//        spaceNode.put("status", "idle");
//        spaceNode.put("gridSearchEnabled", true);
//
//        // Determine search method
//        String searchMethod = "grid";
//        if (space.getSearchMethod() != null) {
//            if (space.getSearchMethod().getRandomsearch() != null) {
//                searchMethod = "random";
//            } else if (space.getSearchMethod().getBayesiansearch() != null) {
//                searchMethod = "bayesian";
//            }
//        }
//        spaceNode.put("searchMethod", searchMethod);
//
//        // Add workflow reference
//        if (space.getAssembledWorkflow() != null &&
//            space.getAssembledWorkflow().getParent() != null) {
//            spaceNode.put("workflow_id", space.getAssembledWorkflow().getParent().getName());
//        }
//
//        // Process steps (tasks and data)
//        ArrayNode stepsArray = objectMapper.createArrayNode();
//
//        if (space.getAssembledWorkflow() != null &&
//            space.getAssembledWorkflow().getParent() != null) {
//            CompositeWorkflow workflow = space.getAssembledWorkflow().getParent();
//
//            // Add tasks
//            for (Task task : workflow.getTasks()) {
//                ObjectNode taskStep = createTaskStep(task, space);
//                stepsArray.add(taskStep);
//            }
//
//            // Add output data nodes
//            for (OutputData outputData : workflow.getOutputs()) {
//                ObjectNode dataStep = createDataStep(outputData);
//                stepsArray.add(dataStep);
//            }
//        }
//
//        spaceNode.set("steps", stepsArray);
//
//        return spaceNode;
//    }
//
//    private ObjectNode createTaskStep(Task task, Space space) {
//        ObjectNode taskStep = objectMapper.createObjectNode();
//
//        taskStep.put("id", "task-" + generateRandomId());
//        taskStep.put("name", task.getName());
//        taskStep.put("type", "task");
//
//        ArrayNode tasksArray = objectMapper.createArrayNode();
//
//        // Find task configurations in space
//        List<ObjectNode> taskVariants = createTaskVariants(task, space);
//        for (ObjectNode variant : taskVariants) {
//            tasksArray.add(variant);
//        }
//
//        // If no variants found, create a default one
//        if (tasksArray.isEmpty()) {
//            ObjectNode defaultVariant = createDefaultTaskVariant(task);
//            tasksArray.add(defaultVariant);
//        }
//
//        taskStep.set("tasks", tasksArray);
//
//        return taskStep;
//    }
//
//    private List<ObjectNode> createTaskVariants(Task task, Space space) {
//        List<ObjectNode> variants = new ArrayList<>();
//
//        // Check space task configurations
//        for (SpaceTaskConfiguration spaceTaskConfig : space.getTaskConfigurations()) {
//            if (spaceTaskConfig.getTaskConfiguration() != null &&
//                spaceTaskConfig.getTaskConfiguration().getTask() != null &&
//                spaceTaskConfig.getTaskConfiguration().getTask().getName().equals(task.getName())) {
//
//                ObjectNode variant = createTaskVariantFromConfig(
//                    spaceTaskConfig.getTaskConfiguration(),
//                    space,
//                    true
//                );
//                variants.add(variant);
//            }
//        }
//
//        // Also check assembled workflow configurations
//        if (space.getAssembledWorkflow() != null) {
//            for (TaskConfiguration taskConfig : space.getAssembledWorkflow().getTaskConfigurations()) {
//                if (taskConfig.getTask() != null &&
//                    taskConfig.getTask().getName().equals(task.getName())) {
//
//                    boolean alreadyAdded = variants.stream()
//                        .anyMatch(v -> v.has("id") &&
//                                 v.get("implementationRef").asText().equals(
//                                     taskConfig.getImplementationRef() != null ?
//                                     taskConfig.getImplementationRef() : ""));
//
//                    if (!alreadyAdded) {
//                        ObjectNode variant = createTaskVariantFromConfig(
//                            taskConfig,
//                            space,
//                            false
//                        );
//                        variants.add(variant);
//                    }
//                }
//            }
//        }
//
//        return variants;
//    }
//
//    private ObjectNode createTaskVariantFromConfig(TaskConfiguration taskConfig, Space space, boolean selected) {
//        ObjectNode variant = objectMapper.createObjectNode();
//
//        variant.put("id", "variant-1-" + generateRandomId());
//        variant.put("name", taskConfig.getTask().getName());
//        variant.put("type", "algorithm");
//        variant.put("selected", selected);
//        variant.put("description", "");
//        variant.put("implementationRef",
//                   taskConfig.getImplementationRef() != null ?
//                   taskConfig.getImplementationRef() : "");
//
//        // Extract hyperparameters from space
//        ArrayNode hyperParameters = objectMapper.createArrayNode();
//
//        for (HyperParameter hyperParam : space.getHyperparameters()) {
//            // Check if this hyperparam is used by this task
//            for (SpaceTaskConfiguration spaceTaskConfig : space.getTaskConfigurations()) {
//                if (spaceTaskConfig.getTaskConfiguration().equals(taskConfig)) {
//                    for (Param param : spaceTaskConfig.getParams()) {
//                        if (param.getValue() != null && param.getValue().getHyperparameter() != null &&
//                            param.getValue().getHyperparameter().equals(hyperParam)) {
//
//                            ObjectNode hyperParamNode = createHyperParameterNode(hyperParam, param);
//                            hyperParameters.add(hyperParamNode);
//                        }
//                    }
//                }
//            }
//        }
//
//        variant.set("hyperParameters", hyperParameters);
//
//        return variant;
//    }
//
//    private ObjectNode createHyperParameterNode(HyperParameter hyperParam, Param param) {
//        ObjectNode hyperParamNode = objectMapper.createObjectNode();
//
//        hyperParamNode.put("name", param.getName());
//        hyperParamNode.put("type", determineHyperParamType(hyperParam));
//
//        // Extract values from domain
//        ArrayNode valuesArray = objectMapper.createArrayNode();
//        Object defaultValue = 0;
//
//        if (hyperParam.getDomain() != null) {
//            Domain domain = hyperParam.getDomain();
//
//            if (domain.getEnumDomain() != null) {
//                for (Value value : domain.getEnumDomain().getValues()) {
//                    valuesArray.add(valueToString(value));
//                }
//                if (!domain.getEnumDomain().getValues().isEmpty()) {
//                    defaultValue = valueToString(domain.getEnumDomain().getValues().get(0));
//                }
//            } else if (domain.getRangeDomain() != null) {
//                RangeDomain rangeDomain = domain.getRangeDomain();
//                valuesArray.add(String.valueOf(rangeDomain.getMin()));
//                valuesArray.add(String.valueOf(rangeDomain.getMax()));
//                defaultValue = rangeDomain.getMin();
//            }
//        }
//
//        hyperParamNode.set("values", valuesArray);
//        hyperParamNode.put("default", defaultValue.toString());
//
//        return hyperParamNode;
//    }
//
//    private String determineHyperParamType(HyperParameter hyperParam) {
//        if (hyperParam.getDomain() != null && hyperParam.getDomain().getType() != null) {
//            String type = hyperParam.getDomain().getType();
//            if (type.contains("integer")) return "integer";
//            if (type.contains("real")) return "real";
//            if (type.contains("string")) return "string";
//            if (type.contains("boolean")) return "boolean";
//        }
//        return "integer";
//    }
//
//    private String valueToString(Value value) {
//        if (value.getInt() != 0) return String.valueOf(value.getInt());
//        if (value.getReal() != 0.0) return String.valueOf(value.getReal());
//        if (value.getString() != null) return value.getString();
//        if (value.isBool()) return String.valueOf(value.isBool());
//        return "";
//    }
//
//    private ObjectNode createDefaultTaskVariant(Task task) {
//        ObjectNode variant = objectMapper.createObjectNode();
//
//        variant.put("id", "variant-1-" + generateRandomId());
//        variant.put("name", task.getName());
//        variant.put("type", "algorithm");
//        variant.put("selected", true);
//        variant.put("description", "");
//        variant.put("implementationRef", "");
//        variant.set("hyperParameters", objectMapper.createArrayNode());
//
//        return variant;
//    }
//
//    private ObjectNode createDataStep(OutputData outputData) {
//        ObjectNode dataStep = objectMapper.createObjectNode();
//
//        dataStep.put("id", "data-" + generateRandomId());
//        dataStep.put("name", outputData.getName());
//        dataStep.put("type", "data");
//        dataStep.set("tasks", objectMapper.createArrayNode());
//
//        return dataStep;
//    }
//
//    private ObjectNode createWorkflowReference(CompositeWorkflow workflow, String workflowId) {
//        ObjectNode workflowRef = objectMapper.createObjectNode();
//
//        workflowRef.put("_id", generateRandomId());
//        workflowRef.put("id_workflow", workflowId);
//        workflowRef.put("name", workflow.getName());
//        workflowRef.put("create_at", System.currentTimeMillis() / 1000);
//        workflowRef.put("update_at", System.currentTimeMillis() / 1000);
//
//        // Create graphical model for workflow
//        WorkflowToJSONConverter workflowConverter = new WorkflowToJSONConverter();
//        try {
//            String workflowJson = workflowConverter.convertToJSON(workflow);
//            ObjectNode graphicalModel = (ObjectNode) objectMapper.readTree(workflowJson);
//            workflowRef.set("graphical_model", graphicalModel);
//        } catch (Exception e) {
//            // Create empty graphical model
//            ObjectNode graphicalModel = objectMapper.createObjectNode();
//            graphicalModel.set("nodes", objectMapper.createArrayNode());
//            graphicalModel.set("edges", objectMapper.createArrayNode());
//            workflowRef.set("graphical_model", graphicalModel);
//        }
//
//        return workflowRef;
//    }
//
//    private String generateRandomId() {
//        return UUID.randomUUID().toString().substring(0, 21);
//    }
}
