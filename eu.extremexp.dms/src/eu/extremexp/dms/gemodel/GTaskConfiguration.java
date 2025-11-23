package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.TaskConfiguration;
import eu.extremexp.dsl.xDSL.TaskConfigurationBody;
import eu.extremexp.dsl.xDSL.XDSLFactory;

public class GTaskConfiguration extends GSingleObject{
    TaskConfiguration eObject;
    public GTaskConfiguration(GTask parent, XDSLFactory factory){
        this.eObject = factory.createTaskConfiguration();
        this.eObject.setTask(parent.getEObject());
    }

    public TaskConfiguration getEObject() {
        return this.eObject;
    }

    public void setTaskConfiguration(TaskConfigurationBody taskConfigurationBody) {
        this.eObject.setTaskConfiguration(taskConfigurationBody);
    }
}
