package com.puravida

import griffon.core.GriffonApplication
import griffon.core.artifact.GriffonController
import griffon.metadata.ArtifactProviderFor

@ArtifactProviderFor(GriffonController)
class GriffdniController {

    GriffdniModel model

    GriffdniView view

    GriffonApplication application

    void login() {
        withMVCGroup('login') { groupAware ->
            def loginController = groupAware.controller
            def loginModel = groupAware.model
            loginController.doLogin()
            if (loginModel.serialnumber) {
                model.nif = loginModel.serialnumber
                model.name = "$loginModel.givenname, $loginModel.surname"
            }
        }
        try{
            destroyMVCGroup('rest')
        }catch(e){
        }
        if( model.nif ){
            createMVC('rest', [nif:model.nif])
        }
    }

    void logout(){
        destroyMVCGroup('rest')
        model.nif = null
    }

    void sign() {

    }

    void exit(){
        application.shutdown()
    }

}