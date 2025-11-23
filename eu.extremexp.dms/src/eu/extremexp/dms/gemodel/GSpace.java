package eu.extremexp.dms.gemodel;

import com.fasterxml.jackson.databind.JsonNode;
import eu.extremexp.dsl.xDSL.ParamValue;
import eu.extremexp.dsl.xDSL.Space;
import eu.extremexp.dsl.xDSL.XDSLFactory;

public class GSpace extends GSingleObject{
    private final Space eObject;
    private final int executionOrder;

    public GSpace (GExperiment gExperiment, JsonNode data, int executionOrder, GAssembledWorkflow gAssembledWorkflow, XDSLFactory factory){
        this.eObject = factory.createSpace();
        this.eObject.setName(this.ID(gExperiment, data.get("name").asText()));
        this.eObject.setAssembledWorkflow(gAssembledWorkflow.getEObject());
        if (data.has("searchMethod")) {
            this.eObject.setStrategy(data.get("searchMethod").asText() + "search");
        }

        this.executionOrder = executionOrder;
    }

    public void configureTask(GSpaceTaskConfiguration gSpaceTaskConfiguration){
        this.eObject.getTaskConfigurations().add(gSpaceTaskConfiguration.getEObject());

    }

    public int getExecutionOrder() {
        return executionOrder;
    }

    public Space getEObject() {
        return eObject;
    }

    public void addHyperParameter(GHyperParameter gHyperParameter) {
        this.eObject.getParam_values().add(gHyperParameter.getEObject());
    }

    public void addSearchMethod(String searchMethod, XDSLFactory factory) {
        switch (searchMethod){
            case "random":
                this.eObject.setStrategy("randomsearch");
                var runs = factory.createAttribute();
                runs.setName("runs");
                ParamValue paramValue = factory.createParamValue();
                // TODO I don't know where this runs = 1 comes from in the example
                paramValue.setPrimitiveValue("1");
                runs.setAttributeValue(paramValue);
                this.eObject.getAttributes().add(runs);
                break;

            case "grid":
                this.eObject.setStrategy("gridsearch");
                break;

        }
    }
}
