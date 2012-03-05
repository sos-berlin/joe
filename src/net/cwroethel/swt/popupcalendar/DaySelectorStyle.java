package net.cwroethel.swt.popupcalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Display;

import net.cwroethel.swt.popupcalendar.*;

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
 * Style Class containing the default style for the GUI components used in
 * the DaySelector as well as accessors to the component templates. 
 * This class is meant to be extended by users longing to set their own definitions.
 * The preferred way to do that is to inherit constructors and all methods from 
 * this class and just implement the overrideStyle() class. This allows the definition
 * of personal settings without having to deal with the internals of the style class.
 * For additional conveniance a DaySelectorStyleAdapter is provided.
 * 
 * @author Will Roethel
 * @version $Revision: 1.2 $
 */
public class DaySelectorStyle extends DateChooserStyle {
  /**
   * The style identifiers for the different MonthSelector components
   */
  public final static int WEEKDAYLABEL = 1;
  public final static int BLANKLABEL = 2;
  public final static int DAYLABEL = 3;
  public final static int TODAYLABEL = 4;
  public final static int SELECTEDLABEL = 5;
  

  /**
   * True if the weekday labels should be shown.
   */
  public boolean showWeekdays = true;

  /**
   * String[] containing the names of the weekdays to be shown.
   */
  protected String[] weekdayName = null;
  protected static String[] weekdayNameDefault = {
      "S", "M", "T", "W", "T", "F", "S"};

  
  /**
   * Set the default values
   */
  protected final void setDefaultStyle(Control control, int style) {
    Label label = (Label)control;
    ((GridData)label.getLayoutData()).widthHint = daySize;
    ((GridData)label.getLayoutData()).heightHint = daySize;

    switch(style) {
      case WEEKDAYLABEL:
        label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        break;

      case BLANKLABEL:
        label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        // label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
        label.setForeground(Display.getCurrent().getSystemColor(
            SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
        break;

      case DAYLABEL:
        label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        break;

      case SELECTEDLABEL:
        label.setBackground(Display.getCurrent().getSystemColor(
            SWT.COLOR_WIDGET_NORMAL_SHADOW));
        label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));       
        break;
      
      case TODAYLABEL:
        label.setBackground(Display.getCurrent().getSystemColor(
            SWT.COLOR_GRAY));
        label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));       
        break;
    }
  }

  /**
   * Get the weekday names used for the weekday labels.
   * @param dayOfTheWeek int
   * @return String
   */
  public final String getWeekdayName(int dayOfTheWeek) {
      return weekdayName[dayOfTheWeek];
  }

  /**
   * Get the array containing the weekday names in the current locale.
   * @return String[]
   */
  public final String[] getWeekdayNames() {
    if (weekdayName == null) {
      try {
        setWeekdayNames(CalendarNamesUtil.getWeekdayNames(locale));
      }
      catch (Exception e) {
        weekdayName = weekdayNameDefault;
      }
    }
    return weekdayName;
  }


  /**
   * Override the default weekday names with a new set. The array must be
   * of length 7 otherwise setWeekdayNames will throw an exception.
   * @param weekdays String[]
   * @throws Exception
   */

  public final void setWeekdayNames(String[] weekdays) throws Exception {
    if (weekdays.length != 7) {
      throw new Exception("Expected String[] with length 7. Found length "
                          + weekdays.length + ". Keeping the old settings.");
    }
    weekdayName = weekdays;
  }
}
