package com.puravida

import es.gob.jmulticard.jse.provider.DnieProvider
import es.gob.jmulticard.jse.smartcardio.SmartcardIoConnection
import griffon.swing.SwingGriffonApplication

import java.security.Provider
import java.security.Security

class Launcher {

    static void main(String[] args) throws Exception {

        SwingGriffonApplication.run(SwingGriffonApplication, args)
    }
}