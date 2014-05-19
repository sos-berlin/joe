/**
 *
 */
package sos.scheduler.editor.classes;

import org.eclipse.swt.widgets.Listener;

/**
 * @author KB
 *
 */
public interface ISOSTableMenueListeners {
	public Listener getNewListener() ;
	public Listener getDeleteListener() ;
	public Listener getCopyListener() ;
	public Listener getPasteListener() ;
	public Listener getInsertListener() ;
	public Listener getCutListener() ;
	public Listener getEditListener() ;
}
