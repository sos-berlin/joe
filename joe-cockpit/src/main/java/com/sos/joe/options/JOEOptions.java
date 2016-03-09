package com.sos.joe.options;

import java.util.HashMap;

import com.sos.JSHelper.Annotations.JSOptionClass;

import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import org.apache.log4j.Logger;

/** \class JOEOptionsOptions - JOEOptions
 *
 * \brief An Options as a container for the Options super class. The Option
 * class will hold all the things, which would be otherwise overwritten at a
 * re-creation of the super-class.
 *
 *
 * 
 *
 * see \see
 * C:\Users\KB\AppData\Local\Temp\scheduler_editor-3587302424923049269.html for
 * (more) details.
 *
 * \verbatim ; mechanicaly created by JobDocu2OptionsClass.xslt from
 * http://www.sos-berlin.com at 20130206180040 \endverbatim */
@JSOptionClass(name = "JOEOptionsOptions", description = "JOEOptions")
public class JOEOptions extends JOEOptionsSuperClass {

    @SuppressWarnings("unused")
    private final String conClassName = "JOEOptionsOptions";
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(JOEOptions.class);

    /** constructors */

    public JOEOptions() {
    } // public JOEOptionsOptions

    public JOEOptions(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    } // public JOEOptionsOptions

    //

    public JOEOptions(HashMap<String, String> JSSettings) throws Exception {
        super(JSSettings);
    } // public JOEOptionsOptions (HashMap JSSettings)

    /** \brief CheckMandatory - prüft alle Muss-Optionen auf Werte
     *
     * \details
     * 
     * @throws Exception
     *
     * @throws Exception - wird ausgelöst, wenn eine mandatory-Option keinen
     *             Wert hat */
    @Override
    // JOEOptionsOptionsSuperClass
    public void CheckMandatory() {
        try {
            super.CheckMandatory();
        } catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    } // public void CheckMandatory ()
}
