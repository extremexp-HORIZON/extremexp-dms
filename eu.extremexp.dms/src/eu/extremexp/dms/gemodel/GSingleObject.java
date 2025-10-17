package eu.extremexp.dms.gemodel;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import java.util.HashMap;

public class GSingleObject implements GObject{
    EObject eObject;
    static HashMap<String, Integer> uniqueNames = new HashMap<>();

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
        if (uniqueNames.containsKey(name)) {
            int counter = uniqueNames.get(name)+1;
            uniqueNames.put(name, counter);
            name = name + ("_" + counter);
        }
        else{
            uniqueNames.put(name, 0);
        }
        return name;
    }


}
