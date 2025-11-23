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

            return ResponseEntity.ok(graphicalJSONModel.formattedSerialize());


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
    public ResponseEntity<String> experiment2dsl(@RequestBody String blobJson) {
        if (blobJson == null || blobJson.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing blob JSON");
        }
        try {
            ExperimentJSONParser experimentJSONParser = new ExperimentJSONParser(blobJson);
            return ResponseEntity.ok(experimentJSONParser.getGraphicalJSONModel().formattedSerialize());


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to convert workflow to DSL: " + e.getMessage());
        }
    }

    @PostMapping(value = "/dsl2json", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> dsl2json(@RequestBody String dslBody) {
        try {
            // TODO: Implement DSL to JSON conversion logic
            String jsonResult = "{\"result\": \"JSON output from DSL\"}";
            return ResponseEntity.ok(jsonResult);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"error\": \"" + e.getMessage() + "\"}");
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
