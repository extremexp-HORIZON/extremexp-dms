package eu.extremexp.dms;

import eu.extremexp.dsl.xDSL.Root;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.google.inject.Injector;
import eu.extremexp.dsl.XDSLStandaloneSetup;
import eu.extremexp.dsl.xDSL.XDSLPackage;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

public class AbstractXDSLModelIO implements ECoreModelIO{

    protected XtextResourceSet resourceSet;
    public Injector injector;
    public Root root;

    public AbstractXDSLModelIO () {
        XDSLPackage.eINSTANCE.eClass();
        injector = new XDSLStandaloneSetup().createInjectorAndDoEMFRegistration();
        resourceSet = injector.getInstance(XtextResourceSet.class);
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, true);
    }

    @Override
    public EObject createModelObject() {
        return null;
    }

    @Override
    public Resource getResource(EObject eObject) {
        return null;
    }
}
