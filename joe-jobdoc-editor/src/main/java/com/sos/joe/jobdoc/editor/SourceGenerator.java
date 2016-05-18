package com.sos.joe.jobdoc.editor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.sos.JSHelper.Basics.JSToolBox;
import com.sos.JSHelper.DataElements.JSDataElementDate;
import com.sos.JSHelper.DataElements.JSDateFormat;
import com.sos.JSHelper.Exceptions.JobSchedulerException;
import com.sos.JSHelper.io.Files.JSFile;
import com.sos.JSHelper.io.Files.JSFolder;
import com.sos.JSHelper.io.Files.JSXMLFile;
import com.sos.resources.SOSProductionResource;

public class SourceGenerator extends JSToolBox {

    private static final Logger LOGGER = Logger.getLogger(SourceGenerator.class);
    private static final String CLASSNAME = "SourceGenerator";
    private static final String XSLT_PARM_EXTENDS_CLASSNAME = "ExtendsClassName";
    private static final String XSLT_PARM_CLASSNAME_EXTENSION = "ClassNameExtension";
    private static final String XSLT_PARM_VERSION = "version";
    private static final String XSLT_PARM_SOURCE_TYPE = "sourcetype";
    private static final String XSLT_PARM_CLASSNAME = "ClassName";
    private static final String XSLT_PARM_WORKER_CLASSNAME = "WorkerClassName";
    private static final String JAVA_FILENAME_EXTENSION = ".java";
    private File jobdocFile;
    private JSFolder outputDir;
    private String packageName;
    private String javaClassName;
    private String defaultLang = "en";
    private boolean standAlone = true;
    private HashMap<String, String> pobjHshMap;
    private String conResource4XslPathName;

    private File copyResource2TempFile(final String pstrResourceName) throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(pstrResourceName);
        if (stream == null) {
            throw new JobSchedulerException("can't get resource for " + pstrResourceName);
        }
        OutputStream resStreamOut = null;
        File objOut = File.createTempFile("SOS", ".xslt");
        objOut.deleteOnExit();
        int readBytes;
        byte[] buffer = new byte[4096];
        try {
            resStreamOut = new FileOutputStream(objOut);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage(), e1);
        } finally {
            stream.close();
            resStreamOut.close();
        }
        return objOut;
    }

    public void execute() {
        LOGGER.setLevel(Level.DEBUG);
        LOGGER.info("Starting transformation");
        conResource4XslPathName = SOSProductionResource.basePackage() + "/xsl/";
        String strMessage = "";
        try {
            String strXMLFileName = jobdocFile.getCanonicalPath();
            strMessage = "Source is " + strXMLFileName + "\n";
            JSXMLFile objXMLFile = new JSXMLFile(strXMLFileName);
            objXMLFile.MustExist();
            String strWorkerClassName = jobdocFile.getName();
            strWorkerClassName = strWorkerClassName.replaceAll("\\..*$", "");
            strWorkerClassName = javaClassName;
            pobjHshMap = new HashMap<String, String>();
            setXSLTParameter("package_name", packageName);
            setXSLTParameter("default_lang", defaultLang);
            if (standAlone) {
                setXSLTParameter("standalone", "true");
            } else {
                setXSLTParameter("standalone", "false");
            }
            String strClassName;
            String strClassNameExtension;
            JSDataElementDate objDate = new JSDataElementDate(now());
            objDate.setFormatPattern(JSDateFormat.dfTIMESTAMPS24);
            objDate.setParsePattern(JSDateFormat.dfTIMESTAMPS24);
            String strTimeStamp = objDate.FormattedValue();
            setXSLTParameter("timestamp", strTimeStamp);
            setXSLTParameter(XSLT_PARM_WORKER_CLASSNAME, strWorkerClassName);
            setXSLTParameter(XSLT_PARM_VERSION, "version");
            setXSLTParameter("XMLDocuFilename", objXMLFile.getAbsolutePath());
            strClassNameExtension = "OptionsSuperClass";
            setXSLTParameter(XSLT_PARM_CLASSNAME_EXTENSION, strClassNameExtension);
            setXSLTParameter(XSLT_PARM_EXTENDS_CLASSNAME, "JSOptionsClass");
            setXSLTParameter(XSLT_PARM_SOURCE_TYPE, "options");
            setXSLTParameter(XSLT_PARM_CLASSNAME, strWorkerClassName);
            String newFileName = strWorkerClassName + strClassNameExtension + JAVA_FILENAME_EXTENSION;
            doTransform("JSJobDoc2JSOptionSuperClass.xsl", objXMLFile, outputDir.newFile(newFileName));
            strMessage += "File generated " + newFileName;
            strClassNameExtension = "Options";
            setXSLTParameter(XSLT_PARM_EXTENDS_CLASSNAME, strWorkerClassName + "OptionsSuperClass");
            setXSLTParameter(XSLT_PARM_CLASSNAME_EXTENSION, strClassNameExtension);
            setXSLTParameter(XSLT_PARM_CLASSNAME, strWorkerClassName + strClassNameExtension);
            doTransform("JSJobDoc2JSOptionClass.xsl", objXMLFile,
                    outputDir.newFile(strWorkerClassName + strClassNameExtension + JAVA_FILENAME_EXTENSION));
            setXSLTParameter(XSLT_PARM_EXTENDS_CLASSNAME, "JobSchedulerJobAdapter");
            strClassNameExtension = "JSAdapterClass";
            setXSLTParameter(XSLT_PARM_CLASSNAME_EXTENSION, strClassNameExtension);
            strClassName = strWorkerClassName + strClassNameExtension;
            setXSLTParameter(XSLT_PARM_CLASSNAME, strClassName);
            setXSLTParameter(XSLT_PARM_WORKER_CLASSNAME, strWorkerClassName);
            setXSLTParameter(XSLT_PARM_SOURCE_TYPE, "JSJavaApiJob");
            doTransform("JSJobDoc2JSAdapterClass.xsl", objXMLFile, outputDir.newFile(strClassName + JAVA_FILENAME_EXTENSION));
            setXSLTParameter(XSLT_PARM_EXTENDS_CLASSNAME, "JSToolBox");
            strClassNameExtension = "";
            setXSLTParameter(XSLT_PARM_CLASSNAME_EXTENSION, strClassNameExtension);
            strClassName = (strWorkerClassName + strClassNameExtension).trim();
            setXSLTParameter(XSLT_PARM_CLASSNAME, strClassName);
            setXSLTParameter(XSLT_PARM_WORKER_CLASSNAME, strWorkerClassName);
            doTransform("JSJobDoc2JSWorkerClass.xsl", objXMLFile, outputDir.newFile(strClassName + JAVA_FILENAME_EXTENSION));
            strClassNameExtension = "Main";
            strClassName = (strWorkerClassName + strClassNameExtension).trim();
            setXSLTParameter(XSLT_PARM_EXTENDS_CLASSNAME, "JSToolBox");
            setXSLTParameter(XSLT_PARM_CLASSNAME_EXTENSION, strClassNameExtension);
            setXSLTParameter(XSLT_PARM_CLASSNAME, strClassName);
            setXSLTParameter(XSLT_PARM_WORKER_CLASSNAME, strWorkerClassName.trim());
            setXSLTParameter(XSLT_PARM_SOURCE_TYPE, "Main");
            doTransform("JSJobDoc2JSMainClass.xsl", objXMLFile, outputDir.newFile(strClassName + JAVA_FILENAME_EXTENSION));
            setXSLTParameter(XSLT_PARM_EXTENDS_CLASSNAME, "JSToolBox");
            strClassNameExtension = "JUnitTest";
            setXSLTParameter(XSLT_PARM_CLASSNAME_EXTENSION, strClassNameExtension);
            strClassName = strWorkerClassName + strClassNameExtension;
            setXSLTParameter(XSLT_PARM_CLASSNAME, strClassName);
            setXSLTParameter(XSLT_PARM_WORKER_CLASSNAME, strWorkerClassName);
            setXSLTParameter(XSLT_PARM_SOURCE_TYPE, "Junit");
            doTransform("JSJobDoc2JSJUnitClass.xsl", objXMLFile, outputDir.newFile(strClassName + JAVA_FILENAME_EXTENSION));
            setXSLTParameter(XSLT_PARM_EXTENDS_CLASSNAME, "JSToolBox");
            strClassNameExtension = "OptionsJUnitTest";
            setXSLTParameter(XSLT_PARM_CLASSNAME_EXTENSION, strClassNameExtension);
            strClassName = strWorkerClassName + strClassNameExtension;
            setXSLTParameter(XSLT_PARM_CLASSNAME, strClassName);
            setXSLTParameter(XSLT_PARM_WORKER_CLASSNAME, strWorkerClassName);
            setXSLTParameter(XSLT_PARM_SOURCE_TYPE, "Junit");
            doTransform("JSJobDoc2JSJUnitOptionSuperClass.xsl", objXMLFile, outputDir.newFile(strClassName + JAVA_FILENAME_EXTENSION));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            strMessage += "run aborted. reason " + e.getMessage().toString();
            throw new JobSchedulerException(e);
        }
    }

    private void setXSLTParameter(final String strVarName, final String strVarValue) {
        final String conMethodName = CLASSNAME + "::setXSLTParameter";
        String strV = strVarValue.trim();
        String strX = String.format("%3$s: Set parameter '%1$s' to Value %2$s.", strVarName, strV, conMethodName);
        LOGGER.debug(strX);
        pobjHshMap.put(strVarName, strV);
    }

    private void doTransform(final String pstrXSLFileName, final JSXMLFile objXMLFile, final JSFile objOutFile) throws Exception {
        try {
            LOGGER.debug("Transformation of " + objOutFile.getAbsolutePath());
            LOGGER.debug("TargetFileName = " + objOutFile.getAbsolutePath());
            File objXSLOptionClassFile = copyResource2TempFile(conResource4XslPathName + pstrXSLFileName);
            setXSLTParameter("XSLTFilename", conResource4XslPathName + pstrXSLFileName);
            objXMLFile.setParameters(pobjHshMap);
            objXMLFile.Transform(objXSLOptionClassFile, objOutFile);
            String strGeneratedContent = objOutFile.getContent();
            LOGGER.info("Size of generated content is " + strGeneratedContent.length());
        } catch (Exception e) {
            throw new JobSchedulerException(String.format("Error occured with xslt-file %1$s, xml-file is %2$s.\nError is %3$s", pstrXSLFileName,
                    objXMLFile.getAbsolutePath(), e.getMessage()), e);
        }
    }

    public void setJobdocFile(final File jobdocFile) {
        this.jobdocFile = jobdocFile;
    }

    public void setOutputDir(final JSFolder outputDir) {
        this.outputDir = outputDir;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public void setDefaultLang(final String defaultLang) {
        this.defaultLang = defaultLang;
    }

    public void setStandAlone(final boolean standAlone) {
        this.standAlone = standAlone;
    }

    public void setJavaClassName(final String javaClassName) {
        this.javaClassName = javaClassName;
    }

}