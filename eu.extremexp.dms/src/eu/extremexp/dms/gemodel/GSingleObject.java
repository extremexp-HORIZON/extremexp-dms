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
        return ID(container, name, true);
    }

    protected String ID(GObject container, String name, boolean transformName){
        String transformedName = name;
        if (transformName) {
            transformedName = name.replace("-", "_");
            transformedName = transformedName.replace(" ", "_");
        } else {
            // Still replace spaces even when preserving hyphens
            transformedName = name.replace(" ", "_");
        }

        if (uniqueNames.containsKey(container)){
            HashMap<String, Integer> subNames = uniqueNames.get(container);
            if (subNames.containsKey(transformedName)) {
                int counter = subNames.get(transformedName)+1;
                subNames.put(transformedName, counter);
                transformedName = transformedName + ("_" + counter);

            }
            else{
                subNames.put(transformedName, 0);
            }
        }
        else{
            uniqueNames.put(container, new HashMap<>());
            HashMap<String, Integer> subNames = uniqueNames.get(container);
            subNames.put(transformedName, 0);
        }

        return transformedName;
    }


}
