package eu.extremexp.dms;

import eu.extremexp.dms.utils.JCompositeWorkflow;
import eu.extremexp.dsl.xDSL.AssembledWorkflow;
import eu.extremexp.dsl.xDSL.CompositeWorkflow;
import eu.extremexp.dsl.xDSL.Root;
import eu.extremexp.dsl.xDSL.Workflow;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class XDSLGraphicalJSONModelIO extends AbstractXDSLModelIO{

//    public XDSLGraphicalJSONModelIO(String dslText) throws IOException {
//        super();
//        this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
//        URI uri = URI.createURI("example.xxp");
//
//        // 4️⃣ Create and load resource
//        Resource resource = this.resourceSet.createResource(uri);
//        resource.load(new ByteArrayInputStream(dslText.getBytes(StandardCharsets.UTF_8)),
//                resourceSet.getLoadOptions());
//
//        // 5️⃣ Access root element or check errors
//        if (!resource.getErrors().isEmpty()) {
//            throw new IOException("Parsing errors: " + resource.getErrors());
//        }
//
//        this.root = (Root) resource.getContents().getFirst();
//    }
//
//    public JCompositeWorkflow getWorkflow(){
//        for (Workflow workflow : this.root.getWorkflows()){
//            if (workflow instanceof CompositeWorkflow) {
//                JCompositeWorkflow jCompositeWorkflow = new JCompositeWorkflow((CompositeWorkflow) workflow);
//
//                ArrayList<AssembledWorkflow> assembledWorkflows = new ArrayList<>();
//                for (Workflow wf : this.root.getWorkflows()){
//                    if (wf instanceof AssembledWorkflow){
//                        if (((AssembledWorkflow) wf).getParent() != null){
//                            if (((AssembledWorkflow) wf).getParent() == workflow){
//                                assembledWorkflows.add((AssembledWorkflow) wf);
//                            }
//                        }
//                    }
//
//                }
//                jCompositeWorkflow.populateNodes(assembledWorkflows);
//                jCompositeWorkflow.populateEdges();
//            }
//        }
//        return null;
//    }

    @Override
    public EObject createModelObject() {
        return super.createModelObject();
    }

    @Override
    public Resource getResource(EObject eObject) {
        return super.getResource(eObject);
    }
}
