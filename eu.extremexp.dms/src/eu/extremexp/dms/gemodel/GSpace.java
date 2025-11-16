package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.Space;
import eu.extremexp.dsl.xDSL.XDSLFactory;

public class GSpace extends GSingleObject{
    private final Space eObject;
    private final int executionOrder;

    public GSpace (String name, int executionOrder, GAssembledWorkflow gAssembledWorkflow,  XDSLFactory factory){
        this.eObject = factory.createSpace();
        this.eObject.setName(this.ID(name));
        this.eObject.setAssembledWorkflow(gAssembledWorkflow.getEObject());
        this.executionOrder = executionOrder;

    }

    public int getExecutionOrder() {
        return executionOrder;
    }

    public Space getEObject() {
        return eObject;
    }
}
