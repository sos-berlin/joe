package com.sos.joe.xml;

import org.jdom.Element;
import org.jdom.Namespace;

import com.sos.joe.xml.jobscheduler.SchedulerDom;

/** @author KB */
public class XMLUtils {

    private static final String STR_DEFAULT = "";
    private static final String INT_DEFAULT = null;
    private static final String BOOLEAN_DEFAULT = null;

    protected void setAttribute(String attribute, String value, String defaultValue, Element element, DomParser dom) {
        if (value == null || value.trim().equals(defaultValue)) {
            element.removeAttribute(attribute);
            if (dom != null) {
                dom.setChanged(true);
            }
        } else if (!value.trim().equals(element.getAttributeValue(attribute))) {
            element.setAttribute(attribute, value.trim());
            if (dom != null) {
                dom.setChanged(true);
                if (dom instanceof SchedulerDom) {
                    if ("order".equals(element.getName())) {
                        ((SchedulerDom) dom).setChangedForDirectory("order",
                                Utils.getAttributeValue("job_chain", element) + "," + Utils.getAttributeValue("id", element), SchedulerDom.MODIFY);
                    } else {
                        ((SchedulerDom) dom).setChangedForDirectory("job", Utils.getAttributeValue("name", element), SchedulerDom.MODIFY);
                    }
                }
            }
        }
    }

    protected void setAttribute(String attribute, String value, Element element, DomParser dom) {
        setAttribute(attribute, value, STR_DEFAULT, element, dom);
    }

    protected void setAttribute(String attribute, String value, String defaultValue, Element element) {
        setAttribute(attribute, value, defaultValue, element, null);
    }

    protected void setAttribute(String attribute, String value, Element element) {
        setAttribute(attribute, value, STR_DEFAULT, element, null);
    }

    protected void setAttribute(String attribute, int value, int defaultValue, Element element, DomParser dom) {
        setAttribute(attribute, "" + value, "" + defaultValue, element, dom);
    }

    protected void setAttribute(String attribute, int value, Element element, DomParser dom) {
        setAttribute(attribute, "" + value, INT_DEFAULT, element, dom);
    }

    protected void setAttribute(String attribute, int value, int defaultValue, Element element) {
        setAttribute(attribute, "" + value, "" + defaultValue, element, null);
    }

    protected void setAttribute(String attribute, int value, Element element) {
        setAttribute(attribute, "" + value, INT_DEFAULT, element, null);
    }

    protected void setAttribute(String attribute, boolean value, boolean defaultValue, Element element, DomParser dom) {
        setAttribute(attribute, value ? "yes" : "no", defaultValue ? "yes" : "no", element, dom);
    }

    protected void setAttribute(String attribute, boolean value, Element element, DomParser dom) {
        setAttribute(attribute, value ? "yes" : "no", BOOLEAN_DEFAULT, element, dom);
    }

    protected void setAttribute(String attribute, boolean value, boolean defaultValue, Element element) {
        setAttribute(attribute, value ? "yes" : "no", defaultValue ? "yes" : "no", element, null);
    }

    protected void setAttribute(String attribute, boolean value, Element element) {
        setAttribute(attribute, value ? "yes" : "no", BOOLEAN_DEFAULT, element, null);
    }

    protected void setBoolean(String attribute, boolean value, Element element) {
        setAttribute(attribute, value ? "true" : "false", element, null);
    }

    protected void setBoolean(String attribute, boolean value, Element element, DomParser dom) {
        setAttribute(attribute, value ? "true" : "false", element, dom);
    }

    protected String getElement(String name, Element parent, Namespace ns) {
        if (parent != null) {
            Element element = parent.getChild(name, ns);
            if (element != null) {
                return element.getTextTrim();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    protected void setElement(String name, String value, boolean optional, Element parent, Namespace ns, DomParser dom) {
        if (parent != null) {
            Element element = parent.getChild(name, ns);
            if (element == null && ((value != null && !value.isEmpty()) || !optional)) {
                element = new Element(name, ns).setText(value);
                parent.addContent(element);
                if (dom != null) {
                    dom.setChanged(true);
                }
            } else if (element != null && (value == null || value.isEmpty()) && optional) {
                element.detach();
                if (dom != null) {
                    dom.setChanged(true);
                }
            } else if (element.getTextTrim() != null && !element.getTextTrim().equals(value)) {
                element.setText(value);
                if (dom != null) {
                    dom.setChanged(true);
                }
            }
        }
    }

    protected String getAttributeValue(String attribute, Element element) {
        if (element != null) {
            String val = element.getAttributeValue(attribute);
            return val == null ? "" : val;
        } else {
            return "";
        }
    }

    protected int getIntValue(String attribute, Element element) {
        return getIntValue(attribute, 0, element);
    }

    protected int getIntValue(String attribute, int defaultValue, Element element) {
        if (element == null) {
            return defaultValue;
        }
        String val = element.getAttributeValue(attribute);
        if (val != null) {
            try {
                return new Integer(val).intValue();
            } catch (Exception e) {
                element.setAttribute(attribute, "" + defaultValue);
            }
        }
        return defaultValue;
    }

    protected boolean isAttributeValue(String attribute, Element element) {
        return "yes".equalsIgnoreCase(getAttributeValue(attribute, element));
    }

    protected boolean getBooleanValue(String attribute, Element element) {
        return "true".equalsIgnoreCase(getAttributeValue(attribute, element));
    }

}