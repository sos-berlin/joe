package sos.scheduler.editor.app;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import net.cwroethel.swt.popupcalendar.DaySelectorStyle;
import net.cwroethel.swt.popupcalendar.DaySelectorStyleAdapter;
import net.cwroethel.swt.popupcalendar.PopupCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class DatePicker extends Composite {
	
    private static final String ISO_PATTERN = "yyyy-MM-dd";

    private static final int    STYLE       = DateFormat.MEDIUM;

    final Color                 color       = new Color(Display.getCurrent(), 74, 149, 214);

    final Color                 lightColor  = new Color(Display.getCurrent(), 205, 226, 255);

    private Button              button      = null;

    private PopupCalendar       calendar    = null;

    private Text                dateField   = null;

    private Calendar            cal         = null;                                          // @jve:decl-index=0:

    private Locale              locale      = Locale.getDefault();                           // @jve:decl-index=0:
    
    //Überprüfung: validFrom muss kleiner als validFrom sein.
    private Date validto = null; 
    private Date validFrom = null; 

    public DatePicker(Composite parent, int style) {
        super(parent, style);
        initialize();

        initCalendar();
    }

  

    private void initialize() {
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.BEGINNING; // Generated
        gridData2.verticalAlignment = GridData.FILL; // Generated
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL; // Generated
        gridData.grabExcessHorizontalSpace = true; // Generated
        gridData.verticalAlignment = GridData.FILL; // Generated
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2; // Generated
        gridLayout.verticalSpacing = 0; // Generated
        gridLayout.marginWidth = 0; // Generated
        gridLayout.marginHeight = 0; // Generated
        gridLayout.horizontalSpacing = 0; // Generated
        dateField = new Text(this, SWT.READ_ONLY | SWT.BORDER);
        dateField.setEditable(false); // Generated
        dateField.setLayoutData(gridData); // Generated
        dateField.setText(""); // Generated
        button = new Button(this, SWT.NONE);
        button.setText("Date..."); // Generated
        button.setLayoutData(gridData2); // Generated
        this.setLayout(gridLayout); // Generated
        button.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
             	if(calendar.getShell().isDisposed()) {                        
            		org.eclipse.swt.widgets.Shell s = new org.eclipse.swt.widgets.Shell(getShell());
                    calendar = new PopupCalendar(s, PopupCalendar.SHOWALL, locale);
            	}
                if (!calendar.isOpen()) {
                	
                    Control comp = (Control) e.getSource();
                    calendar.open(comp, SWT.RIGHT);
                } else
                    calendar.close();
            }
        });
        
        
        setSize(new Point(242, 45));
               
        
    }


    private void initCalendar() {
    	org.eclipse.swt.widgets.Shell s = new org.eclipse.swt.widgets.Shell(getShell());
 
        calendar = new PopupCalendar(s, PopupCalendar.SHOWALL, locale);
    
        // add the adapter.
        calendar.setDaySelectorStyle(new DaySelectorStyleAdapter() {

            // This is the method used to override the defaults. The 'style'
            // here
            // refers to the different components of the day selector.
            public void overrideStyle(Control control, int style) {
                switch (style) {
                    // The DAYLABEL is the label showing the days of the current
                    // month.
                    case DaySelectorStyle.DAYLABEL:
                        // set the background color
                        control.setBackground(lightColor);

                        // Make the labels 30 pixels wide.
                        ((GridData) control.getLayoutData()).widthHint = 30;
                        break;
                }
            }
        });

        // Now update the weekday names. The array must contain 7 elements
        // otherwise
        // an an exception is cast.
        try {
            calendar.getDaySelectorStyle().setWeekdayNames(new String[] { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" });
        } catch (Exception e) {
        	
        	try {
        		new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName(), e);
        	} catch(Exception ee) {
        		//tu nichts
        	}
        
            System.out.println(e);
        }
        
        calendar.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (calendar.getDate() != null) {
                	
                	if(validFrom != null && calendar.getDate().getTime().before(validFrom)) {
                		//System.out.println("datum ist früher als erlaub");
                		dateField.setBackground(Options.getRequiredColor());
                	} else if(validto != null && calendar.getDate().getTime().after(validto)) {
                		//System.out.println("datum ist später als erlaub");
                		dateField.setBackground(Options.getRequiredColor());
                	} else {
                		dateField.setBackground(null);
                	}
                    setDate(calendar.getDate().getTime());
                }
            }
        });

        calendar.getDateChooser().paint();
    }


    private void setDisplay(Date date) {
        DateFormat df = DateFormat.getDateInstance(STYLE, locale);
        dateField.setText(df.format(date));
    }


    public Date getDate() {
        return cal == null ? null : cal.getTime();
    }


    public void setDate(Date date) {
        if (cal == null)
            cal = Calendar.getInstance(locale);

        cal.setTime(date);
        calendar.setDate(cal);
        calendar.getDateChooser().paint();
        setDisplay(date);
    }


    public boolean setISODate(String isoDate) {
        Date d = new SimpleDateFormat(ISO_PATTERN).parse(isoDate, new ParsePosition(0));
        if (d != null)
            setDate(d);
        return d != null;
    }


    public String getISODate() {
        if (cal == null)
            return "";
        else
            return new SimpleDateFormat(ISO_PATTERN).format(cal.getTime());
    }

   /* 
    //test
     public String getISODateTime() {
        if (cal == null)
            return "";
        else
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(cal.getTime());
    }*/

    public void setNow() {
        setDate(new Date());
    }


    public void dispose() {
        calendar.dispose();
        super.dispose();
    }


    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        dateField.setEnabled(enabled);
        button.setEnabled(enabled);
    }


    public void setVisible(boolean visible) {
        super.setVisible(visible);
        dateField.setVisible(visible);
        button.setVisible(visible);
    }


    public void setBackground(Color color) {
        dateField.setBackground(color);
    }


    public void addModifyListener(ModifyListener listener) {
        dateField.addModifyListener(listener);
    }


    public void removeModifyListener(ModifyListener listener) {
        dateField.removeModifyListener(listener);
    }


    public void setToolTipText(String string) {
        super.setToolTipText(string);
        dateField.setToolTipText(string);
        button.setToolTipText(Messages.getTooltip("datepicker.button"));
    }
    
    public void setEditable(boolean editable) {
    	dateField.setEditable(editable);
    }
    
    public String getDateText() {
    	return dateField.getText();
    }

    /**
     * Gültig ab. 
     * Datum vor dem date sind nicht gültig
     *  
     * @param date
     */
    
    public void validFrom(Date date) {
       validFrom = date; 	
    }
   
    /**
     * Gültig bis.
     * 
     *  Datum nach date sind nicht gültig
     * @param date
     */
    public void validto(Date date) {
    	validto = date;
    }
} // @jve:decl-index=0:visual-constraint="10,10"
