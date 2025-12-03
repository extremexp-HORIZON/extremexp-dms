package eu.extremexp.dms.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.extremexp.dsl.xDSL.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RootToJSONConvertor {
//    private final ObjectMapper objectMapper;

    public RootToJSONConvertor(Root root){
//        objectMapper = new ObjectMapper();
//
//        ObjectNode result = objectMapper.createObjectNode();
//        ArrayNode arrayNode = objectMapper.createArrayNode();
//
//        HashMap<CompositeWorkflow, List<AssembledWorkflow>> compositeWorkflowListHashMap = new HashMap<>();
//        for (Workflow workflow : root.getWorkflows()){
//            if (workflow instanceof CompositeWorkflow){
//                if (!(compositeWorkflowListHashMap.containsKey(workflow))){
//                    compositeWorkflowListHashMap.put( (CompositeWorkflow) workflow, new ArrayList<>());
//                }
//            }
//        }
//
//        for (Workflow workflow : root.getWorkflows()){
//            if (workflow instanceof AssembledWorkflow){
//                AssembledWorkflow assembledWorkflow = (AssembledWorkflow) workflow;
//                compositeWorkflowListHashMap.get(assembledWorkflow.getParent()).add(assembledWorkflow);
//            }
//        }
//
//        compositeWorkflowListHashMap.forEach(
//                (CompositeWorkflow composite, List<AssembledWorkflow> list) -> {
//                    WorkflowsToJSONConverter workflowsToJSONConverter = new WorkflowsToJSONConverter(composite, list);
//                    try {
//                        arrayNode.add(workflowsToJSONConverter.convertToJSON());
//                    } catch (JsonProcessingException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//
//        result.set("workflows", arrayNode);
//        System.out.println(result.toPrettyString());

    }
}
