package net.cwroethel.swt.popupcalendar;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/*******************************************************************************
 * Copyright (c) 2005 Will Roethel. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Will Roethel -
 * initial API and implementation
 ******************************************************************************/

/**
 * Class containing the style information to be used for drawing the month
 * selector. This class is meant to be inherited to define custom styles. The
 * preferred way to do that is to inherit constructors and all methods from this
 * class and just implement the overrideStyle() class. This allows the
 * definition of personal settings without having to deal with the internals of
 * the style class. For additional conveniance a MonthSelectorStyleAdapter is
 * provided.
 * 
 * @author Will Roethel, http://www.cwroethel.net
 * @version $Revision: 1.2 $
 */
public class MonthSelectorStyle extends DateChooserStyle {

    /**
     * The style identifiers for the different MonthSelector components
     */
    public static final int PREVIOUSMONTHBUTTON    = 1;

    public static final int NEXTMONTHBUTTON        = 2;

    public static final int MONTHNAMELABEL         = 3;

    // button decoration
    /**
     * Icon to be used as decoration for the button used to select the previous
     * month.
     */
    public String           previousMonthImageName = "Previous.gif";

    /**
     * Icon used as decoration for the button used to select the next month.
     */
    public String           nextMonthImageName     = "Next.gif";

    /**
     * Month names. Can be overridden using the setMonthNames method.
     */
    protected String[]      monthName              = null;

    protected String[]      monthNameDefault       = { "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December" };


    /**
     * Set the default values for the various swing components used by
     * DateChooser.
     */
    protected void setDefaultStyle(Control control, int style) {
        Label label;
        GridData data;
        switch (style) {
            case PREVIOUSMONTHBUTTON:
                label = (Label) control;
                data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
                data.widthHint = 16;
                data.heightHint = 16;
                label.setLayoutData(data);
                break;

            case NEXTMONTHBUTTON:
                label = (Label) control;
                data = new GridData(GridData.HORIZONTAL_ALIGN_END);
                data.widthHint = 16;
                data.heightHint = 16;
                label.setLayoutData(data);
                break;

            case MONTHNAMELABEL:
                label = (Label) control;
                data = new GridData(GridData.FILL_BOTH);
                label.setText("Month");
                label.setLayoutData(data);
                break;
        }
    }


    /**
     * Get the name of a month for a given index. The index corresponds to the
     * values of java.util.Calendar.MONTH, i.e. January == 0.
     * 
     * @param month int
     * @return String
     */
    public String getMonthName(int month) {
        if (monthName == null) {
            try {
                setMonthNames(CalendarNamesUtil.getMonthNames(locale));
            } catch (Exception e) {
                monthName = monthNameDefault;
            }
        }
        return monthName[month];
    }


    /**
     * Override the default weekday names with a new set. The array must be of
     * length 12 otherwise an exception with be thrown. The matching of month
     * names with index values follows the convention in java.util.Calendar with
     * January having index 0.
     * 
     * @param months String[]
     * @throws Exception
     */
    public void setMonthNames(String[] months) throws Exception {
        if (months.length != 12) {
            throw new Exception("Expected String[] with length 12. Found length " + months.length
                    + ". Keeping the old settings.");
        }
        monthName = months;
    }


    public String[] getMonthNames() {
        return monthName != null ? monthName : monthNameDefault;
    }
}
