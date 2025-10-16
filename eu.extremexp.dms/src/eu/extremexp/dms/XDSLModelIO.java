package eu.extremexp.dms;

import eu.extremexp.dsl.xDSL.Root;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.serializer.impl.Serializer;

public class XDSLModelIO extends AbstractXDSLModelIO{

    public XDSLModelIO (AbstractXDSLModelIO abstractXDSLModelIO){
        this.root = abstractXDSLModelIO.root;
    }

    public String formattedSerialize(){
        Serializer serializer = injector.getInstance(Serializer.class);
        String dslText = serializer.serialize(this.root);

        return this.format(dslText);
    }

//    public void convert(String filePath) {
//
//        String dslText = serializer.serialize(this.rootElement);
//        try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(filePath))){
//            outputStreamWriter.write(this.format(dslText));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    private String format(String dsl){
        int indent = 0;
        boolean newLine = false;
        StringBuilder newText = new StringBuilder();
        for (var token : dsl.split(" ") ){
            switch (token){
                case "{":
                    newLine = true;
                    newText.append("{");
                    indent++;
                    break;
                case ";":
                    newLine = true;
                    newText.append(";");
                    break;
                case "}":
                    if (newLine){
                        newText.append("\n");
                    }
                    newLine = true;
                    indent--;
                    newText.append("\t".repeat(Math.max(0, indent)));
                    newText.append("}\n");
                    break;
                default:
                    if (newLine){
                        newText.append("\n");
                        newText.append("\t".repeat(Math.max(0, indent)));
                        newText.append(token);
                    }
                    else{
                        newText.append(" ").append(token);
                    }
                    newLine = false;
                    break;

            }

        }
        return newText.toString();
    }
}
