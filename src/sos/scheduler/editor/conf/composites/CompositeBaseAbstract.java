package sos.scheduler.editor.conf.composites;

import static sos.scheduler.editor.app.SOSJOEMessageCodes.JOE_E_0002;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import sos.scheduler.editor.app.ErrorLog;
import sos.scheduler.editor.classes.CompositeBaseClass;

public abstract class CompositeBaseAbstract<T> extends CompositeBaseClass  {
	@SuppressWarnings("unused")
	private final String	conSVNVersion	= "$Id$";

	private final Logger	logger			= Logger.getLogger(this.getClass());
	private final String	conClassName	= "CompositeBaseAbstract";
	protected T				objDataProvider	= null;
	public static enum enuOperationMode  {
		New, Edit, Delete, Browse, Insert
	};
	public enuOperationMode	OperationMode	= enuOperationMode.Edit;

	protected Composite		objMainControl;

	public CompositeBaseAbstract(final T pobjDataProvider, final enuOperationMode enuMode) {
		super();
		objDataProvider = pobjDataProvider;
		OperationMode = enuMode;
	}

	public CompositeBaseAbstract(final Composite parent, final T pobjDataProvider, final enuOperationMode enuMode) {
		super(parent, SWT.NONE);
		try {
			objDataProvider = pobjDataProvider;
			objParent = parent;
			objParent.setRedraw(false);
			OperationMode = enuMode;
			init = true;
			createMainMontrol(parent);
			createGroup(objMainControl);
			init();
			logger.debug(conClassName);
		}
		catch (Exception e) {
			new ErrorLog(JOE_E_0002.params(conClassName), e);
		}
		finally {
			objParent.setRedraw(true);
			objMainControl.layout();
			objParent.layout();
			init = false;
		}

	}

	public Composite createComposite (final Composite parent) {
		objParent = parent;
		objParent.setRedraw(false);
		init = true;
		createMainMontrol(parent);
		createGroup(objMainControl);
		init();

		objParent.setRedraw(true);
		objParent.layout();
		init = false;

		return objMainControl;
	}

	private void createMainMontrol(final Composite parent) {
		init = true;
		objMainControl = new Composite(parent, SWT.NONE);

		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginTop = 0;
		gridLayout.marginBottom = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		objMainControl.setLayout(gridLayout);
		objMainControl.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

	}

	public abstract void createGroup(final Composite parent);
	public abstract void init();

	@Override
	protected abstract void applyInputFields(final boolean flgT) ;
}
