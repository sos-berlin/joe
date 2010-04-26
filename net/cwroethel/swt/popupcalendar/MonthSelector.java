package net.cwroethel.swt.popupcalendar;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;

/*******************************************************************************
 * Copyright (c) 2005 Will Roethel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Will Roethel -
 * initial API and implementation
 ******************************************************************************/

/**
 * The month selector contains two buttons scroll a month forward or backward
 * separated by a label showing the currently selected month name and year. If
 * one of the buttons has been pressed the month selector fires an ActionEvent
 * with event id DateChooserAction.previousMonth, or DateChooserAction.nextMonth
 * (depending on the button pressed). The style of the buttons and the label can
 * be defined by either providing a custom instance of the MonthSelectorStyle
 * class or by getting access to that class and changing the default values for
 * the gui components there.
 * 
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 */
public class MonthSelector extends DateChooserPanel {

    /**
     * Image used to decorate the next month selector button.
     */
    private Image             nextMonthImage     = null;

    /**
     * Image used to decorate the previous month selector button.
     */
    private Image             previousMonthImage = null;

    /**
     * The instance of MonthSelectorStyle to be used for drawing this
     * MonthSelector.
     */
    public MonthSelectorStyle style              = null;

    private CCombo            monthSelector;

    private Spinner           yearSelector;

    private boolean           doUpdate           = true;


    /**
     * Default constructor.
     */
    public MonthSelector(Composite parent) {
        super(parent, SWT.LEFT_TO_RIGHT);
        style = new MonthSelectorStyle();
        init();
    }


    /**
     * Constructor overriding the default style with a provided style.
     * 
     * @param newStyle MonthSelectorStyle
     */
    public MonthSelector(Composite parent, MonthSelectorStyle style) {
        super(parent, SWT.LEFT_TO_RIGHT);
        this.style = style;
        init();
    }


    /**
     * Sets up the gui objects and draws the panel.
     */
    private void init() {

        // make sure to get rid of the default graphics resources.
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {

                if (previousMonthImage != null) {
                    previousMonthImage.dispose();
                }
                if (nextMonthImage != null) {
                    nextMonthImage.dispose();
                }
            }
        });

        // set the widget defaults - some overkill here...
        GridLayout thisLayout = new GridLayout(4, false);
        thisLayout.horizontalSpacing = 5;
        thisLayout.verticalSpacing = 0;
        thisLayout.marginWidth = 3;
        thisLayout.marginHeight = 3;
        setLayout(thisLayout);
        setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));

        GridData buttonData = new GridData();
        buttonData.verticalAlignment = GridData.CENTER;
        // gdata.horizontalIndent = 5;
        buttonData.heightHint = 20;

        GridData monthData = new GridData();
        monthData.verticalAlignment = GridData.CENTER;
        monthData.heightHint = 20;
        monthData.horizontalAlignment = GridData.FILL;
        monthData.grabExcessHorizontalSpace = true;
        // gdata2.horizontalIndent = 5;

        GridData yearData = new GridData();
        yearData.verticalAlignment = GridData.CENTER;
        // gdata3.horizontalIndent = 5;
        yearData.widthHint = 34;
        // yearData.heightHint = 16;

        Button previous = new Button(this, SWT.NONE);
        previous.setLayoutData(buttonData);
        // define the label to select the previous month. This needs to be
        // a label since we want to change the label colors.
        // Label previousMonthLabel = new Label(this, SWT.CENTER);
        // previousMonthLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
        // style.setStyle(previousMonthLabel,
        // MonthSelectorStyle.PREVIOUSMONTHBUTTON);

        monthSelector = new CCombo(this, SWT.READ_ONLY | SWT.FLAT);
        monthSelector.setLayoutData(monthData);
        monthSelector.setItems(style.getMonthNames());
        monthSelector.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }


            public void widgetSelected(SelectionEvent e) {
                Event event = new Event();
                event.widget = MonthSelector.this;
                event.detail = DateChooserAction.monthSelected;
                SelectionEvent selectionEvent = new SelectionEvent(event);
                sendSelectionEvent(selectionEvent);
            }
        }); 

        yearSelector = new Spinner(this, SWT.FLAT);
        yearSelector.setLayoutData(yearData);
        yearSelector.setMaximum(9999);
        yearSelector.setMinimum(1970);
        yearSelector.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }


            public void widgetSelected(SelectionEvent e) {
                doUpdate = false;
                Event event = new Event();
                event.widget = MonthSelector.this;
                event.detail = DateChooserAction.yearSelected;
                SelectionEvent selectionEvent = new SelectionEvent(event);
                sendSelectionEvent(selectionEvent);
                doUpdate = true;
            }
        });

        Button next = new Button(this, SWT.NONE);
        next.setLayoutData(buttonData);
        // label to select the next month.
        // Label nextMonthLabel = new Label(this, SWT.FLAT);
        // nextMonthLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
        // style.setStyle(nextMonthLabel, MonthSelectorStyle.NEXTMONTHBUTTON);

        // add mouse listeners
        // set up button for previous month
        Display display = Display.getCurrent();
        InputStream imageStream;
        try {
            imageStream = MonthSelector.class.getResource(style.previousMonthImageName).openStream();
            previousMonthImage = new Image(display, imageStream);
            if (previousMonthImage != null) {
                previous.setImage(previousMonthImage);
            }
        } catch (Exception e) {
            // ignore
        }
        previous.addMouseListener(new MouseAdapter() {
            public void mouseUp(MouseEvent mouseEvent) {
                Event event = new Event();
                event.widget = MonthSelector.this;
                event.detail = DateChooserAction.previousMonthSelected;
                SelectionEvent selectionEvent = new SelectionEvent(event);
                sendSelectionEvent(selectionEvent);
            }
        });

        // set up label and listener for selecting next month
        try {
            imageStream = MonthSelector.class.getResource(style.nextMonthImageName).openStream();
            nextMonthImage = new Image(display, imageStream);
            if (nextMonthImage != null) {
                next.setImage(nextMonthImage);
            }
        } catch (Exception e) {
            // ignore
        }
        next.addMouseListener(new MouseAdapter() {
            public void mouseUp(MouseEvent mouseEvent) {
                Event event = new Event();
                event.widget = MonthSelector.this;
                event.detail = DateChooserAction.nextMonthSelected;
                SelectionEvent selectionEvent = new SelectionEvent(event);
                sendSelectionEvent(selectionEvent);
            }
        });
    }


    public void setMonth(String name) {
        monthSelector.select(monthSelector.indexOf(name));
        monthSelector.clearSelection();
    }


    public void setYear(int year) {
        yearSelector.setSelection(year);
    }


    public int getMonth() {
        return monthSelector.getSelectionIndex();
    }


    public int getYear() {
        return yearSelector.getSelection();
    }


    public boolean doUpdate() {
        return doUpdate;
    }
}
