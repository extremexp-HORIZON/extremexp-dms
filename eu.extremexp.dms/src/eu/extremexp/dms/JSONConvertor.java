package eu.extremexp.dms;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.eclipse.emf.ecore.EObject;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.serializer.ISerializer;

import java.io.*;

public class JSONConvertor extends FileBasedDMS{
    public JSONConvertor(String directoryPath) {
        super(directoryPath);
        XtextResourceSet resourceSet = this.injector.getInstance(XtextResourceSet.class);
        resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, true);
    }


    public void convert(EObject eObject, String jsonPath) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(EObject.class, new EObjectSerializer());
        mapper.registerModule(module);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        ObjectWriter writer = mapper.writer(new CustomPrettyPrinter());
        try {
            OutputStream out = new FileOutputStream(jsonPath);
            writer.writeValue(out, eObject);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void convert(String jsonPath) {
        this.convert(this.model, jsonPath);

    }

}

