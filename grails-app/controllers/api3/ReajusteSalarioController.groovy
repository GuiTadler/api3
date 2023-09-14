package api3

import api3.traits.ExceptionHandlers
import api3.plugin.LogService
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse

class ReajusteSalarioController implements ExceptionHandlers{
    static responseFormats = ['json']
    static defaultAction = "get"
    static allowedMethods = [
            list: 'GET',
            get: 'GET',
            save: 'POST',
            update: 'PUT',
            delete: 'DELETE'
    ]

    RestBuilder rest = new RestBuilder()
    LogService logService = new LogService()
    String baseUrl = 'http://localhost:8080/api2/ReajusteSalario/'

    def index() {
        render "Bem-vindo à API de Reajustes de Salário!"
    }

    def list() {
        def url = "${baseUrl}list"
        def resp = restRequest(url, 'GET')
        respond(resp.json, status: resp.status)
    }

    def get(Long id) {
        def url = "${baseUrl}get/${id}"
        def resp = restRequest(url, 'GET')
        respond(resp.json, status: resp.status)
    }

    def save() {
        def url = "${baseUrl}save"
        def resp = restRequest(url, 'POST', [
                idFuncionario: request.JSON.idFuncionario,
                dataReajuste: request.JSON.dataReajuste,
                valorSalario: request.JSON.valorSalario
        ])
        if (resp.status == 200) {
            Map dadosLog = [operation: 'POST', situation: 'Success', resource: 'ReajusteSalario', resourceId: resp.json.id.toString()]
            logService.salvarLog(dadosLog)
        }
        respond(resp.json, status: resp.status)
    }

    def update(Long id) {
        def url = "${baseUrl}update/${id}"
        def resp = restRequest(url, 'PUT', [
                idFuncionario: request.JSON.idFuncionario,
                dataReajuste: request.JSON.dataReajuste,
                valorSalario: request.JSON.valorSalario
        ])
        if (resp.status == 200) {
            Map dadosLog = [operation: 'PUT', situation: 'Success', resource: 'ReajusteSalario', resourceId: id.toString()]
            logService.salvarLog(dadosLog)
        }
        respond(resp.json, status: resp.status)
    }

    def delete(Long id) {
        def url = "${baseUrl}delete/${id}"
        def resp = restRequest(url, 'DELETE')
        if (resp.status == 200) {
            Map dadosLog = [operation: 'DELETE', situation: 'Success', resource: 'ReajusteSalario', resourceId: id.toString()]
            logService.salvarLog(dadosLog)
        }
        respond(resp.json, status: resp.status)
    }

    private RestResponse restRequest(String url, String method, Map body = [:]) {
        if (method == 'GET') {
            return rest.get(url)
        } else if (method == 'POST') {
            return rest.post(url) {
                json body
            }
        } else if (method == 'PUT') {
            return rest.put(url) {
                json body
            }
        } else if (method == 'DELETE') {
            return rest.delete(url)
        }

        throw new IllegalArgumentException("Método HTTP inválido: ${method}")
    }
}
