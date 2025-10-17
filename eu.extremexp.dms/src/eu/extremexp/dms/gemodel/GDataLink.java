package eu.extremexp.dms.gemodel;

import eu.extremexp.dsl.xDSL.*;

public class GDataLink extends GSingleObject {
    InputDataLink inputDataLink = null;
    OutputDataLink outputDataLink = null;
    TaskDataLink taskDataLink = null;

    public GDataLink (GTask source, GOutputData target,  XDSLFactory factory){
        this.outputDataLink = factory.createOutputDataLink();

        this.outputDataLink.setInputNode(source.getEObject());
        this.outputDataLink.setInputNodeData(target.getEObject().getName());

        this.outputDataLink.setOutputData(target.getEObject());
    }

    public GDataLink (GInputData source, GTask target,  XDSLFactory factory){
        this.inputDataLink = factory.createInputDataLink();

        this.inputDataLink.setInputData(source.getEObject());

        this.inputDataLink.setOutputNode(target.getEObject());
        this.inputDataLink.setOutputNodeData(source.getEObject().getName());
    }

    public GDataLink (GTask source, GTask target,  XDSLFactory factory){
        this.taskDataLink = factory.createTaskDataLink();

        this.taskDataLink.setInputNode(source.getEObject());
        this.taskDataLink.setInputNodeData("unset");

        this.taskDataLink.setOutputNode(target.getEObject());
        this.taskDataLink.setOutputNodeData("unset");
    }

    public DataLink getDataLink(){
        if (inputDataLink != null){
            return inputDataLink;
        }
        if (outputDataLink != null){
            return outputDataLink;
        }
        if (taskDataLink != null){
            return taskDataLink;
        }
        return null;
    }




}
