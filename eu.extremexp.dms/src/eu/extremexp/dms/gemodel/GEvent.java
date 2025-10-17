package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.Event;
import eu.extremexp.dsl.xDSL.EventValue;
import eu.extremexp.dsl.xDSL.XDSLFactory;


public class GEvent extends GSingleObject{
    Event eObject;

    public GEvent(EventValue eventValue, XDSLFactory factory) {
        this.eObject = factory.createEvent();
        this.eObject.setEventValue(eventValue);

    }

    public Event getEObject(){
        return this.eObject;
    }
}
