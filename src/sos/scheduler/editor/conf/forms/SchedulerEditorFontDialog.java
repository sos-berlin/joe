/*
 * Created on 03.09.2010
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package sos.scheduler.editor.conf.forms;

import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;

import sos.scheduler.editor.app.MainWindow;
import sos.scheduler.editor.app.Options;
import com.swtdesigner.SWTResourceManager;

public class SchedulerEditorFontDialog {
   
 
   private FontData fontData;
   private RGB foreGround;
   
   public SchedulerEditorFontDialog() {
	  super();
      
   } 
   
   public SchedulerEditorFontDialog(String fontName_, int fontSize_, int fontStyle_, RGB foreGround_) {
	  super();
      this.fontData = new FontData(fontName_,fontSize_,fontStyle_);
      this.foreGround = foreGround_;
   }
   
   public SchedulerEditorFontDialog(String fontName_, int fontSize_, int fontType_) {
	  super();
      this.fontData = new FontData(fontName_,fontSize_,SWT.NORMAL);
	  this.foreGround = new RGB(0,0,0);
   }
   
   public SchedulerEditorFontDialog(FontData fontData_,RGB foreGround_) {
	  super();
	  this.foreGround = foreGround_;
      this.fontData = fontData_;
   }
   
   public SchedulerEditorFontDialog(FontData fontData_) {
	  super();
	  this.foreGround = new RGB(0,0,0);
      this.fontData = fontData_;
   }
   
   public SchedulerEditorFontDialog(String fontData_) {
	  super();
	  this.foreGround = new RGB(0,0,0);
      this.fontData = new FontData(fontData_);
   }
   
  public void readFontData() {
	  String s = Options.getProperty("script_editor_font");
	  if (s == null) {
		 this.fontData = new FontData("Courier New",8,SWT.NORMAL);
      }else {
         this.fontData = new FontData(s);
      }
   	  
	  s = Options.getProperty("script_editor_font_color");
	  if (s == null) s = "";
   	  s = s.replaceAll("RGB.*\\{(.*)\\}", "$1");
   	  String[] colours =  Pattern.compile(",").split(s);
   	  try {
   	     int r = Integer.parseInt(colours[0].trim());
         int g = Integer.parseInt(colours[1].trim());
   	     int b = Integer.parseInt(colours[2].trim());
         this.foreGround = new RGB(r,g,b);
   	  }catch (NumberFormatException e){
   		  System.out.println("Wrong colour in Profile");
   	      this.foreGround = new RGB(0,0,0);
   	  }
    	 
       
  }
  
  private void saveFontData(FontData f, RGB foreGround) {
	  Options.setProperty("script_editor_font", f.toString());
	  Options.setProperty("script_editor_font_color", foreGround.toString());
	  Options.saveProperties();
	  this.fontData = f;
	  this.foreGround = foreGround;
  }
  
  public void show() {
	 final Display d = new Display();
	 show(d);
  }
  
  public FontData getFontData() {
	 return this.fontData;
  }
  
  public RGB getForeGround() {
	 return this.foreGround;
  }
  
  public void show(Display display) {
	  
	    final Display d = display;
	    final Shell s1 = new Shell(d);
		final Shell s = new Shell(MainWindow.getSShell(), SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
	    final RGB aktForeGround = this.foreGround;

	    s.setSize(302, 160);
	    s.setText("Font Dialog");
	    s.setLayout(new GridLayout(11, false));

	    new Label(s, SWT.NONE);
	    final Text t = new Text(s, SWT.BORDER | SWT.WRAP | SWT.MULTI);
	    
	    t.setText("Sample Text");
        t.setFont(SWTResourceManager.getFont("Courier New", 8, SWT.NORMAL));
        t.setForeground(new Color(d, this.foreGround));
        
        t.setFont(new Font(d, this.fontData));
        t.setForeground(new Color(d, this.foreGround));
        
	    GridData gd_t = new GridData(SWT.FILL, SWT.FILL, false, false, 10, 1);
	    gd_t.heightHint = 74;
	    t.setLayoutData(gd_t);
	    new Label(s, SWT.NONE);
	    final Button btnChange = new Button(s, SWT.PUSH | SWT.BORDER);
	    btnChange.setText("Change Font");
	    btnChange.addSelectionListener(new SelectionAdapter() {
	      public void widgetSelected(SelectionEvent e) {
	        FontDialog fd = new FontDialog(s, SWT.NONE);
	        fd.setText("Select Font");
	        fd.setRGB(t.getForeground().getRGB());
	        fd.setFontList(t.getFont().getFontData());

 	        FontData newFont = fd.open();
	        if(newFont==null)
	            return;
	        t.setFont(new Font(d, newFont));
	        t.setForeground(new Color(d, fd.getRGB()));
	        
	      }
	    });
	    
	    Button btnSave = new Button(s, SWT.NONE);
	    btnSave.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    	   saveFontData(t.getFont().getFontData()[0],t.getForeground().getRGB());
	    	   s.dispose();
	    	}
	    });
	    btnSave.setText("Save");
	    
	    Button btnReset = new Button(s, SWT.NONE);
	    btnReset.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
		        t.setFont(new Font(d, fontData));
		        t.setForeground(new Color(d, aktForeGround));
	    	}
	    });
	    btnReset.setText("Reset");
	    
	    Button btnCancel = new Button(s, SWT.NONE);
	    btnCancel.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    	   s.dispose();
	    	}
	    });
	    btnCancel.setText("Cancel");
	 
	    new Label(s, SWT.NONE);
	    s.open();


	    while (!s.isDisposed()) {
	      if (!d.readAndDispatch())
	        d.sleep();
	    }
	    //d.dispose();
	       
  }
  public static void main(String[] a) {
	 SchedulerEditorFontDialog s = new SchedulerEditorFontDialog("Courier new", 12, SWT.BOLD);
	 s.show();

  }
}