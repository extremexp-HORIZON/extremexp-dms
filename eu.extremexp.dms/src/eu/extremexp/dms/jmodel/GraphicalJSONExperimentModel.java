package eu.extremexp.dms.jmodel;

import eu.extremexp.dms.AbstractXDSLModelIO;
import eu.extremexp.dms.gemodel.*;
import eu.extremexp.dms.utils.JStep;
import eu.extremexp.dsl.xDSL.*;


import java.util.*;

public class GraphicalJSONExperimentModel extends AbstractXDSLModelIO {

    Map<String, GObject> gIDs;
    GExperiment gExperiment;

    public GraphicalJSONExperimentModel(
            List<JStep> steps,
            List<GraphicalJSONWorkflowModel> graphicalJSONWorkflowModels,
            String experimentName){

        this.gIDs = new HashMap<>();

        this.gExperiment = new GExperiment( experimentName, XDSLFactory.eINSTANCE);

        for (JStep jStep : steps){
            var spaces = this.createSpaces(jStep, graphicalJSONWorkflowModels, XDSLFactory.eINSTANCE);
            for (var space: spaces){
                this.gExperiment.addSpace(space);
            }
        }
        this.gExperiment.setControl(XDSLFactory.eINSTANCE);

    }


    private List<GSpace> createSpaces(JStep jStep, List<GraphicalJSONWorkflowModel> graphicalJSONWorkflowModels, XDSLFactory factory){
        List<GSpace> gSpaces = new ArrayList<>();

        for (var jsonSpace : jStep.data().get("spaces")){
            List<String> variants = new ArrayList<>();
            for (var miniStep : jsonSpace.get("steps")) {
                for (var miniTask : miniStep.get("tasks")) {
                    if (miniTask.get("selected").asBoolean()) {
                        variants.add(miniTask.get("id").asText());
                    }
                }
            }
            GAssembledWorkflow gAssembledWorkflow = null;
            for (var workflowModel : graphicalJSONWorkflowModels){
                gAssembledWorkflow = workflowModel.getAssembledWorkflow(variants);
                if (gAssembledWorkflow != null){
                    break;
                }
            }
            if (gAssembledWorkflow != null) {
                GSpace gSpace = new GSpace(
                        this.gExperiment,
                        jStep.data(),
                        jStep.data().get("executionOrder").asInt(),
                        gAssembledWorkflow,
                        factory);

                gSpace.addSearchMethod(jsonSpace.get("searchMethod").asText(), factory);

                for (var ministep : jsonSpace.get("steps")){
                    if (ministep.has("type") && ministep.get("type").asText().equals("task")){
                        for (var task : ministep.get("tasks")){
                            if (task.has("selected") && task.get("selected").asBoolean()){
                                TaskConfiguration taskConfiguration= null;
                                GSpaceTaskConfiguration newTaskConfiguration = null;

                                for (TaskConfiguration tc: gAssembledWorkflow.getEObject().getTaskConfigurations()){
                                    if (tc.getTask().getName().equals(task.get("name").asText())){
                                        taskConfiguration = tc;
                                        break;
                                    }
                                }
                                if (task.has("hyperParameters")){
                                    if (taskConfiguration != null) {
                                        newTaskConfiguration = new GSpaceTaskConfiguration(taskConfiguration, factory);
                                    }

                                    var hyperParameters = task.get("hyperParameters");
                                    for (var hyperParameter : hyperParameters) {
                                        GHyperParameter gHyperParameter = new GHyperParameter(
                                                gSpace, hyperParameter.get("name").asText()+"_value", factory);
                                        // TODO change the domain to given kind/type in new JSON
                                        gHyperParameter.setValue(
                                                hyperParameter.get("type").asText(),
                                                "enum",
                                                hyperParameter.get("default").asText(),
                                                hyperParameter.get("values"),
                                                factory);

                                        gSpace.addHyperParameter(gHyperParameter);
                                        if (newTaskConfiguration != null) {
                                            for (var param : taskConfiguration.getTaskConfiguration().getParams()){
                                                if (param.getName().equals(hyperParameter.get("name").asText().replace(" ", "_"))){
                                                    newTaskConfiguration.addParam(
                                                            hyperParameter.get("name").asText().replace(" ", "_"),
                                                            gHyperParameter,
                                                            factory);
                                                    gSpace.configureTask(newTaskConfiguration);
                                                    break;
                                                }
                                            }
                                        }


                                    }
                                }
                                // TODO create for description and implementationRef
                                // add them if they are not configured
                            }
                        }
                    }
                }

                gSpaces.add(gSpace);
            }
        }
        return gSpaces;
    }

    public Experiment getExperiment(){
        return this.gExperiment.getEObject();
    }

}
