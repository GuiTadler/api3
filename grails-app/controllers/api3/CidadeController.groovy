package api3

import api3.traits.ExceptionHandlers
import api3.plugin.LogService
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import org.grails.web.json.JSONObject

import java.time.LocalDate

class CidadeController implements ExceptionHandlers {
    static responseFormats = ['json']
    static defaultAction = 'get'
    static allowedMethods = [
            list: 'GET',
            get: 'GET',
            save: 'POST',
            update: 'PUT',
            delete: 'DELETE'
    ]

    RestBuilder rest = new RestBuilder()
    LogService logService = new LogService()

    Object call(String action, Long id) {
        def url = "http://localhost:8080/api2/cidade/${action}"
        if (id != null) {
            url += "?id=${id}"
        }

        def resp = restRequest(action, url)
        JSONObject jsonResp = resp.json as JSONObject

        if (action in ['save', 'update', 'delete']) {
            logService.salvarLog(request, jsonResp, LocalDate.now())
        }

        respond(jsonResp, status: resp.status)
    }

    private RestResponse restRequest(String action, String url) {
        if (['list', 'get'].contains(action)) {
            return rest.get(url)
        } else if (['save', 'update'].contains(action)) {
            return rest.post(url) { json(nome: request.JSON.nome) }
        } else if (action == 'delete') {
            return rest.delete(url)
        }

        throw new IllegalArgumentException("Ação inválida: ${action}")
    }
}
