package com.puravida.griffon.rest

/**
 * Created by jorge on 17/06/16.
 */
import griffon.core.artifact.GriffonController
import griffon.metadata.ArtifactProviderFor
import griffon.plugins.wslite.WsliteHandler
import wslite.http.HTTPClientException
import wslite.rest.ContentType
import wslite.rest.RESTClient
import wslite.rest.Response

import javax.inject.Inject

@ArtifactProviderFor(GriffonController)
class RestController {

    RestModel model
    RestView  view

    @Inject
    private WsliteHandler wsliteHandler

    int rowIndex = 1

    def config = [
            url: 'http://localhost:8080/xls/madridgug',
            id : 'madridgug'
    ]

    void mvcGroupInit(Map<String, Object> args) {
        rowIndex = 1
        more()
    }

    void more(){
        try {
            wsliteHandler.withRest(config, { Map<String, Object> params, RESTClient client ->
                Response response = client.get(
                        path: "/$rowIndex",
                        accept: ContentType.JSON)
                runInsideUIAsync {
                    model.list.add response.json[0]
                }
            })
            rowIndex++
        }catch(e){
            model.foundEnd = true
        }
    }

}
