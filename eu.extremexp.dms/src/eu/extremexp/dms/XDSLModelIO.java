package eu.extremexp.dms;


import eu.extremexp.dsl.xDSL.Root;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

public class XDSLModelIO extends AbstractXDSLModelIO{

    public XDSLModelIO (String dsl) throws RuntimeException, FileNotFoundException {
        super();
        this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
        Random random = new Random();

        String filename = "example" + random.nextInt(0, 10000) +".xxp";
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(filename), StandardCharsets.UTF_8)) {
            writer.write(dsl);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        URI uri = URI.createURI(filename);
        Resource resource = resourceSet.getResource(uri, true);

        // Access root elements
        List<EObject> contents = resource.getContents();

        if (contents.size() != 1){
            throw  new RuntimeException("No ROOT was found in the DSL");
        }

        this.root = (Root) contents.getFirst();

        // Delete the file after loading
        File f = new File(filename);
        if (f.exists()) {
            if (!f.delete()) {
                System.err.println("Failed to delete file: " + filename);
            }
        }
    }

    public Root getRoot(){
        return this.root;
    }

}
