
import latambuses.*
import cl.pullman.webservices.*

import javax.annotation.PostConstruct

import org.apache.commons.codec.binary.Base64
import java.security.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class PullmanService {

    PullmanClient pullmanClient = null
    def grailsApplication
    def pullmanSessionId = null
    def id = null
    def url
    String clave 
    String validacion 
    @PostConstruct
    def init(){
        url = grailsApplication.config.pullman.url
        pullmanClient = new PullmanClient()
        clave = grailsApplication.config.pullman.key
        validacion = grailsApplication.config.pullman.secret
        pullmanClient.url = url
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

    Map conciliacionComercioIntegrado(Date fechaDesde, Date fechaHasta,
                                 String montoTransaccion, String cantidadTransaccion){
        id = pullmanClient.startSession()
        log.info "pullman Service conciliacion session:"+id
        Map result
        String claveEntryptada = encryptar()
        log.info "clave Encryptada:"+claveEntryptada
        try{
            result = pullmanClient.conciliacionComercioIntegrado( fechaDesde, claveEntryptada,
                                                    fechaHasta,  montoTransaccion, cantidadTransaccion)
        }catch (org.springframework.ws.soap.client.SoapFaultClientException e){
            log.error "PULLMAN - SERVICE: Error CONCILIACION Soap fault" + e.message
        } catch(Exception ex){
            log.error "PULLMAN - SERVICE: Error CONCILIACION  " + ex.message
        }
        log.info "Pullman Service - Conciliacion " + result
        return result
    }

    Map detalleConciliacion(String detalle){
        id = pullmanClient.startSession()
        Map result
        String claveEntryptada = encryptar()
        log.info "clave Encryptada:"+claveEntryptada
        try{
            result = pullmanClient.detalleConciliacion( claveEntryptada, detalle)
        }catch (org.springframework.ws.soap.client.SoapFaultClientException e){
            log.error "PULLMAN - SERVICE: Error CONCILIACION Soap fault" + e.message
        } catch(Exception ex){
            log.error "PULLMAN - SERVICE: Error CONCILIACION  " + ex.message
        }
        log.info "Pullman Service - Conciliacion " + result
        return result
    }

    def encryptar(){
        String texto = clave
        String secretKey = validacion
        String base64EncryptedString = ""
        try {
            MessageDigest md = MessageDigest.getInstance("MD5")
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"))
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24)
            SecretKey key = new SecretKeySpec(keyBytes, "DESede")
            Cipher cipher = Cipher.getInstance("DESede")
            cipher.init(Cipher.ENCRYPT_MODE, key)
            byte[] plainTextBytes = texto.getBytes("utf-8")
            byte[] buf = cipher.doFinal(plainTextBytes)
            byte[] base64Bytes = Base64.encodeBase64(buf)
            base64EncryptedString = new String(base64Bytes)
        } catch (Exception ex) {
            ex.printStackTrace()
        }
        return base64EncryptedString;

    }

    
}
