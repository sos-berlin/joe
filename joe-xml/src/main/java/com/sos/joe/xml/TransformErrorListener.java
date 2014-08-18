/**
 * 
 */
package com.sos.joe.xml;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformErrorListener implements ErrorListener {
	@SuppressWarnings("unused") private final String		conClassName		= this.getClass().getSimpleName();
	@SuppressWarnings("unused") private static final String	conSVNVersion		= "$Id$";
	@SuppressWarnings("unused") private final Logger		logger				= LoggerFactory.getLogger(this.getClass());
	private final StringBuffer								strMsgBuffer		= new StringBuffer();
	private int										intNoOfErrors		= 0;
	private int										intNoOfFatalErrors	= 0;
	private int												intNoOfWarnings		= 0;

	public TransformErrorListener() {
	}

	public boolean isBufferNotEmpty() {
		return strMsgBuffer.length() > 0;
	}
	public String getBuffer() {
		return strMsgBuffer.toString();
	}

	private void add2Buffer(final String pstrS) {
		strMsgBuffer.append(pstrS + "\n");
	}

	@Override public void warning(TransformerException exception) {
		String strS = exception.getMessage();
		add2Buffer("warning: " + strS);
//		logger.warn(strS, exception);
		intNoOfWarnings++;
		// Don't throw an exception and stop the processor
		// just for a warning; but do log the problem
	}

	@Override public void error(TransformerException exception) throws TransformerException {
		String strS = exception.getMessage();
		add2Buffer("Error: " + strS);
//		logger.error(strS, exception);
		intNoOfErrors++;
		// XSLT is not as draconian as XML. There are numerous errors
		// which the processor may but does not have to recover from; 
		// e.g. multiple templates that match a node with the same
		// priority. I do not want to allow that so I throw this 
		// exception here.
		//	    throw exception;
	}

	@Override public void fatalError(TransformerException exception) throws TransformerException {
		String strS = exception.getMessage();
		add2Buffer("fatalError: " + strS);
//		logger.error(strS, exception);
		intNoOfFatalErrors++;
		// This is an error which the processor cannot recover from; 
		// e.g. a malformed stylesheet or input document
		// so I must throw this exception here.
		//	    throw exception;
	}
}