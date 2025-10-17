package eu.extremexp.dms.api;

public class Workflow2DslRequest {
    private String name;
    private String blob; // JSON string of the workflow graph

    public Workflow2DslRequest() {}

    public Workflow2DslRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
