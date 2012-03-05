package net.cwroethel.swt.popupcalendar;

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
 * Actions defined and used by the various components of DateChooser.
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.1 $
 */
public class DateChooserAction {

  /**
   * Empty event.
   */
  public static final int nullEvent = 0;

  /**
   * Indicates that next month button of the month selector has been selected.
   */
  public static final int nextMonthSelected = 1;

  /**
   * Indicates that the previous month button of the month selector has been
   * selected.
   */
  public static final int previousMonthSelected = 2;

  /**
   * The 'Today' button of the today selector has been pressed.
   */
  public static final int todaySelected = 3;

  /**
   * A day of the day selector has been selected.
   */
  public static final int calendarButtonSelected = 4;

  /**
   * A date has been selected. Event fired by PopupCalendar.
   */
  public static final int dateSelected = 5;
}
