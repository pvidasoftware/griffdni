package com.puravida.griffon.agreement

import com.puravida.GriffdniController
import com.puravida.griffon.dnie.DnieService
import griffon.core.artifact.GriffonController
import griffon.metadata.ArtifactProviderFor

/**
 * Created by jorge on 17/06/16.
 */
@ArtifactProviderFor(GriffonController)
class AgreementController {

    AgreementModel model

    AgreementView view

    GriffdniController parentController

    @javax.inject.Inject
    private DnieService dnieService

    void mvcGroupInit(Map<String, Object> args) {
    }

    void signAgreement(){

        try {
            dnieService.signPdf(model.dnie, new File(model.file), new File(model.fileSigned))
            view.showSuccess()
            parentController.agreementSigned()
        }catch( e ){
            view.showError(e.toString())
        }

    }
}
