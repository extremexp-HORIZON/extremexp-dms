package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.Task;
import eu.extremexp.dsl.xDSL.XDSLFactory;

import java.util.HashSet;


public class GTask extends GSingleObject{
    Task eObject;


    public GTask( GCompositeWorkflow gCompositeWorkflow, String name, XDSLFactory factory){
        eObject = factory.createTask();
        eObject.setName(this.ID(gCompositeWorkflow, name));
        eObject.setAbstract(true);
    }


    public Task getEObject(){
        return this.eObject;
    }
}
