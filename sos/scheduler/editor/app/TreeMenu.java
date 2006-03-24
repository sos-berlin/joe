package sos.scheduler.editor.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.jdom.Element;
import org.jdom.JDOMException;

import sos.scheduler.editor.forms.MainWindow;

public class TreeMenu {

	private DomParser _dom;

	private Tree _tree;

	private Menu _menu;

	private Clipboard _cb;

	private Element _copy;

	private MainWindow _gui;

	public TreeMenu(Tree tree, DomParser dom, MainWindow gui) {
		_tree = tree;
		_dom = dom;
		_gui = gui;
		createMenu();
	}

	public int message(String message, int style) {
		MessageBox mb = new MessageBox(_tree.getShell(), style);
		mb.setMessage(message);
		return mb.open();
	}

	private Element getElement() {
		if (_tree.getSelectionCount() > 0) {
			TreeData data = (TreeData) _tree.getSelection()[0].getData();
			if (data != null && data instanceof TreeData) {
				if (data.getChild() != null)
					return data.getElement().getChild(data.getChild());
				else
					return data.getElement();
			} else
				return null;
		} else
			return null;
	}

	private void createMenu() {
		_menu = new Menu(_tree.getShell(), SWT.POP_UP);
		MenuItem item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getInfoListener());
		item.setText("Show Info");

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getXMLListener());
		item.setText("Show XML");

		item = new MenuItem(_menu, SWT.SEPARATOR);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getCopyListener());
		item.setText("Copy");

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getClipboardListener());
		item.setText("Copy to Clipboard");

		item = new MenuItem(_menu, SWT.SEPARATOR);

		item = new MenuItem(_menu, SWT.PUSH);
		item.addListener(SWT.Selection, getPasteListener());
		item.setText("Paste");

		_menu.addListener(SWT.Show, new Listener() {
			public void handleEvent(Event e) {
				MenuItem[] items = _menu.getItems();
				disableMenu();
				if (_tree.getSelectionCount() > 0) {
					Element element = getElement();
					if (element != null) {
						items[0].setEnabled(true); // show info
						items[1].setEnabled(true); // show xml
						items[4].setEnabled(true); // copy to clipboard

						String name = element.getName();

						if (name.equals("job"))
							items[3].setEnabled(true); // copy
						else if (name.equals("run_time"))
							items[3].setEnabled(true); // copy

						if (_copy != null) {
							String cName = _copy.getName();

							if (name.equals("jobs") && cName.equals("job"))
								items[6].setEnabled(true); // paste
							if (name.equals("job") && cName.equals("run_time"))
								items[6].setEnabled(true); // paste
						}
					}
				}
			}
		});
	}

	public Menu getMenu() {
		return _menu;
	}

	private void disableMenu() {
		MenuItem[] items = _menu.getItems();
		for (int i = 0; i < items.length; i++)
			items[i].setEnabled(false);
	}

	private Listener getInfoListener() {
		return new Listener() {
			public void handleEvent(Event e) {
				Element element = getElement();
				if (element != null) {
					try {
						String filename = _dom.transform(element);

						Program prog = Program.findProgram("html");
						if (prog != null)
							prog.execute(filename);
						else {
							Runtime.getRuntime().exec(
									Options.getBrowserExec(filename, null));
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						message("Error: " + ex.getMessage(), SWT.ICON_ERROR
								| SWT.OK);
					}
				}
			}
		};
	}

	private String getXML() {
		Element element = getElement();
		String xml = "";
		if (element != null) {
			try {
				xml = _dom.getXML(element);
			} catch (JDOMException ex) {
				message("Error: " + ex.getMessage(), SWT.ICON_ERROR | SWT.OK);
				return null;
			}
		}
		return xml;
	}

	private void copyClipboard(String content){
		
			if (_cb == null)
				_cb = new Clipboard(_tree.getDisplay());

			TextTransfer transfer = TextTransfer.getInstance();
			_cb.setContents(new Object[] { content },
					new Transfer[] { transfer });
		
	}
	
	private Listener getXMLListener() {
		return new Listener() {
			public void handleEvent(Event e) {
				Element element = getElement();
				if (element != null) {
					String xml = getXML();
					if (xml == null) // error
						return;

					Font font = new Font(Display.getDefault(), "Courier New",
							8, SWT.NORMAL);
					TextDialog dialog = new TextDialog(_tree.getShell(),
							SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL
									| SWT.RESIZE, SWT.BORDER | SWT.H_SCROLL
									| SWT.V_SCROLL);
					dialog.setSize(new Point(500, 400));
					dialog.setContent(xml);
					dialog.setClipBoard(true);
					dialog.getStyledText().setFont(font);
					dialog.getStyledText().setEditable(true);
					String s = dialog.open();
					if (dialog.isClipBoardClick()){
						copyClipboard( s );
					}
					
					if (font != null)
						font.dispose();
				}
			}
		};
	}

	private Listener getCopyListener() {
		return new Listener() {
			public void handleEvent(Event e) {
				Element element = getElement();
				if (element != null)
					_copy = (Element) element.clone();
			}
		};
	}

	private Listener getClipboardListener() {
		return new Listener() {
			public void handleEvent(Event e) {
				Element element = getElement();
				if (element != null) {
					String xml = getXML();
					if (xml == null) // error
						return;

					copyClipboard (xml);
				}
			}
		};
	}

	private Listener getPasteListener() {
		return new Listener() {
			public void handleEvent(Event e) {
				Element target = getElement();
				if (target != null && _copy != null) {
					String tName = target.getName();
					String cName = _copy.getName();

					if (tName.equals("jobs") && cName.equals("job")) { // copy
						// job
						String append = "copy("
								+ (target.getChildren("job").size() + 1);
						_copy.setAttribute("name",append + ")of_" + Utils.getAttributeValue(
								"name", _copy));
						target.addContent(_copy);
						_gui.updateJobs();
					} else if (tName.equals("job") && cName.equals("run_time")) { // copy
						// run_time
						target.removeChildren("run_time");
						target.addContent(_copy);
						_gui.updateJob();
					}
				}
			}
		};
	}

}
