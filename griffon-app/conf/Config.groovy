import com.puravida.MultipleDevicesWindowDisplayHandler

application {
    title = 'Griffon DNIe'
    startupGroups = ['griffdni']
    autoShutdown = true
}

mvcGroups {
    // MVC Group for "griffdni"
    'griffdni' {
        model      = 'com.puravida.GriffdniModel'
        view       = 'com.puravida.GriffdniView'
        controller = 'com.puravida.GriffdniController'
    }

    'login' {
        model      = 'com.puravida.griffon.dnie.LoginModel'
        view       = 'com.puravida.griffon.dnie.LoginView'
        controller = 'com.puravida.griffon.dnie.LoginController'
    }

    'rest' {
        model      = 'com.puravida.griffon.rest.RestModel'
        view       = 'com.puravida.griffon.rest.RestView'
        controller = 'com.puravida.griffon.rest.RestController'
    }

    'agreement' {
        model      = 'com.puravida.griffon.agreement.AgreementModel'
        view       = 'com.puravida.griffon.agreement.AgreementView'
        controller = 'com.puravida.griffon.agreement.AgreementController'
    }
}

windowManager {
    defaultHandler = new MultipleDevicesWindowDisplayHandler()
}
