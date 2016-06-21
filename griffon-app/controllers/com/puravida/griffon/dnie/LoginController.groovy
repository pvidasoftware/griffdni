package com.puravida.griffon.dnie

import es.gob.jmulticard.ui.passwordcallback.PasswordCallbackManager
import griffon.core.artifact.GriffonController
import griffon.metadata.ArtifactProviderFor

/**
 * Created by jorge on 10/06/16.
 */
@ArtifactProviderFor(GriffonController)
class LoginController {

    LoginModel model

    LoginView view

    @javax.inject.Inject
    private DnieService dnieService

    void mvcGroupInit(Map<String, Object> args) {
    }

    boolean doLogin(){
        model.instrucctions = 'Inserte su DNIe en el lector y pulse el botón "Login"'
        view.show()
        model.dnie != null
    }

    void identifyUser(){

        model.instrucctions = 'Por favor, espere'
        view.showSecond()

        PasswordCallbackManager.setDialogOwner(view.builder.centerPane)
        try{

            model.dnie = dnieService.identifyUser()

            view.close()

        }catch ( e ){
            model.instrucctions = "$e"
            view.showFirst()
        }
    }

}
