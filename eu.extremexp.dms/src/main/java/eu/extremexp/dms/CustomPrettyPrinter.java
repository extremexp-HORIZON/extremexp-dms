package eu.extremexp.dms;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

import java.io.IOException;

public class CustomPrettyPrinter extends DefaultPrettyPrinter {

    public CustomPrettyPrinter() {
        _arrayIndenter = new DefaultIndenter("\t", DefaultIndenter.SYS_LF);
        _objectIndenter = new DefaultIndenter("\t", DefaultIndenter.SYS_LF);
    }

    @Override
    public DefaultPrettyPrinter createInstance() {
        return new CustomPrettyPrinter();
    }

    @Override
    public void writeObjectFieldValueSeparator(JsonGenerator gen) throws IOException {
        gen.writeRaw(" : ");
    }
}
