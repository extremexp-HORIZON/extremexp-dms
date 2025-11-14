package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.Control;
import eu.extremexp.dsl.xDSL.RegularExpLink;
import eu.extremexp.dsl.xDSL.XDSLFactory;

public class GControl extends GSingleObject {
    Control eObject;

    public GControl(XDSLFactory factory){
        this.eObject = factory.createControl();
    }

    public void addRegularExpLink(RegularExpLink regularExpLink){
        this.eObject.getFlows().add(regularExpLink);
    }

    public Control getEObject() {
        return this.eObject;
    }

}
