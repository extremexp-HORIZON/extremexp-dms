package eu.extremexp.dms;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.eclipse.emf.ecore.EObject;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;

public class JSONConvertor extends FileBasedDMS{
    public JSONConvertor(String directoryPath) {
        super(directoryPath);
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

