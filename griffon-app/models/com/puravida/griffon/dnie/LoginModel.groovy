package com.puravida.griffon.dnie

import griffon.core.artifact.GriffonModel
import griffon.metadata.ArtifactProviderFor
import groovy.beans.Bindable

/**
 * Created by jorge on 10/06/16.
 */
@ArtifactProviderFor(GriffonModel)
class LoginModel {

    @Bindable String instrucctions = ''

    int count = 0

    String serialnumber
    String givenname
    String surname
}
