package com.sos.joe.options;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import com.sos.JSHelper.Options.JSOptionsClass;
import com.sos.JSHelper.Options.SOSOptionFolderName;

@JSOptionClass(name = "JOEOptionsOptionsSuperClass", description = "JOEOptionsOptionsSuperClass")
public class JOEOptionsSuperClass extends JSOptionsClass {

    private static final long serialVersionUID = -49615551365204939L;
    private static final String CLASSNAME = "JOEOptionsOptionsSuperClass";

    @JSOptionDefinition(name = "JOEHomeDir", description = "", key = "JOEHomeDir", type = "SOSOptionString", mandatory = false)
    public SOSOptionFolderName JOEHomeDir = new SOSOptionFolderName(this, CLASSNAME + ".JOEHomeDir", "", "env:SOS_JOE_HOME", "env:SOS_JOE_HOME",
            false);

    public SOSOptionFolderName getJOEHomeDir() {
        return JOEHomeDir;
    }

    public void setJOEHomeDir(final SOSOptionFolderName p_JOEHomeDir) {
        JOEHomeDir = p_JOEHomeDir;
    }

    @JSOptionDefinition(name = "JOEJobDocDir", description = "", key = "JOEJobDocDir", type = "SOSOptionString", mandatory = false)
    public SOSOptionFolderName JOEJobDocDir = new SOSOptionFolderName(this, CLASSNAME + ".JOEJobDocDir", "", "env:SOS_JOBDOC_DIR",
            "env:SOS_JOBDOC_DIR", false);

    public SOSOptionFolderName getJOEJobDocDir() {
        return JOEJobDocDir;
    }

    public void setJOEJobDocDir(final SOSOptionFolderName p_JOEJobDocDir) {
        JOEJobDocDir = p_JOEJobDocDir;
    }

    public JOEOptionsSuperClass() {
        objParentClass = this.getClass();
    }

    public JOEOptionsSuperClass(final JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    }

    public JOEOptionsSuperClass(final HashMap<String, String> JSSettings) throws Exception {
        this();
        this.setAllOptions(JSSettings);
    }

    @Override
    public void setAllOptions(final HashMap<String, String> pobjJSSettings) {
        flgSetAllOptions = true;
        objSettings = pobjJSSettings;
        super.Settings(objSettings);
        super.setAllOptions(pobjJSSettings);
        flgSetAllOptions = false;
    }

    @Override
    public void checkMandatory() throws JSExceptionMandatoryOptionMissing, Exception {
        try {
            super.checkMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    }

    @Override
    public void commandLineArgs(final String[] pstrArgs) {
        super.commandLineArgs(pstrArgs);
        this.setAllOptions(super.objSettings);
    }

}