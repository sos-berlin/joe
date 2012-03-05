package net.cwroethel.swt.popupcalendar;

import net.cwroethel.swt.popupcalendar.*;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Button;

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
 * Define the style of the 'Today Button'. 
 * This class is meant to be inherited to define custom styles.
 * The preferred way to do that is to inherit constructors and all methods from 
 * this class and just implement the overrideStyle() class. This allows the definition
 * of personal settings without having to deal with the internals of the style class.
 * For additional conveniance a TodaySelectorStyleAdapter is provided.
 * @author not attributable
 * @version 1.0
 */
public class TodaySelectorStyle extends DateChooserStyle {
  public static final int TODAYBUTTON = 1;

  protected final void setDefaultStyle(Control component, int style) {
    switch (style) {
      case TODAYBUTTON:
        ((Button) component).setText("Today");
        break;
    }
  }

}
