package eu.extremexp.dms;

import com.google.inject.Injector;
import eu.extremexp.dsl.XDSLStandaloneSetup;
import eu.extremexp.dsl.xDSL.XDSLPackage;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
public abstract class  AbstractDMS implements DesignModelStorage{

    protected  XtextResourceSet resourceSet;
    public AbstractDMS () {
        XDSLPackage.eINSTANCE.eClass();
        Injector injector = new XDSLStandaloneSetup().createInjectorAndDoEMFRegistration();
        resourceSet =  injector.getInstance(XtextResourceSet.class);
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, true);
    }
}

