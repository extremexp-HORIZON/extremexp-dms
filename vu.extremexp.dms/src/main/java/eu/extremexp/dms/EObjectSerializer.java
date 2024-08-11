package eu.extremexp.dms;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.io.IOException;

public class EObjectSerializer extends StdSerializer<EObject> {

    public EObjectSerializer() {
        this(null);
    }

    public EObjectSerializer(Class<EObject> t) {
        super(t);
    }

    @Override
    public void serialize(EObject eObject, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        for (EStructuralFeature feature : eObject.eClass().getEAllStructuralFeatures()) {
            Object value = eObject.eGet(feature);
            gen.writeObjectField(feature.getName(), value);
        }
        gen.writeEndObject();
    }
}