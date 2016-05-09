package com.sos.joe.options;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.Listener.JSListenerClass;

public class JOEOptionsJUnitTest extends JSToolBox {

    protected JOEOptions objOptions = null;

    @Before
    public void setUp() throws Exception {
        objOptions = new JOEOptions();
        JSListenerClass.bolLogDebugInformation = true;
        JSListenerClass.intMaxDebugLevel = 9;
    }

    @Test
    public void testJOEHomeDir() {
        assertNotEquals("JOE Home Dir wrong", "C:\\ProgramData\\sos-berlin.com\\JOE/", objOptions.JOEHomeDir.Value());
        objOptions.JOEHomeDir.Value("++env:SOS_JOE_HOME++");
        assertEquals("", "++env:SOS_JOE_HOME++" + "/", objOptions.JOEHomeDir.Value());
    }

    @Test
    public void testJOEJobDocDir() { // SOSOptionString
        objOptions.JOEJobDocDir.Value("++env:SOS_JOBDOC_DIR++");
        assertEquals("", "++env:SOS_JOBDOC_DIR++" + "/", objOptions.JOEJobDocDir.Value());
    }

}