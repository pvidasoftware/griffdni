package com.puravida

import com.puravida.griffon.dnie.LoginModel
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
        withMVC 'login', { loginModel, loginView, loginController ->
          loginController.doLogin()
          if (loginModel.serialnumber) {
              model.nif = loginModel.serialnumber
              model.name = "$loginModel.givenname, $loginModel.surname"
              createMVC('rest', [nif:model.nif])
              createMVC('agreement', [nif:model.nif])
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

    void sign() {

    }

    void exit(){
        application.shutdown()
    }

}