package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.CompositeWorkflow;
import eu.extremexp.dsl.xDSL.EventValue;
import eu.extremexp.dsl.xDSL.Workflow;
import eu.extremexp.dsl.xDSL.XDSLFactory;


import java.util.ArrayList;
import java.util.List;

public class GCompositeWorkflow extends GSingleObject{
    private final CompositeWorkflow eObject;
    private final List<GTask> gTasks ;
    private final List<GInputData> gInputs;
    private final List<GOutputData> gOutputs;

    private List<GAssembledWorkflow> gAssembledWorkflows;
    private List<GRegularLink> gRegularLinks;
    private List<GDataLink> gDataLinks;

    private EventValue startValue;
    private EventValue endValue;


    public GCompositeWorkflow(String name, XDSLFactory eInstance){
        this.eObject = eInstance.createCompositeWorkflow();
        this.eObject.setName(this.ID(name));

        this.gTasks = new ArrayList<>();
        this.gInputs = new ArrayList<>();
        this.gOutputs = new ArrayList<>();
    }

    @Override
    public Workflow getEObject(){
        return this.eObject;
    }


    public void addTask(GTask task) {
        this.gTasks.add(task);
        this.eObject.getTasks().add(task.getEObject());
    }

    public void addLink(GRegularLink regularLink) {
        this.eObject.getLinks().add(regularLink.getEObject());
    }

    public void addInputData(GInputData gInputData) {
        this.gInputs.add(gInputData);
        this.eObject.getInputs().add(gInputData.getEObject());
    }

    public void addOutputData(GOutputData gOutputData) {
        this.gOutputs.add(gOutputData);
        this.eObject.getOutputs().add(gOutputData.getEObject());
    }

}
