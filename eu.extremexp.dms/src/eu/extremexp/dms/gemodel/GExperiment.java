package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GExperiment extends GSingleObject {
    private final Experiment eObject;
    private HashMap<Integer, List<Space>> spaceOrder;
//    private final GControl gControl ;
//    private final List<GInputData> gInputs;
//    private final List<GOutputData> gOutputs;
//    private final List<GDataConfiguration> gDataConfigurations;
//
//    private List<GAssembledWorkflow> gAssembledWorkflows;
//    private List<GRegularLink> gRegularLinks;
//    private List<GDataLink> gDataLinks;
//
//    private EventValue startValue;
//    private EventValue endValue;


    public GExperiment(String name, XDSLFactory factory){
        this.eObject = factory.createExperiment();
        this.eObject.setName(this.ID(name));
        spaceOrder = new HashMap<>();


//        this.eObject = eInstance.createCompositeWorkflow();
//        this.eObject.setName(this.ID(name));
//
//        this.gTasks = new ArrayList<>();
//        this.gInputs = new ArrayList<>();
//        this.gOutputs = new ArrayList<>();
//        this.gDataConfigurations = new ArrayList<>();
    }

    @Override
    public Experiment getEObject(){
        return this.eObject;
    }


    public void addSpace(GSpace space){
        this.eObject.getSpaces().add(space.getEObject());
        if (!spaceOrder.containsKey(space.getExecutionOrder())){
            spaceOrder.put(space.getExecutionOrder(), new ArrayList<>());
        }
        spaceOrder.get(space.getExecutionOrder()).add(space.getEObject());
    }

    public void createControl(XDSLFactory factory){

    }

//
//    public void addTask(GTask task) {
//        this.gTasks.add(task);
//        this.eObject.getTasks().add(task.getEObject());
//    }
//
//    public void addRegularLink(GRegularLink regularLink) {
//        this.eObject.getLinks().add(regularLink.getEObject());
//    }
//
//    public void addChainLink(GChainLink chainLink) {
//        this.eObject.getLinks().add(chainLink.getEObject());
//    }
//
//    public void addInputData(GInputData gInputData) {
//        this.gInputs.add(gInputData);
//        this.eObject.getInputs().add(gInputData.getEObject());
//    }
//
//    public void addOutputData(GOutputData gOutputData) {
//        this.gOutputs.add(gOutputData);
//        this.eObject.getOutputs().add(gOutputData.getEObject());
//    }
//
//    public void addDataConfiguration(GDataConfiguration dataConf) {
//        this.gDataConfigurations.add(dataConf);
//        this.eObject.getDataConfigurations().add(dataConf.getEObject());
//    }


//    public void addDataLink(GDataLink gDataLink) {
//        var gDataLinkObject = gDataLink.getDataLink();
//        if (gDataLinkObject != null){
//
//            this.eObject.getDataLinks().add(gDataLinkObject);
//        }
//    }
}
