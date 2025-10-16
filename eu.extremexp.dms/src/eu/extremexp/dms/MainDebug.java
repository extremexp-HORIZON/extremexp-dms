package eu.extremexp.dms;

import eu.extremexp.dms.utils.JEdge;
import eu.extremexp.dms.utils.JNode;
import eu.extremexp.dms.utils.ParseJSON;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class MainDebug {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java MainDebug <path-to-json-file>");
            System.err.println("Example: java MainDebug examples/med.json");
            return;
        }

        String jsonContent;
        try {
            jsonContent = Files.readString(Path.of(args[0]));
        } catch (Exception e) {
            System.err.println("Failed to read JSON file: " + args[0]);
            e.printStackTrace();
            return;
        }

        // Parse graph JSON into nodes and edges
        ParseJSON parser = new ParseJSON(jsonContent);
        List<JNode> jNodes = parser.getNodes();
        List<JEdge> jEdges = parser.getEdges();

        // Placeholder: deal with the map output from ParseJSON
        System.out.println("Parsed nodes: " + jNodes.size());
        System.out.println("Parsed edges: " + jEdges.size());

        var graphicalJSONxDSLModelIO = new GraphicalJSONxDSLModelIO(jNodes, jEdges, "med");
        XDSLModelIO xDSLModelIO = new XDSLModelIO(graphicalJSONxDSLModelIO);
        System.out.println(xDSLModelIO.formattedSerialize());
//
//        edges.entrySet().stream().limit(5).forEach(entry -> {
//            JEdge e = entry.getKey();
//            ParseJSON.Endpoints ep = entry.getValue();
//            System.out.println(" - " + e.id() + " [" + e.type() + "]: " + ep.source().id() + " -> " + ep.target().id());
//        });

        // Placeholder for future xDSL integration using GraphicalJSONxDLSModelIO
        // GraphicalJSONxDLSModelIO graphicalJSONxDLSModelIO = new GraphicalJSONxDLSModelIO(jsonContent);
        // var root = graphicalJSONxDLSModelIO.createDummyRoot();
        // XDSLModelIO xDSLModelIO = new XDSLModelIO(root);
        // System.out.println(xDSLModelIO.formattedSerialize());
    }
}
