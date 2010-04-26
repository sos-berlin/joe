package net.cwroethel.swt.popupcalendar;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

/*******************************************************************************
 * Copyright (c) 2005 Will Roethel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Will Roethel -
 * initial API and implementation
 ******************************************************************************/

/**
 * Select a day for a given month using the classic calendar view of a day with
 * days arranged in a grid. If a day is selected DaySelector fires an
 * ActionEvent with an event id DateChooserAction.calendarButtonSelected. The
 * selected day itself can be retrieved by using getSource to get a reference to
 * the DaySelector instance and then looking at selectedDay. For a detailed
 * example please look at DateChooser.
 * 
 * @author Will Roethel
 * @version $Revision: 1.2 $
 */
public class DaySelector extends DateChooserPanel {

    // Vector to store the labels containing the individual days.
    private Vector                          dayLabels            = new Vector();

    /**
     * Locale dependent offset for the first day of the week.
     */
    private int                             firstDayOfWeekOffset = 0;

    /**
     * Define the default font used in the lables. Needs to be disposed when
     * this widget is disposed.
     */
    private Font                            defaultWeekdayFont   = new Font(Display.getCurrent(), "*", 8, SWT.BOLD);

    /**
     * The style definitions used for drawing the day selector
     */
    private DaySelectorStyle                style;

    /**
     * The selected day.
     */
    public int                              selectedDay          = -1;

    /**
     * The common instance of the mouse adapter used for all days.
     */
    private DaySelectorButton_mouseListener dayMouseListener     = new DaySelectorButton_mouseListener(this);


    /**
     * Constructor overriding the default style.
     * 
     * @param newStyle DaySelectorStyle
     */
    public DaySelector(Composite parent) {
        super(parent, SWT.LEFT_TO_RIGHT);
        style = new DaySelectorStyle();
        init();
    }


    /**
     * Constructor providing the style class.
     * 
     * @param parent
     * @param style
     */
    public DaySelector(Composite parent, DaySelectorStyle style) {
        super(parent, SWT.LEFT_TO_RIGHT);
        this.style = style;
        init();
    }


    // Common initialization tasks.
    private void init() {

        // dispose all the graphics resources.
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                defaultWeekdayFont.dispose();
            }
        });

        // out common default settings
        GridLayout thisLayout = new GridLayout(7, true);
        thisLayout.horizontalSpacing = 0;
        thisLayout.verticalSpacing = 0;
        thisLayout.marginWidth = 0;
        thisLayout.marginHeight = 0;
        setLayout(thisLayout);
    }


    /**
     * Draw the day selector in a grid layout.
     * 
     * @param currentDate Calendar
     */
    public void paintCalendarPane(Calendar currentDate) {
        selectedDay = -1;
        firstDayOfWeekOffset = currentDate.getFirstDayOfWeek();

        // add the weekday labels if requested
        if (style.showWeekdays) {
            paintWeekdayLabels(firstDayOfWeekOffset);
        }

        // create the day labels.
        for (int iDay = 1; iDay <= 42; iDay++) {
            Label thisDay = new Label(this, SWT.CENTER);
            thisDay.setAlignment(SWT.CENTER);
            thisDay.setLayoutData(new GridData(GridData.FILL_BOTH));
            // thisDay.setFont(defaultDayFont);
            dayLabels.addElement(thisDay);
        }
    }


    /**
     * Add the weekday labels
     */
    private void paintWeekdayLabels(int firstDayOfWeekOffset) {
        // write the weekday labels
        String[] weekDay = style.getWeekdayNames();
        for (int iDay = 0; iDay < 7; iDay++) {
            Label thisWeekday = new Label(this, SWT.CENTER);
            thisWeekday.setFont(defaultWeekdayFont);
            thisWeekday.setLayoutData(new GridData(GridData.FILL_BOTH));
            style.setStyle(thisWeekday, DaySelectorStyle.WEEKDAYLABEL);
            int index = iDay + firstDayOfWeekOffset - 1;
            if (index > 6) {
                index = index - 7;
            }
            thisWeekday.setText(weekDay[index]);
        }
    }


    /**
     * Add the blank labels before and after the days of the current month.
     * 
     * @param firstDay int
     * @param noOfDays int
     * @param blankLabelIterator Iterator
     */

    private void addBlankLabels(int firstDay, int noOfDays, Iterator dayLabelIterator) {
        int currentDay = firstDay;
        Label blankLabel;
        for (int iDay = 1; iDay <= noOfDays; iDay++) {
            blankLabel = (Label) dayLabelIterator.next();
            style.setStyle(blankLabel, DaySelectorStyle.BLANKLABEL);
            blankLabel.setText("" + currentDay);
            currentDay++;
        }
    }


    /**
     * Action taken when a day is selected. This fires an ActionEvent with id
     * DateChooserAction.calendarButtonSelected.
     * 
     * @param mouseEvent MouseEvent
     */
    public void daySelectorButton_mouseReleased(MouseEvent mouseEvent) {
        int iDay = -1;
        try {
            iDay = Integer.parseInt(((Label) mouseEvent.getSource()).getText().trim());
        } catch (NumberFormatException e) {
            // ignore
        }
        selectedDay = iDay;
        Event event = new Event();
        event.widget = this;
        event.detail = DateChooserAction.calendarButtonSelected;
        SelectionEvent selectionEvent = new SelectionEvent(event);
        sendSelectionEvent(selectionEvent);
    }


    public void updateCalendar(Calendar currentDate, Calendar selectedDate) {
        // remove all selection listeners from all day labels
        for (int i = 0; i < dayLabels.size(); i++) {
            ((Label) dayLabels.elementAt(i)).removeMouseListener(dayMouseListener);
        }

        // thisMonth is a utility calendar instance to build the
        // calendar panel around the currently selected calendar value
        Calendar thisMonth = (Calendar) currentDate.clone();
        int lastDay = thisMonth.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        // last day of last month
        thisMonth.add(java.util.Calendar.MONTH, -1);
        int lastDayOfLastMonth = thisMonth.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        // reset thisMonth to the original value
        thisMonth.add(java.util.Calendar.MONTH, +1);

        // get the weekday of the first of this month
        thisMonth.set(java.util.Calendar.DAY_OF_MONTH, 1);
        int firstOfMonth = thisMonth.get(java.util.Calendar.DAY_OF_WEEK);

        // get an iterator over all our day labels and update them one by one.
        Iterator dayLabelIterator = dayLabels.iterator();

        // add blanks until the first of the month...
        int labelsToDraw = firstOfMonth - firstDayOfWeekOffset;
        if (labelsToDraw < 0) {
            labelsToDraw = labelsToDraw + 7;
        }
        if (labelsToDraw > 0) {
            int startDay = lastDayOfLastMonth - labelsToDraw - 1;
            addBlankLabels(startDay, labelsToDraw, dayLabelIterator);
        }

        // now start the counting for the current month
        Calendar calNow = Calendar.getInstance();
        boolean isCurrentMonth = false;
        if ((calNow.get(Calendar.YEAR) == thisMonth.get(Calendar.YEAR))
                && (calNow.get(Calendar.MONTH) == thisMonth.get(Calendar.MONTH))) {
            isCurrentMonth = true;
        }

        // ////
        boolean isSelectedMonth = false;
        if (selectedDate != null) {
            if ((thisMonth.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR))
                    && (thisMonth.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH))) {
                isSelectedMonth = true;
            }
        }

        // add the labels for the current month
        Label thisDay;
        for (int iDay = 1; iDay <= lastDay; iDay++) {
            thisDay = (Label) dayLabelIterator.next();
            if (isCurrentMonth && iDay == calNow.get(Calendar.DAY_OF_MONTH)) {
                style.setStyle(thisDay, DaySelectorStyle.TODAYLABEL);
            } else if (isSelectedMonth && iDay == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                style.setStyle(thisDay, DaySelectorStyle.SELECTEDLABEL);
            }

            else {
                style.setStyle(thisDay, DaySelectorStyle.DAYLABEL);
            }
            thisDay.setText("" + iDay);
            thisDay.addMouseListener(dayMouseListener);
        }

        // add remaining blanks - max. days in calendar is 42 (6 weeks)
        int noOfDays = 42 - lastDay - labelsToDraw;
        addBlankLabels(1, noOfDays, dayLabelIterator);
    }
}

/**
 * Common mouse adapter class used by all selectable day labels.
 * 
 * @author Will Roethel
 * @version $Revision: 1.2 $
 */
class DaySelectorButton_mouseListener implements MouseListener {

    private DaySelector adaptee;


    DaySelectorButton_mouseListener(DaySelector adaptee) {
        this.adaptee = adaptee;
    }


    public void mouseUp(MouseEvent mouseEvent) {
        adaptee.daySelectorButton_mouseReleased(mouseEvent);
    }


    public void mouseDoubleClick(MouseEvent mouseEvent) {
    }


    public void mouseDown(MouseEvent mouseEvent) {
    }
}
