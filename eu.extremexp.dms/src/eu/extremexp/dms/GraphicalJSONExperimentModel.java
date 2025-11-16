package eu.extremexp.dms;

import eu.extremexp.dms.gemodel.*;
import eu.extremexp.dms.utils.JStep;
import eu.extremexp.dsl.xDSL.*;


import java.util.*;

public class GraphicalJSONExperimentModel extends AbstractXDSLModelIO{

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
//
//        JNode startNode = null;
//        JNode endNode = null;
//        for (var node : nodes){
//            switch (node.type()) {
//                case "task":
//                    this.createTask(gCompositeWorkflow, XDSLFactory.eINSTANCE, node);
//                    break;
//                case "data":
//                    this.createData(gCompositeWorkflow, XDSLFactory.eINSTANCE, node, edges);
//                    break;
//                case "start":
//                    this.createStart(XDSLFactory.eINSTANCE, node);
//                    startNode = node;
//                    break;
//                case "end":
//                    this.createEnd(XDSLFactory.eINSTANCE, node);
//                    endNode = node;
//                    break;
//                default:
//            }
//        }
//
//
//        addRegularLinks(gCompositeWorkflow, startNode, endNode, XDSLFactory.eINSTANCE, edges);
//        edges.forEach(edge -> {
//            if (edge.type().equals("dataflow")){
//                this.createDataLink(gCompositeWorkflow, edge, XDSLFactory.eINSTANCE);
//            }
//        });
//
//        List<Map<String, JsonNode>>  combincations = populateAssembledWorkflows(nodes);
//        List<GAssembledWorkflow>  gAsWs = this.createAssembledWorks(gCompositeWorkflow, combincations, XDSLFactory.eINSTANCE);
//
//
//        this.root.getWorkflows().add(gCompositeWorkflow.getEObject());
//        gAsWs.forEach(gAssembledWorkflow -> {
//            this.root.getWorkflows().add(gAssembledWorkflow.getEObject());
//        });

    }


    private List<GSpace> createSpaces(JStep jStep, List<GraphicalJSONWorkflowModel> graphicalJSONWorkflowModels, XDSLFactory factory){
        List<GSpace> gSpaces = new ArrayList<>();
        for (var jsonSpace : jStep.data().get("spaces")){
            for (var miniStep : jsonSpace.get("steps")){
                for (var miniTask : miniStep.get("tasks")) {
                    GObject connectedWorkflow = null;
                    for (var workflowModel : graphicalJSONWorkflowModels) {
                        try {
                            connectedWorkflow = workflowModel.getGObject(miniTask.get("id").asText());
                            if (connectedWorkflow instanceof GAssembledWorkflow) {
                                GSpace gSpace = new GSpace(
                                        jsonSpace.get("name").asText(),
                                        jStep.data().get("executionOrder").asInt(),
                                        (GAssembledWorkflow) connectedWorkflow,
                                        factory);

                                gSpaces.add(gSpace);
                            }
                        } catch (NoSuchElementException ignored) {
                        }
                    }

                }
            }
        }
        return gSpaces;
    }
//
//    private List<GAssembledWorkflow> createAssembledWorks(GCompositeWorkflow gCompositeWorkflow,
//                                                          List<Map<String, JsonNode>> combinations, XDSLFactory factory) {
//        ArrayList<GAssembledWorkflow> gAws = new ArrayList<>();
//        combinations.forEach(combination -> {
//            GAssembledWorkflow gAssembledWorkflow = new GAssembledWorkflow(gCompositeWorkflow, factory);
//            combination.forEach((key, variant) -> {
//                GTask task = (GTask) gIDs.get(key);
//                gAssembledWorkflow.addTaskConfiguration(task, variant, factory);
//            });
//            gAws.add(gAssembledWorkflow);
//
//        });
//        return  gAws;
//    }
//
//    private List<Map<String, JsonNode>> populateAssembledWorkflows(List<JNode> nodes) {
//        Map<String, List<JsonNode>> map = new HashMap<>();
//        nodes.forEach(node -> {
//            if (node.type().equals("task")) {
//                map.computeIfAbsent(node.id(), k -> new ArrayList<>());
//                node.data().get("variants").forEach(variant -> {
//                    map.get(node.id()).add(variant);
//                });
//            }
//        });
//
//        // Compute all combinations
//        List<Map<String, JsonNode>> combinations = new ArrayList<>();
//        combinations.add(new HashMap<>()); // start with one empty combination
//
//        for (Map.Entry<String, List<JsonNode>> entry : map.entrySet()) {
//            String name = entry.getKey();
//            List<JsonNode> variants = entry.getValue();
//
//            List<Map<String, JsonNode>> newCombinations = new ArrayList<>();
//
//            for (Map<String, JsonNode> existing : combinations) {
//                for (JsonNode variant : variants) {
//                    Map<String, JsonNode> copy = new HashMap<>(existing);
//                    copy.put(name, variant);
//                    newCombinations.add(copy);
//                }
//            }
//
//            combinations = newCombinations;
//        }
//
//
//        return combinations;
//
//    }
//
//
//    private void createDataLink(GCompositeWorkflow  gCompositeWorkflow, JEdge edge, XDSLFactory factory) {
//        if (gIDs.containsKey(edge.sourceId()) && gIDs.containsKey(edge.targetId())) {
//            GObject source = gIDs.get(edge.sourceId());
//            GObject target = gIDs.get(edge.targetId());
//            GDataLink gDataLink;
//            if (source.getClass() == GTask.class){
//                if (target.getClass() == GTask.class){
//                    gDataLink = new GDataLink((GTask) source, (GTask) target, factory);
//                }
//                else{
//                    gDataLink = new GDataLink((GTask) source, (GOutputData) target, factory);
//                }
//            }
//            else{
//                if (target.getClass() == GTask.class){
//                    gDataLink = new GDataLink((GInputData) source, (GTask) target, factory);
//                }
//                else{
//                    gDataLink = null;
//                }
//            }
//
//            if (gDataLink != null){
//                gCompositeWorkflow.addDataLink(gDataLink);
//            }
//
//        }
//
//    }
//
//    private ArrayList<String> createChain(JNode startNode, JNode endNode, List<JEdge> edges){
//        ArrayList<String> sortedIDs = new ArrayList<>();
//        String item = startNode.id();
//
//
//        while (!item.equals(endNode.id())){
//            for (var edge : edges){
//                if (edge.type().equals("regular")) {
//                    if (item.equals(edge.sourceId())) {
//                        sortedIDs.add(item);
//                        item = edge.targetId();
//                        break;
//                    }
//                }
//            }
//
//        }
//        sortedIDs.add(endNode.id());
//        return sortedIDs;
//    }
//
//
//    private void addRegularLinks(GCompositeWorkflow  gCompositeWorkflow, JNode startNode, JNode endNode, XDSLFactory factory, List<JEdge> edges) {
//        GChainLink chainLink = new GChainLink(factory);
//        ArrayList<String> arrayList = createChain(startNode, endNode, edges);
//
//        chainLink.start((GEvent) gIDs.get(arrayList.getFirst()));
//        for(int i = 1; i< arrayList.toArray().length - 1; i++){
//            chainLink.addTasks(factory, (GTask) gIDs.get(arrayList.get(i)));
//
//        }
//        chainLink.end((GEvent) gIDs.get(arrayList.getLast()));
//        gCompositeWorkflow.addChainLink(chainLink);
//
//    }
//
//
//
//    @Override
//    public EObject createModelObject() {
//        return super.createModelObject();
//    }
//
//    @Override
//    public Resource getResource(EObject eObject) {
//        return super.getResource(eObject);
//    }
//
//    private void createTask(GCompositeWorkflow  gCompositeWorkflow, XDSLFactory factory, JNode node){
//
//        String currentVariant = node.data().get("currentVariant").asText();
//        node.data().get("variants").forEach(variant -> {
//            if (variant.get("id_task").asText().equals(currentVariant)){
//                String taskName = variant.get("name").asText();
//                GTask task = new GTask(taskName, factory);
//                gCompositeWorkflow.addTask(task);
//                this.gIDs.put(node.id(), task);
//                return;
//            }
//        });
//
//
//
//    }
//
//    private void createData(GCompositeWorkflow  gCompositeWorkflow, XDSLFactory factory, JNode node, List<JEdge> edges){
//        edges.forEach(edge -> {
//            if (edge.sourceId().equals(node.id())){
//                // create inputData
//                GInputData gInputData = new GInputData(node.data().get("name").asText(), factory);
//                gCompositeWorkflow.addInputData(gInputData);
//                this.gIDs.put(node.id(), gInputData);
//
//                GDataConfiguration dataConf = new GDataConfiguration(node.data(), factory, gInputData.getEObject());
//                gCompositeWorkflow.addDataConfiguration(dataConf);
//
//
//                return;
//            }
//            if (edge.targetId().equals(node.id())){
//                // create outputData
//                GOutputData gOutputData = new GOutputData(node.data().get("name").asText(), factory);
//                gCompositeWorkflow.addOutputData(gOutputData);
//                this.gIDs.put(node.id(), gOutputData);
//
//                GDataConfiguration dataConf = new GDataConfiguration(node.data(), factory, gOutputData.getEObject());
//                gCompositeWorkflow.addDataConfiguration(dataConf);
//                return;
//            }
//        });
//    }
//
//
//
//    private void createStart(XDSLFactory factory, JNode node){
//        GEvent start = new GEvent(EventValue.START, factory);
//        gIDs.put(node.id(), start);
//
//    }
//
//    private void createEnd(XDSLFactory factory, JNode node){
//        GEvent end = new GEvent(EventValue.END, factory);
//        gIDs.put(node.id(), end);
//    }

    public Experiment getExperiment(){
        return this.gExperiment.getEObject();
    }

}
