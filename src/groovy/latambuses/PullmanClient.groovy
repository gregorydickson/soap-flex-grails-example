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

	def buscaServicio(def sessionId,def codigoCiudadOrigen,def codigoCiudadDestino){
		setDefaultUri("http://webservices.pullman.cl/Desarrollo/BuscaSalidaServicio")
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller()
		marshaller.setPackagesToScan("cl.pullman.webservices")
		setMarshaller(marshaller)
		setUnmarshaller(marshaller)

		
		GetBuscaServicio request = new GetBuscaServicio()
		request.codigoComercio = "INT0000010"
		request.puntoComercio = "VOY01"
		request.idSession = sessionId
		request.codigoCiudadOrigen = codigoCiudadOrigen
		request.codigoCiudadDestino = codigoCiudadDestino
		//fecha es YYYYMMDD
		request.fechaSalida = "20150310" //date
		request.horaSalida = "0000" //all hours, I think
		request.empresa = "TODOS"
		
		SoapActionCallback callback = new SoapActionCallback("http://webservices.pullman.cl/Desarrollo/BuscaSalidaServicio")
		JAXBElement response = getWebServiceTemplate().marshalSendAndReceive(request,callback)

		GetBuscaServicioResponse servicioResponse = response.getValue()
		servicioResponse.detalleServicios
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

	def buscaPlanillaWeb(def sessionId, def bus){
		setDefaultUri("http://webservices.pullman.cl/Desarrollo/BuscaPlanillaWeb")
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller()
		marshaller.setPackagesToScan("cl.pullman.webservices")
		setMarshaller(marshaller)
		setUnmarshaller(marshaller)

		GetPlanillaWeb request = new GetPlanillaWeb()
		request.codigoComercio = "INT0000010"
		request.puntoComercio = "VOY01"
		request.idSession = sessionId
		request.bus = bus
		SoapActionCallback callback = new SoapActionCallback("http://webservices.pullman.cl/Desarrollo/BuscaPlanillaWeb")
		def response = getWebServiceTemplate().marshalSendAndReceive(request,callback)
		GetPlanillaWebResponse servicioResponse = response
		servicioResponse.detalleBus
	}

	List buscaDisponibilidadAsiento(def sessionId, Trip trip){
		setDefaultUri("http://webservices.pullman.cl/Desarrollo/BuscaDisponibilidadAsiento")
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller()
		marshaller.setPackagesToScan("cl.pullman.webservices")
		setMarshaller(marshaller)
		setUnmarshaller(marshaller)

		GetDisponibilidadAsiento request = new GetDisponibilidadAsiento()
		request.codigoComercio = "INT0000010"
		request.puntoComercio = "VOY01"
		request.idSession = sessionId

    	request.idServicio = trip.internalTripID
    	request.bus = trip.bus.busNumber
    	request.codigoTerminalOrigen = trip.codigoTerminalOrigen
    	request.codigoTerminalDestino = trip.codigoTerminalDestino
    	def pullmanDateFormat = new SimpleDateFormat('yyyyMMdd')
    	Date date = trip.departureTime
    	date.clearTime()
    	request.fechaSalida = pullmanDateFormat.format(date)
		
		SoapActionCallback callback = new SoapActionCallback("http://webservices.pullman.cl/Desarrollo/BuscaDisponibilidadAsiento")
		def response = getWebServiceTemplate().marshalSendAndReceive(request,callback)
		GetDisponibilidadAsientoResponse servicioResponse = response.getValue()
		servicioResponse.marcaAsiento
	}


}