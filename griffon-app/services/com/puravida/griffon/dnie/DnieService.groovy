package com.puravida.griffon.dnie

import es.gob.jmulticard.jse.provider.DnieProvider
import es.gob.jmulticard.jse.smartcardio.SmartcardIoConnection
import es.gob.jmulticard.ui.passwordcallback.gui.DnieCallbackHandler
import griffon.core.artifact.GriffonService

/**
 * Created by jorge on 21/06/16.
 */
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface
import org.bouncycastle.asn1.ASN1Primitive
import org.bouncycastle.asn1.x500.style.BCStyle
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.cert.jcajce.JcaCertStore
import org.bouncycastle.cms.CMSSignedData
import org.bouncycastle.cms.CMSSignedDataGenerator
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
import org.bouncycastle.util.Store

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
import java.security.cert.X509Certificate

@javax.inject.Singleton
@griffon.metadata.ArtifactProviderFor(GriffonService)
class DnieService {


    Provider provider

    void loadProvider() {
        provider = new DnieProvider(new SmartcardIoConnection())
        Security.addProvider(provider)
    }


    Dnie identifyUser(){

        if( !provider )
            loadProvider()

        Dnie ret = new Dnie()

        final KeyStore ks = KeyStore.getInstance('DNI')

        final CallbackHandler callbackHandler = new DnieCallbackHandler()

        final KeyStore.LoadStoreParameter lsp = new KeyStore.LoadStoreParameter() {
            @Override
            public KeyStore.ProtectionParameter getProtectionParameter() {
                return new KeyStore.CallbackHandlerProtection(callbackHandler)
            }
        };

        ks.load(lsp)

        ret.certificate = ks.getCertificate('CertAutenticacion')
        ret.privateKey = (PrivateKey) ks.getKey('CertAutenticacion', null);
        ret.chain = ks.getCertificateChain('CertAutenticacion');

        if (ret.certificate && ret.certificate instanceof X509Certificate) {

            final org.bouncycastle.asn1.x509.Certificate x509Certificate =
                    org.bouncycastle.asn1.x509.Certificate.getInstance(ASN1Primitive.fromByteArray(ret.certificate.encoded))

            ret.givenname = x509Certificate.getSubject().getRDNs(BCStyle.GIVENNAME).first().first.value
            ret.surname = x509Certificate.getSubject().getRDNs(BCStyle.SURNAME).first().first.value
            ret.serialnumber = x509Certificate.getSubject().getRDNs(BCStyle.SERIALNUMBER).first().first.value

        }

        ret
    }

    void signPdf( final Dnie dnie, final File fSource, final File fDestination){

        if( !provider )
            identifyUser()

        Path source = Paths.get(fSource.absolutePath);
        Path destination = Paths.get(fDestination.absolutePath);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)

        final PDDocument doc = PDDocument.load(fDestination)
        final PDSignature signature = new PDSignature()

        signature.filter = PDSignature.FILTER_ADOBE_PPKLITE
        signature.subFilter = PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED
        signature.signDate = Calendar.instance

        doc.addSignature(signature, new SignatureInterface() {
            @Override
            byte[] sign(InputStream content) throws IOException {
                List<Certificate> certList = [dnie.certificate]
                Store certs = new JcaCertStore(certList)
                CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
                org.bouncycastle.asn1.x509.Certificate cert =
                        org.bouncycastle.asn1.x509.Certificate.getInstance(
                                ASN1Primitive.fromByteArray(dnie.certificate.encoded));

                ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA256WithRSA").build(dnie.privateKey);
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
        FileOutputStream fos = new FileOutputStream(fDestination)
        doc.saveIncremental(fos);
    }

}
