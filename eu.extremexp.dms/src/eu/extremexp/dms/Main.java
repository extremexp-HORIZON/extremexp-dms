package eu.extremexp.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

@SpringBootApplication
@RestController
public class Main {

    public Main() {

    }

    // TODO api/json2dsl
    // TODO api/dsl2json

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

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
