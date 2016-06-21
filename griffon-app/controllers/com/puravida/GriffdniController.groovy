package com.puravida

import com.puravida.griffon.dnie.LoginController
import com.puravida.griffon.dnie.LoginModel
import com.puravida.griffon.dnie.LoginView
import griffon.core.GriffonApplication
import griffon.core.ShutdownHandler
import griffon.core.artifact.GriffonController
import griffon.metadata.ArtifactProviderFor

import javax.annotation.Nonnull

@ArtifactProviderFor(GriffonController)
class GriffdniController implements ShutdownHandler{

    GriffdniModel model

    GriffdniView view

    GriffonApplication application

    void mvcGroupInit(Map<String, Object> args) {
        application.addShutdownHandler(this)
    }

    boolean canShutdown(@Nonnull GriffonApplication application){
        return model.nif == null
    }

    void onShutdown(@Nonnull GriffonApplication application){

    }

    void login() {
        withMVC 'login', { LoginModel loginModel, LoginView loginView, LoginController loginController ->

          if ( loginController.doLogin() ) {
              model.nif = loginModel.dnie.serialnumber
              model.name = loginModel.fullname
              createMVC('rest', [ nif: model.nif ])
              createMVC('agreement', [dnie : loginModel.dnie ])
          }

        }
    }

    void logout(){
        try{
            destroyMVCGroup('rest')
            destroyMVCGroup('agreement')
            model.nif = null
        }catch(e){
        }
    }

    void agreementSigned(){
        logout()
    }

    void exit(){
        application.shutdown()
    }

}