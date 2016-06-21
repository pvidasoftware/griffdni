package com.puravida.griffon.agreement

import com.puravida.GriffdniController
import com.puravida.griffon.dnie.CMSProcessableInputStream
import com.puravida.griffon.dnie.DnieService
import es.gob.jmulticard.jse.provider.DnieProvider
import es.gob.jmulticard.jse.smartcardio.SmartcardIoConnection
import es.gob.jmulticard.ui.passwordcallback.PasswordCallbackManager
import es.gob.jmulticard.ui.passwordcallback.gui.DnieCallbackHandler
import griffon.core.artifact.GriffonController

/**
 * Created by jorge on 17/06/16.
 */
import griffon.metadata.ArtifactProviderFor
import griffon.plugins.wslite.WsliteHandler
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface
import org.bouncycastle.asn1.ASN1Primitive
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.jcajce.JcaCertStore
import org.bouncycastle.cms.CMSSignedData
import org.bouncycastle.cms.CMSSignedDataGenerator
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
import org.bouncycastle.util.Store
import wslite.rest.ContentType
import wslite.rest.RESTClient
import wslite.rest.Response

import javax.inject.Inject
import javax.security.auth.callback.CallbackHandler
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Provider
import java.security.Security
import java.security.cert.Certificate

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
