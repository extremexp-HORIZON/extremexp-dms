package eu.extremexp.dms.utils;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.extremexp.dsl.xDSL.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCompositeWorkflow {
    public Map<String, JNode> jNodes;
    public Map<String, JEdge> jEdges;
    CompositeWorkflow compositeWorkflow;

    public JCompositeWorkflow(CompositeWorkflow compositeWorkflow){
        jNodes = new HashMap<>();
        jEdges = new HashMap<>();
        this.compositeWorkflow = compositeWorkflow;
    }

    private ArrayNode createParams(List<Param> params){
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        ArrayNode arrayNode = objectMapper.createArrayNode();

        for (Param param : params){
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("abstract", false);
            objectNode.put("id", "id_"+param.getName());
            objectNode.put("name", param.getName());

            if (param.isAssigned()){
                ArrayNode valueNode = objectMapper.createArrayNode();
                String value = param.getValue().getPrimitiveValue();
                try {
                    float v = Float.parseFloat(value);
                    valueNode.add(v);
                    objectNode.put("type","real");
                }
                catch (NumberFormatException ex){
                    valueNode.add(value);
                    objectNode.put("type","string");
                }
            }

            arrayNode.add(objectNode);
        }
        return arrayNode;
    }

    private JsonNode createVariant(TaskConfiguration taskConfiguration, int variantCounter){
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("description", taskConfiguration.getTaskConfiguration().getDescription());
        jsonNode.put("implementationRef", taskConfiguration.getTask().getName());
        jsonNode.put("isAbstract", true);
        jsonNode.put("is_composite", false);
        jsonNode.put("name", taskConfiguration.getTask().getName());

        ArrayNode params = createParams(taskConfiguration.getTaskConfiguration().getParams());


        jsonNode.put("variant", variantCounter);
        return jsonNode;

    }

    public void populateNodes(List<AssembledWorkflow> assembledWorkflowList){
        assert compositeWorkflow != null;
        for (Task task : compositeWorkflow.getTasks()){
                JNode jNode = new JNode(task.getName(), "task", null );
                jNodes.putIfAbsent(task.getName(), jNode);
                int variantCounter = 0;

                for (AssembledWorkflow assembledWorkflow : assembledWorkflowList){
                    for (TaskConfiguration taskConfiguration : assembledWorkflow.getTaskConfigurations()){
                        if (taskConfiguration.getTask() == task){
                            JsonNode jsonNode = createVariant(taskConfiguration, variantCounter++);
                        }
                    }
                }

        }
        for (InputData data : compositeWorkflow.getInputs()){
            JNode jNode = getJNode(data);
            jNodes.putIfAbsent(data.getName(), jNode);
        }
        for (OutputData data : compositeWorkflow.getOutputs()) {
            JNode jNode = getJNode(data);
            jNodes.putIfAbsent(data.getName(), jNode);
        }
        jNodes.putIfAbsent("START", new JNode("START", "start", null));
        jNodes.putIfAbsent("END", new JNode("END", "end", null));

    }

    private JNode getJNode(Data data) {
        ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());

        // Create a base JSON node for this data
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("name", data.getName());

        // Find matching DataConfiguration (if any)
        for (DataConfiguration dc : compositeWorkflow.getDataConfigurations()) {
            if (dc.getData() == data) {
                if (dc.getPath() != null) {
                    jsonNode.put("field", dc.getPath());
                }
                break; // found the match, stop looping
            }
        }

        // Wrap it in your custom JNode class
        return new JNode(data.getName(), "data", jsonNode);
    }

    public void populateEdges(){
        assert this.compositeWorkflow != null;
        int counter = 0;
        for (Link link : this.compositeWorkflow.getLinks()){
            if (link instanceof RegularLink){
                counter = createRegularEdge((RegularLink) link, counter);

            }
        }

        for (DataLink dataLink : this.compositeWorkflow.getDataLinks()){
            if (dataLink instanceof InputDataLink){
                counter = createInputDataLink((InputDataLink) dataLink, counter);
            }
            if (dataLink instanceof OutputDataLink){
                counter = createOutputDataLink((OutputDataLink) dataLink, counter);
            }
            if (dataLink instanceof TaskDataLink){
                counter = createTaskDataLink((TaskDataLink) dataLink, counter);
            }
        }

    }

    private int createRegularEdge(RegularLink link, int counter){
        Node input = link.getInput();
        for (Node output : link.getOutput()){
            String key = "regular" + counter++;
            String inputId , outputId ;
            if (input.getRef() != null){
                inputId = ((Task) input.getRef()).getName();
            }
            else {
                if (input instanceof Event){
                    inputId = ((Event) input).getEventValue().getLiteral();
                }
                else{
                    continue;
                }
            }
            if (output.getRef() != null){
                outputId = ((Task) output.getRef()).getName();
            }
            else {
                if (output instanceof Event){
                    outputId = ((Event) output).getEventValue().getLiteral();
                }
                else{
                    continue;
                }
            }

            jEdges.put(key, new JEdge(key, "regular", inputId, outputId));
            input = output;

        }
        return counter;
    }

    private int createInputDataLink(InputDataLink link, int counter) {
        InputData inputData = link.getInputData();

        String outputId ;
        Node outputNode = link.getOutputNode();
        if (outputNode instanceof Task) {
            outputId = ((Task) outputNode).getName();
        } else {
            if (outputNode instanceof Event) {
                outputId = ((Event) outputNode).getEventValue().getLiteral();
            } else {
                return counter;
            }
        }
        String key = "dataflow" + counter++;
        jEdges.put(key, new JEdge(key, "dataflow", inputData.getName(), outputId));


        return counter;
    }

    private int createOutputDataLink(OutputDataLink link, int counter) {
        OutputData outputData = link.getOutputData();

        String inputId ;
        Node inputNode = link.getInputNode();
        if (inputNode instanceof Task) {
            inputId = ((Task) inputNode).getName();
        } else {
            if (inputNode instanceof Event) {
                inputId = ((Event) inputNode).getEventValue().getLiteral();
            } else {
                return counter;
            }
        }
        String key = "dataflow" + counter++;
        jEdges.put(key, new JEdge(key, "dataflow", inputId, outputData.getName()));


        return counter;
    }

    private int createTaskDataLink(TaskDataLink link, int counter) {

        String inputId , outputId ;
        Node inputNode = link.getInputNode();
        Node outputNode = link.getOutputNode();

        if (inputNode instanceof Task) {
            inputId = ((Task) inputNode).getName();
        } else {
            if (inputNode instanceof Event) {
                inputId = ((Event) inputNode).getEventValue().getLiteral();
            } else {
                return counter;
            }
        }

        if (outputNode instanceof Task) {
            outputId = ((Task) outputNode).getName();
        } else {
            if (outputNode instanceof Event) {
                outputId = ((Event) outputNode).getEventValue().getLiteral();
            } else {
                return counter;
            }
        }

        String key = "dataflow" + counter++;
        jEdges.put(key, new JEdge(key, "dataflow", inputId, outputId));

        return counter;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
