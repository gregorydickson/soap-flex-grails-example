import latambuses.*
import org.quartz.JobExecutionContext
import groovyx.gpars.GParsPool
import groovy.time.TimeCategory

import cl.pullman.websales.to.CiudadTO
import cl.pullman.websales.to.EmpresaTO
import cl.pullman.websales.to.ServicioTO
import cl.pullman.websales.to.TarifaTO

//Blaze DS java library used for pullman scraping
import flex.messaging.io.amf.client.AMFConnection
import flex.messaging.io.amf.client.exceptions.ClientStatusException
import flex.messaging.messages.AcknowledgeMessage
import flex.messaging.messages.CommandMessage
import flex.messaging.messages.Message
import flex.messaging.messages.RemotingMessage
import flex.messaging.util.UUIDUtils
import org.apache.http.client.CookieStore
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import java.text.SimpleDateFormat

class PullmanScraperJob  {

    static triggers = {
        cron name: 'pullmanTrigger', cronExpression: "0 0 18 * * ?"
        //simple name: 'pullmanTrigger', startDelay: 6000, repeatInterval: 1000*60*60*24
    }

    def group = "LatAmGroup"
    def description = "Bus pullman scraper"

    public void execute(JobExecutionContext context) {

        try{
            log.info 'Scraping Pullman Routes Job'
            def startTime = System.currentTimeMillis()

            def routes = []
            def routeIds = []
            def pullman = Company.findByName("pullman bus")
            //create pullman routes if needed
            if(CompanyRoute.countByCompany(pullman) == 0) {
                log.error "NO PULLMAN ROUTES FOR FLASH/FLEX SCRAPER"
                return
            } else {
                routes = CompanyRoute.findAllByCompany(pullman)
                //routes = CompanyRoute.findAllByRouteInListAndCompany(someRoutes,pullman)
                log.info "PULLMAN Staring New Scrape"
                routes.collect{routeIds << it.id}
            }

            GParsPool.withPool(30) {
                routeIds.eachParallel { id ->

                    try {
                        def connectionParams = createConnection()

                        CompanyRoute.withNewSession{

                            def companies = []
                            def company = Company.findByName("pullman bus")
                            companies << company
                            company = Company.findByName("elqui bus")
                            companies << company
                            company = Company.findByName("atacama vip")
                            companies << company
                            company = Company.findByName("los corsarios")
                            companies << company
                            company = Company.findByName("fichtur vip")
                            companies << company
                            company = Company.findByName("los libertadores")
                            companies << company
                            company = Company.findByName("los conquistadores")
                            companies << company
                            company = Company.findByName("cidher")
                            companies << company
                            company = Company.findByName("Buses Moraga")
                            companies << company

                            def datesToScrape = []
                            Date today = new Date()
                            today.clearTime()
                            datesToScrape << today
                            datesToScrape << (today + 1)
                            datesToScrape << (today + 2)
                            datesToScrape << (today + 3)
                            datesToScrape << (today + 4)
                            datesToScrape << (today + 5)
                            datesToScrape << (today + 6)
                            datesToScrape << (today + 7)
                            datesToScrape << (today + 8)

                            def route = CompanyRoute.get(id)

                            scrapeRoute(connectionParams, route, datesToScrape,companies)
                        }
                        connectionParams.amfConnection.close()
                    } catch (Exception e){
                        log.error ("Exception in Route : ",  e)
                    }
                }
            }
            log.info("PULLMAN FLASH-FLEX SCRAPE COMPLETE")

        } catch(Throwable e){
            log.error ("Exception in Pullman Scraper: ",  e)
        }
    }

    /**
     * Create an AMF connection to: http://www.ventapasajes.cl/fullpassServer/messagebroker/amf
     * @return a Map with keys 'amfConnection' and 'dsId' whose values are an AMFConnection
     * and the value of FLEX_CLIENT_ID_HEADER respectively.
     */
    def createConnection() {

        HttpGet httpGet = new HttpGet('http://www.ventapasajes.cl/fullpassServer/SessionIdServlet')

        CookieStore cookieStore = new BasicCookieStore()

        HttpClientBuilder builder = HttpClients.custom()
        builder.setDefaultCookieStore(cookieStore)
        CloseableHttpClient httpClient = builder.build()

        log.debug "Executing request to: $httpGet"
        CloseableHttpResponse response = httpClient.execute(httpGet)
        log.debug "Executed request to: $httpGet"

        def headers = response.getAllHeaders()
        headers.each { header ->
            log.debug "Header $header.name:$header.value"
        }
        def cookies = cookieStore.getCookies()
        cookies.each { cookie ->
            log.debug "Cookie $cookie.name:$cookie.value"
        }

        def amfConnection = new AMFConnection()
        log.debug 'Creating AMF Connection'

        def url = "http://www.ventapasajes.cl/fullpassServer/messagebroker/amf"

        try {
            amfConnection.connect(url)
            log.debug "Connected to $url"
        } catch (ClientStatusException e) {
            log.error "Exception connecting to Pullman MessageBroker: $exception.message", e
            throw e
        }
        amfConnection.addHttpRequestHeader('Content-type', 'application/x-amf')
        cookies.each { cookie ->
            amfConnection.addHttpRequestHeader("Cookie", "$cookie.name=$cookie.value")
            log.debug "Adding a cookie to the request: $cookie.name=$cookie.value"
        }

        //Ping to get the DSId (Flex session id)
        def cmdMsg = new CommandMessage()
        cmdMsg.setOperation(CommandMessage.CLIENT_PING_OPERATION)
        cmdMsg.setMessageId(UUIDUtils.createUUID())
        cmdMsg.setHeader(Message.FLEX_CLIENT_ID_HEADER, "nil")
        //def obj = amfConnection.call(null, cmdMsg)
        def resp = (AcknowledgeMessage) amfConnection.call(null, cmdMsg)
        def dsId = resp.getHeader(Message.FLEX_CLIENT_ID_HEADER)
        log.debug "GOT THE DSID: $dsId"

        [amfConnection: amfConnection, dsId: dsId]
    }

    /**
     * Call this action when you finish your work with the connection to close it
     * @param connectionParams use the value obtained from createConnection()
     * @return
     */
    def closeConnection(Map connectionParams) {
        connectionParams.amfConnection.close()
        log.debug 'Closed AFM Connection'
    }

    def callAFM(Map connectionParams, String operation, String destination, Object[] body = new Object[0]) {

        def message = new RemotingMessage()
        message.setOperation(operation)
        message.setDestination(destination)
        message.setBody(body)
        message.setMessageId(UUIDUtils.createUUID())
        message.setHeader('DSId', connectionParams.dsId)

        //put the body in the message
        def params = new Object[1]
        params[0] = message
        //log.info "message: " + message
        //TODO try to recover if it fails
        def result = connectionParams.amfConnection.call("null", params)
        // RESULT OBJECT TYPE CHANGES DEPENDING ON IF RUNNING IN FORKED MODE
        // RETURNS ASObject if not in forked mode, ServicoTO if in forked mode
        //log.info "result: " + result
        result //(AcknowledgeMessage)
    }

    /**
     * Scrape the pullman bus services for a given the date and route
     * @param connectionParams use createConnection()
     * @param route a route to scrape
     * @param date the String date to be used in the scrape with format "dd/MM/yyyy"
     * @return
     */
    def scrapeRoute(connectionParams, route, datesToScrape,companies) {
        SimpleDateFormat dateFormat = new SimpleDateFormat('dd/MM/yyyy')
        datesToScrape.each{date ->

            def pullmanCompanies = companies.collectEntries{ item -> [item.pullmanCode,item]}

            def seatTypes = [:]
            seatTypes << [clasico: SeatType.findOrSaveByName("Clásico")]
            seatTypes << [semiCama: SeatType.findOrSaveByName("Semi Cama")]//SEM*
            seatTypes << [salonCama: SeatType.findOrSaveByName("Salón Cama")]//SAL*
            seatTypes << [premium: SeatType.findOrSaveByName("Premium")]

            log.info "Scraping route $route"
            String formattedDate = dateFormat.format(date)
            deleteTripsByDate(companies, route,date)

            def body = new Object[7]
            body[0] = formattedDate
            body[1] = new CiudadTO(codigo: route.startLocationCode, nombre: route.route.origin.name)
            body[2] = new CiudadTO(codigo: route.endLocationCode, nombre: route.route.destination.name)
            body[3] = "0000"
            body[4] = 0
            body[5] = new EmpresaTO()
            body[6] = 1

            def result
            try {
                result = callAFM(connectionParams, 'getServicios', 'ServiciosBean', body)
            } catch(Exception e){
                closeConnection(connectionParams)
                log.error "Call AFM Exception - sleeping and trying new connection " + e
                Thread.sleep(60000)
                //create new connection and retry
                connectionParams = createConnection()
                result = callAFM(connectionParams, 'getServicios', 'ServiciosBean', body)
            }
            result.body.each { ServicioTO servicioTO ->

                def trip1 = buildTrip(route, pullmanCompanies[servicioTO.empresa], servicioTO, servicioTO.bus.precioPiso1, seatTypes,date)
                try{
                    trip1.save(failOnError : true, flush: true)
                } catch(Exception e){
                    log.error "ERROR SAVING FIRST TRIP ON PULLMAN FLASH-FLEX " + e
                }

                try{
                    if(servicioTO.bus.precioPiso2.clase && servicioTO.bus.precioPiso2.tarifa){ //ignore blank seats and blank fares
                        def trip2 = buildTrip(route, pullmanCompanies[servicioTO.empresa], servicioTO, servicioTO.bus.precioPiso2, seatTypes,date)
                        if(!(trip1.seatType == trip2.seatType && trip1.companyRoute.company == trip2.companyRoute.company && trip1.price == trip2.price && trip1.departureTime == trip2.departureTime && trip1.arrivalTime == trip2.arrivalTime)){
                            trip2.save(failOnError : true, flush: true)
                        }
                    }
                } catch (Exception e){
                    log.error "ERROR SAVING SECOND TRIP ON PULLMAN FLASH-FLEX " + e
                }
            }
        }
    }

    private Trip buildTrip( CompanyRoute companyRoute, Company company, ServicioTO servicioTO, TarifaTO tarifaTO, Map seatTypes, Date date) {

        def trip = new Trip()
        def dateTimeFormat = new SimpleDateFormat('dd/MM/yyyy HHmm')
        trip.price = tarifaTO.tarifa.toInteger()
        trip.departureHour = servicioTO.tramoHoraSalida.substring(0,2).toInteger()
        log.info "FECHA SALIDA:$servicioTO.tramoFechaSalida TRAMO HORA SALIDA:$servicioTO.tramoHoraSalida"
        Date dateFromPullman = dateTimeFormat.parse("$servicioTO.tramoFechaSalida $servicioTO.tramoHoraSalida")

        trip.departureTime = dateFromPullman
        log.info "DEPARTURE TIME: " +trip.departureTime + "*****************************"
        trip.arrivalTime = dateTimeFormat.parse("$servicioTO.tramoFechaLlegada $servicioTO.tramoHoraLlegada")
        //tarifaTO.clase contains the values: [SAL09, SEM42, SEM14, SEM46, SAL12, SAL31, SAL08, SEM45, EJE40, SEM44, SEM16, SEM47]
        def seatType = seatTypes['clasico']
        if(tarifaTO.clase.contains('SEM') || tarifaTO.clase.contains('EJE')){
            seatType = seatTypes['semiCama']
        }else if(tarifaTO.clase.contains('SAL')){
            seatType = seatTypes['salonCama']
        }
        trip.seatType = seatType
        Route route = companyRoute.route
        //see if we need to create a CompanyRoute if the return
        //company is different than the CompanyRoute used to drive the scrape
        CompanyRoute companyRoute2 = CompanyRoute.findOrSaveWhere([
                company: company,
                route: route
        ])
        trip.companyRoute = companyRoute2

        return trip
    }

    def deleteTripsByDate(companies, companyRoute, date){

        Route route = Route.findByOriginAndDestination(companyRoute.route.origin, companyRoute.route.destination)

        Date upperDepartureTime
        use(TimeCategory) {
            upperDepartureTime = date + 23.hours + 59.minutes
        }

        List companyRoutes = CompanyRoute.findAllByRouteAndCompanyInList(route,companies)

        def trips = Trip.findAllByIsTicketableAndCompanyRouteInListAndDepartureTimeBetween(
                false,
                companyRoutes,
                date,
                upperDepartureTime
        )
        trips*.delete(failOnError:true)
    }
}