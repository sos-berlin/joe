package net.cwroethel.swt.popupcalendar;

import org.eclipse.swt.widgets.Control;

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
 * Adapter to allow simple overriding of defaults. Use
 *  PopupCalendar popupCal = new PopupCalendar(shell, style);
 *  ...
 *  popupCal.setMonthSelectorStyle( new MonthSelectorStyleAdapter() {
 *     public void overrideStyle(Control control, int style) {
 *       switch (style) {
 *         case MonthSelectorStyle.PREVIOUSMONTHBUTTON:
 *           // button used for selecting the previous month
 *           break;
 *
 *         case MonthSelectorStyle.NEXTMONTHBUTTON:
 *           // button used for selecting the next month
 *           break;
 *
 *         case MonthSelectorStyle.MONTHNAMELABEL:
 *           // label used for showing the current month and year.
 *           control.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
 *           break;
 *       }
 *     }
 *  });
 * </pre>
 * @author W. Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 */
public class MonthSelectorStyleAdapter extends MonthSelectorStyle {

  public void overrideStyle(Control component, int style) {
  }
}
