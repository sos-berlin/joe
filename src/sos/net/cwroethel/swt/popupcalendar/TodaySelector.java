package net.cwroethel.swt.popupcalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

/*******************************************************************************
 * Copyright (c) 2005 Will Roethel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Will Roethel -
 * initial API and implementation
 ******************************************************************************/

/**
 * DateChooser component to quickly select todays date. The TodaySelector is
 * comprised of only one button. If the button is pressed an SelectionEvent with
 * event detail id DateChooserAction.todaySelected is fired.
 * 
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 */
public class TodaySelector extends DateChooserPanel {

    private TodaySelectorStyle style;


    /**
     * Default constructor.
     */
    public TodaySelector(Composite parent) {
        super(parent, SWT.LEFT_TO_RIGHT);
        style = new TodaySelectorStyle();
        init();
    }


    /**
     * Constructor overriding the default style.
     * 
     * @param newStyle TodaySelectorStyle
     */
    public TodaySelector(Composite parent, TodaySelectorStyle style) {
        super(parent, SWT.LEFT_TO_RIGHT);
        this.style = style;
        init();
    }


    /**
     * Set up the gui components and draw the panel.
     */
    private void init() {
        // again - our defaults
        GridLayout thisLayout = new GridLayout(1, false);
        thisLayout.horizontalSpacing = 0;
        thisLayout.verticalSpacing = 0;
        thisLayout.marginWidth = 0;
        thisLayout.marginHeight = 0;
        setLayout(thisLayout);
        setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));

        // add the button
        Button todayButton = new Button(this, SWT.PUSH);
        GridData data = new GridData(GridData.CENTER | GridData.FILL_HORIZONTAL);
        todayButton.setLayoutData(data);
        style.setStyle(todayButton, TodaySelectorStyle.TODAYBUTTON);

        // add the selection listener and send a signal to all listeners if
        // today button was selected.
        todayButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                Event event = new Event();
                event.widget = TodaySelector.this;
                event.detail = DateChooserAction.todaySelected;
                selectionEvent = new SelectionEvent(event);
                sendSelectionEvent(selectionEvent);
            }
        });
    }
}
