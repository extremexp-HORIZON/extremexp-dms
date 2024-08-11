package eu.extremexp.dms;

import eu.extremexp.dsl.xDSL.*;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;


public class FileBasedDMS extends AbstractDMS {

    protected Root model;
    protected HashMap<String, EObject> eObjectMap;
    @Override
    public EObject get(String name) {
        return eObjectMap.get(name);
    }

    @Override
    public void put(String name, EObject element) {
        eObjectMap.put(name, element);
    }

    public void convert( String outputPath){
        return;
    }

    private Root expFileToModel(String filepath) throws IOException{
        Resource resource = resourceSet.getResource(URI.createFileURI(filepath), true);
        return (Root) resource.getContents().get(0);
    }
    private static boolean isEXPFile(Path path, LinkOption... options) {
        return path.toString().toLowerCase().endsWith(".xxp");
    }

    public FileBasedDMS (String directoryPath) {
        eObjectMap = new HashMap<>();

        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            List<Path> resolvedPaths = paths.filter(Files::isRegularFile).filter(FileBasedDMS::isEXPFile).toList();
            for (var path : resolvedPaths){
                this.model = expFileToModel(path.toString());
                if (model.getTask()!=null){
                    this.put(model.getTask().getName(), model.getTask());
                }
                else{
                    this.put(model.getPacakge().getName(), model.getPacakge());
                }

            }
        }
        catch (IOException _ioe) {
            System.out.println("path does not exist: " + _ioe.getLocalizedMessage());
        }
        catch (Exception ex) {
            System.out.println("Syntax error:" + ex.getMessage());
        }
    }


}
