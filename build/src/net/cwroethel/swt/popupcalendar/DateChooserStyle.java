package net.cwroethel.swt.popupcalendar;

import java.util.Locale;

import org.eclipse.swt.widgets.Control;

/*******************************************************************************
 * Copyright (c) 2005 Will Roethel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Will Roethel -
 * initial API and implementation
 ******************************************************************************/

/**
 * <p>
 * Class containing all style information on the GUI elements used by
 * DateChooser. The preferred way to override these settings is to use the
 * methods provided by PopupCalendar to modify the look and feel of the swing
 * components.
 * 
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 * @see DateChooser
 * @see DateChooserPanel
 */
public abstract class DateChooserStyle {
    static int       daySize = 18;

    protected Locale locale  = Locale.getDefault();


    public DateChooserStyle() {
    }


    /**
     * Set the default values for the various swing components used by
     * DateChooser.
     */
    protected void setDefaultStyle(Control component, int style) {
    }


    /**
     * Properties meant to contain user definitions.
     * 
     * @param component
     * @param style
     */
    protected void overrideStyle(Control component, int style) {
    }


    protected void setLocale(Locale locale) {
        this.locale = locale;
    }


    /**
     * Run the style definitions over the control. Calls overrideStyle which
     * allows overriding of the default style with the various style adapter
     * classes.
     * 
     * @param component
     * @param style
     */
    public void setStyle(Control component, int style) {
        setDefaultStyle(component, style);
        overrideStyle(component, style);
    }

}
