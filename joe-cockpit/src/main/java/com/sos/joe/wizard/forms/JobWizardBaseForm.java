/**
 * 
 */
package com.sos.joe.wizard.forms;
import org.eclipse.swt.widgets.Button;
import org.jdom.Element;

import com.sos.joe.globals.interfaces.ISchedulerUpdate;
import com.sos.joe.xml.jobscheduler.SchedulerDom;

/**
 * @author KB
 *
 */
public class JobWizardBaseForm {
	protected SchedulerDom		dom			= null;
	protected ISchedulerUpdate	update		= null;
	protected Element			job			= null;
	protected Element			jobBackUp	= null;
	protected Button			butCancel	= null;
	protected Button			butShow		= null;
	protected Button			butNext		= null;
	
	/** Hilsvariable für das Schliessen des Dialogs. 
	 * Das wird gebraucht wenn das Dialog über den "X"-Botten (oben rechts vom Dialog) geschlossen wird .*/
	protected boolean				closeDialog				= false;

}
