package com.puravida

import griffon.core.artifact.GriffonView
import griffon.metadata.ArtifactProviderFor

import javax.swing.*
import java.awt.*

@ArtifactProviderFor(GriffonView)
class GriffdniView {

    FactoryBuilderSupport builder

    GriffdniModel model

    def tabPanel

    void initUI() {
        builder.with {
            try {
                application(size: [320, 160], id: 'mainWindow',
                        extendedState: JFrame.MAXIMIZED_BOTH,
                        defaultCloseOperation: WindowConstants.DO_NOTHING_ON_CLOSE,
                        title: application.configuration['application.title'],
                        iconImage: imageIcon('/griffon-icon-48x48.png').image,
                        iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                                     imageIcon('/griffon-icon-32x32.png').image,
                                     imageIcon('/griffon-icon-16x16.png').image]
                ) {

                    panel() {
                        borderLayout()
                        toolBar(constraints: BorderLayout.NORTH) {
                            button(id: 'loginButton', text: "Login",
                                    icon : fontAwesomeIcon('fa-sign-in', size:48),
                                    verticalTextPosition:SwingConstants.BOTTOM,
                                    horizontalTextPosition:SwingConstants.CENTER,
                                    enabled : bind { model.nif == null},
                                    loginAction)

                            button(id: 'logoutButton', text: "Logout",
                                    icon : fontAwesomeIcon('fa-sign-out', size:48),
                                    verticalTextPosition:SwingConstants.BOTTOM,
                                    horizontalTextPosition:SwingConstants.CENTER,
                                    enabled : bind { model.nif != null},
                                    logoutAction)

                            separator(  )

                            button(id: 'exitButton', text: "Salir",
                                    icon : fontAwesomeIcon('fa-power-off', size:48),
                                    verticalTextPosition:SwingConstants.BOTTOM,
                                    horizontalTextPosition:SwingConstants.CENTER,
                                    enabled : bind { !model.dirty },
                                    exitAction)

                            label id:'welcome', text: bind{
                                if( model.nif )
                                    "Bienvenido $model.name ($model.nif)"
                                else
                                    "Identifiquese en el sistema"
                            }
                        }

                        tabbedPane( id:'centerPanel', constraints: CENTER ) {

                        }

                        panel( constraints: SOUTH){
                            flowLayout()
                            label text:model.version
                        }
                    }
                }
            }catch(e){
                println e
                System.exit(-1)
            }
        }
    }
}