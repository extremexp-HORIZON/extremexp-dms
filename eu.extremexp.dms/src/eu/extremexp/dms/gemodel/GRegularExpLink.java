package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.ParallelNodes;
import eu.extremexp.dsl.xDSL.RegularExpLink;
import eu.extremexp.dsl.xDSL.Space;
import eu.extremexp.dsl.xDSL.XDSLFactory;

import java.util.List;

public class GRegularExpLink extends GSingleObject{
    RegularExpLink eObject ;

    public GRegularExpLink(boolean started, boolean ended, XDSLFactory factory){
        this.eObject = factory.createRegularExpLink();
        this.eObject.setStarted(started);
        this.eObject.setEnded(ended);
    }

    public void addParallelNodes (List<Space> spaces, XDSLFactory factory){
        ParallelNodes parallelNodes = factory.createParallelNodes();
        for (Space sp : spaces){
            parallelNodes.getNodes().add(sp);
        }

        this.eObject.getParallelNodes().add(parallelNodes);

    }

    public void addSingleNode(Space space, XDSLFactory factory){
        this.eObject.getNodes().add(space);
    }

    public RegularExpLink getEObject() {
        return this.eObject;
    }
}
