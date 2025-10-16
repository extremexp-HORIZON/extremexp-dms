package eu.extremexp.dms.gemodel;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public class GSingleObject implements GObject{
    EObject eObject;

    @Override
    public EObject getEObject() {
        return this.eObject;
    }

    @Override
    public void storeEObject(Resource resource) {
        resource.getContents().add(this.getEObject());
    }

    protected String ID(String name){
        name = name.replace("-", "_");
        name = name.replace(" ", "_");
        return name;
    }


}
