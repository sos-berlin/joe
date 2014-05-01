package sos.scheduler.editor.app;

import org.jdom.Element;

@Deprecated
public class JSObjectElement {
    protected int     _type;
    protected Element _element;

    protected String strName = "";

    public JSObjectElement() {
    }


    public JSObjectElement(final int type, final Element element) {
        _type = type;
        _element = element;
        strName = getName();
    }

    public JSObjectElement(final int type, final Element element, final String helpKey) {
        this(type, element);
    }


    public int getType() {
        return _type;
    }


    public JSObjectElement setType(final int type) {
        _type = type;
        return this;
    }


    public Element getElement() {
        return _element;
    }

    public JSObjectElement getJSObjectElement() {
        return this;
    }


    public JSObjectElement setElement(final Element element) {
        _element = element;
        return this;
    }


    public boolean equals(final int type) {
        return _type == type;
    }

    public boolean TypeEqualTo(final int type) {
        return _type == type;
    }

	public String getName() {
		String name = Utils.getAttributeValue("name", _element);
		if (name == null || name.length() <= 0) {
			name = _element.getAttributeValue("id");
		}
		if (name == null) {
			name = "???";
		}
		return name;
	}

	public  String getTitle() {
		String strTitle = Utils.getAttributeValue("title", _element);
		if (strTitle == null) {
			strTitle = "";
		}
		return strTitle;
	}

	public String getFileName () {
//		String strFileName = get
		return "";
	}

	public String getNameAndTitle() {
		String strTitle = this.getTitle();
		String strObjectNameAndTitle = getName();

		if (isEnabled() == false) {
			strObjectNameAndTitle += SOSJOEMessageCodes.JOE_M_JobCommand_Disabled.label();
		}

		if (strTitle.trim().length() > 0) {
			strObjectNameAndTitle += " - " + strTitle;
		}
		return strObjectNameAndTitle;
	}

	public boolean isEnabled() {
		String enabledAttr = Utils.getAttributeValue("enabled", _element);
		boolean enabled = enabledAttr.equalsIgnoreCase("yes") || enabledAttr.length() == 0;
		return enabled;
	}

	public boolean NameEqualTo(final String pstrName) {
		return strName.equalsIgnoreCase(pstrName);
	}
}
