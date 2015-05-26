
import latambuses.*
import cl.pullman.webservices.*

import javax.annotation.PostConstruct


class PullmanService {

    PullmanClient pullmanClient = null
    def pullmanSessionId = null
    def id = null
    
    @PostConstruct
    def init(){
        pullmanClient = new PullmanClient()
        //pullmanClient.init()
        log.info "PULLMAN SERVICE INIT"
    }

    def session() {
        if(!id){
            log.info "GETTTING PULLMAN SESSION"
            id = pullmanClient.startSession()
            log.info "GOT PULLMAN SESSION:"+ id
        }
        return id
    }
    def newSession() {
        log.info "GETTING NEW SESSION PULLMAN"
        pullmanSessionId = pullmanClient.startSession()
    }

    def ciudades(){
        log.info "PULLMAN CLIENT Origen Destino"
        if(!pullmanClient){
            pullmanClient = new PullmanClient()
        }
        def id = pullmanSessionId ?: session()
        try{
            pullmanClient.buscaCiudad(id)
        }catch (org.springframework.ws.soap.client.SoapFaultClientException e){
            id = newSession()
            pullmanClient.buscaCiudad(id)
        }
    }

    def origenDestino(def codigoCiudad){
        log.info "PULLMAN CLIENT Origen Destino"
        def id = pullmanSessionId ?: session()
        if(!pullmanClient){
            pullmanClient = new PullmanClient()
        }
        try{
            pullmanClient.origenDestino(id,codigoCiudad)
        }catch (org.springframework.ws.soap.client.SoapFaultClientException e){
            id = newSession()
            pullmanClient.origenDestino(id,codigoCiudad)
        }
    }
    
    def buscaTarifaServicio(def codigoCiudadOrigen,def codigoCiudadDestino,def date){
        //log.info "BUSCA SALIDA SERVICIO TARIFA"
        def id = pullmanSessionId ?: session()
        if(!pullmanClient){
            pullmanClient = new PullmanClient()
        }
        //log.info 'CALLING BUSC'
        try{
            pullmanClient.buscaSalidaServicioTarifa(id,codigoCiudadOrigen,codigoCiudadDestino,date)
        }catch (org.springframework.ws.soap.client.SoapFaultClientException e){
            id = newSession()
            pullmanClient.buscaSalidaServicioTarifa(id,codigoCiudadOrigen,codigoCiudadDestino,date)
        }
    }

    
}