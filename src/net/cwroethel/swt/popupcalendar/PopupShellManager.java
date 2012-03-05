package net.cwroethel.swt.popupcalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * A class to manage the popup windows. To create a popup window with arbitrary
 * contents simply:
 * <pre>
 *   ...
 *   PopupShellManager popupShell =
 *     new PopupShellManager(contentsPane.getShell());
 *   Shell shell = popupShell.getShell();
 *   ... // treat shell just as you would any other shell
 *  
 *  // create a button to open the shell. Pretty much anything else 
 *  // could be used to do that.
 *  Button button = new Button(...);
 *  select.addSelectionListener(new SelectionAdapter() {
 *  public void widgetSelected(SelectionEvent event) {
 *    Control comp = (Control)event.getSource();
 *    
 *    // align the shell with the right edge of the control that 
 *    // opened it if there is enough space.
 *    popupShell.openShell(comp, SWT.RIGHT);
 *    }
 *  });
 *  ...
 * </pre>
 * Even though the popup shell disappears it still needs to be disposed when it's
 * not needed anymore. This can either be done using the dispose() method of
 * PopupShellManager or by calling dispose on the managed shell itself.
 * (PopupShellManager itself is not a swt component and does not need to be
 * disposed).
 *  
 *  PopupShellManager can be subclassed safely.
 * @author Will
 *
 */
public class PopupShellManager {

  // store the default style definitions passed on in the constructor.
  private int defaultStyle = 0;
  private Shell shell = null;
  
  // Don't create an empty PopupShellManager.
  private PopupShellManager() {
  }

  /**
   * Create a PopupShellManager that manages a shell with the 
   * default style. (SWT.LEFT_TO_RIGHT).
   * @param parent
   */
  public PopupShellManager(Shell parent) {
    this(parent, SWT.LEFT_TO_RIGHT);
  }

  /**
   * Create a PopupShellManager with the provided style. Pretty much any style is
   * allowed that is supported by Shell.
   * @param parent
   * @param style
   */
  public PopupShellManager(Shell parent, int style) {
    shell = new Shell(parent, style);

    // close dialog if user selects outside of the shell
    // from PopupList
    shell.addListener(SWT.Deactivate, new Listener() {
      public void handleEvent(Event e) {
        shell.setVisible(false);
      }
    });
  }

  
  /**
   * Return the managed shell.
   * @return
   */
  public Shell getShell() {
    return shell;
  }
  
  
  /**
   * Draw the popup window at origin x, y measured in the same reference frame
   * as control (i.e. control's parent frame). If the popup window does not
   * fully fit in the screen, attempts will be made to reposition it to make
   * it fit.
   * @param control Control
   * @param x int
   * @param y int
   */
  public void openShell(Control control, int x, int y) {

    // calculate the position of the popup window
    Point controlOrig = getAbsoluteLocation(control);
    int xAbs = x + controlOrig.x;
    int yAbs = y + controlOrig.y;
    if (xAbs < 0) {
      xAbs = 0;
    }
    if (yAbs < 0) {
      yAbs = 0;
    }

    shell.pack();
    Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
    // Point size = datePane.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
    Point pDraw = checkBounds(new Rectangle(xAbs, yAbs, size.x, size.y),
        controlOrig);

    // set the bounds
    // setBounds(pDraw.x, pDraw.y, size.x, size.y);
    shell.setLocation(pDraw.x, pDraw.y);
    shell.open();
    shell.setFocus();


    Display display = shell.getDisplay();
    while (!shell.isDisposed() && shell.isVisible()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
  }


  /**
   * Open the popup window relative to a given control. The popup window will
   * be positioned below the control if it fits on the screen and above the
   * control otherwise, and aligned either with
   * the left edge (LEFT), right edge (RIGHT), or centered
   * (CENTER) below or above the control.
   * @param control Control
   * @param style int
   */
  public void openShell(Control control, int style) {

    // calculate the position of the popup window
    shell.pack();
    Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);

    Point controlOrig = getAbsoluteLocation(control);
    Point controlSize = control.getSize();
    int yAbs = controlOrig.y + controlSize.y;
    int xAbs = 0;

    switch (style) {
      case SWT.LEFT:
        xAbs = controlOrig.x;
        break;

      case SWT.RIGHT:
        xAbs = controlOrig.x + controlSize.x - size.x;
        break;

      case SWT.CENTER:
        xAbs = controlOrig.x + controlSize.x/2 - size.x/2;
        break;

      default:
        xAbs = controlOrig.x;
        break;
    }

    // check boundaries
    if (xAbs < 0) {
      xAbs = 0;
    }
    if (yAbs < 0) {
      yAbs = 0;
    }

    // check the screen size
    Point pDraw = checkBounds(new Rectangle(xAbs, yAbs, size.x, size.y),
        controlOrig);


    // set the bounds
    // setBounds(pDraw.x, pDraw.y, size.x, size.y);
    shell.setLocation(pDraw.x, pDraw.y);
    shell.open();
    shell.setFocus();
    // datePane.getTodaySelector().setFocus();


    Display display = shell.getDisplay();
    while (!shell.isDisposed() && shell.isVisible()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
  }

  /**
   * Open the popup window using the default style defined in the constructor.
   * @param control
   */
  public void openShell(Control control) {
    openShell(control, defaultStyle);
  }


  /**
   * Check if the popup window fits on the screen.
   * @param rect Rectangle
   * @param orig Point
   * @return Point
   */
  private Point checkBounds(Rectangle rect, Point orig) {
    int x = rect.x;
    int y = rect.y;
    int xMax = x + rect.width;
    int yMax = y + rect.height;

    // get the screen size
    Rectangle screenSize = shell.getDisplay().getBounds();

    // make window popup above control if the
    // bottom of the popup window is off-screen and fits on top.
    if ( (yMax > (screenSize.height - 30)) &&
        ( (orig.y - rect.height) > 30)) {
      y = orig.y - rect.height;
      yMax = y + rect.height;
    }

    if ( (xMax > (screenSize.width - 30)) &&
        ( (screenSize.width - rect.width) > 30)) {
      x = screenSize.width - 30 - rect.width;
      xMax = x + rect.width;
    }
    return new Point(x, y);
  }


  /**
   * Calculate the absolute screen position of the origin of the control.
   * @param control Control
   * @return Point
   */
  public Point getAbsoluteLocation(Control control) {
    Point orig = control.getLocation();
    while (control.getParent() != null) {
      control = control.getParent();
      orig.x = orig.x + control.getLocation().x;
      orig.y = orig.y + control.getLocation().y;

      // if shell then we have to add the client area
      if (Shell.class.isInstance(control)) {
        int frame = (control.getSize().x -
                     ((Shell)control).getClientArea().width)/2;
        orig.x = orig.x + frame;
        orig.y = orig.y + control.getSize().y -
            ((Shell)control).getClientArea().height - frame;
        break;
      }
    }
    return orig;
  }

  /**
   * Dispose the popup shell and all it's children.
   */
  public void dispose() {
    shell.dispose();
  }

}
