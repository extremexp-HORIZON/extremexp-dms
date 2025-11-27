package eu.extremexp.dms.utils;

import com.google.inject.Injector;
import eu.extremexp.dsl.XDSLStandaloneSetup;
import eu.extremexp.dsl.xDSL.Root;
import eu.extremexp.dsl.xDSL.XDSLPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Utility class to parse DSL text and extract the Root element using Xtext framework
 */
public class DSLParser {
    
    private final Injector injector;
    private final XtextResourceSet resourceSet;
    
    public DSLParser() {
        // Initialize Xtext for DSL parsing
        XDSLPackage.eINSTANCE.eClass();
        injector = new XDSLStandaloneSetup().createInjectorAndDoEMFRegistration();
        resourceSet = injector.getInstance(XtextResourceSet.class);
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, true);
    }
    
    /**
     * Parse DSL text and return the Root element
     * 
     * @param dslText The DSL text to parse
     * @return Root element containing workflows and experiments
     * @throws IOException if parsing fails
     */
    public Root parseDSL(String dslText) throws IOException {
        if (dslText == null || dslText.isBlank()) {
            throw new IllegalArgumentException("DSL text cannot be null or empty");
        }
        
        // Create a resource for the DSL text
        Resource resource = resourceSet.createResource(URI.createURI("temp.xxp"));
        
        // Load the DSL text into the resource
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
            dslText.getBytes(StandardCharsets.UTF_8)
        );
        resource.load(inputStream, resourceSet.getLoadOptions());
        
        // Check for errors
        if (!resource.getErrors().isEmpty()) {
            StringBuilder errorMsg = new StringBuilder("Failed to parse DSL:\n");
            resource.getErrors().forEach(error -> 
                errorMsg.append("- ").append(error.getMessage()).append("\n")
            );
            throw new IOException(errorMsg.toString());
        }
        
        // Get the root element
        if (resource.getContents().isEmpty()) {
            throw new IOException("No content found in DSL");
        }
        
        Object rootObject = resource.getContents().get(0);
        if (!(rootObject instanceof Root)) {
            throw new IOException("Root element not found in DSL");
        }
        
        return (Root) rootObject;
    }
}
