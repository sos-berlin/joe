/**
 * 
 */
package com.sos.joe.xml;

import org.jdom.Element;

/** @author KB */
public class ElementWrapper extends XMLUtils {

    private Element objElement = null;

    public ElementWrapper(final Element pobjElement) {
        objElement = pobjElement;
    }

    public Element getElement() {
        return objElement;
    }

    public String getAttributeValue(String attribute) {
        if (objElement != null) {
            String val = objElement.getAttributeValue(attribute);
            return val == null ? "" : val;
        } else {
            return "";
        }
    }

}
