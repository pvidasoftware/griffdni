package com.puravida.griffon.agreement

import com.puravida.griffon.dnie.CMSProcessableInputStream
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

    void mvcGroupInit(Map<String, Object> args) {
        runInsideUIAsync{
            view.showPage(1)
        }
    }


    void signAgreement(){

        final KeyStore ks = KeyStore.getInstance('DNI')
        final CallbackHandler callbackHandler
        PasswordCallbackManager.setDialogOwner(view.builder.mainPanel)
        callbackHandler = new DnieCallbackHandler()

        final KeyStore.LoadStoreParameter lsp = new KeyStore.LoadStoreParameter() {
            @Override
            public KeyStore.ProtectionParameter getProtectionParameter() {
                return new KeyStore.CallbackHandlerProtection(callbackHandler)
            }
        };

        ks.load(lsp)

        final Certificate certificate = ks.getCertificate('CertAutenticacion')
        final PrivateKey privateKey = (PrivateKey) ks.getKey('CertAutenticacion', null);
        final Certificate[] chain = ks.getCertificateChain('CertAutenticacion');


        Path source = Paths.get(model.file);
        Path destination = Paths.get(model.fileSigned);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)

        final PDDocument doc = PDDocument.load(new File(model.fileSigned))
        final PDSignature signature = new PDSignature()

        signature.filter = PDSignature.FILTER_ADOBE_PPKLITE
        signature.subFilter = PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED
        signature.signDate = Calendar.instance

        doc.addSignature(signature, new SignatureInterface() {
            @Override
            byte[] sign(InputStream content) throws IOException {
                List<Certificate> certList = [certificate]
                Store certs = new JcaCertStore(certList)
                CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
                org.bouncycastle.asn1.x509.Certificate cert =
                        org.bouncycastle.asn1.x509.Certificate.getInstance(
                                ASN1Primitive.fromByteArray(certificate.getEncoded()));

                ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA256WithRSA").build(privateKey);
                gen.addSignerInfoGenerator(
                        new JcaSignerInfoGeneratorBuilder(
                                new JcaDigestCalculatorProviderBuilder().build()).
                                build(sha1Signer, new X509CertificateHolder(cert)));
                gen.addCertificates(certs);
                CMSProcessableInputStream msg = new CMSProcessableInputStream(content);
                CMSSignedData signedData = gen.generate(msg, false);

                return signedData.encoded
            }
        });
        FileOutputStream fos = new FileOutputStream(model.fileSigned)
        doc.saveIncremental(fos);
    }
}
