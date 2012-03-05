package net.cwroethel.swt.popupcalendar;

import java.util.Calendar;
import java.util.Locale;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.SWT;

import net.cwroethel.swt.popupcalendar.*;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionAdapter;

/*******************************************************************************
* Copyright (c) 2005 Will Roethel.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Will Roethel - initial API and implementation
*******************************************************************************/

/**
 * <p>Panel containing the actual calendar. Can also be used independent
 * of JPopupCalendar just by creating a DateChooser and calling the
 * paint() method.</p>
 * <p>DateChooser is made up of three parts:
 * <ul>
 * <li> The month selector, which contains the two buttons to select between
 *  months separated by a label showing the current month and year.</li>
 * <li> The day selector displaying the days of the current month in a grid
 * layout.</li>
 * <li> And a button to quickly select todays date (the today selector).</li>
 * </ul>
 * The three components are independent and can be turned on or off preferrably 
 * using the style options PopupCalendar.SHOWMONTH, PopupCalendar.SHOWTODAY, 
 * PopupCalendar.SHOWALL. Rearranging
 * the components as for the Swing version of DateChooser is currently not
 * possible.
 * DateChooser can be used as a standalone widget:
 * <pre>
 *   ...
 *   final DateChooser dateChooser = new DateChooser(contentsPane,
 *     PopupCalendar.SHOWALL | SWT.CENTER);
 *   ... // you could customize dateChooser here)
 *   dateChooser.paint();
 *   ...
 *   // read out the selected date - could be null if no date was selected.
 *   Calendar selectedDate = dateChooser.getDate();
 * </pre>
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 */

public class DateChooser extends DateChooserPanel {

  private Composite contentsPane;

  /**
   * If true the month selector is shown.
   */
  private boolean showMonthSelector = false;
  
  /**
   * If true the today selector is shown.
   */
  private boolean showTodaySelector = false;


  // the graphical components of the selector
  private TodaySelector todaySelector = null;
  private MonthSelector monthSelector = null;
  private DaySelector daySelector = null;

  // the style classes for the components
  private TodaySelectorStyle todaySelectorStyle = null;
  private MonthSelectorStyle monthSelectorStyle = null;
  private DaySelectorStyle daySelectorStyle = null;

  // current calendar value used in the selection
  private Calendar currentDate = null;
  private Calendar selectedDate = null;

  // the locale to be used
  private Locale locale = null;

  /**
   * Default constructor. Style options are SHOWMONTH, SHOWTODAY OR SHOWALL and
   * all options supported by Composite. The default locale will be used.
   * @param parent Composite
   * @param style int
   */
  public DateChooser(Composite parent, int style) {
    this(parent, style, Locale.getDefault());
  }

  /**
   * Constructor setting a specific locale to manage looks and language.
   * @param parent Composite
   * @param style int
   * @param locale Locale
   */
  public DateChooser(Composite parent, int style, Locale locale) {
    super(parent, style);
    this.locale = locale;
    currentDate = Calendar.getInstance(locale);
    setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
    setLayout(new GridLayout(1, false));
    setStyle(style);
  }


  // resolve the style. 
  private void setStyle(int style) {
    if ( (style & PopupCalendar.SHOWMONTH) == PopupCalendar.SHOWMONTH) {
      showMonthSelector(true);
    }
    if ((style & PopupCalendar.SHOWTODAY) == PopupCalendar.SHOWTODAY) {
      showTodaySelector(true);
    }
  }
  
  /**
   * Show a month selector at the top of the calendar widget.
   * @param bool
   */
  public void showMonthSelector(boolean bool) {
    showMonthSelector = bool;
  }
  
  /**
   * Display a 'Today' button at the bottom of the calendar widget.   
   * @param bool
   */
  public void showTodaySelector(boolean bool) {
    showTodaySelector = bool;
  }
  
  
  /**
   * Set the start date for this calendar.
   * @param currentDate Calendar
   */
  public void setDate(Calendar currentDate) {
    this.currentDate = currentDate;
  }

  /**
   * Create and pack the GUI components into the container. This is separated
   * from the constructor since the style definitions can be changed to
   * customize the look and feel. All style changes have to be applied before
   * calling pack for these to be visible. The components are only created once and
   * reused by re-labeling and rearranging listeners. This makes updating about 
   * 6-7 times faster.
   */
  public void paint() {
    if (contentsPane == null) {
      contentsPane = new Composite(this, SWT.LEFT_TO_RIGHT);
      contentsPane.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
      try {
        initCalendarPane();
      }
      catch (Exception exception) {
        exception.printStackTrace();
      }
    }
    
    // update the graphical components. Set the labels and listeners.
    updateCalendar();
  }


  /**
   * Get access to the style definitions for the Today button.
   * @return TodaySelectorStyle
   */
  public TodaySelectorStyle getTodaySelectorStyle() {
    if (todaySelectorStyle == null) {
      todaySelectorStyle = new TodaySelectorStyle();
    }
    return todaySelectorStyle;
  }

  
  /**
   * Get the class instance defining the style of the month selector.
   * @return MonthSelectorStyle
   */
  public MonthSelectorStyle getMonthSelectorStyle() {
    if (monthSelectorStyle == null) {
      monthSelectorStyle = new MonthSelectorStyle();
    }
    return monthSelectorStyle;
  }

  /**
   * Get the class instance defining the settings for the day selector.
   * @return DaySelectorStyle
   */
  public DaySelectorStyle getDaySelectorStyle() {
    if (daySelectorStyle == null) {
      daySelectorStyle = new DaySelectorStyle();
    }
    return daySelectorStyle;
  }


  /**
   * Get the monthSelector panel if existing. Can be null.
   * @return MonthSelector
   */
  public MonthSelector getMonthSelector() {
    return monthSelector;
  }


  /**
   * Get the daySelector panel if existing. Can be null.
   * @return DaySelector
   */
  public DaySelector getDaySelector() {
    return daySelector;
  }


  /**
   * Get the todaySelector panel if existing. Can be null.
   * @return TodaySelector
   */
  public TodaySelector getTodaySelector() {
    return todaySelector;
  }



  /**
   * Resets the selected and current dates. The current date is the date
   * currently displayed in the calendar. The selected date is the date
   * selected by the user.
   */
  public void resetDates() {
    selectedDate = null;
    currentDate = Calendar.getInstance();
  }

  /**
   * Reset the selected date to null. Usefull when an instance of DateChooser
   * is used several times but not expected to store a selected date between
   * uses.
   */
  public void resetSelectedDate() {
    selectedDate = null;
  }


  /**
   * Override the style settings for the today button with a set of user
   * defined settings.
   * @param selectorStyle TodaySelectorStyle
   */
  public void setTodaySelectorStyle(TodaySelectorStyle selectorStyle) {
    todaySelectorStyle = selectorStyle;
  }

  /**
   * Override the style settings for the today button with a set of user
   * defined settings.
   * @param selectorStyle MonthSelectorStyle
   */
  public void setMonthSelectorStyle(MonthSelectorStyle selectorStyle) {
    monthSelectorStyle = selectorStyle;
  }

  /**
   * Override the day selector styles with a user defined new selector.
   * @param selectorStyle DaySelectorStyle
   */
  public void setDaySelectorStyle(DaySelectorStyle selectorStyle) {
    daySelectorStyle = selectorStyle;
  }

  /**
   * Initializes the calendar panel and draws all components. This is only needed
   * the first time the calendar is created. All successive uses will update
   * existing calendar components.
   * @throws Exception
   */
  private void initCalendarPane() throws Exception {

    // set layout defaults
    FormLayout thisLayout = new FormLayout();
    thisLayout.marginHeight = 0;
    thisLayout.marginWidth = 0;
    contentsPane.setLayout(thisLayout);

    // update or set the locale
    getMonthSelectorStyle().setLocale(locale);
    getDaySelectorStyle().setLocale(locale);

    // set up the month selector panel
    if (showMonthSelector) {
      monthSelector = new MonthSelector(contentsPane, getMonthSelectorStyle());
      String yearString = " " + currentDate.get(java.util.Calendar.YEAR);
      monthSelector.setMonthName(getMonthSelectorStyle().getMonthName(
          currentDate.get(java.util.Calendar.MONTH)) + yearString);
      
      // add the listener
      monthSelector.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          if (e.detail == DateChooserAction.previousMonthSelected) {
            currentDate.add(java.util.Calendar.MONTH, -1);
            paint();
          }
          if (e.detail == DateChooserAction.nextMonthSelected) {
            currentDate.add(java.util.Calendar.MONTH, 1);
            paint();
          }
        }
      });
    }

    // day selector is put into an additional Composite to make the layout look
    // better. I.e. the month selector needs to be a bit wider than the days
    // in the day selector.
    
    Composite dayPane = new Composite(contentsPane, SWT.LEFT_TO_RIGHT);
    dayPane.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
    GridLayout dayPaneLayout = new GridLayout(1, true);
    dayPaneLayout.marginHeight = 0;
    dayPaneLayout.marginWidth = 8;
    dayPane.setLayout(dayPaneLayout);

    // set up the calendar panel - put everything into the new Composite.
    daySelector = new DaySelector(dayPane, getDaySelectorStyle());
    daySelector.paintCalendarPane(currentDate);
    
    // add the listener
    daySelector.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        if (e.detail == DateChooserAction.calendarButtonSelected) {
          currentDate.set(java.util.Calendar.DAY_OF_MONTH,
              ( (DaySelector) e.getSource()).selectedDay);
          selectedDate = (Calendar) currentDate.clone();
          sendSelectionEvent(null);
          updateCalendar();
        }
      }
    });
    

    // add the today selector panel if needed.
    if (showTodaySelector) {
      todaySelector = new TodaySelector(contentsPane, getTodaySelectorStyle());
      
      // add the listener
      todaySelector.addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          if (e.detail == DateChooserAction.todaySelected) {
            currentDate.setTime(Calendar.getInstance().getTime());
            selectedDate = (Calendar) currentDate.clone();
            sendSelectionEvent(null);
            updateCalendar();
          }
        }
      });
    }

    
    // we have to layout the calendar after everything else is set up 
    // since we don't know which components we need in the layout.
    FormData daySelectorData = new FormData();
    daySelectorData.right = new FormAttachment(100, 0);
    daySelectorData.left = new FormAttachment(0, 0);
    // daySelector.setLayoutData(daySelectorData);
    dayPane.setLayoutData(daySelectorData);

    // attach the month selector to the top if it should be shown.
    if (showMonthSelector) {
      FormData monthSelectorData = new FormData();
      monthSelectorData.top = new FormAttachment(0, 0);
      monthSelectorData.right = new FormAttachment(100, 0);
      monthSelectorData.left = new FormAttachment(0, 0);
      monthSelector.setLayoutData(monthSelectorData);
      daySelectorData.top = new FormAttachment(monthSelector, 0);
    }

    // put the today selector on the bottom.
    if (showTodaySelector) {
      FormData todaySelectorData = new FormData();
      todaySelectorData.bottom = new FormAttachment(100, 0);
      todaySelectorData.left = new FormAttachment(25, 0);
      todaySelectorData.right = new FormAttachment(75, 0);
      // todaySelectorData.height = 30;
      todaySelectorData.top = new FormAttachment(dayPane, 0, SWT.BOTTOM);
      // todaySelectorData.top = new FormAttachment(daySelector, 0, SWT.BOTTOM);
      todaySelector.setLayoutData(todaySelectorData);
      // daySelectorData.bottom = new FormAttachment(todaySelector);
    }
  }
  
  
  /**
   * Update the view to display settings for the new date.
   */
  public void updateCalendar() {
    setVisible(false);
    if (monthSelector != null) {
      String yearString = " " + currentDate.get(java.util.Calendar.YEAR);
      monthSelector.setMonthName(getMonthSelectorStyle().getMonthName(
          currentDate.get(java.util.Calendar.MONTH)) + yearString);
    }   
    daySelector.updateCalendar(currentDate, selectedDate);    
    setVisible(true);
  }


  /**
   * Returns the selected date.
   * @return Calendar
   */
  public Calendar getDate() {
    return (Calendar) selectedDate;
  }
}

