package eu.extremexp.dms;

import eu.extremexp.dsl.xDSL.Experiment;
import eu.extremexp.dsl.xDSL.Workflow;
import eu.extremexp.dsl.xDSL.XDSLFactory;
import org.eclipse.emf.common.util.URI;


public class GraphicalJSONModel extends AbstractXDSLModelIO{

    public GraphicalJSONModel(){

        var resource = this.resourceSet.createResource(URI.createURI("temp.xxp"));
        this.root = XDSLFactory.eINSTANCE.createRoot();
        resource.getContents().add(this.root);
    }

    public void addWorkflows(GraphicalJSONWorkflowModel graphicalJSONWorkflowModel){
        for (Workflow workflow: graphicalJSONWorkflowModel){
            this.root.getWorkflows().add(workflow);
        }
    }


    public void addExperiment(GraphicalJSONExperimentModel graphicalJSONExperimentModel){
        this.root.getExperiments().add(graphicalJSONExperimentModel.getExperiment());
    }

}
