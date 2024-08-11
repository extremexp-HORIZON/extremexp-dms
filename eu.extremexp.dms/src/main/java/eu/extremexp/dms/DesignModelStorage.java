package eu.extremexp.dms;

import org.eclipse.emf.ecore.EObject;

public interface DesignModelStorage {

    public EObject get(String name) ;

    public void put(String name, EObject element) ;
}

