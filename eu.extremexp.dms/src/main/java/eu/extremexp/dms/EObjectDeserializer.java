package eu.extremexp.dms;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import eu.extremexp.dsl.xDSL.Component;
import eu.extremexp.dsl.xDSL.ExperimentPackage;
import eu.extremexp.dsl.xDSL.Workflow;
import eu.extremexp.dsl.xDSL.XDSLPackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.serializer.ISerializer;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResourceSet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class EObjectDeserializer extends JsonDeserializer<EObject> {

    private final ISerializer serializer;

    public EObjectDeserializer(ISerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public EObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        EObject rootObject = createRootObject();
        ExperimentPackage ep = null;
        if (p.currentToken() == JsonToken.START_OBJECT) {
            p.nextToken();

            while (p.currentToken() != JsonToken.END_OBJECT) {
                String fieldName = p.getCurrentName();

                if (ep == null){
                    if (fieldName.equals("name")) {
                        System.out.println(p.currentValue());
                       ep = createPackage(rootObject, "pc");
                       Component cm = (Component) EcoreUtil.create(XDSLPackage.Literals.COMPONENT);
                       Workflow wf = (Workflow) createWorkflow("some");
                       cm.setWorkflow(wf);
                       ep.getComponents().add(cm);
                    }
                }
                else {

                }

//
//                // Find the corresponding feature in the EClass
//                EStructuralFeature feature = eClass.getEStructuralFeature(fieldName);
//                if (feature != null) {
//                    Object value = null;
//                    // Deserialize based on the type of the feature
//                    if (feature.isMany()) {
//                        // If the feature is a list, handle it accordingly
//                        value = p.readValueAs(List.class);
//                    } else {
//                        value = p.readValueAs(feature.getEType().getInstanceClass());
//                    }
//                    // Set the feature value in the EObject
//                    eObject.eSet(feature, value);
//                } else {
//                    // Skip unknown fields or handle them as needed
//                    p.skipChildren();
//                }

                p.nextToken(); // Move to the next field or end of object
            }
        }
        System.out.println(rootObject);
        // Serialize to DSL
        String dslText = serializer.serialize(rootObject);
        System.out.println("Generated DSL:");
        System.out.println(dslText);

        return rootObject;
    }

    private EObject createRootObject() {
        // Create an instance of your Root class
        return EcoreUtil.create(XDSLPackage.Literals.ROOT);
    }

    private ExperimentPackage createPackage(EObject rootObject, String packageName) {
        ExperimentPackage ep = (ExperimentPackage) EcoreUtil.create(XDSLPackage.Literals.EXPERIMENT_PACKAGE);
        ep.setName(packageName);
        rootObject.eSet(XDSLPackage.Literals.ROOT__PACKAGE, ep);
        return ep;
    }

    private Workflow createWorkflow(String workflowName) {
        Workflow workflow = (Workflow) EcoreUtil.create(XDSLPackage.Literals.WORKFLOW);
        workflow.eSet(XDSLPackage.Literals.WORKFLOW__NAME, workflowName);
        return workflow;
    }

//    private EObject createExperiment(JsonNode experimentNode) {
//        EObject experiment = EcoreUtil.create(XDSLPackage.Literals.EXPERIMENT);
//        experiment.eSet(XDSLPackage.Literals.EXPERIMENT__NAME, experimentNode.get("name").asText());
//        return experiment;
//    }
//


    // Static method to register the deserializer with Jackson
    public static void registerDeserializer(ObjectMapper mapper, ISerializer serializer) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(EObject.class, new EObjectDeserializer(serializer));
        mapper.registerModule(module);
    }
}
