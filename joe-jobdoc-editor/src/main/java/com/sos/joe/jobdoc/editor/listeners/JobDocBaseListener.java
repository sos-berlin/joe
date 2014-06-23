/**
 * 
 */
package com.sos.joe.jobdoc.editor.listeners;

import org.jdom.Element;

import com.sos.joe.xml.ElementWrapper;
import com.sos.joe.xml.XMLUtils;


/**
 * @author KB
 *
 */
public class JobDocBaseListener <T> extends XMLUtils {
	
	protected T _dom;
	protected Element				_parent;
	protected ElementWrapper objW = null;
	protected Element objE = null;
	
	protected void initElement (final Element objE) {
		objW = new ElementWrapper(objE);
	}
	
	protected ElementWrapper getWrapper (final Element objE ) {
		return new ElementWrapper (objE) ;
	}
}
