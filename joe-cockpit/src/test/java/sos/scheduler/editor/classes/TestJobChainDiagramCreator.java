package sos.scheduler.editor.classes;

import java.io.File;

import org.junit.Test;

public class TestJobChainDiagramCreator {

    @Test
    public void test() throws Exception {
        JobChainDiagramCreator jobChainDiagramCreator = new JobChainDiagramCreator(new File("c:/temp/file.xml"), new File("c:/temp"));
        jobChainDiagramCreator.createGraphwizFile(true);
    }

}