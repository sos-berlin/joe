package net.cwroethel.swt.popupcalendar;

import org.eclipse.swt.widgets.Control;

/*******************************************************************************
 * Copyright (c) 2005 Will Roethel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Will Roethel -
 * initial API and implementation
 ******************************************************************************/

/**
 * Adapter for simple customization of the 'today' button. Since there is only
 * one component in this panel there no switch or if clause is required to
 * specify the component. Just override the overrideStyle class, e.g.
 * 
 * <pre>
 * PopupCalendar.setTodaySelectorStyle(new TodaySelectorStyleAdapter() {
 *     public void overrideStyle(Control control, int style) {
 *         control.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
 *     }
 * });
 * </pre>
 * 
 * @author W. Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 */
public class TodaySelectorStyleAdapter extends TodaySelectorStyle {

    /**
     * Method invoked after the default settings are applied to the todya
     * button. To override the button style, just specify the attributes for
     * components. Remember that the Today button is a Button, so it may be
     * necessary to type case in order to apply an attribute, e.g. ((Button)
     * component).setText("blabla") .
     * 
     * @param component Control
     * @param style int
     */
    public void overrideStyle(Control component, int style) {
    }
}
