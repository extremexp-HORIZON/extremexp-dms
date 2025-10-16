package eu.extremexp.dms.gemodel;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public interface GObject {
    EObject getEObject();

    void storeEObject(Resource resource);
}
