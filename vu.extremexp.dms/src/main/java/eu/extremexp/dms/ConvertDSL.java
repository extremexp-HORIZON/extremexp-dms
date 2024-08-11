package eu.extremexp.dms;

public class ConvertDSL {
    public static void main(String [] args){
        if (args.length < 2 || args[0].equals("-h") || args[0].equals("--help")) {
            printUsage();
            return;
        }

        String dslPath = args[0];
        String convertor = args[1];
        FileBasedDMS fileBasedDesignModelStorage;
        switch (convertor) {
            case "-j":
            case "--json":
                fileBasedDesignModelStorage = new JSONConvertor(dslPath);
                fileBasedDesignModelStorage.convert(dslPath.replace(".xxp",".json"));
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
        System.out.println(" ---- ");
        System.out.println("  -h, --help  - Show this help message");
    }
}
