package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.*;

public class GChainLink extends GSingleObject{
    RegularLink eObject ;
    boolean ended, started;

    public GChainLink(XDSLFactory factory){
        this.eObject = factory.createRegularLink();
        started = ended = false;
    }

    public void start(GEvent startNode){
        this.eObject.setInput(startNode.getEObject());
        started = true;
    }

    public void addTasks(XDSLFactory factory, GTask task){
        if (started && !ended){
            Node node = factory.createNode();
            node.setRef(task.getEObject());
            this.eObject.getOutput().add(node);
        }
        else {
            System.out.println("ERROR , first start, and before end add tasks");
        }
    }

    public void end(GEvent endNode){
        this.eObject.getOutput().add(endNode.getEObject());
        ended = true;
    }


    public RegularLink getEObject() {
        return this.eObject;
    }
}
