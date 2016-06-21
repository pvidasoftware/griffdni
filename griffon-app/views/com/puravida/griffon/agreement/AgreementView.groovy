package com.puravida.griffon.agreement

import com.puravida.GriffdniView
import griffon.core.artifact.GriffonView
import griffon.metadata.ArtifactProviderFor

import javax.swing.ListSelectionModel
import javax.swing.SwingConstants

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel

import java.awt.BorderLayout
import java.nio.ByteBuffer
import java.nio.channels.FileChannel;

/**
 * Created by jorge on 17/06/16.
 */
@ArtifactProviderFor(GriffonView)
class AgreementView {
    FactoryBuilderSupport builder

    AgreementModel model

    GriffdniView parentView

    PagePanel pagePanel = new PagePanel(size: [300,400])

    void initUI() {
        builder.with {
            panel( id : 'mainPanel'){
                borderLayout()

                button id: 'signButton', text: "Sign",
                        icon : fontAwesomeIcon('fa-pencil'),
                        verticalTextPosition:SwingConstants.BOTTOM,
                        horizontalTextPosition:SwingConstants.CENTER,
                        constraints: BorderLayout.NORTH,
                        signAgreementAction

                scrollPane id:'pdfPanel', viewportView: pagePanel, constraints: BorderLayout.CENTER
            }
        }
    }

    void mvcGroupInit(Map<String, Object> args) {
        parentView.builder.centerPanel.addTab("Agreement", builder.mainPanel)
    }

    void mvcGroupDestroy(){
        parentView.builder.centerPanel.remove(builder.mainPanel)
    }

    void showPage(int pageIndex){
        File file = new File(model.file)
        RandomAccessFile raf = new RandomAccessFile(file, "r")
        FileChannel channel = raf.channel
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdfFile = new PDFFile(buf)
        PDFPage page = pdfFile.getPage(pageIndex)
        pagePanel.showPage(page)
    }


}
