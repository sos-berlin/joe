package sos.scheduler.editor.conf.composites;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
 
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import sos.scheduler.editor.classes.JobChainDiagramCreator;
 


import com.sos.dialog.components.SOSMenuItem;
import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

public class JobChainDiagramComposite extends Composite {

    
    private Group gJobchainDiagramm;
    private File inputFile;
    private File outputDir;
    private JobChainDiagramCreator jobChainDiagramCreator;
    private String xml="";
    private File diagramFile ;
    private Display display;
    private Timer inputTimer;
    private boolean fitToScreen=true;
    private boolean showErrorNodes=true;
    private Canvas canvas;
    private int headerHeight;
    private Menu contentMenu;
    
    public class InputTask extends TimerTask {
        public void run() {
            if (display == null) {
                display = Display.getDefault();
            }
            display.syncExec(new Runnable() {
                public void run() {
                    try {
                        jobChainDiagram(xml,inputFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    inputTimer.cancel();
                };
            });
        }
    }
    
    public JobChainDiagramComposite(Composite parent_,int headerHeight_) {
        super(parent_, SWT.NONE);
        headerHeight = headerHeight_;
        inputTimer = new Timer();
     }

    /**
     * Create contents of the window
     */
    private void createContents() {
        if (gJobchainDiagramm != null){
            gJobchainDiagramm.dispose();
        }
        gJobchainDiagramm = new Group(this, SWT.NONE);
        gJobchainDiagramm.setLayout(new GridLayout(1, false));

        GridData gdJobchainDiagram = new GridData(SWT.FILL, GridData.FILL, true, true);

        gJobchainDiagramm.setLayoutData(gdJobchainDiagram);
        gJobchainDiagramm.setText("Jobchain Diagram");
        
    }
    
    private boolean saveXML(final String xml, String filename) {
        boolean saveFile = false;
        try {
            SAXBuilder builder2 = new SAXBuilder();

            Document doc = builder2.build(new StringReader(xml));
            SchedulerDom dom = new SchedulerDom(SchedulerDom.DIRECTORY);
            saveFile = dom.writeElement(filename, doc);
        }
        
        catch (Exception e) {
            ErrorLog.message("could not save file " + filename + ". cause:" + e.getMessage(), SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
            saveFile = false;
        }
        finally{
            return saveFile;
        }
    }

    
    public void jobChainDiagram(String xml_, File inputFile_) throws Exception {
        xml=xml_;
        inputFile = inputFile_;
        outputDir = inputFile_.getParentFile();
        saveXML(xml, inputFile.getAbsolutePath());
        
        this.getShell().addListener (SWT.Resize,  new Listener () {
            public void handleEvent (Event e) {
                if (!gJobchainDiagramm.isDisposed()){
                  resetInputTimer();
                }
            }
          });
  
      
               
        GridLayout layout = new GridLayout(1, false);
        this.setLayout(layout);
        createContents();
 
        createMenue();
        diagramFile = generateDiagram(inputFile,outputDir);
        showDiagram(diagramFile);


        if (inputFile.getAbsolutePath().endsWith("~")){
            inputFile.delete();
        }
        this.layout();
    }
    
    private File generateDiagram(File inputFile, File outputDir) throws Exception{
        jobChainDiagramCreator = new JobChainDiagramCreator(inputFile,outputDir);
        jobChainDiagramCreator.createGraphwizFile(showErrorNodes);
        return jobChainDiagramCreator.getOutfile();
    }

   private void showDiagram(File diagramFile) throws Exception {

        Image originalImage = null;
        try {
        	FileInputStream fis = new FileInputStream(diagramFile);
            originalImage = new Image(gJobchainDiagramm.getDisplay(), fis);
            fis.close();
             
        } catch (FileNotFoundException e1) {
            new ErrorLog(e1.getLocalizedMessage(), e1);
        }
 
        double scale = 1;
        if (fitToScreen){
            Rectangle rect = originalImage.getBounds();
            double a =  this.getShell().getClientArea().height-headerHeight;
            double b = rect.height;
            scale = a/b;
            }
            if (scale > 1){
                scale = 1;
            }
           
        
        final Image image = new Image(gJobchainDiagramm.getDisplay(),
                 originalImage.getImageData().scaledTo((int)(originalImage.getBounds().width*scale),(int)(originalImage.getBounds().height*scale)));
        
        final Point origin = new Point(0, 0);
        canvas = new Canvas(gJobchainDiagramm, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        canvas.setMenu(contentMenu);
        final ScrollBar hBar = canvas.getHorizontalBar();
        hBar.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(final Event e) {
                int hSelection = hBar.getSelection();
                int destX = -hSelection - origin.x;
                Rectangle rect = image.getBounds();
                canvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
                origin.x = -hSelection;
            }
        });
        final ScrollBar vBar = canvas.getVerticalBar();
        vBar.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(final Event e) {
                int vSelection = vBar.getSelection();
                int destY = -vSelection - origin.y;
                Rectangle rect = image.getBounds();
                canvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
                origin.y = -vSelection;
            }
        });
        
         
        
        canvas.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(final Event e) {
                Rectangle rect = image.getBounds();
                Rectangle client = canvas.getClientArea();
                hBar.setMaximum(rect.width);
                vBar.setMaximum(rect.height);
                hBar.setThumb(Math.min(rect.width, client.width));
                vBar.setThumb(Math.min(rect.height, client.height));
                int hPage = rect.width - client.width;
                int vPage = rect.height - client.height;
                int hSelection = hBar.getSelection();
                int vSelection = vBar.getSelection();
                if (hSelection >= hPage) {
                    if (hPage <= 0){
                        hSelection = 0;
                    }
                    origin.x = -hSelection;
                }
                if (vSelection >= vPage) {
                    if (vPage <= 0){
                        vSelection = 0;
                    }
                    origin.y = -vSelection;
                }
                canvas.redraw();
            }
        });
        canvas.addListener(SWT.Paint, new Listener() {
            @Override
            public void handleEvent(final Event e) {
                GC gc = e.gc;
                gc.drawImage(image, origin.x, origin.y);
                Rectangle rect = image.getBounds();
                Rectangle client = canvas.getClientArea();
                int marginWidth = client.width - rect.width;
                if (marginWidth > 0) {
                    gc.fillRectangle(rect.width, 0, marginWidth, client.height);
                }
                int marginHeight = client.height - rect.height;
                if (marginHeight > 0) {
                    gc.fillRectangle(0, rect.height, client.width, marginHeight);
                }
            }
        });
 
       this.layout();
    }
   
   public void createMenue() {
       contentMenu = new Menu(gJobchainDiagramm);
       gJobchainDiagramm.setMenu(contentMenu);
       // =============================================================================================
       final MenuItem showErrorMenuItem = new SOSMenuItem(contentMenu, SWT.CHECK,"showErrornodes","Show error nodes",true);
        showErrorNodes = showErrorMenuItem.getSelection();
        showErrorMenuItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
            
           public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
               try {
                   showErrorNodes = showErrorMenuItem.getSelection();
                   jobChainDiagram(xml,inputFile);
                } catch (Exception e1) {
                   e1.printStackTrace();
               }
           }

           public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
           }
       });
       // =============================================================================================
             
       new MenuItem(contentMenu, SWT.SEPARATOR);
       final SOSMenuItem fitToScreenMenuItem = new SOSMenuItem(contentMenu, SWT.CHECK,"fittoScreen","Fit to Screen",true);
       fitToScreen = fitToScreenMenuItem.getSelection();
       fitToScreenMenuItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
           public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
               try {
                   fitToScreen = fitToScreenMenuItem.getSelection();
                   jobChainDiagram(xml,inputFile);
                } catch (Exception e1) {
                   e1.printStackTrace();
               }
           }

           public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
           }
       });
        
       
   }   

  
   
   public void resetInputTimer() {
       inputTimer.cancel();
       inputTimer = new Timer();
       inputTimer.schedule(new InputTask(), 1 * 200, 1 * 200);
   }

}