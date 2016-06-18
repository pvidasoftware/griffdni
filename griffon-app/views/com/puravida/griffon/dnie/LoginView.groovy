package com.puravida.griffon.dnie

import griffon.core.artifact.GriffonView
import griffon.metadata.ArtifactProviderFor

import javax.swing.*
import java.awt.*

@ArtifactProviderFor(GriffonView)
class LoginView {

    FactoryBuilderSupport builder

    LoginModel model

    def parentView

    void initUI() {
        builder.with {
            panel( id: 'centerPane'){
                cardLayout()

                panel(id:'first') {
                    borderLayout()
                    panel(constraints: BorderLayout.CENTER) {
                        borderLayout()
                        label constraints: BorderLayout.NORTH, text: bind { model.instrucctions }
                        button constraints: BorderLayout.CENTER, id: 'loginButton', text: "Login", readDnieAction
                    }
                }

                panel(id:'second') {
                    borderLayout()
                    panel(constraints: BorderLayout.CENTER) {
                        borderLayout()
                        label constraints: BorderLayout.NORTH, text: bind { model.instrucctions }
                    }
                }

            }
        }
    }

    JDialog dialog
    void show() {
        JFrame mainWindow = parentView.builder.mainWindow as JFrame
        JPanel panel = builder.centerPane as JPanel
        dialog = new JDialog(mainWindow,
                'Identificacion', true,
                parentView.builder.mainWindow.graphicsConfiguration)
        dialog.contentPane = panel
        dialog.pack()

        double w = mainWindow.width
        double h = mainWindow.height
        int x = (int) (dialog.bounds.x + (w / 3));
        int y = (int) (dialog.bounds.y + (h / 3));

        Point corner = new Point(
                (x >= 0 ? x : 0),
                (y >= 0 ? y : 0)
        );
        dialog.setLocation(corner);

        showFirst()
        dialog.visible = true
    }

    void showFirst(){
        JPanel panel = builder.centerPane as JPanel
        CardLayout cl = panel.layout as CardLayout
        cl.first(panel)
    }

    void showSecond(){
        JPanel panel = builder.centerPane as JPanel
        CardLayout cl = panel.layout as CardLayout
        cl.next(panel)
    }

    void close(){
        dialog.dispose()
    }
}