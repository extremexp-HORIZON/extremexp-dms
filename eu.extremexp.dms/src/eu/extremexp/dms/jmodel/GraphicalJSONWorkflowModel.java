package eu.extremexp.dms.jmodel;

import com.fasterxml.jackson.databind.JsonNode;
import eu.extremexp.dms.gemodel.*;
import eu.extremexp.dms.utils.JEdge;
import eu.extremexp.dms.utils.JNode;
import eu.extremexp.dsl.xDSL.*;

import java.util.*;

public class GraphicalJSONWorkflowModel  implements Iterable<Workflow>{
    Map<String, GObject> gIDs;
    Map<GAssembledWorkflow, Set<String>> variants;
    List<Workflow> eObjects;

    public GraphicalJSONWorkflowModel(List<JNode> nodes, List<JEdge> edges, String workflowName){
        this.gIDs = new HashMap<>();
        this.variants = new HashMap<>();
        GCompositeWorkflow gCompositeWorkflow = new GCompositeWorkflow(workflowName, XDSLFactory.eINSTANCE);

        JNode startNode = null;
        JNode endNode = null;
        for (var node : nodes){
            switch (node.type()) {
                case "task":
                    this.createTask(gCompositeWorkflow, XDSLFactory.eINSTANCE, node);
                    break;
                case "data":
                    this.createData(gCompositeWorkflow, XDSLFactory.eINSTANCE, node, edges);
                    break;
                case "start":
                    this.createStart(XDSLFactory.eINSTANCE, node);
                    startNode = node;
                    break;
                case "end":
                    this.createEnd(XDSLFactory.eINSTANCE, node);
                    endNode = node;
                    break;
                default:
            }
        }


        addRegularLinks(gCompositeWorkflow, startNode, endNode, XDSLFactory.eINSTANCE, edges);
        edges.forEach(edge -> {
            if (edge.type().equals("dataflow")){
                this.createDataLink(gCompositeWorkflow, edge, XDSLFactory.eINSTANCE);
            }
        });

        List<Map<String, JsonNode>>  combinations = populateAssembledWorkflows(nodes);
        this.eObjects = this.createAssembledWorks(gCompositeWorkflow, combinations, XDSLFactory.eINSTANCE);

        this.eObjects.addFirst(gCompositeWorkflow.getEObject());

    }


    private List<Workflow> createAssembledWorks(GCompositeWorkflow gCompositeWorkflow,
                               List<Map<String, JsonNode>> combinations, XDSLFactory factory) {
        ArrayList<Workflow> gAws = new ArrayList<>();
        combinations.forEach(combination -> {
            GAssembledWorkflow gAssembledWorkflow = new GAssembledWorkflow(gCompositeWorkflow, factory);
            combination.forEach((key, variant) -> {
                GTask task = (GTask) gIDs.get(key);
                gAssembledWorkflow.addTaskConfiguration(task, variant, factory);
                if (!this.variants.containsKey(gAssembledWorkflow)){
                    this.variants.put(gAssembledWorkflow, new HashSet<>());
                }
                this.variants.get(gAssembledWorkflow).add(variant.get("id_task").asText());

            });
            gAws.add(gAssembledWorkflow.getEObject());

        });
        return  gAws;
    }

    private List<Map<String, JsonNode>> populateAssembledWorkflows(List<JNode> nodes) {
        Map<String, List<JsonNode>> map = new HashMap<>();
        nodes.forEach(node -> {
            if (node.type().equals("task")) {
                map.computeIfAbsent(node.id(), k -> new ArrayList<>());
                node.data().get("variants").forEach(variant -> {
                    map.get(node.id()).add(variant);
                });
            }
        });

        // Compute all combinations
        List<Map<String, JsonNode>> combinations = new ArrayList<>();
        combinations.add(new HashMap<>()); // start with one empty combination

        for (Map.Entry<String, List<JsonNode>> entry : map.entrySet()) {
            String name = entry.getKey();
            List<JsonNode> variants = entry.getValue();

            List<Map<String, JsonNode>> newCombinations = new ArrayList<>();

            for (Map<String, JsonNode> existing : combinations) {
                for (JsonNode variant : variants) {
                    Map<String, JsonNode> copy = new HashMap<>(existing);
                    copy.put(name, variant);
                    newCombinations.add(copy);
                }
            }

            combinations = newCombinations;
        }


        return combinations;

    }


    private void createDataLink(GCompositeWorkflow  gCompositeWorkflow, JEdge edge, XDSLFactory factory) {
        if (gIDs.containsKey(edge.sourceId()) && gIDs.containsKey(edge.targetId())) {
            GObject source = gIDs.get(edge.sourceId());
            GObject target = gIDs.get(edge.targetId());
            GDataLink gDataLink;
            if (source.getClass() == GTask.class){
                if (target.getClass() == GTask.class){
                    gDataLink = new GDataLink((GTask) source, (GTask) target, factory);
                }
                else{
                    gDataLink = new GDataLink((GTask) source, (GOutputData) target, factory);
                }
            }
            else{
                if (target.getClass() == GTask.class){
                    gDataLink = new GDataLink((GInputData) source, (GTask) target, factory);
                }
                else{
                    gDataLink = null;
                }
            }

            if (gDataLink != null){
                gCompositeWorkflow.addDataLink(gDataLink);
            }

        }

    }

    private List<LinkedList<String>> createChain(List<JEdge> edges){
        
        List<JEdge> starts = new ArrayList<>();
        List<JEdge> ends = new ArrayList<>();
        List<JEdge> tasks = new ArrayList<>();

        // Separate edges into starts, ends, and task connections
        for (JEdge jEdge : edges){
           if (jEdge.sourceId().startsWith("start")){
                starts.add(jEdge);
           } else if (jEdge.targetId().startsWith("end")){
                ends.add(jEdge);
           } else {
                tasks.add(jEdge);
           }
        }

        List<LinkedList<String>> linkedLists = new ArrayList<>();
        
        // For each start-end combination, find all paths
        for (JEdge start : starts) {
            for (JEdge end : ends) {
                // Find all paths from start.targetId to end.sourceId through tasks
                List<List<String>> paths = findAllPaths(start.targetId(), end.sourceId(), tasks);
                
                // Create chains for each path
                for (List<String> path : paths) {
                    LinkedList<String> chain = new LinkedList<>();
                    chain.add(start.sourceId());  // start node
                    chain.addAll(path);           // intermediate tasks
                    chain.add(end.targetId());    // end node
                    linkedLists.add(chain);
                }
            }
        }

        return linkedLists;
    }

    /**
     * Find all paths from startTaskId to endTaskId through the given task edges.
     * Uses DFS to explore all possible paths.
     */
    private List<List<String>> findAllPaths(String startTaskId, String endTaskId, List<JEdge> taskEdges) {
        List<List<String>> allPaths = new ArrayList<>();
        List<String> currentPath = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        
        currentPath.add(startTaskId);
        visited.add(startTaskId);
        
        dfsPathFinder(startTaskId, endTaskId, taskEdges, currentPath, visited, allPaths);
        
        return allPaths;
    }

    /**
     * DFS helper to find all paths from current node to target.
     */
    private void dfsPathFinder(String current, String target, List<JEdge> edges, 
                               List<String> currentPath, Set<String> visited, 
                               List<List<String>> allPaths) {
        if (current.equals(target)) {
            // Found a complete path
            allPaths.add(new ArrayList<>(currentPath));
            return;
        }
        
        // Find all edges starting from current node
        for (JEdge edge : edges) {
            if (edge.sourceId().equals(current) && !visited.contains(edge.targetId())) {
                // Visit the next node
                visited.add(edge.targetId());
                currentPath.add(edge.targetId());
                
                dfsPathFinder(edge.targetId(), target, edges, currentPath, visited, allPaths);
                
                // Backtrack
                currentPath.remove(currentPath.size() - 1);
                visited.remove(edge.targetId());
            }
        }
    }


    private void addRegularLinks(GCompositeWorkflow  gCompositeWorkflow, JNode startNode, JNode endNode, XDSLFactory factory, List<JEdge> edges) {
        List<JEdge> regularEdges = new ArrayList<>();
        for (JEdge edge : edges){
            if (edge.type().equals("regular")){
                regularEdges.add(edge);
            }
        }

        // Create all possible chains from starts through tasks to ends
        List<LinkedList<String>> chains = createChain(regularEdges);

        // Create a ChainLink for each chain
        for (LinkedList<String> chain : chains) {
            GChainLink chainLink = new GChainLink(factory);

            for(String item : chain){
                if (item.startsWith("start")){
                    chainLink.start(new GEvent(EventValue.START, factory));
                } else if (item.startsWith("end")) {
                    chainLink.end(new GEvent(EventValue.END, factory));
                } else {
                    chainLink.addTasks(factory, (GTask) gIDs.get(item));
                }
            }
            gCompositeWorkflow.addChainLink(chainLink);
        }
    }
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

    private void createTask(GCompositeWorkflow  gCompositeWorkflow, XDSLFactory factory, JNode node){

        String currentVariant = node.data().get("currentVariant").asText();
        node.data().get("variants").forEach(variant -> {
            if (variant.get("id_task").asText().equals(currentVariant)){
                String taskName = variant.get("name").asText();
                GTask task = new GTask(gCompositeWorkflow, taskName, factory);
                gCompositeWorkflow.addTask(task);
                this.gIDs.put(node.id(), task);
                return;
            }
        });



    }

    private void createData(GCompositeWorkflow  gCompositeWorkflow, XDSLFactory factory, JNode node, List<JEdge> edges){
        edges.forEach(edge -> {
            if (edge.sourceId().equals(node.id())){
                // create inputData
                GInputData gInputData;
                if (node.data().has("name")) {
                    gInputData = new GInputData(gCompositeWorkflow, node.data().get("name").asText(), factory);
                }
                else{
                    gInputData = new GInputData(gCompositeWorkflow, "inputData", factory);
                }
                gCompositeWorkflow.addInputData(gInputData);
                this.gIDs.put(node.id(), gInputData);

                GDataConfiguration dataConf = new GDataConfiguration(node.data(), factory, gInputData.getEObject());
                gCompositeWorkflow.addDataConfiguration(dataConf);


                return;
            }
            if (edge.targetId().equals(node.id())){
                // create outputData
                GOutputData gOutputData;
                if (node.data().has("name")) {
                    gOutputData = new GOutputData(gCompositeWorkflow, node.data().get("name").asText(), factory);
                }
                else{
                    gOutputData = new GOutputData(gCompositeWorkflow,"outputData", factory);
                }
                gCompositeWorkflow.addOutputData(gOutputData);
                this.gIDs.put(node.id(), gOutputData);

                GDataConfiguration dataConf = new GDataConfiguration(node.data(), factory, gOutputData.getEObject());
                gCompositeWorkflow.addDataConfiguration(dataConf);
                return;
            }
        });
    }



    private void createStart(XDSLFactory factory, JNode node){
        GEvent start = new GEvent(EventValue.START, factory);
        gIDs.put(node.id(), start);

    }

    private void createEnd(XDSLFactory factory, JNode node){
        GEvent end = new GEvent(EventValue.END, factory);
        gIDs.put(node.id(), end);
    }

    public GObject getGObject(String key){
        return this.gIDs.get(key);
    }

    public GAssembledWorkflow getAssembledWorkflow(List<String> variantIDs){
        List<GAssembledWorkflow> foundWorkflows = new ArrayList<>();

        this.variants.forEach((GAssembledWorkflow k,  Set<String> v) -> {
            if (foundWorkflows.isEmpty()) {
                boolean all = false;
                for (String variantKey : variantIDs) {
                    if (!v.contains(variantKey)) {
                        all = false;
                        break;
                    }
                    else{
                        all = true;
                    }
                }
                if (all){
                    foundWorkflows.add(k);
                    }
            }

        });

        if (foundWorkflows.size() == 1){
            return foundWorkflows.getFirst();
        }
        else{
            return null;
        }

    }

    @Override
    public Iterator<Workflow> iterator() {
        return eObjects.iterator();
    }
}
