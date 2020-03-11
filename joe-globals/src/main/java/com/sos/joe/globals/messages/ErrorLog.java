package com.sos.joe.globals.messages;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.joe.globals.options.Options;

import sos.util.SOSClassUtil;
import sos.util.SOSStandardLogger;

public class ErrorLog extends Exception {

    private static final long serialVersionUID = -4414810697191992062L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorLog.class);
    private static Shell sShell = null;

    public ErrorLog(final String msg) {
        super();
        try {
            init();
            message(msg, SWT.ERROR);
            LOGGER.info(msg);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public ErrorLog(final String msg, final Exception e) {
        super();
        try {
            init();
            JobSchedulerException objJSE = new JobSchedulerException(msg, e);
            String strMsg = msg + "\n" + objJSE.getExceptionText();
            message(strMsg, SWT.ERROR);
            LOGGER.error(strMsg, e);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public ErrorLog(final String application, String msg, final Exception e) {
        super();
        try {
            init();
            JobSchedulerException objJSE = new JobSchedulerException(msg, e);
            String strMsg = msg + "\n" + objJSE.getExceptionText();
            message(application, strMsg, SWT.ERROR);
            LOGGER.error(strMsg);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void init() {
        String filename = "";
        try {
            if (LOGGER != null) {
                return;
            }
            filename = Options.getSchedulerData();
            if (filename.endsWith("/") || filename.endsWith("\\")) {
                filename = filename + "logs";
            } else {
                filename = filename + "/logs";
            }
            if (!new java.io.File(filename).exists()) {
                new java.io.File(filename).mkdirs();
            }
            filename = filename + "/scheduler_editor.log";
        } catch (Exception e) {
            if (LOGGER != null) {
                LOGGER.debug("error in " + SOSClassUtil.getMethodName() + ", cause: " + e.getMessage());
            }
        }

    }

    public String getErrorMessage(final Exception ex) {
        String s = "";
        try {
            Throwable tr = ex.getCause();
            if (ex.toString() != null) {
                s = ex.toString();
            }
            while (tr != null) {
                if (s.indexOf(tr.toString()) == -1) {
                    s = (s.length() > 0 ? s + ", " : "") + tr.toString();
                }
                tr = tr.getCause();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return s;
    }

    public static SOSStandardLogger getLogger() {
        return null;
    }

    public static Shell getSShell() {
        return sShell;
    }

    public static int message(String message, int style) {
        return message(getSShell(), message, style);
    }

    public static int message(String application, String message, int style) {
        return message(getSShell(), application, message, style);
    }

    public static int message(Shell shell, String pstrMessage, int style) {
        MessageBox mb = new MessageBox(shell, style);
        if (mb == null) {
            return -1;
        }
        if (pstrMessage == null) {
            pstrMessage = "??????";
        }
        mb.setMessage(pstrMessage);
        String title = Messages.getLabel("message");
        if ((style & SWT.ICON_ERROR) != 0) {
            title = Messages.getLabel("error");
        } else {
            if ((style & SWT.ICON_INFORMATION) != 0) {
                title = Messages.getLabel("information");
            } else if ((style & SWT.ICON_QUESTION) != 0) {
                title = Messages.getLabel("question");
            } else if ((style & SWT.ICON_WARNING) != 0) {
                title = Messages.getLabel("warning");
            }
        }
        mb.setText("JOE: " + title);
        return mb.open();
    }

    public static int message(Shell shell, String application, String pstrMessage, int style) {
        MessageBox mb = new MessageBox(shell, style);
        if (mb == null) {
            return -1;
        }
        if (pstrMessage == null) {
            pstrMessage = "??????";
        }
        mb.setMessage(pstrMessage);
        String title = Messages.getLabel("message");
        if ((style & SWT.ICON_ERROR) != 0) {
            title = Messages.getLabel("error");
        } else {
            if ((style & SWT.ICON_INFORMATION) != 0) {
                title = Messages.getLabel("information");
            } else if ((style & SWT.ICON_QUESTION) != 0) {
                title = Messages.getLabel("question");
            } else if ((style & SWT.ICON_WARNING) != 0) {
                title = Messages.getLabel("warning");
            }
        }
        mb.setText(application + ": " + title);
        return mb.open();
    }

    public static void setSShell(Shell pobjShell) {
        sShell = pobjShell;
    }

}