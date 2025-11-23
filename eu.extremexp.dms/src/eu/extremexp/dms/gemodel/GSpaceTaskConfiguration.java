package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.Param;
import eu.extremexp.dsl.xDSL.TaskConfiguration;
import eu.extremexp.dsl.xDSL.XDSLFactory;

public class GSpaceTaskConfiguration extends GSingleObject{
    TaskConfiguration eObject;

    public GSpaceTaskConfiguration(TaskConfiguration relateTaskConfiguration,  XDSLFactory factory){
        this.eObject = factory.createTaskConfiguration();
        this.eObject.setTask(relateTaskConfiguration.getTask());
        this.eObject.setTaskConfiguration(factory.createTaskConfigurationBody());

    }

    public void addParam(String name, GHyperParameter hyperParameter, XDSLFactory factory) {
        Param p = factory.createParam();
        p.setName(name);
        p.setAssigned(true);
        p.setRef(hyperParameter.getEObject());
        this.eObject.getTaskConfiguration().getParams().add(p);
    }

    public TaskConfiguration getEObject(){
        return this.eObject;
    }
}
