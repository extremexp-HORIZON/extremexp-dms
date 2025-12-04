package eu.extremexp.dms.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.extremexp.dsl.xDSL.*;

import java.util.*;

/**
 * Converts DSL Workflow objects to JSON format
 */
public class WorkflowToJSONConverter {


    public static String ConvertOneWorkflow(Root root) {

        List<String> workflowJSONs = new ArrayList<>();

        HashMap<CompositeWorkflow, List<AssembledWorkflow>> compositeWorkflowListHashMap = new HashMap<>();
        for (Workflow workflow : root.getWorkflows()) {
            if (workflow instanceof CompositeWorkflow) {
                if (!(compositeWorkflowListHashMap.containsKey(workflow))) {
                    compositeWorkflowListHashMap.put((CompositeWorkflow) workflow, new ArrayList<>());
                }
            }
        }

        for (Workflow workflow : root.getWorkflows()) {
            if (workflow instanceof AssembledWorkflow) {
                AssembledWorkflow assembledWorkflow = (AssembledWorkflow) workflow;
                compositeWorkflowListHashMap.get(assembledWorkflow.getParent()).add(assembledWorkflow);
            }
        }


        compositeWorkflowListHashMap.forEach(
                (CompositeWorkflow composite, List<AssembledWorkflow> list) -> {
                    WorkflowsToJSONConverter workflowsToJSONConverter = new WorkflowsToJSONConverter(composite, list);
                    try {
                        workflowJSONs.add(workflowsToJSONConverter.convertToJSON().toPrettyString());

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });

        if (workflowJSONs.isEmpty()){

            return "";
        }
        return workflowJSONs.getFirst();
    }
}
