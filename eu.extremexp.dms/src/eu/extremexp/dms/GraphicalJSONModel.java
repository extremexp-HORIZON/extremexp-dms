package eu.extremexp.dms;

import eu.extremexp.dms.jmodel.GraphicalJSONExperimentModel;
import eu.extremexp.dsl.xDSL.CompositeWorkflow;
import eu.extremexp.dsl.xDSL.Workflow;
import eu.extremexp.dsl.xDSL.XDSLFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.serializer.impl.Serializer;


public class GraphicalJSONModel extends AbstractXDSLModelIO{

    public GraphicalJSONModel(){

        var resource = this.resourceSet.createResource(URI.createURI("temp.xxp"));
        this.root = XDSLFactory.eINSTANCE.createRoot();
        resource.getContents().add(this.root);
    }

    public void addWorkflow(Workflow workflow){
        this.root.getWorkflows().add(workflow);
    }

    public void addExperiment(GraphicalJSONExperimentModel graphicalJSONExperimentModel){
        this.root.getExperiments().add(graphicalJSONExperimentModel.getExperiment());
    }

    public String getRootDSL(){
        Serializer serializer = injector.getInstance(Serializer.class);
        String dslText = serializer.serialize(this.root);

        return this.format(dslText);
    }

    public String getWorkflowsDSL(){
        Serializer serializer = injector.getInstance(Serializer.class);
        StringBuilder dslBuild = new StringBuilder();

        // CompositeWorkflow first
        for (EObject workflow: this.root.getWorkflows()){
            if (workflow instanceof CompositeWorkflow){
                String dslText = serializer.serialize(workflow);
                dslBuild.append(dslText).append("\n\n");
            }
        }

        for (EObject workflow: this.root.getWorkflows()){
            if (!(workflow instanceof CompositeWorkflow)){
                String dslText = serializer.serialize(workflow);
                dslBuild.append(dslText).append("\n\n");
            }
        }
        return this.format(dslBuild.toString());
    }

    public String getExperimentDSL(){
        Serializer serializer = injector.getInstance(Serializer.class);
        StringBuilder dslBuild = new StringBuilder();

        // create imports
        for (EObject workflow: this.root.getWorkflows()){
            if (workflow instanceof CompositeWorkflow){
                dslBuild.append("import \"").append(((CompositeWorkflow) workflow).getName()).append(".xxp\" ;\n\n");
            }
        }

        for (EObject experiment: this.root.getExperiments()){
            String dslText = serializer.serialize(experiment);

            dslBuild.append(dslText);
            dslBuild.append("\n");

        }
        dslBuild.append("\n");
        return this.format(dslBuild.toString());
    }



    private String format(String dsl){
        int indent = 0;
        boolean newLine = false;
        StringBuilder newText = new StringBuilder();
        String lastToken = "";
        for (var token : dsl.split(" ") ){
            switch (token){
                case "{":
                    newLine = true;
                    newText.append("{");
                    indent++;
                    break;
                case ";":
                    newLine = true;
                    newText.append(";");
                    break;
                case "}":
                    if (newLine){
                        newText.append("\n");
                    }
                    newLine = true;
                    indent--;
                    newText.append("\t".repeat(Math.max(0, indent)));
                    newText.append("}\n");
                    break;
                default:
                    if (newLine){
                        newText.append("\n");
                        newText.append("\t".repeat(Math.max(0, indent)));
                        newText.append(token);
                    }
                    else{
                        if (token.equals(".") || lastToken.equals(".")){
                            newText.append(token);
                        }
                        else{
                            newText.append(" ").append(token);
                        }
                    }
                    newLine = false;
                    break;

            }
            lastToken = token;

        }
        return newText.toString();
    }
}
