package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.RegularExpLink;
import eu.extremexp.dsl.xDSL.RegularLink;
import eu.extremexp.dsl.xDSL.Space;
import eu.extremexp.dsl.xDSL.XDSLFactory;

public class GRegularExpLink extends GSingleObject{
    RegularExpLink eObject ;

    public GRegularExpLink(boolean started, boolean ended, XDSLFactory factory){
        this.eObject = factory.createRegularExpLink();
        this.eObject.setStarted(started);
        this.eObject.setEnded(ended);
    }

    public void addSpace(XDSLFactory factory, Space space){
        this.eObject.getNodes().add(space);
    }

    public RegularExpLink getEObject() {
        return this.eObject;
    }
}
