package com.sos.joe.xml;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

public class TransformErrorListener implements ErrorListener {

    private final StringBuilder strMsgBuilder = new StringBuilder();

    public TransformErrorListener() {
        //
    }

    public boolean isBufferNotEmpty() {
        return strMsgBuilder.length() > 0;
    }

    public String getBuffer() {
        return strMsgBuilder.toString();
    }

    private void add2Buffer(final String pstrS) {
        strMsgBuilder.append(pstrS + "\n");
    }

    @Override
    public void warning(TransformerException exception) {
        String strS = exception.getMessage();
        add2Buffer("warning: " + strS);
    }

    @Override
    public void error(TransformerException exception) throws TransformerException {
        String strS = exception.getMessage();
        add2Buffer("Error: " + strS);
    }

    @Override
    public void fatalError(TransformerException exception) throws TransformerException {
        String strS = exception.getMessage();
        add2Buffer("fatalError: " + strS);
    }

}