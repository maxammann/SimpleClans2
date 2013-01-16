package com.p000ison.dev.simpleclans2.util;

import java.io.StringWriter;

/**
 * Represents a PrettyJSONWriter
 */
public class PrettyJSONWriter extends StringWriter {

    private int indent = 0;

    @Override
    public void write(int c) {
        if (((char) c) == '[' || ((char) c) == '{') {
            super.write(c);
            super.write('\n');
            indent++;
            writeIndentation();
        } else if (((char) c) == ',') {
            super.write(c);
            super.write('\n');
            writeIndentation();
        } else if (((char) c) == ']' || ((char) c) == '}') {
            super.write('\n');
            indent--;
            writeIndentation();
            super.write(c);
        } else {
            super.write(c);
        }
    }

    private void writeIndentation() {
        for (int i = 0; i < indent; i++) {
            super.write("   ");
        }
    }
}
