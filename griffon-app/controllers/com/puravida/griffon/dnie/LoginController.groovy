package com.puravida.griffon.dnie

import es.gob.jmulticard.jse.provider.DnieProvider
import es.gob.jmulticard.jse.smartcardio.SmartcardIoConnection
import es.gob.jmulticard.ui.passwordcallback.PasswordCallbackManager
import es.gob.jmulticard.ui.passwordcallback.gui.DnieCallbackHandler
import griffon.core.GriffonApplication
import griffon.core.artifact.GriffonController
import griffon.metadata.ArtifactProviderFor
import org.bouncycastle.asn1.ASN1Primitive
import org.bouncycastle.asn1.x500.style.BCStyle

import javax.security.auth.callback.CallbackHandler
import java.security.KeyStore
import java.security.PrivateKey
import java.security.Provider
import java.security.Security
import java.security.cert.Certificate
import java.security.cert.X509Certificate

/**
 * Created by jorge on 10/06/16.
 */
@ArtifactProviderFor(GriffonController)
class LoginController {

    LoginModel model

    LoginView view

    GriffonApplication application

    void mvcGroupInit(Map<String, Object> args) {
        model.instrucctions = 'Inserte su DNIe en el lector y pulse el botón "Login"'
    }

    void doLogin(){
        view.show()
    }

    void readDnie(){

        try {
            model.instrucctions = 'Por favor, espere'
            view.showSecond()
        }catch(e){
            println e
        }

        try {
            Provider provider = new DnieProvider(new SmartcardIoConnection())
            Security.addProvider(provider)

            final KeyStore ks = KeyStore.getInstance('DNI')

            final CallbackHandler callbackHandler
            PasswordCallbackManager.setDialogOwner(view.builder.centerPane)
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

            if (certificate && certificate instanceof X509Certificate) {

                final org.bouncycastle.asn1.x509.Certificate x509Certificate =
                        org.bouncycastle.asn1.x509.Certificate.getInstance(ASN1Primitive.fromByteArray(certificate.encoded))

                model.givenname = x509Certificate.getSubject().getRDNs(BCStyle.GIVENNAME).first().first.value
                model.surname = x509Certificate.getSubject().getRDNs(BCStyle.SURNAME).first().first.value
                model.serialnumber = x509Certificate.getSubject().getRDNs(BCStyle.SERIALNUMBER).first().first.value

                //////////////////////////////
                /* firmar usando certificado autentificacion
                Path source = Paths.get('/home/jorge/Descargas/recibo_luz.pdf');
        Path destination = Paths.get('/home/jorge/Descargas/recibo_luz_signed.pdf');
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
        final PDDocument doc = PDDocument.load(new File('/home/jorge/Descargas/recibo_luz_signed.pdf'))
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
        FileOutputStream fos = new FileOutputStream('/home/jorge/Descargas/recibo_luz_signed.pdf')
        doc.saveIncremental(fos);
                */

                view.close()
            }
        }catch ( e ){
            model.instrucctions = "$e"
            view.showFirst()
        }
    }

}
