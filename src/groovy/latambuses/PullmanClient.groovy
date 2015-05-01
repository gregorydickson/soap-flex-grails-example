package latambuses

import java.text.SimpleDateFormat

import org.springframework.ws.client.core.support.WebServiceGatewaySupport
import org.springframework.ws.soap.client.core.SoapActionCallback
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import javax.xml.bind.JAXBElement


import cl.pullman.webservices.*

class PullmanClient extends WebServiceGatewaySupport {
	def id = null
	def startSession(){
		log.info "staring SESSION PULLMAN"
		setDefaultUri("http://webservices.pullman.cl/Desarrollo/StartSession")
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller()
		marshaller.setPackagesToScan("cl.pullman.webservices")
		setMarshaller(marshaller)
		setUnmarshaller(marshaller)

		
		GetStartSession request = new GetStartSession()
		request.setCodigoComercio("INT0000010")
		request.setPuntoComercio("VOY01")
		
		SoapActionCallback callback = new SoapActionCallback("http://webservices.pullman.cl/Desarrollo/StartSession")
		def element = getWebServiceTemplate().marshalSendAndReceive(request,callback)
		GetStartSessionResponse response = element
		log.info "GOT SESSION PULLMAN"
		return response.idSession
	}

	def buscaCiudad(def sessionId){
		setDefaultUri("http://webservices.pullman.cl/Desarrollo/BuscaCiudad")
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller()
		marshaller.setPackagesToScan("cl.pullman.webservices")
		setMarshaller(marshaller)
		setUnmarshaller(marshaller)
		
		GetBuscaCiudad request = new GetBuscaCiudad()
		request.codigoComercio = "INT0000010"
		request.puntoComercio = "VOY01"
		request.idSession = sessionId
		
		SoapActionCallback callback = new SoapActionCallback("http://webservices.pullman.cl/Desarrollo/BuscaCiudad")
		def response = getWebServiceTemplate().marshalSendAndReceive(request,callback)
		GetBuscaCiudadResponse ciudades = response
		ciudades.ciudadOrigen
	}

	def origenDestino(def sessionId,def codigoCiudadOrigen){
		setDefaultUri("http://webservices.pullman.cl/Desarrollo/BuscaOrigenDestino")
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller()
		marshaller.setPackagesToScan("cl.pullman.webservices")
		setMarshaller(marshaller)
		setUnmarshaller(marshaller)

		
		GetOrigenDestino request = new GetOrigenDestino()
		request.codigoComercio = "INT0000010"
		request.puntoComercio = "VOY01"
		request.idSession = sessionId
		request.codigoCiudadOrigen = codigoCiudadOrigen
		
		SoapActionCallback callback = new SoapActionCallback("http://webservices.pullman.cl/Desarrollo/BuscaOrigenDestino")
		GetOrigenDestinoResponse response = getWebServiceTemplate().marshalSendAndReceive(request,callback)
		
		response.ciudadDestino
	}

	def buscaSalidaServicioTarifa(def sessionId,def codigoCiudadOrigen,def codigoCiudadDestino,def date){
		//log.info "DATE "+date
		setDefaultUri("http://webservices.pullman.cl/Desarrollo/BuscaSalidaServicioTarifa")
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller()
		marshaller.setPackagesToScan("cl.pullman.webservices")
		setMarshaller(marshaller)
		setUnmarshaller(marshaller)

		
		GetBuscaServicioTarifa request = new GetBuscaServicioTarifa()
		request.codigoComercio = "INT0000010"
		request.puntoComercio = "VOY01"
		request.idSession = sessionId
		request.codigoCiudadOrigen = codigoCiudadOrigen
		request.codigoCiudadDestino = codigoCiudadDestino
		//fecha es YYYYMMDD
		request.fechaSalida = date//"20150310" 
		request.horaSalida = "0000" //all hours, I think
		request.empresa = "TODOS"
		
		SoapActionCallback callback = new SoapActionCallback("http://webservices.pullman.cl/Desarrollo/BuscaSalidaServicioTarifa")
		GetBuscaServicioTarifaResponse response = getWebServiceTemplate().marshalSendAndReceive(request,callback)
		//GetBuscaServicioTarifaResponse servicioResponse = response.getValue()
		response.detalleServicioTarifa
	}


	def buscaTarifaServicio(def sessionId,def codigoCiudadOrigen,def codigoCiudadDestino,def date){
		//log.info "DATE "+date
		setDefaultUri("http://webservices.pullman.cl/Desarrollo/BuscaTarifaServicio")
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller()
		marshaller.setPackagesToScan("cl.pullman.webservices")
		setMarshaller(marshaller)
		setUnmarshaller(marshaller)

		
		GetTarifaServicio request = new GetTarifaServicio()
		request.codigoComercio = "INT0000010"
		request.puntoComercio = "VOY01"
		request.ptoVta = "VOY"
		request.idSession = sessionId
		request.codigoCiudadOrigen = codigoCiudadOrigen
		request.codigoCiudadDestino = codigoCiudadDestino
		//fecha es YYYYMMDD
		request.fechaSalida = date//"20150310" 
		request.horaSalida = "0000" //all hours, I think
		request.empresa = "TODOS"
		
		SoapActionCallback callback = new SoapActionCallback("http://webservices.pullman.cl/Desarrollo/BuscaTarifaServicio")
		GetTarifaServicioResponse response = getWebServiceTemplate().marshalSendAndReceive(request,callback)
		//GetBuscaServicioTarifaResponse servicioResponse = response.getValue()
		
	}

	
}