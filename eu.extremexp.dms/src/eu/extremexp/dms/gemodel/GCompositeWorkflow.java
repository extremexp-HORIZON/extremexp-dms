package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.*;


import java.util.ArrayList;
import java.util.List;

public class GCompositeWorkflow extends GSingleObject{
    private final CompositeWorkflow eObject;
    private final List<GTask> gTasks ;
    private final List<GInputData> gInputs;
    private final List<GOutputData> gOutputs;
    private final List<GDataConfiguration> gDataConfigurations;

    private List<GAssembledWorkflow> gAssembledWorkflows;
    private List<GRegularLink> gRegularLinks;
    private List<GDataLink> gDataLinks;

    private EventValue startValue;
    private EventValue endValue;


    public GCompositeWorkflow(String name, XDSLFactory eInstance){
        this.eObject = eInstance.createCompositeWorkflow();
        this.eObject.setName(this.ID(this, name));

        this.gTasks = new ArrayList<>();
        this.gInputs = new ArrayList<>();
        this.gOutputs = new ArrayList<>();
        this.gDataConfigurations = new ArrayList<>();
    }

    @Override
    public Workflow getEObject(){
        return this.eObject;
    }


    public void addTask(GTask task) {
        this.gTasks.add(task);
        this.eObject.getTasks().add(task.getEObject());
    }

    public void addRegularLink(GRegularLink regularLink) {
        this.eObject.getLinks().add(regularLink.getEObject());
    }

    public void addChainLink(GChainLink chainLink) {
        this.eObject.getLinks().add(chainLink.getEObject());
    }

    public void addInputData(GInputData gInputData) {
        this.gInputs.add(gInputData);
        this.eObject.getInputs().add(gInputData.getEObject());
    }

    public void addOutputData(GOutputData gOutputData) {
        this.gOutputs.add(gOutputData);
        this.eObject.getOutputs().add(gOutputData.getEObject());
    }

    public void addDataConfiguration(GDataConfiguration dataConf) {
        this.gDataConfigurations.add(dataConf);
        this.eObject.getDataConfigurations().add(dataConf.getEObject());
    }


    public void addDataLink(GDataLink gDataLink) {
        var gDataLinkObject = gDataLink.getDataLink();
        if (gDataLinkObject != null){

            this.eObject.getDataLinks().add(gDataLinkObject);
        }
    }
}
