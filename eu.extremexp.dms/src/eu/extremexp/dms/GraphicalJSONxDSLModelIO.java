package eu.extremexp.dms;

import eu.extremexp.dms.gemodel.*;
import eu.extremexp.dms.utils.JEdge;
import eu.extremexp.dms.utils.JNode;
import eu.extremexp.dms.utils.ParseJSON;
import eu.extremexp.dsl.xDSL.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GraphicalJSONxDSLModelIO extends AbstractXDSLModelIO{

        Map<String, GSingleObject> gIDs;
//    public Root createDummyRoot(){
//        var resource = this.resourceSet.createResource(URI.createURI("temp.xxp"));
//
//        Root r = XDSLFactory.eINSTANCE.createRoot();
//        resource.getContents().add(r);
//
//        System.out.println(r.eResource());
//        CompositeWorkflow cm = XDSLFactory.eINSTANCE.createCompositeWorkflow();
//        resource.getContents().add(cm);
//
//        cm.setName("ABC");
//
//    Task t = createTask(resource, "task_1");
//
//
//    InputData d = createInputData(resource, "d1");

        DataConfiguration dc = XDSLFactory.eINSTANCE.createDataConfiguration();
//
//        ParamValue pv = XDSLFactory.eINSTANCE.createParamValue();
//
//        ParamValueEnum pve = XDSLFactory.eINSTANCE.createParamValueEnum();
//        ParamValueList pvl = XDSLFactory.eINSTANCE.createParamValueList();
//        ParamValueRange pvr = XDSLFactory.eINSTANCE.createParamValueRange();
//
//        pvr.setStep(0);
//        pvr.setEnd(0);
//        pvr.setStart(0);
//
//        pv.setPrimitiveValue("");
//        pvl.getValues().add("");
//        pve.getValues().add("");
//
//        pv.setRangeValue(pvr);
//        pv.setListValue(pvl);
//        pv.setEnumValue(pve);
//
//
//        dc.setData(d);
//        dc.setType("some-type");
//        dc.setPath("some path maybe data[\"fields\"]");
//        dc.setDefaultValue(pv);

    // Task already configured in createTask(...)

//        cm.getInputs().add(d);
//        cm.getTasks().add(t);
//
//
//        AssembledWorkflow asm = XDSLFactory.eINSTANCE.createAssembledWorkflow();
//        resource.getContents().add(asm);
//        asm.setName("asm1");
//        asm.setParent(cm);
//
//        r.getWorkflows().add(cm);
//        r.getWorkflows().add(asm);
//
//        return r;
//    }

    public GraphicalJSONxDSLModelIO(List<JNode> nodes, List<JEdge> edges, String workflowName){
        this.gIDs = new HashMap<>();
        var resource = this.resourceSet.createResource(URI.createURI("temp.xxp"));

        GCompositeWorkflow gCompositeWorkflow = new GCompositeWorkflow(workflowName, XDSLFactory.eINSTANCE);


        // first create abstract tasks
        nodes.forEach(node -> {
            switch (node.type()) {
                case "task":
                    this.createTask(gCompositeWorkflow, XDSLFactory.eINSTANCE, node);
                    break;
                case "data":
                    this.createData(gCompositeWorkflow, XDSLFactory.eINSTANCE, node, edges);
                    break;
                default:


            }
        });


        edges.forEach(edge -> {
            this.createRegularLink(gCompositeWorkflow, XDSLFactory.eINSTANCE, edge);
        });

        //
        this.root = XDSLFactory.eINSTANCE.createRoot();
        this.root.getWorkflows().add(gCompositeWorkflow.getEObject());
        resource.getContents().add(this.root);

    }

    @Override
    public EObject createModelObject() {
        return super.createModelObject();
    }

    @Override
    public Resource getResource(EObject eObject) {
        return super.getResource(eObject);
    }

    // --- Private helpers to create common model elements ---

//    private Task createTask(String name) {
//        Task t = XDSLFactory.eINSTANCE.createTask();
//        t.setName(name);
//        t.setAbstract(false);
//        t.setConfigured(true);
//        TaskConfigurationBody tcb = XDSLFactory.eINSTANCE.createTaskConfigurationBody();
//        t.setTaskConfiguration(tcb);
//        return t;
//    }

    private void createTask(GCompositeWorkflow  gCompositeWorkflow, XDSLFactory factory, JNode node){
        GTask task = new GTask(node.id(), factory);
        gCompositeWorkflow.addTask(task);

        this.gIDs.put(node.id(), task);

    }

    private void createData(GCompositeWorkflow  gCompositeWorkflow, XDSLFactory factory, JNode node, List<JEdge> edges){
        edges.forEach(edge -> {
            if (edge.sourceId().equals(node.id())){
                // create inputData
                GInputData gInputData = new GInputData(node.id(), factory);
                gCompositeWorkflow.addInputData(gInputData);
                this.gIDs.put(node.id(), gInputData);
                return;
            }
            if (edge.targetId().equals(node.id())){
                // create outputData
                GOutputData gOutputData = new GOutputData(node.id(), factory);
                gCompositeWorkflow.addOutputData(gOutputData);
                this.gIDs.put(node.id(), gOutputData);
                return;
            }
        });
    }

    private void createRegularLink(GCompositeWorkflow gCompositeWorkflow, XDSLFactory factory, JEdge edge){
        if (gIDs.containsKey(edge.sourceId()) && gIDs.containsKey(edge.targetId()) && edge.type().equals("regular")) {
            GRegularLink regularLink = new GRegularLink(
                    (GTask) gIDs.get(edge.sourceId()),
                    (GTask) gIDs.get(edge.targetId()),
                    factory
            );

            gCompositeWorkflow.addLink(regularLink);
        }
    }


}
