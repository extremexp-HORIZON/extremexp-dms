package eu.extremexp.dms;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public interface ECoreModelIO {
    EObject createModelObject();
    Resource getResource(EObject eObject);

}
