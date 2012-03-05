package net.cwroethel.swt.popupcalendar;

import java.util.Vector;

import net.cwroethel.swt.popupcalendar.*;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.SWT;


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
 * DateChooserPanel is an abstract class used as container for the three
 * DateChooser component classes. If the components are used by DateChooser,
 * they are registered with an SelectionListener and, depending on the 
 * SelectionEvent detail (event.detail), the corresponding actions are taken.
 * <pre>   daySelector = new DaySelector(shell);
 *   daySelector.addSelectionListener(new SelectionAdapter() {
 *      public void widgetSelected(SelectionEvent e) {
 *         ... do something...
 *      }
 *    });
 * </pre>
 *
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 */
public abstract class DateChooserPanel extends Composite {
  protected Vector selectionListeners = new Vector();
  

  /**
   * Default constructor.
   * @param parent
   * @param style
   */
  public DateChooserPanel(Composite parent, int style) {
    super(parent, style);
  }
  
 
  /**
   * Add a SelectionListener.
   * @param listener
   */
  public void addSelectionListener(SelectionListener listener) {
    if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
    selectionListeners.addElement(listener);
  }
  

  /**
   * Copy of the generic error method defined in org.eclipse.swt.widgets.Widget. 
   */
  protected void error(int code) {
    SWT.error(code);
  }

  
  /**
   * Remove a SelectionListener.
   * @param listener
   */
  public void removeSelectionListener(SelectionListener listener) {
    selectionListeners.removeElement(listener);
  }


  /**
   * Notify all listeners of a selection.
   * @param selectionEvent
   */
  protected void sendSelectionEvent(SelectionEvent selectionEvent) {
    if (selectionListeners == null) {
      return;
    }
    if (selectionEvent == null) {
      Event event = new Event();
      event.type = SWT.Selection;
      Display display = Display.getCurrent();
      event.display = display;
      event.widget = this;
      selectionEvent = new SelectionEvent(event);
    }
    for (int i = 0; i<selectionListeners.size(); i++) {
      SelectionListener listener = 
        (SelectionListener) selectionListeners.elementAt(i);
      if (listener != null) listener.widgetSelected(selectionEvent);
    }
  }
}
