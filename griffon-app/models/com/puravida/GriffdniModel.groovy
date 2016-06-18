package com.puravida

import griffon.core.artifact.GriffonModel
import griffon.metadata.ArtifactProviderFor
import griffon.transform.Observable

@ArtifactProviderFor(GriffonModel)
class GriffdniModel {

    String version = "GriffDNIe v1.0"

    @Observable boolean dirty = false

    @Observable int clickCount = 0

    @Observable String nif

    @Observable String name

    public void setNif(String nif){
        firePropertyChange('dirty', this.dirty, this.dirty = nif != null)
        firePropertyChange('nif', this.nif, this.nif = nif)
    }

}