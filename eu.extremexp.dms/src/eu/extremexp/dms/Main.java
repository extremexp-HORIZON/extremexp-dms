package eu.extremexp.dms;

import java.util.List;

import eu.extremexp.dms.utils.ExperimentJSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import eu.extremexp.dms.utils.JEdge;
import eu.extremexp.dms.utils.JNode;
import eu.extremexp.dms.utils.WorkflowJSONParser;

@SpringBootApplication
@RestController
public class Main {

    public Main() {

    }

    // --- New API endpoints ---
    // 1) workflow2dsl: takes name + blob (JSON) and returns DSL text
    // 2) dsl2workflow: placeholder
    // 3) experiment2dsl: placeholder
    // 4) dsl2experiment: placeholder

    /**
     * Convert JSON to XMI (model instance).
     */
    @PostMapping(
            value = "/convert_to_model",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public String convertToModel(@RequestBody String json) {
        return "works";

    }

    /**
     * Convert XMI to JSON.
     */
    @PostMapping(
            value = "/convert_from_model",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String convertFromModel(@RequestBody String xmi) {
        return "works too";
    }

    /**
     * Convert UI graph JSON (examples/*.json structure) to XMI of the XDSL model (DSL-Model based XMI).
     */
    @PostMapping(
            value = "/convert_graph_json",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public String convertGraphJson(@RequestBody String graphJson) {
        return "works as well";
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
            // Parse the workflow JSON; currently we just ensure it parses.
            XDSLModelIO xDSLModelIO = getWorkflowDSLModel(name, blobJson);
            return ResponseEntity.ok(xDSLModelIO.formattedSerialize());


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to convert workflow to DSL: " + e.getMessage());
        }
    }

    private static XDSLModelIO getWorkflowDSLModel(String name, String blobJson) {
        WorkflowJSONParser parser = new WorkflowJSONParser(blobJson);
        List<JNode> jNodes = parser.getNodes();
        List<JEdge> jEdges = parser.getEdges();
        // Minimal placeholder DSL text until full mapping is implemented
        String wfName = (name == null || name.isBlank()) ? "workflow" : name;

        var graphicalJSONxDSLModelIO = new GraphicalJSONWorkflowModel(jNodes, jEdges, wfName);
        return new XDSLModelIO(graphicalJSONxDSLModelIO);
    }

    private static XDSLModelIO getExperimentDSLModel(String name, String blobJson) {
        ExperimentJSONParser parser = new ExperimentJSONParser(blobJson);
//        List<JNode> jNodes = parser.getNodes();
//        List<JEdge> jEdges = parser.getEdges();
        // Minimal placeholder DSL text until full mapping is implemented
        String wfName = (name == null || name.isBlank()) ? "workflow" : name;
        StringBuilder sb = new StringBuilder();

//        var graphicalJSONxDSLModelIO = new GraphicalJSONWorkflowModel(jNodes, jEdges, wfName);
        return null ;//new XDSLModelIO(graphicalJSONxDSLModelIO);
    }

    @PostMapping(
            value = "/api/dsl2workflow",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> dsl2workflow(@RequestBody String dslText) {
        try {
            System.out.println(dslText);
            XDSLGraphicalJSONModelIO xDSLGraphicalJSONModelIO = new XDSLGraphicalJSONModelIO(dslText);
            String body = "{}" ;
            return ResponseEntity.ok(body);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to convert workflow to DSL: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping(
            value = "/api/experiment2dsl",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> experiment2dsl(
            @RequestParam(name = "name", required = false) String name,
            @RequestBody String blobJson
    ) {
        if (blobJson == null || blobJson.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing blob JSON");
        }
        try {
            // Parse the workflow JSON; currently we just ensure it parses.
//            XDSLModelIO xDSLModelIO = getExperimentDSLModel(name, blobJson);
            return ResponseEntity.ok("...");


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to convert workflow to DSL: " + e.getMessage());
        }
    }

    @PostMapping(
            value = "/api/dsl2experiment",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> dsl2experiment(@RequestBody String dslText) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("dsl2experiment not implemented yet");
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
