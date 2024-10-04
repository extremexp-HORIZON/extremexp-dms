package eu.extremexp.dms;

import com.google.inject.Injector;
import eu.extremexp.dsl.XDSLStandaloneSetup;
import eu.extremexp.dsl.xDSL.XDSLPackage;

public abstract class  AbstractDMS implements DesignModelStorage{

    public Injector injector;
    public AbstractDMS () {
        XDSLPackage.eINSTANCE.eClass();
        this.injector = new XDSLStandaloneSetup().createInjectorAndDoEMFRegistration();
    }
}

