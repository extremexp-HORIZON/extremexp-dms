package eu.extremexp.dms.svg;

import eu.extremexp.emf.model.workflow.*;
import org.eclipse.emf.common.util.EList;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generator of SVG from a Workflow specification.
 */
public class SVGGenerator {

    private static final String WORKFLOW_BEGINNING = "node [shape=box];\nrankdir=TB;";
    private static final String GROUP_BEGINNING = "cluster=true;\nrank=same;";
    private static final String START_NODE = "START [shape=circle,style=filled,color=black,label=\"\"];\n";
    private static final String END_NODE = "END [shape=doublecircle,style=filled,color=black,label=\"\"];\n";
    private static final String PARALLEL_NODE = " [shape=diamond,label=<<FONT POINT-SIZE=\"20\">+</FONT>>];\n";
    private static final String EXCLUSIVE_NODE = " [shape=diamond,label=<<FONT POINT-SIZE=\"20\">&nbsp;</FONT>>];\n";
    private static final String INCLUSIVE_NODE = " [shape=diamond,label=<<FONT POINT-SIZE=\"20\">o</FONT>>];\n";
    private static final String JOIN_NODE = " [shape=diamond,label=<<FONT POINT-SIZE=\"20\">V</FONT>>];\n";

    private static AtomicInteger counter = new AtomicInteger();
    private static Map<Node, String> operatorNames = new HashMap<>();

    /**
     * Generates SVG.
     * @param workflow a workflow
     * @return SVG as a byte array
     * @throws SVGGeneratorException if SVG cannot be generated
     */
    public static byte[] generateSVG(Workflow workflow) throws SVGGeneratorException {
        String workflowName = workflow.getName();
        Path dotFilePath;
        try {
            dotFilePath = Files.createTempFile(workflowName, ".dot");
        } catch (IOException ex) {
            throw new SVGGeneratorException(ex);
        }
        try (BufferedWriter dotWritter = Files.newBufferedWriter(dotFilePath)) {
            if (workflow instanceof CompositeWorkflow compositeWorkflow) {
                dotWritter.write("digraph ");
                dotWritter.write(workflowName);
                dotWritter.write(" {\nnode [shape=box];\nrankdir=TB;\n");

                for (InputData inputData: workflow.getInputs()) {
                    dotWritter.write(inputData.getName() + " [label=<<B>" + inputData.getName() + "</B>>,style=dashed];\n");
                }

                for (Node node : compositeWorkflow.getNode()) {
                    switch (node) {
                        case Task task ->
                                dotWritter.write(task.getName() + " [label=<<B>" + task.getName() + "</B>>];\n");
                        case Event event when event.getName().equals(EventValue.START) ->
                                dotWritter.write(START_NODE);
                        case Event event when event.getName().equals(EventValue.END) ->
                                dotWritter.write(END_NODE);
                        case Parallel operator -> {
                            String name = getNodeName(operator);
                            dotWritter.write(name + PARALLEL_NODE);
                        }
                        case Exclusive operator -> {
                            String name = getNodeName(operator);
                            dotWritter.write(name + EXCLUSIVE_NODE);
                        }
                        case Inclusive operator -> {
                            String name = getNodeName(operator);
                            dotWritter.write(name + INCLUSIVE_NODE);
                        }
                        case Join operator -> {
                            String name = getNodeName(operator);
                            dotWritter.write(name + JOIN_NODE);
                        }
                        default ->
                                throw new SVGGeneratorException("Unsupported NODE kind: " + Node.class.getSimpleName());
                    }
                }

                for (Link link: compositeWorkflow.getLinks()) {
                    Node inputNode = link.getInput();
                    Node outputNode = link.getOutput();
                    switch (link) {
                        case RegularLink regularLink ->
                                dotWritter.write(getNodeName(inputNode) + "->" + getNodeName(outputNode) + ";\n");
                        case ConditionalLink conditionalLink -> {
                            String condition = "";
                            if (!conditionalLink.getCondition().isEmpty()) {
                                condition = ",label=\"" + conditionalLink.getCondition() + "\"";
                            }
                            dotWritter.write(getNodeName(inputNode) + "->" + getNodeName(outputNode) + "[arrowtail=ediamond,dir=both" + condition + "];\n");
                        }
                        case ExceptionalLink exceptionalLink -> {
                            String condition = "";
                            if (!exceptionalLink.getEvent().isEmpty()) {
                                condition = ",label=\"" + exceptionalLink.getEvent() + "\"";
                            }
                            dotWritter.write(getNodeName(inputNode) + "->" + getNodeName(outputNode) + "[arrowtail=obox,dir=both" + condition + "];\n");
                        }
                        default ->
                                throw new SVGGeneratorException("Unsupported LINK kind: " + Node.class.getSimpleName());
                    }
                }

                for (OutputData outputData: workflow.getOutputs()) {
                    dotWritter.write(outputData.getName() + " [label=<<B>" + outputData.getName() + "</B>>,style=dashed];\n");
                }

                for (DataLink dataLink: compositeWorkflow.getDataLinks()) {
                    String input = null;
                    String output = null;
                    String label = null;
                    if (dataLink.getInputdata() != null) {
                        input = dataLink.getInputdata().getName();
                    }
                    if (dataLink.getOutputdata() != null) {
                        output = dataLink.getOutputdata().getName();
                    }
                    EList<TaskData> taskDataList = dataLink.getTaskData();
                    if (taskDataList.size() == 1) {
                        if (input == null) {
                            input = taskDataList.getFirst().getTask().getName();
                            label = input + "." + taskDataList.getFirst().getDataName();
                        } else {
                            output = taskDataList.getFirst().getTask().getName();
                            label = output + "." + taskDataList.getFirst().getDataName();
                        }
                    } else if (taskDataList.size() == 2) {
                        input = taskDataList.getFirst().getTask().getName();
                        output = taskDataList.getLast().getTask().getName();
                        label = input + "." + taskDataList.getFirst().getDataName() + "-" + output + "." + taskDataList.getLast().getDataName();
                        input += ":w";
                        output += ":w";
                    }
                    if (label == null) {
                        label = " [style=dashed];\n";
                    } else {
                        label = " [label=<<I>" + label + "</I>>,style=dashed];\n";
                    }
                    dotWritter.write(input + " -> " + output + label);
                }

                dotWritter.write("}\n");
            }
        } catch (IOException ex) {
            throw new SVGGeneratorException(ex);
        }

        return executeCommand("dot", "-Tsvg", dotFilePath.toAbsolutePath().toString());
    }

    private static String getNodeName(Node node) throws SVGGeneratorException {
        return switch (node) {
            case Task task ->
                    task.getName();
            case Event event ->
                    event.getName().getLiteral();
            case Parallel operator -> {
                if (operatorNames.containsKey(operator)) {
                    yield operatorNames.get(operator);
                } else {
                    String name = "PARALLEL_" + counter.getAndIncrement();
                    operatorNames.put(operator, name);
                    yield name;
                }
            }
            case Exclusive operator -> {
                if (operatorNames.containsKey(operator)) {
                    yield operatorNames.get(operator);
                } else {
                    String name = "EXCLUSIVE_" + counter.getAndIncrement();
                    operatorNames.put(operator, name);
                    yield name;
                }
            }
            case Inclusive operator -> {
                if (operatorNames.containsKey(operator)) {
                    yield operatorNames.get(operator);
                } else {
                    String name = "INCLUSIVE_" + counter.getAndIncrement();
                    operatorNames.put(operator, name);
                    yield name;
                }
            }
            case Join operator -> {
                if (operatorNames.containsKey(operator)) {
                    yield operatorNames.get(operator);
                } else {
                    String name = "JOIN_" + counter.getAndIncrement();
                    operatorNames.put(operator, name);
                    yield name;
                }
            }
            default ->
                    throw new SVGGeneratorException("Unsupported node kind: " + Node.class.getSimpleName());
        };
    }

    private static byte[] executeCommand(String... command) throws SVGGeneratorException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            process = processBuilder.start();

            InputStream inputStream = process.getInputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new SVGGeneratorException("DOT command failed with exit code " + exitCode);
            }
        } catch (IOException | InterruptedException ex) {
            throw new SVGGeneratorException("Error executing command", ex);
        } finally {
            // Ensure the process resources are cleaned up
            if (process != null) {
                process.destroy();
            }
        }

        return byteArrayOutputStream.toByteArray();
    }
}
