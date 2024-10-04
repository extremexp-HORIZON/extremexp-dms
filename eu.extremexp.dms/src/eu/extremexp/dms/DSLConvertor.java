package eu.extremexp.dms;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.extremexp.dsl.xDSL.XDSLPackage;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.serializer.ISerializer;

import java.io.*;

public class DSLConvertor extends FileBasedDMS{
    String jsonPath;
    public DSLConvertor(String jsonPath) {
        this.jsonPath = jsonPath;
        XtextResourceSet resourceSet = this.injector.getInstance(XtextResourceSet.class);
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, true);
    }


    public void convert(String jsonPath, String newPath) {
        SimpleModule module = new SimpleModule();
        ObjectMapper mapper = new ObjectMapper();
        ISerializer serializer = injector.getInstance(ISerializer.class); // Obtain the Xtext serializer
        EObjectDeserializer.registerDeserializer(mapper, serializer);
        mapper.registerModule(module);

        try {
            // Read JSON from file
            InputStream in = new FileInputStream(jsonPath);
            EObject eObject = mapper.readValue(in, EObject.class); // Deserialize JSON to EObject

            System.out.println(eObject.eResource());
//            // Now write the converted EObject (DSL) to the new file
//            OutputStream out = new FileOutputStream(newPath);
//            mapper.writer().writeValue(out, eObject); // Serialize EObject back to JSON (or another format if needed)
//
//            // Close streams
//            in.close();
//            out.close();
        } catch (IOException e) {
            throw new RuntimeException("Error during JSON to DSL conversion", e);
        }
    }

    @Override
    public void convert(String xxpPath) {
        this.convert(this.jsonPath, xxpPath);

    }

}

