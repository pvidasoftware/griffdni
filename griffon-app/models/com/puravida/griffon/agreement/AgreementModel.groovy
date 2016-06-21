package com.puravida.griffon.agreement

import com.puravida.griffon.dnie.Dnie
import griffon.core.artifact.GriffonModel
import griffon.metadata.ArtifactProviderFor

/**
 * Created by jorge on 17/06/16.
 */
@ArtifactProviderFor(GriffonModel)
class AgreementModel {

    String file = 'MadridGUG.pdf'

    Dnie dnie

    String getNif(){
        dnie.serialnumber
    }

    String getFileSigned(){
        "${file}_${nif}.pdf"
    }

    void mvcGroupInit(Map<String, Object> args) {
        this.dnie = args.dnie
    }
}
