package net.cwroethel.swt.popupcalendar;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.SWT;

import java.io.InputStream;

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
 * The month selector contains two buttons scroll a month forward or backward
 * separated by a label showing the currently selected month name and year.
 * If one of the buttons has been pressed the month selector fires an
 * ActionEvent with event id DateChooserAction.previousMonth, or
 * DateChooserAction.nextMonth (depending on the button pressed).
 * The style of the buttons and the label can be defined by either
 * providing a custom instance of the MonthSelectorStyle class or
 * by getting access to that class and changing the default values for the
 * gui components there.
 *
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 */
public class MonthSelector extends DateChooserPanel {

  /**
   * The default font for the label containing the month name.
   * Needs to be disposed when this widget is disposed.
   */
  private final Font monthLabelFont = new Font(Display.getCurrent(), 
      "*", 8, SWT.BOLD);
  
  /**
   * Image used to decorate the next month selector button.
   */
  private Image nextMonthImage = null;
  
  /**
   * Image used to decorate the previous month selector button.
   */
  private Image previousMonthImage = null;
   
  
  /**
   * The instance of MonthSelectorStyle to be used for drawing this
   * MonthSelector.
   */
  public MonthSelectorStyle style = null;

  private Label monthNameLabel;

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
        monthLabelFont.dispose();
        
        if (previousMonthImage != null) {
          previousMonthImage.dispose();
        }
        if (nextMonthImage != null) {
          nextMonthImage.dispose();
        }
      }
    });    
    
    // set the widget defaults - some overkill here...
    GridLayout thisLayout = new GridLayout(3, false);
    thisLayout.horizontalSpacing = 0;
    thisLayout.verticalSpacing = 0;
    thisLayout.marginWidth = 0;
    thisLayout.marginHeight = 0;
    setLayout(thisLayout);
    setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));

    // define the label to select the previous month. This needs to be
    // a label since we want to change the label colors.
    Label previousMonthLabel = new Label(this, SWT.CENTER);
    previousMonthLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
    style.setStyle(previousMonthLabel, MonthSelectorStyle.PREVIOUSMONTHBUTTON);

    // define the label containing the current month and year.
    monthNameLabel = new Label(this, SWT.CENTER);
    // monthNameLabel.setBackground(monthLabelBackground);
    monthNameLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
    monthNameLabel.setFont(monthLabelFont);
    style.setStyle(monthNameLabel, MonthSelectorStyle.MONTHNAMELABEL);
    
    
    // label to select the next month. 
    Label nextMonthLabel = new Label(this, SWT.FLAT);
    nextMonthLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
    style.setStyle(nextMonthLabel, MonthSelectorStyle.NEXTMONTHBUTTON);


    // add mouse listeners
    // set up button for previous month
    Display display = Display.getCurrent();
    InputStream imageStream;
    try {
      imageStream = MonthSelector.class.getResource(
          style.previousMonthImageName).openStream();
      previousMonthImage = new Image(display, imageStream);
      if (previousMonthImage != null) {
        previousMonthLabel.setImage(previousMonthImage);
      }
    }
    catch (Exception e) {
      // ignore
    }
    previousMonthLabel.addMouseListener(new MouseAdapter() {
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
      imageStream = MonthSelector.class.getResource(
          style.nextMonthImageName).openStream();
      nextMonthImage = new Image(display, imageStream);
      if (nextMonthImage != null) {
       nextMonthLabel.setImage(nextMonthImage);
      }
    }
    catch (Exception e) {
      // ignore
    }
    nextMonthLabel.addMouseListener(new MouseAdapter() {
      public void mouseUp(MouseEvent mouseEvent) {
        Event event = new Event();
        event.widget = MonthSelector.this;
        event.detail = DateChooserAction.nextMonthSelected;
        SelectionEvent selectionEvent = new SelectionEvent(event);
        sendSelectionEvent(selectionEvent);
      }
    });
  }

  /**
   * Set the text to be displayed in the month label.
   * @param monthName String
   */
  public void setMonthName(String monthName) {
    if (monthNameLabel != null) {
      monthNameLabel.setText(monthName);
    }
  }
}
