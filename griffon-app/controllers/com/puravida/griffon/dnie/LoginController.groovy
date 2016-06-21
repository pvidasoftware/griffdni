package com.puravida.griffon.dnie

import es.gob.jmulticard.jse.provider.DnieProvider
import es.gob.jmulticard.jse.smartcardio.SmartcardIoConnection
import es.gob.jmulticard.ui.passwordcallback.PasswordCallbackManager
import es.gob.jmulticard.ui.passwordcallback.gui.DnieCallbackHandler
import griffon.core.GriffonApplication
import griffon.core.artifact.GriffonController
import griffon.metadata.ArtifactProviderFor
import org.bouncycastle.asn1.ASN1Primitive
import org.bouncycastle.asn1.x500.style.BCStyle

import javax.security.auth.callback.CallbackHandler
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Provider
import java.security.Security
import java.security.cert.Certificate
import java.security.cert.X509Certificate

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
