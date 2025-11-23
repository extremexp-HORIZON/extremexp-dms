package eu.extremexp.dms;

import eu.extremexp.dsl.xDSL.Root;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.serializer.impl.Serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class XDSLModelIO extends AbstractXDSLModelIO{

    public XDSLModelIO (AbstractXDSLModelIO abstractXDSLModelIO){
        this.root = abstractXDSLModelIO.root;
    }



}
