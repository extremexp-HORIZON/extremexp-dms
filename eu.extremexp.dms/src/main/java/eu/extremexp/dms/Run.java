package eu.extremexp.dms;

public class Run {
    public static void main(String [] args){
        if (args.length < 2 || args[0].equals("-h") || args[0].equals("--help")) {
            printUsage();
            return;
        }

        String filepath = args[0];
        String convertor = args[1];
        FileBasedDMS fileBasedDesignModelStorage;
        switch (convertor) {
            case "-j":
            case "--json":
                fileBasedDesignModelStorage = new JSONConvertor(filepath);
                fileBasedDesignModelStorage.convert(filepath.replace(".xxp",".json"));
                break;
            case "-d":
            case "--dsl":
                fileBasedDesignModelStorage = new DSLConvertor(filepath);
                fileBasedDesignModelStorage.convert(filepath.replace(".json",".xxpg"));
                break;
            default:
                System.out.println("Unknown convertor: " + convertor);
                printUsage();
        }
    }
    private static void printUsage() {
        System.out.println("Usage: java ConvertDSL DSLPath <convertor>");
        System.out.println("convertors:");
        System.out.println(" -j, --json - Verify and convert DSL to JSON");
        System.out.println(" -d, --dsl - Verify and convert JSON to DSL");
        System.out.println(" ---- ");
        System.out.println("  -h, --help  - Show this help message");
    }
}
