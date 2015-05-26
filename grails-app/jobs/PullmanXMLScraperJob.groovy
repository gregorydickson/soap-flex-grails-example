import latambuses.*
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.commons.lang.WordUtils
import org.quartz.JobExecutionException
import org.quartz.Job
import org.quartz.JobExecutionContext
import static grails.async.Promises.*
import groovyx.gpars.GParsPool
import groovy.time.TimeCategory

import latambuses.*
import cl.pullman.webservices.*

import java.text.SimpleDateFormat

import grails.transaction.Transactional

class PullmanXMLScraperJob  {

    
    def pullmanService

    
    static triggers = {
        //cron name: 'pullmanXMLTrigger', cronExpression: "0 0 1 * * ?"
        simple name: 'pullmanXMLTrigger', startDelay: 1000, repeatInterval: 1000*60*60*24
    }

    def group = "LatAmGroup"
    def description = "Bus pullmanXML scraper"


    public void execute(JobExecutionContext context) {
        
        try{
            log.info 'Scraping PullmanXML Routes Job'
            def startTime = System.currentTimeMillis()


            //updateCitiesAndroutes()
            
            def routeIds = []
            log.info "LOCATIONS ROUTES COMPANYROUTES PULLMAN DONE ********"
            def origin = Location.findByName("Santiago")
            def dest = Location.findByName("Temuco")
            def someRoutes = Route.findAllByOriginAndDestination(origin,dest)
            def pullman = Company.findByName("pullman bus")
            def routes = []
            routes = CompanyRoute.findAllByCompany(pullman)
            //routes = CompanyRoute.findAllByRouteInListAndCompany(someRoutes,pullman)
            log.info "PULLMAN--XML Staring New Scrape"
            routes.collect{routeIds << it.id}
            PullmanClient getsessionclient = new PullmanClient()
            def sessionId = getsessionclient.startSession()
            if(routes) {
                GParsPool.withPool(40) {
                    routeIds.eachParallel { id ->
                        try {
                            CompanyRoute.withNewSession{
                                PullmanClient pullmanClient = new PullmanClient()
                                pullmanClient.id = sessionId
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
                                company = Company.findByName( "cidher")
                                companies << company
                                
                                def scrapeStart = new Date()
                                def datesToScrape = []
                                def date = new Date()
                                date.clearTime()
                                //Pullman only allows +2 in BuscaSalidaServicioTarifa
                                for ( i in 2..31 ) {
                                    datesToScrape << (date + i)
                                }
                                
                                
                                def route = CompanyRoute.get(id)
                                
                                processRoute(route, datesToScrape,companies, scrapeStart, pullmanClient)
                                route.lastScrapedDate = new Date()
                                route.save(failOnError:true)
                                log.info "Unscraped the Route PULLMAN"
                            }
                        } catch (Exception e){
                            log.error ("Exception in PULLMAN Route : ",  e)
                        }
                    }   
                }
            }

            log.info("Took ${System.currentTimeMillis() - startTime} ms to scrape ${routes?.size()} pullam routes job")
        
        } catch(Throwable e){
            log.error ("Exception in Pullman Scraper: ",  e)
        }
    }
    

    def processRoute( route, datesToScrape,companies, 
                    scrapeStart, PullmanClient pullmanClient) {
        datesToScrape.each{date ->
            log.info "Scraping Pullman $route  $date"
            def startTime = System.currentTimeMillis()

            
            def pullmanCompanies = companies.collectEntries{ item -> [item.pullmanCode,item]}
            
            def seatTypes = [:]
            seatTypes << [clasico: SeatType.findOrSaveByName("Clásico")]
            seatTypes << [semiCama: SeatType.findOrSaveByName("Semi Cama")]//SEM*
            seatTypes << [salonCama: SeatType.findOrSaveByName("Salón Cama")]//SAL*
            seatTypes << [premium: SeatType.findOrSaveByName("Premium")]
            
            def pullmanDate = new SimpleDateFormat('yyyyMMdd')
            
            deleteTripsByDate(companies,route,date)
            

            def result
            try {
                try{
                    result = pullmanClient.buscaSalidaServicioTarifa(pullmanClient.id, route.startLocationCode,route.endLocationCode, pullmanDate.format(date))
                }catch (org.springframework.ws.soap.client.SoapFaultClientException e){
                    log.error "PULLMAN CLIENT ERROR" + e
                    def id = pullmanClient.startSession()
                    pullmanClient.id = id
                    result = pullmanClient.buscaSalidaServicioTarifa(pullmanClient.id, route.startLocationCode,route.endLocationCode, pullmanDate.format(date))
                }
                result.each{salidaServicio->
                    buildTrip(route, pullmanCompanies, salidaServicio, seatTypes,date)
                }
                
            } catch(Exception e){
                log.error "####EXCEPTION PULLMAN scrape $route" + e
            }
            
        }
    }

    Trip buildTrip( def companyRoute, def pullmanCompanies,
                    SalidaServicio info, def seatTypes, Date date) {

        log.info "**********************  BUILDING a PULLMAN TRIP *****************************"
        def pullmanReturnDate = new SimpleDateFormat('ddMMyyyy')
        def dateTimeFormat = new SimpleDateFormat('ddMMyyyy HHmm')
        def trip = new Trip()

        trip.price = info.tarifa.toInteger()
        def departureHour = info.horaSalida.substring(0,2).toInteger()
        log.info "horaSalida"+ info.horaSalida
        log.info "departureHour"+departureHour
        def departureMin = info.horaSalida.substring(2,4).toInteger()
        log.info "departureMin"+departureMin
        
        Date departure  
        
        use(TimeCategory){
            departure = date +  departureHour.hours + departureMin.minutes
            log.info "depature: "+departure
        }
        
        trip.departureTime = departure
        trip.departureHour = departureHour
        
        log.info "info.fechaLlegada:"+info.fechaLlegada+",horaLlegada"+info.horaLlegada
        trip.arrivalTime = dateTimeFormat.parse("$info.fechaLlegada $info.horaLlegada")
        //clase contains the values: [SAL09, SEM42, SEM14, SEM46, SAL12, SAL31, SAL08, SEM45, EJE40, SEM44, SEM16, SEM47]
        def seatType = seatTypes['clasico']
        if(info.clasePiso.contains('SEM') || info.clasePiso.contains('EJE')){
            seatType = seatTypes['semiCama']
        }else if(info.clasePiso.contains('SAL')){
            seatType = seatTypes['salonCama']
        }
        trip.seatType = seatType
        
        Company company = pullmanCompanies[info.codigoEmpresa]
        log.info "Empresa in return data is:"+ info.codigoEmpresa
        Route route = companyRoute.route
        //see if we need to create a CompanyRoute if the return
        //company is different than the CompanyRoute used to drive the scrape
        CompanyRoute companyRoute2 = CompanyRoute.findOrSaveWhere([
            company: company,
            route: route
        ])
        log.info "GOT the CompanyRoute"
        def bus = Bus.findOrSaveByBusNumber(info.numeroBus)
        log.info "GOT the Bus"
        trip.bus = bus
        trip.internalTripID = info.idServicio
        trip.codigoTerminalOrigen = info.codigoOrigenTerminal
        trip.codigoTerminalDestino = info.codigoDestinoTerminal
        trip.companyRoute = companyRoute2
        trip.isTicketable = true
        trip.showInSearch = false
        trip.save(failOnError:true)
        log.info "**********************  BUILT a PULLMAN TRIP *****************************"
        return trip
    }

    def deleteTripsByDate(companies, companyRoute, date){
        
        Route route = Route.findByOriginAndDestination(companyRoute.route.origin, companyRoute.route.destination)

        Date upperDepartureTime
        use(TimeCategory) {
            upperDepartureTime = date + 23.hours + 59.minutes
        }
        log.info "DELETING DATE:"+ date
        log.info "DELETING DATE2:"+upperDepartureTime
        log.info "CompanyRoute:"+companyRoute.id
        List companyRoutes = CompanyRoute.findAllByRouteAndCompanyInList(route,companies)
        
        def trips = Trip.findAllByIsTicketableAndCompanyRouteInListAndDepartureTimeBetween(
                true,
                companyRoutes,
                date,
                upperDepartureTime
        )
        
        trips*.delete(failOnError:true,flush:true)
        
    }

    def updateCitiesAndroutes(){
        log.info "STARING GET PULLMAN CITIES"
            //pull cities and create/update routes 
            def ciudades = pullmanService.ciudades()
            log.info "STARING GET PULLMAN CITIES"
            def destinos = [:]
            ciudades.each{ciudad ->
                log.info "GETTING A LIST OF DESTINOS for:" + ciudad.nombre
                destinos << [(ciudad):pullmanService.origenDestino(ciudad.codigo)]
            }
            log.info "******* RETRIEVED PULLMAN CIUDADES  ****************"

            Country chile = Country.findByNameAndCodeAndCurrency("Chile", "CL", "CLP")
            def pullman = Company.findByName("pullman bus")
            
            destinos.each{origen,dest->
                log.info "ORIGEN " + origen.nombre 
                dest.each{destino->
                    log.info "Destino:" + destino.nombre

                    CompanyRoute.withTransaction{
                        def startLocation = Location.findOrSaveWhere([
                            name:WordUtils.capitalizeFully(origen.nombre),
                            country:chile, 
                            description:WordUtils.capitalizeFully(origen.nombre)
                        ])
                        log.info "saved/found start location ${startLocation}"
                        def endLocation = Location.findOrSaveWhere([
                            name: WordUtils.capitalizeFully(destino.nombre),
                            country: chile,
                            description: WordUtils.capitalizeFully(destino.nombre)
                        ])
                        log.info "saved/found end location ${endLocation}"
                        Route route = Route.findOrSaveWhere([
                            origin: startLocation,
                            destination: endLocation
                        ])
                        log.info "Saved route: ${route}"
                        log.info "route " + route
                        log.info "start code " + origen.codigo
                        log.info "end code "  + destino.codigo
                        CompanyRoute companyRoute = CompanyRoute.findOrSaveWhere([
                            company: pullman,
                            route: route,
                            startLocationCode: origen.codigo,
                            endLocationCode: destino.codigo
                        ])
                        log.info "Saved companyRoute: $companyRoute" 
                    }
                }
            }

    }
    


    
}
