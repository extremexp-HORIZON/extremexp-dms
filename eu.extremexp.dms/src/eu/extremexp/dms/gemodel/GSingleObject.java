package eu.extremexp.dms.gemodel;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import java.util.HashMap;

public class GSingleObject implements GObject{
    EObject eObject;
    static HashMap<GObject, HashMap<String,Integer>> uniqueNames = new HashMap<>();

    @Override
    public EObject getEObject() {
        return this.eObject;
    }

    @Override
    public void storeEObject(Resource resource) {
        resource.getContents().add(this.getEObject());
    }

    protected String ID(GObject container, String name){
        name = name.replace("-", "_");
        name = name.replace(" ", "_");
        if (uniqueNames.containsKey(container)){
            HashMap<String, Integer> subNames = uniqueNames.get(container);
            if (subNames.containsKey(name)) {
                int counter = subNames.get(name)+1;
                subNames.put(name, counter);
                name = name + ("_" + counter);

            }
            else{
                subNames.put(name, 0);
            }
        }
        else{
            uniqueNames.put(container, new HashMap<>());
            HashMap<String, Integer> subNames = uniqueNames.get(container);
            subNames.put(name, 0);
        }

        return name;
    }


}
