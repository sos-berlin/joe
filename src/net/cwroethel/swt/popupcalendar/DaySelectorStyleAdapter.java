package net.cwroethel.swt.popupcalendar;

import net.cwroethel.swt.popupcalendar.DaySelectorStyle;

import org.eclipse.swt.widgets.*;

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
 * Adapter to allow for simple customization of labels used to display
 * the weekdays, the current days of the month, the current day and the
 * remaining days of the previous and next month.
 *<pre>
 *  PopupCalendar popupCal = new PopupCalendar(shell, style);
 *  ...
 *  popupCal.setDaySelectorStyle( new DaySelectorStyleAdapter() {
 *     public void overrideStyle(Control control, int style) {
 *       switch (style) {
 *         case DaySelectorStyle.WEEKDAYLABEL:
 *           // labels showing the weekday names
 *           break;
 *
 *         case DaySelectorStyle.BLANKLABEL:
 *           // labels used for the remaining days of the previous and next month
 *           break;
 *
 *         case DaySelectorStyle.DAYLABEL:
 *           // override the labels for the days of the current month
 *           control.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
 *           break;
 *
 *         case DaySelectorStyle.TODAYLABEL:
 *           // override the label showing the current day of the month
 *           break;
 *       }
 *     }
 *  });
 * </pre>
 * @author W. Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 */
public abstract class DaySelectorStyleAdapter extends DaySelectorStyle {

  /**
   * Method used to override the default settings.
   * @param component Control
   * @param style int
   */
    
  public void overrideStyle(Control component, int style) {
  }
}
