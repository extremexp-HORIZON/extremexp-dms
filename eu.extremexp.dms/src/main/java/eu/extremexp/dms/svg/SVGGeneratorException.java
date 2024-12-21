package eu.extremexp.dms.svg;

/**
 * Exception to report errors related to SVG generation.
 */
public class SVGGeneratorException extends RuntimeException {
    public SVGGeneratorException() {
    }

    public SVGGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SVGGeneratorException(Throwable cause) {
        super(cause);
    }

    public SVGGeneratorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SVGGeneratorException(String message) {
        super(message);
    }
}
