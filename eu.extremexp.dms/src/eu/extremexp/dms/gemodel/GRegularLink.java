package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.Node;
import eu.extremexp.dsl.xDSL.RegularLink;
import eu.extremexp.dsl.xDSL.XDSLFactory;

public class GRegularLink {
    RegularLink eObject;

    public GRegularLink(GTask source, GTask target, XDSLFactory factory){

        Node sourceNode = factory.createNode();
        sourceNode.setRef(source.getEObject());

        Node targetNode = factory.createNode();
        targetNode.setRef(target.getEObject());

        this.eObject = factory.createRegularLink();
        this.eObject.setInput(sourceNode);
        this.eObject.getOutput().add(targetNode);


    }

    public RegularLink getEObject(){
        return this.eObject;
    }

}
