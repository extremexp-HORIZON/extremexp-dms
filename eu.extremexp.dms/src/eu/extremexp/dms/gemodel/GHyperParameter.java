package eu.extremexp.dms.gemodel;

import com.fasterxml.jackson.databind.JsonNode;
import eu.extremexp.dsl.xDSL.*;

import java.util.ArrayList;

public class GHyperParameter extends GSingleObject{
    Param eObject;

    public GHyperParameter (GSpace container, String name, XDSLFactory factory){
        eObject = factory.createParam();
        eObject.setName(this.ID(container, name));

    }

    public void setValue(String type, String domain, String defaultValue , JsonNode values, XDSLFactory factory){
        ParamValue paramValue = factory.createParamValue();
        switch (domain){
            case "enum":
                ParamValueEnum paramValueEnum = factory.createParamValueEnum();
                for (var value : values){
                    if (type.equals("string")){
                         paramValueEnum.getValues().add("\"" + value.asText() + "\"");
                    }
                    else{
                        paramValueEnum.getValues().add(value.asText());
                    }
                }
                paramValue.setEnumValue(paramValueEnum);
                this.eObject.setValue(paramValue);
                return;
            case "range":
                ParamValueRange paramValueRange = factory.createParamValueRange();
                ArrayList<Integer> parsedValues = new ArrayList<>();
                for (var value : values){
                    parsedValues.add(value.asInt());
                }
                if (parsedValues.size() == 1){
                    paramValueRange.setStart(0);
                    paramValueRange.setEnd(parsedValues.getFirst());
                    paramValueRange.setStep(1);
                }
                if (parsedValues.size() == 2){
                    paramValueRange.setStart(parsedValues.getFirst());
                    paramValueRange.setEnd(parsedValues.getLast());
                    paramValueRange.setStep(1);
                }
                if (parsedValues.size() > 2){
                    paramValueRange.setStart(parsedValues.getFirst());
                    paramValueRange.setEnd(parsedValues.get(1));
                    paramValueRange.setStep(parsedValues.getLast());
                }

                paramValue.setRangeValue(paramValueRange);
                this.eObject.setValue(paramValue);
                return;
        }
        paramValue.setPrimitiveValue(defaultValue);
        this.eObject.setValue(paramValue);

    }

    public Param getEObject(){
        return this.eObject;
    }
}
