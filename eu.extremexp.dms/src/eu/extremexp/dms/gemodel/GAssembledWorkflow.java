package eu.extremexp.dms.gemodel;

import com.fasterxml.jackson.databind.JsonNode;
import eu.extremexp.dsl.xDSL.*;

import java.util.ArrayList;
import java.util.Map;

public class GAssembledWorkflow extends GSingleObject{
    AssembledWorkflow eObject;

    public GAssembledWorkflow(GCompositeWorkflow parent, XDSLFactory factory){
        this.eObject = factory.createAssembledWorkflow();
        this.eObject.setParent(parent.getEObject());
        this.eObject.setName(this.ID(parent.getEObject().getName() + "_assembled"));

    }

    @Override
    public AssembledWorkflow getEObject() {
        return this.eObject;
    }

    public void addTaskConfiguration(GTask task, JsonNode variant, XDSLFactory factory) {
        TaskConfiguration taskConfiguration = factory.createTaskConfiguration();
        taskConfiguration.setTask(task.getEObject());

        TaskConfigurationBody taskConfigurationBody = factory.createTaskConfigurationBody();
        taskConfiguration.setTaskConfiguration(taskConfigurationBody);

        taskConfigurationBody.setDescription(variant.get("description").asText());
        taskConfigurationBody.setPrimitiveImplementation(variant.get("implementationRef").asText());

        variant.get("parameters").forEach(paramData -> {
            Param param = factory.createParam();
            param.setName(this.ID(paramData.get("name").asText()));
            if ( paramData.get("values").isArray()){
                paramData.get("values").forEach(value -> {
                    switch (paramData.get("type").asText()){
                        case "string":
                            ParamValue stringValue = factory.createParamValue();
                            stringValue.setPrimitiveValue("\"" + value.asText() + "\"");
                            param.setAssigned(true);
                            param.setValue(stringValue);
                            break;
                        case "integer":
                            ParamValue intValue = factory.createParamValue();
                            intValue.setPrimitiveValue(value.asText() );
                            param.setAssigned(true);
                            param.setValue(intValue);
                            break;
                    }
                });
            }

            taskConfigurationBody.getParams().add(param);
        });

        this.eObject.getTaskConfigurations().add(taskConfiguration);
    }
}
