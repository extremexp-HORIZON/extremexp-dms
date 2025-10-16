package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.Task;
import eu.extremexp.dsl.xDSL.XDSLFactory;


public class GTask extends GSingleObject{
    Task eObject;

    public GTask(String name, XDSLFactory factory){
        eObject = factory.createTask();
        eObject.setName(this.ID(name));
        eObject.setAbstract(true);
    }

    @Override
    public Task getEObject(){
        return this.eObject;
    }
}
