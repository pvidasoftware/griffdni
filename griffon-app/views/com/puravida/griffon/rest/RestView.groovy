package com.puravida.griffon.rest

import com.puravida.GriffdniView
import griffon.core.artifact.GriffonView
import griffon.metadata.ArtifactProviderFor

/**
 * Created by jorge on 17/06/16.
 */
import javax.swing.*

@ArtifactProviderFor(GriffonView)
class RestView {

    FactoryBuilderSupport builder

    RestModel model

    GriffdniView parentView

    void initUI() {
        builder.with {
            panel( id : 'mainPanel'){

                label text: "Buscando $model.nif"

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
        parentView.builder.centerPanel.addTab("MadridGug Rest",builder.mainPanel)
    }

    void mvcGroupDestroy(){
        parentView.builder.centerPanel.remove(builder.mainPanel)
    }
}
