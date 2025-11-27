package eu.extremexp.dms;


import eu.extremexp.dms.utils.ExperimentJSONParser;
import eu.extremexp.dms.utils.JEdge;
import eu.extremexp.dms.utils.JNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainDebug {
    public static void main(String[] args) {
        if (args.length == 0){
            System.err.println("not enough arguments, enter path to DSL");
        }

        try(InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(args[0]))){
            StringBuilder stringBuilder = new StringBuilder();
            int ch;
            while ((ch = inputStreamReader.read()) != -1){
                stringBuilder.append((char) ch);
            }
            String blobJson = stringBuilder.toString();
            ExperimentJSONParser experimentJSONParser = new ExperimentJSONParser(blobJson);
            System.out.println(experimentJSONParser.getGraphicalJSONModel().getExperimentDSL());
        }
        catch (IOException err){
            System.err.println("error: "+ err.getMessage());
        }

//        try(InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(args[0]))){
//            StringBuilder stringBuilder = new StringBuilder();
//            int ch;
//            while ((ch = inputStreamReader.read()) != -1){
//                stringBuilder.append((char) ch);
//            }
//            String blobJson = stringBuilder.toString();
//            ExperimentJSONParser parser = new ExperimentJSONParser(blobJson);
//
//        }
//        catch (IOException err){
//            System.err.println("error: "+ err.getMessage());
//        }


    }
}
