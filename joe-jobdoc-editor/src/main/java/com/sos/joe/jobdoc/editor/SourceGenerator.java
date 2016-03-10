package com.sos.joe.jobdoc.editor;

import java.io.File;
import java.io.FileInputStream;
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
import com.sos.JSHelper.io.Files.JSXSLTFile;
import com.sos.resources.SOSProductionResource;

public class SourceGenerator extends JSToolBox {

    private static final Logger LOGGER = Logger.getLogger(SourceGenerator.class);
    private final String conClassName = "SourceGenerator";
    private final String conXsltParmExtendsClassName = "ExtendsClassName";
    private final String conXsltParmClassNameExtension = "ClassNameExtension";
    private final String conXsltParmVersion = "version";
    private final String conXsltParmSourceType = "sourcetype";
    private final String conXsltParmClassName = "ClassName";
    private final String conXsltParmWorkerClassName = "WorkerClassName";
    private final String conJavaFilenameExtension = ".java";
    private File jobdocFile;
    private JSFolder outputDir;
    private String packageName;
    private String javaClassName;
    private String defaultLang = "en";
    private boolean standAlone = true;
    private HashMap<String, String> pobjHshMap;
    private String conResource4XslPathName;
    private static final Logger logger = Logger.getLogger(SourceGenerator.class);

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
        logger.setLevel(Level.DEBUG);
        logger.info("Starting transformation");
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

            JSXSLTFile objXSLFile;
            pobjHshMap = new HashMap<String, String>();

            setXSLTParameter("package_name", packageName);

            setXSLTParameter("default_lang", defaultLang);
            if (standAlone) {
                setXSLTParameter("standalone", "true");
            } else {
                setXSLTParameter("standalone", "false");
            }

            String strClassName, strClassNameExtension;
            JSDataElementDate objDate = new JSDataElementDate(Now());
            objDate.setFormatPattern(JSDateFormat.dfTIMESTAMPS24);
            objDate.setParsePattern(JSDateFormat.dfTIMESTAMPS24);
            String strTimeStamp = objDate.FormattedValue();
            setXSLTParameter("timestamp", strTimeStamp);

            setXSLTParameter(conXsltParmWorkerClassName, strWorkerClassName);
            setXSLTParameter(conXsltParmVersion, "version");
            setXSLTParameter("XMLDocuFilename", objXMLFile.getAbsolutePath());
            {
                strClassNameExtension = "OptionsSuperClass";
                setXSLTParameter(conXsltParmClassNameExtension, strClassNameExtension);
                setXSLTParameter(conXsltParmExtendsClassName, "JSOptionsClass");
                setXSLTParameter(conXsltParmSourceType, "options");
                setXSLTParameter(conXsltParmClassName, strWorkerClassName);

                String newFileName = strWorkerClassName + strClassNameExtension + conJavaFilenameExtension;
                doTransform("JSJobDoc2JSOptionSuperClass.xsl", objXMLFile, outputDir.newFile(newFileName));
                strMessage += "File generated " + newFileName;
            }
            {
                strClassNameExtension = "Options";
                setXSLTParameter(conXsltParmExtendsClassName, strWorkerClassName + "OptionsSuperClass");
                setXSLTParameter(conXsltParmClassNameExtension, strClassNameExtension);
                setXSLTParameter(conXsltParmClassName, strWorkerClassName + strClassNameExtension);

                doTransform("JSJobDoc2JSOptionClass.xsl", objXMLFile, outputDir.newFile(strWorkerClassName + strClassNameExtension + conJavaFilenameExtension));
            }

            {
                setXSLTParameter(conXsltParmExtendsClassName, "JobSchedulerJobAdapter");
                strClassNameExtension = "JSAdapterClass";
                setXSLTParameter(conXsltParmClassNameExtension, strClassNameExtension);
                strClassName = strWorkerClassName + strClassNameExtension;
                setXSLTParameter(conXsltParmClassName, strClassName);
                setXSLTParameter(conXsltParmWorkerClassName, strWorkerClassName);
                setXSLTParameter(conXsltParmSourceType, "JSJavaApiJob");
                doTransform("JSJobDoc2JSAdapterClass.xsl", objXMLFile, outputDir.newFile(strClassName + conJavaFilenameExtension));
            }
            {
                setXSLTParameter(conXsltParmExtendsClassName, "JSToolBox");
                strClassNameExtension = "";
                setXSLTParameter(conXsltParmClassNameExtension, strClassNameExtension);
                strClassName = (strWorkerClassName + strClassNameExtension).trim();
                setXSLTParameter(conXsltParmClassName, strClassName);
                setXSLTParameter(conXsltParmWorkerClassName, strWorkerClassName);
                doTransform("JSJobDoc2JSWorkerClass.xsl", objXMLFile, outputDir.newFile(strClassName + conJavaFilenameExtension));
            }
            {
                strClassNameExtension = "Main";
                strClassName = (strWorkerClassName + strClassNameExtension).trim();
                setXSLTParameter(conXsltParmExtendsClassName, "JSToolBox");
                setXSLTParameter(conXsltParmClassNameExtension, strClassNameExtension);
                setXSLTParameter(conXsltParmClassName, strClassName);
                setXSLTParameter(conXsltParmWorkerClassName, strWorkerClassName.trim());
                setXSLTParameter(conXsltParmSourceType, "Main");
                doTransform("JSJobDoc2JSMainClass.xsl", objXMLFile, outputDir.newFile(strClassName + conJavaFilenameExtension));
            }
            {
                setXSLTParameter(conXsltParmExtendsClassName, "JSToolBox");
                strClassNameExtension = "JUnitTest";
                setXSLTParameter(conXsltParmClassNameExtension, strClassNameExtension);
                strClassName = strWorkerClassName + strClassNameExtension;
                setXSLTParameter(conXsltParmClassName, strClassName);
                setXSLTParameter(conXsltParmWorkerClassName, strWorkerClassName);
                setXSLTParameter(conXsltParmSourceType, "Junit");
                doTransform("JSJobDoc2JSJUnitClass.xsl", objXMLFile, outputDir.newFile(strClassName + conJavaFilenameExtension));
            }
            {
                setXSLTParameter(conXsltParmExtendsClassName, "JSToolBox");
                strClassNameExtension = "OptionsJUnitTest";
                setXSLTParameter(conXsltParmClassNameExtension, strClassNameExtension);
                strClassName = strWorkerClassName + strClassNameExtension;
                setXSLTParameter(conXsltParmClassName, strClassName);
                setXSLTParameter(conXsltParmWorkerClassName, strWorkerClassName);
                setXSLTParameter(conXsltParmSourceType, "Junit");

                doTransform("JSJobDoc2JSJUnitOptionSuperClass.xsl", objXMLFile, outputDir.newFile(strClassName + conJavaFilenameExtension));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            strMessage += "run aborted. reason " + e.getStackTrace().toString();
            throw new JobSchedulerException(e);
        } finally {
        }
    }

    private void setXSLTParameter(final String strVarName, final String strVarValue) {
        final String conMethodName = conClassName + "::setXSLTParameter";

        String strV = strVarValue.trim();
        String strX = String.format("%3$s: Set parameter '%1$s' to Value %2$s.", strVarName, strV, conMethodName);
        logger.debug(strX);
        pobjHshMap.put(strVarName, strV);
    }

    private void doTransform(final String pstrXSLFileName, final JSXMLFile objXMLFile, final JSFile objOutFile) throws Exception {

        try {
            logger.debug("Transformation of " + objOutFile.getAbsolutePath());

            logger.debug("TargetFileName = " + objOutFile.getAbsolutePath());
            File objXSLOptionClassFile = copyResource2TempFile(conResource4XslPathName + pstrXSLFileName);
            setXSLTParameter("XSLTFilename", conResource4XslPathName + pstrXSLFileName);

            objXMLFile.setParameters(pobjHshMap);

            objXMLFile.Transform(objXSLOptionClassFile, objOutFile);

            String strGeneratedContent = objOutFile.getContent();
            logger.info("Size of generated content is " + strGeneratedContent.length());
        } catch (Exception e) {
            throw new JobSchedulerException(String.format("Error occured with xslt-file %1$s, xml-file is %2$s.\nError is %3$s", pstrXSLFileName, objXMLFile.getAbsolutePath(), e.getLocalizedMessage()), e);
        }

    }

    // TODO fix this in JSFile
    private String getContent(final String strFileName) {

        String strB = "";

        int filesize = 0;
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(strFileName);
            final byte[] buffer = new byte[4000];
            while (true) {
                final int bytesRead = fin.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                filesize += bytesRead;
                strB = strB + new String(buffer);
            }

        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            } catch (final IOException e) {
            }
        }

        final String strT = strB.substring(0, filesize);
        return strT;
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