package eu.europa.esig.dss.web.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class YesNoBooleanDeserializer extends JsonDeserializer<Boolean> {
    public static final String YES = "Y";
    public static final String NO = "N";

    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.getText().equalsIgnoreCase(YES)) {
            return Boolean.TRUE;
        } else if (p.getText().equalsIgnoreCase(NO)) {
            return Boolean.FALSE;
        }
        return null;
    }
}
