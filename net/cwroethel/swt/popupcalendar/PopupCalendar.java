package net.cwroethel.swt.popupcalendar;

import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/*******************************************************************************
 * Copyright (c) 2005 Will Roethel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Will Roethel -
 * initial API and implementation
 ******************************************************************************/

/**
 * <p>
 * A date picker popup window. To use it is to first create the popup calendar:<br>
 * 
 * <pre>
 *   ...
 *    PopupCalendar popupCal = new PopupCalendar(shell, PopupCalendar.SHOWALL);
 *    ...
 * </pre>
 * 
 * or explicitly providing a locale
 * 
 * <pre>
 *    PopupCalendar popupCal = new PopupCalendar(shell, PopupCalendar.SHOWALL,
 *                                               Locale.FRENCH);
 *    ...
 * </pre>
 * 
 * The window itself is opened using the open() method e.g. with:
 * 
 * <pre>
 *    ...
 *    popupCal.open(shell, x, y);
 *    ...
 * </pre>
 * 
 * to display the calendar, e.g. in response to a mouse click. The selected date
 * can then be retrieved by calling<br>
 * 
 * <pre>
 *    ...
 *    Calendar selectedDate = popupCalendar.getDate();
 *    if (selectedDate != null) {
 *       ... do something
 *    }
 *    ...
 * </pre>
 * 
 * The selectedDate has to be checked since it can be undefined (null) if no
 * date has been selected.
 * </p>
 * <p>
 * PopupCalendar support SelectionListeners / SelectionEvents to nofify that a
 * date has been selected. Note however that since PopupCalendar itself is not a
 * widget, it can not be the official source of a SelectionEvent. PopupCalendar
 * simply forwards SelectionEvents origining from the underlying DateChooser.
 * </p>
 * <p>
 * The PopupCalendar creates a new shell with the shell provided in the
 * constructor as parent. If the PopupCalendar is not used anymore it should be
 * disposed either by calling the dispose() method or by retrieving the shell
 * with getShell() and calling dispose() on that.
 * </p>
 * 
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 */
public class PopupCalendar {

    // the popup window
    private Shell           shell;

    // keep track of the selection listeners.
    protected Vector        selectionListeners = new Vector();

    // store the default style definitions passed on in the constructor.
    private int             defaultStyle       = 0;

    // Locale locale = Locale.getDefault();
    private DateChooser     datePane           = null;

    // Style definitions used throughout this package
    /**
     * If set shows the current month in the selector. Used as style option for
     * DateChooser.
     */
    public static final int SHOWMONTH          = 1;

    /**
     * If set displays the Today Button at the bottom of the calendar widget.
     * Used as style option for DateChooser.
     */
    public static final int SHOWTODAY          = 2;

    /**
     * Show month selector and today button.
     */
    public static final int SHOWALL            = SHOWMONTH | SHOWTODAY;

    /**
     * Align the popup window with the left edge of a composite. Equivalent to
     * SWT.LEFT.
     */
    public final static int LEFT               = SWT.LEFT;

    /**
     * Align the popup window with the right edge of a composite (specified in
     * open()). Equivalent to SWT.RIGHT.
     */
    public final static int RIGHT              = SWT.RIGHT;

    /**
     * Align the popup window to be centered with the composite specified in
     * open().
     */
    public final static int CENTER             = SWT.CENTER;

    /**
     * Obsolete, kept for backward compatibility. Use LEFT or SWT.LEFT instead.
     */
    public final static int ALIGN_LEFT         = LEFT;

    /**
     * Obsolete, kept for backward compatibility. Use RIGHT or SWT.RIGHT
     * instead.
     */
    public final static int ALIGN_RIGHT        = RIGHT;

    /**
     * Obsolete, kept for backward compatibility. Use CENTER or SWT.CENTER
     * instead.
     */
    public final static int ALIGN_CENTER       = CENTER;


    /**
     * Construct a popup calendar providing the parent shell and a specific
     * locale.
     * 
     * @param parent Shell
     * @param locale Locale
     */
    public PopupCalendar(Shell parent, int style, Locale locale) {

        shell = new Shell(parent, SWT.LEFT_TO_RIGHT);
        defaultStyle = style;

        // close dialog if user selects outside of the shell
        shell.addListener(SWT.Deactivate, new Listener() {
            public void handleEvent(Event e) {
                // shell.setVisible(false);
            }
        });

        // set the locale and initialize the DateChooser panel
        datePane = new DateChooser(shell, style | SWT.LEFT_TO_RIGHT, locale);
        datePane.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                PopupCalendar.this.sendSelectionEvent(e);
                shell.setVisible(false);
            }
        });
    }


    /**
     * Create a popup calendar for a given shell using the default locale.
     * 
     * @param shell Shell
     */
    public PopupCalendar(Shell parent, int style) {
        this(parent, style, Locale.getDefault());
    }


    /**
     * Draw the popup window at origin x, y measured in the same reference frame
     * as control (i.e. control's parent frame). If the popup window does not
     * fully fit in the screen, attempts will be made to reposition it to make
     * it fit.
     * 
     * @param control Control
     * @param x int
     * @param y int
     */
    public void open(Control control, int x, int y) {
        datePane.paint();
        datePane.pack();

        // calculate the position of the popup window
        Point controlOrig = getAbsoluteLocation(control);
        int xAbs = x + controlOrig.x;
        int yAbs = y + controlOrig.y;
        if (xAbs < 0) {
            xAbs = 0;
        }
        if (yAbs < 0) {
            yAbs = 0;
        }

        Point size = datePane.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
        Point pDraw = checkBounds(new Rectangle(xAbs, yAbs, size.x, size.y), controlOrig);

        // set the bounds
        shell.setBounds(pDraw.x, pDraw.y, size.x, size.y);
        shell.open();
        datePane.setFocus();

        Display display = shell.getDisplay();
        while (!shell.isDisposed() && shell.isVisible()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }


    public boolean isOpen() {
        return shell.isVisible();
    }


    public void close() {
        shell.setVisible(false);
    }


    /**
     * Open the popup window relative to a given control. The popup window will
     * be positioned below the control if it fits on the screen and above the
     * control otherwise, and aligned either with the left edge (LEFT), right
     * edge (RIGHT), or centered (CENTER) below or above the control.
     * 
     * @param control Control
     * @param style int
     */
    public void open(Control control, int style) {
        datePane.paint();
        datePane.pack();

        // calculate the position of the popup window
        Point size = datePane.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);

        Point controlOrig = getAbsoluteLocation(control);
        Point controlSize = control.getSize();
        int yAbs = controlOrig.y + controlSize.y;
        int xAbs = 0;

        switch (style) {
            case SWT.LEFT:
                xAbs = controlOrig.x;
                break;

            case SWT.RIGHT:
                xAbs = controlOrig.x + controlSize.x - size.x;
                break;

            case SWT.CENTER:
                xAbs = controlOrig.x + controlSize.x / 2 - size.x / 2;
                break;

            default:
                xAbs = controlOrig.x;
                break;
        }

        // check boundaries
        if (xAbs < 0) {
            xAbs = 0;
        }
        if (yAbs < 0) {
            yAbs = 0;
        }

        // check the screen size
        Point pDraw = checkBounds(new Rectangle(xAbs, yAbs, size.x, size.y), controlOrig);

        // set the bounds
        shell.setBounds(pDraw.x, pDraw.y, size.x, size.y);
        shell.open();
        // datePane.getTodaySelector().setFocus();

        Display display = shell.getDisplay();
        while (!shell.isDisposed() && shell.isVisible()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }


    /**
     * Open the popup window using the default style defined in the constructor.
     * 
     * @param control
     */
    public void open(Control control) {
        open(control, defaultStyle);
    }


    /**
     * Check if the popup window fits on the screen.
     * 
     * @param rect Rectangle
     * @param orig Point
     * @return Point
     */
    private Point checkBounds(Rectangle rect, Point orig) {
        int x = rect.x;
        int y = rect.y;
        int xMax = x + rect.width;
        int yMax = y + rect.height;

        // get the screen size
        Rectangle screenSize = shell.getDisplay().getBounds();

        // make window popup above control if the
        // bottom of the popup window is off-screen and fits on top.
        if ((yMax > (screenSize.height - 30)) && ((orig.y - rect.height) > 30)) {
            y = orig.y - rect.height;
            yMax = y + rect.height;
        }

        if ((xMax > (screenSize.width - 30)) && ((screenSize.width - rect.width) > 30)) {
            x = screenSize.width - 30 - rect.width;
            xMax = x + rect.width;
        }
        return new Point(x, y);
    }


    /**
     * Calculate the absolute screen position of the origin of the control.
     * 
     * @param control Control
     * @return Point
     */
    public Point getAbsoluteLocation(Control control) {
        Point orig = control.getLocation();
        while (control.getParent() != null) {
            control = control.getParent();
            orig.x = orig.x + control.getLocation().x;
            orig.y = orig.y + control.getLocation().y;

            // if shell then we have to add the client area
            if (Shell.class.isInstance(control)) {
                int frame = (control.getSize().x - ((Shell) control).getClientArea().width) / 2;
                orig.x = orig.x + frame;
                orig.y = orig.y + control.getSize().y - ((Shell) control).getClientArea().height - frame;
                break;
            }
        }
        return orig;
    }


    /**
     * Reset date displayed in the calendar to the current date and reset the
     * selected date. Useful when re-using an existing instance of
     * PopupCalendar.
     */
    public void resetDates() {
        datePane.resetDates();
    }


    /**
     * reset the selected date. Useful when re-using an existing instance of
     * PopupCalendar.
     */
    public void resetSelectedDate() {
        datePane.resetSelectedDate();
    }


    /**
     * Returns the selected date. If no date has been selected and no Calendar
     * instance has been registered with JPopupCalendar, <tt>getDate</tt> can
     * return null.
     * 
     * @return Calendar
     */
    public Calendar getDate() {
        return datePane.getDate();
    }


    /**
     * Show the Today Selector Button.
     * 
     * @param showSelector boolean
     */
    public void showTodaySelector(boolean showSelector) {
        datePane.showTodaySelector(showSelector);
    }


    /**
     * Show the month selector. Only useful if the month is required to stay
     * fixed otherwise the calendar will not allow to change months.
     * 
     * @param showSelector boolean
     */
    public void showMonthSelector(boolean showSelector) {
        datePane.showMonthSelector(showSelector);
    }


    /**
     * Show the weekday names in the calendar.
     * 
     * @param showSelector boolean
     */
    public void showWeekdayLabels(boolean showSelector) {
        getDaySelectorStyle().showWeekdays = showSelector;
    }


    // setters

    /**
     * Override the month selector with a user defined new selector.
     * 
     * @param newStyle MonthSelectorStyle
     */
    public void setMonthSelectorStyle(MonthSelectorStyle newStyle) {
        datePane.setMonthSelectorStyle(newStyle);
    }


    /**
     * Override the day selector styles with a user defined new selector.
     * 
     * @param newStyle DaySelectorStyle
     */
    public void setDaySelectorStyle(DaySelectorStyle newStyle) {
        datePane.setDaySelectorStyle(newStyle);
    }


    /**
     * Override the style settings for the today button with a set of user
     * defined settings.
     * 
     * @param newStyle TodaySelectorStyle
     */
    public void setTodaySelectorStyle(TodaySelectorStyle newStyle) {
        datePane.setTodaySelectorStyle(newStyle);
    }


    /**
     * Set the startup date of the popup calendar.
     * 
     * @param date Calendar
     */
    public void setDate(Calendar date) {
        datePane.setDate(date);
    }


    // getters

    /**
     * Get the class instance defining the style of the month selector.
     * 
     * @return MonthSelectorStyle
     */
    public MonthSelectorStyle getMonthSelectorStyle() {
        return datePane.getMonthSelectorStyle();
    }


    /**
     * Get the class instance defining the settings for the day selector.
     * 
     * @return DaySelectorStyle
     */
    public DaySelectorStyle getDaySelectorStyle() {
        return datePane.getDaySelectorStyle();
    }


    /**
     * Get access to the style definitions for the Today button.
     * 
     * @return TodaySelectorStyle
     */
    public TodaySelectorStyle getTodaySelectorStyle() {
        return datePane.getTodaySelectorStyle();
    }


    /**
     * Get the date panel itself, i.e the full calendar panel that sits inside
     * the popup window.
     * 
     * @return DateChooser
     */
    public DateChooser getDateChooser() {
        return datePane;
    }


    /**
     * Simple pretend-copy of the generic SWT addSelectionListener.
     * 
     * @param listener
     */
    public void addSelectionListener(SelectionListener listener) {
        selectionListeners.addElement(listener);
    }


    /**
     * Simple pretend-copy of the generic SWT removeSelectionListener.
     * 
     * @param listener
     */
    public void removeSelectionListener(SelectionListener listener) {
        selectionListeners.removeElement(listener);
    }


    /**
     * Notify all selection listeners of the event.
     * 
     * @param eventType
     * @param selectionEvent
     */
    private void sendSelectionEvent(SelectionEvent selectionEvent) {
        if (selectionListeners == null) {
            return;
        }
        if (selectionEvent == null) {
            Event event = new Event();
            event.type = SWT.Selection;
            Display display = Display.getCurrent();
            event.display = display;
            event.widget = datePane;
            selectionEvent = new SelectionEvent(event);
        }
        for (int i = 0; i < selectionListeners.size(); i++) {
            SelectionListener listener = (SelectionListener) selectionListeners.elementAt(i);
            if (listener != null)
                listener.widgetSelected(selectionEvent);
        }
    }


    /**
     * Dispose the popup window and all it's children.
     */
    public void dispose() {
        shell.dispose();
    }


    /**
     * Return the shell used to display the popup window.
     * 
     * @return
     */
    public Shell getShell() {
        return shell;
    }

}
