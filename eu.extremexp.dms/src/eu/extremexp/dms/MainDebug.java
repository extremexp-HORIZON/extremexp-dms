package eu.extremexp.dms;


import eu.extremexp.dms.converter.RootToJSONConvertor;
import eu.extremexp.dms.jmodel.GraphicalJSONWorkflowModel;
import eu.extremexp.dms.utils.ExperimentJSONParser;
import eu.extremexp.dms.utils.JEdge;
import eu.extremexp.dms.utils.JNode;
import eu.extremexp.dms.utils.WorkflowJSONParser;
import eu.extremexp.dsl.xDSL.Workflow;
import org.springframework.http.ResponseEntity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainDebug {
    public static void main(String[] args) {
        if (args.length == 0){
            System.err.println("not enough arguments, enter path to DSL");
        }

//        try(InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(args[0]))){
//            StringBuilder stringBuilder = new StringBuilder();
//            int ch;
//            while ((ch = inputStreamReader.read()) != -1){
//                stringBuilder.append((char) ch);
//            }
//            String dsl = stringBuilder.toString();
//            XDSLModelIO xdslModelIO = new XDSLModelIO(dsl);
//            RootToJSONConvertor rootToJSONConvertor = new RootToJSONConvertor(xdslModelIO.getRoot());
//
//
//
//        }
//        catch (IOException err){
//            System.err.println("error: "+ err.getMessage());
//        }
//        catch (RuntimeException err){
//            System.err.println("error: "+ err.getMessage());
//        }

        try(InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(args[0]))){
            StringBuilder stringBuilder = new StringBuilder();
            int ch;
            while ((ch = inputStreamReader.read()) != -1){
                stringBuilder.append((char) ch);
            }
            String blobJson = stringBuilder.toString();
            WorkflowJSONParser workflowJSONParser = new WorkflowJSONParser(blobJson);
            GraphicalJSONWorkflowModel graphicalJSONWorkflowModel = new GraphicalJSONWorkflowModel(
                    workflowJSONParser.getNodes(),
                    workflowJSONParser.getEdges(),
                    "test"
            );

            GraphicalJSONModel graphicalJSONModel = new GraphicalJSONModel();
            for (Workflow wf : graphicalJSONWorkflowModel){
                graphicalJSONModel.addWorkflow(wf);
            }

            System.out.println(graphicalJSONModel.getRootDSL());
        }
        catch (IOException err){
            System.err.println("error: "+ err.getMessage());
        }

//        try(InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(args[0]))){
//            StringBuilder stringBuilder = new StringBuilder();
//            int ch;
//            while ((ch = inputStreamReader.read()) != -1){
//                stringBuilder.append((char) ch);
//            }
//            String blobJson = stringBuilder.toString();
//            ExperimentJSONParser parser = new ExperimentJSONParser(blobJson);
//
//        }
//        catch (IOException err){
//            System.err.println("error: "+ err.getMessage());
//        }


    }
}
