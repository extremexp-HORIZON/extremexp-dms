package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.InputData;
import eu.extremexp.dsl.xDSL.OutputData;
import eu.extremexp.dsl.xDSL.XDSLFactory;

public class GOutputData extends GSingleObject{
    OutputData eObject;

    public GOutputData(GCompositeWorkflow gCompositeWorkflow, String id, XDSLFactory factory) {
        eObject = factory.createOutputData();
        eObject.setName(this.ID(gCompositeWorkflow, id));
    }

    @Override
    public OutputData getEObject() {
        return this.eObject;
    }
}
