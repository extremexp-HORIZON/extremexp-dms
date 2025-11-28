package eu.extremexp.dms.gemodel;

import com.fasterxml.jackson.databind.JsonNode;
import eu.extremexp.dsl.xDSL.Data;
import eu.extremexp.dsl.xDSL.DataConfiguration;
import eu.extremexp.dsl.xDSL.XDSLFactory;
import org.eclipse.emf.ecore.EObject;

public class GDataConfiguration extends GSingleObject{
    DataConfiguration eObject;

    public GDataConfiguration(JsonNode data, XDSLFactory factory, Data dataRef) {
        eObject = factory.createDataConfiguration();
        eObject.setData(dataRef);

        if (data.has("path")){
            eObject.setPath(data.get("path").asText());
        }

        if (data.has("project")){
            eObject.setProject(data.get("project").asText());
        }

        if (data.has("name")){
            eObject.setName(data.get("name").asText());
        }

    }

    @Override
    public DataConfiguration getEObject() {
        return this.eObject;
    }
}
