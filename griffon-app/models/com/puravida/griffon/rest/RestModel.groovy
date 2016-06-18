package com.puravida.griffon.rest

import ca.odell.glazedlists.BasicEventList
import ca.odell.glazedlists.EventList
import ca.odell.glazedlists.SortedList
import griffon.core.artifact.GriffonModel

/**
 * Created by jorge on 17/06/16.
 */
import griffon.metadata.ArtifactProviderFor
import groovy.beans.Bindable

@ArtifactProviderFor(GriffonModel)
class RestModel {

    @Bindable foundEnd = false

    EventList list = new SortedList( new BasicEventList(), { a, b ->
        return a.value <=> b.value
    } as Comparator)

}
