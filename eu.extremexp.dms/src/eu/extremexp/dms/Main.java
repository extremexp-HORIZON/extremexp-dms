package eu.extremexp.dms;


import eu.extremexp.dms.jmodel.GraphicalJSONWorkflowModel;
import eu.extremexp.dms.utils.ExperimentJSONParser;
import eu.extremexp.dsl.xDSL.Workflow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import eu.extremexp.dms.utils.WorkflowJSONParser;
import eu.extremexp.dms.converter.WorkflowToJSONConverter;

@SpringBootApplication
@RestController
public class Main {

    public Main() {

    }

    @PostMapping(
            value = "/api/workflow2dsl",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> workflow2dsl(
            @RequestParam(name = "name", required = false) String name,
            @RequestBody String blobJson
    ) {
        if (blobJson == null || blobJson.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing blob JSON");
        }
        try {
            WorkflowJSONParser workflowJSONParser = new WorkflowJSONParser(blobJson);
            GraphicalJSONWorkflowModel graphicalJSONWorkflowModel = new GraphicalJSONWorkflowModel(
                    workflowJSONParser.getNodes(),
                    workflowJSONParser.getEdges(),
                    name
            );

            GraphicalJSONModel graphicalJSONModel = new GraphicalJSONModel();
            for (Workflow wf : graphicalJSONWorkflowModel){
                graphicalJSONModel.addWorkflow(wf);
            }

            return ResponseEntity.ok(graphicalJSONModel.getRootDSL());


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to convert workflow to DSL: " + e.getMessage());
        }
    }

    @PostMapping(
            value = "/api/experiment2dsl",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> experiment2dsl(
            @RequestBody String blobJson,
            @RequestParam(name = "scope", required = false) String scope
    ) {
        if (blobJson == null || blobJson.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing blob JSON");
        }
        try {
            ExperimentJSONParser experimentJSONParser = new ExperimentJSONParser(blobJson);

            if (scope == null || scope.isBlank() || scope.equals("root")){
                return ResponseEntity.ok(experimentJSONParser.getGraphicalJSONModel().getRootDSL());
            } else if (scope.equals("experiment")) {
                return ResponseEntity.ok(experimentJSONParser.getGraphicalJSONModel().getExperimentDSL());
            } else if (scope.equals("workflow")) {
                return ResponseEntity.ok(experimentJSONParser.getGraphicalJSONModel().getWorkflowsDSL());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Undefined scope, choose from root, experiment or workflow");


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to convert workflow to DSL: " + e.getMessage());
        }
    }

    @PostMapping(
            value = "/api/dsl2json",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> workflowDsl2json(@RequestBody String dslText) {
        if (dslText == null || dslText.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Missing DSL text\"}");
        }
        
        try {
            XDSLModelIO xdslModelIO = new XDSLModelIO(dslText);
            String jsonBody = WorkflowToJSONConverter.ConvertOneWorkflow(xdslModelIO.getRoot());

            return ResponseEntity.ok(jsonBody);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to convert DSL to JSON: " + 
                          e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    @PostMapping(
            value = "/api/experiment/dsl2json",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> experimentDsl2json(@RequestBody String dslText) {
        if (dslText == null || dslText.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"Missing DSL text\"}");
        }
        
        try {
//            DSLParser parser = new DSLParser();
//            Root root = parser.parseDSL(dslText);
//
//            // Find the first experiment
//            if (root.getExperiments().isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("{\"error\": \"No experiment found in DSL\"}");
//            }
//
//            Experiment experiment = root.getExperiments().get(0);
//
//            ExperimentToJSONConverter converter = new ExperimentToJSONConverter();
//            String jsonResult = converter.convertToJSON(experiment, root);
            
            return ResponseEntity.ok("NOT IMPLEMENTED YET");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to convert DSL to JSON: " + 
                          e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        java.util.Map<String, Object> defaults = new java.util.HashMap<>();
        defaults.put("server.address", "0.0.0.0");
        defaults.put("server.port", "8866");
        app.setDefaultProperties(defaults);
        app.run(args);
    }
    
}
