package api3.traits

import grails.artefact.controller.RestResponder

trait ExceptionHandlers implements RestResponder  {

    def handleException(Exception e) {
        respond([message: "Ocorreu um erro no servidor, contate o administrador!"], status: 400)

    }
}
