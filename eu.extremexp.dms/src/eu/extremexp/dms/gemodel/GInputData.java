package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.InputData;
import eu.extremexp.dsl.xDSL.XDSLFactory;
import org.eclipse.emf.ecore.EObject;

public class GInputData extends GSingleObject{
    InputData eObject;

    public GInputData(String id, XDSLFactory factory) {
        eObject = factory.createInputData();
        eObject.setName(this.ID(id));
    }

    @Override
    public InputData getEObject() {
        return this.eObject;
    }
}
