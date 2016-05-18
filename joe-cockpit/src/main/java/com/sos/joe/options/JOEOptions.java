package com.sos.joe.options;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;

@JSOptionClass(name = "JOEOptionsOptions", description = "JOEOptions")
public class JOEOptions extends JOEOptionsSuperClass {

    private static final long serialVersionUID = 1L;

    public JOEOptions() {
    }

    public JOEOptions(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    }

    public JOEOptions(HashMap<String, String> JSSettings) throws Exception {
        super(JSSettings);
    }

    @Override
    public void checkMandatory() {
        try {
            super.checkMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    }

}