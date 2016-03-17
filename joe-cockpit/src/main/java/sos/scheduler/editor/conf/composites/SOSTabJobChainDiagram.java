package sos.scheduler.editor.conf.composites;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

public class SOSTabJobChainDiagram extends CTabItem {

    private JobChainDiagramComposite composite;
    private CTabFolder parent;
    File inputFile;
    File outputDir;

    public SOSTabJobChainDiagram(String caption, CTabFolder parent_) {
        super(parent_, SWT.NONE);
        parent = parent_;
        setText(caption);
        composite = new JobChainDiagramComposite(parent, 190);

        this.setControl(composite);
        composite.layout();
    }

    public void jobChainDiagram(String xml, File inputFile_) throws Exception {
        composite.jobChainDiagram(xml, inputFile_);
    }

}