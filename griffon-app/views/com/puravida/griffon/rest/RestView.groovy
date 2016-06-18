package com.puravida.griffon.rest

import com.puravida.GriffdniModel
import com.puravida.GriffdniView
import griffon.core.artifact.GriffonView

/**
 * Created by jorge on 17/06/16.
 */
import griffon.metadata.ArtifactProviderFor

import javax.swing.ListSelectionModel
import javax.swing.SwingConstants
import java.awt.BorderLayout

@ArtifactProviderFor(GriffonView)
class RestView {

    FactoryBuilderSupport builder

    RestModel model

    GriffdniView parentView

    void initUI() {
        builder.with {
            panel( id : 'restPanel'){

                button id: 'moreButton', text: "More",
                        icon : fontAwesomeIcon('fa-step-forward'),
                        verticalTextPosition:SwingConstants.BOTTOM,
                        horizontalTextPosition:SwingConstants.CENTER,
                        enabled : bind{ !model.foundEnd },
                        moreAction

                scrollPane preferredSize: [480,350],constraints:'wrap', {

                    table id:'listTable',
                            rowHeight:35, rowMargin:6,
                            selectionMode: ListSelectionModel.SINGLE_SELECTION,
                            focusable: true, {
                        tableFormat = defaultTableFormat(columnNames:[
                                'name',
                                'row',
                                'cell',
                                'value'
                        ])
                        eventTableModel source: model.list , format: tableFormat

                        installTableComparatorChooser source: model.list
                    }
                }

            }
        }
    }

    void mvcGroupInit(Map<String, Object> args) {
        parentView.builder.centerPanel.removeAll()
        parentView.builder.centerPanel.add builder.restPanel
        parentView.builder.centerPanel.revalidate()
        parentView.builder.centerPanel.repaint()
    }

}
