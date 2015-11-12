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




import java.text.SimpleDateFormat
import javax.xml.transform.stream.StreamResult

import grails.transaction.Transactional
//To create routes for Altanet
class AltanetJob  {

    static triggers = {
        //simple name: 'altanetTrigger', startDelay: 1000, repeatInterval: 1000*60*60*24
        //simple name: 'altanetTrigger', startDelay: 1000, repeatInterval: 1000*60*60*24
    }

    def group = "LatAmGroup"
    def description = " Altanet Integration"
    
    
    
    
    public void execute(JobExecutionContext context) {
        
        try{
            log.info ' Altanet Update Routes Job'
            def startTime = System.currentTimeMillis()

            def altanet = Company.findByName("Altanet Pasajes")
            createRoutes(altanet)
            

        
        } catch(Throwable e){
            log.error ("Exception in ALTANET Scraper: ",  e)
        }
    }
    

    
    def createRoutes(Company company) {
        // check if there are missing routes
        
        log.info "### CREATING NEEDED ROUTES FOR Altanet"
        Country chile = Country.findByName("Chile")
        Country argentina = Country.findByName("Argentina")
        
        def startcountry
        def endcountry

        def startLocation 
        def endLocation
        log.info "creating start location"
        //startLocation = new Location()
        //startLocation.name = "Salamanca"
        //startLocation.country = chile
        //startLocation.description = "Salamanca"
        //startLocation.save(validate:true, flush:true)
        //log.info "saved start location ${startLocation}"
                        
        data.each { start ->
            data.each { end ->
                if(start != end) {
                    
                        if(start.country == 'Chile') {
                            startcountry = chile
                        } else {
                            startcountry = argentina
                        }
                        if(end.country == 'Chile'){
                            endcountry = chile
                        } else{
                            endcountry = argentina
                        }

                        log.info "start.country: " + start.country
                        log.info "end.country: " + end.country
                        startLocation = Location.findByNameAndCountryAndDescription(start.name, startcountry, start.name)
                        if(startLocation == null){
                            log.info "creating start location:"+start.name
                            startLocation = new Location()
                            startLocation.name = start.name
                            startLocation.country = startcountry
                            startLocation.description = start.name
                            startLocation.save(validate:true)
                        }
                        log.info "saved/found start location ${startLocation}"
                        endLocation = Location.findByNameAndCountryAndDescription(end.name, endcountry, end.name)
                        if(endLocation == null){
                            log.info "creating endlocation:"end.name
                            endLocation = new Location()
                            endLocation.name = start.name
                            endLocation.country = endcountry
                            endLocation.description = start.name
                            endLocation.save(validate:true)
                        }

                        log.info "saved/found end location ${endLocation}"
                        
                        Route route = Route.findWhere([
                            origin: startLocation,
                            destination: endLocation
                        ])

                        if(route == null){
                            route = new Route()
                            route.origin = startLocation
                            route.destination = endLocation
                            route.distance = 0
                            route.duration = 0
                            route.save(validate:true)
                        }
                        log.info "Saved route: ${route}"
                        log.info "company " + company
                        log.info "route " + route
                        log.info "start code " + start.code
                        log.info "end code "  + end.code
                        CompanyRoute companyRoute = CompanyRoute.findOrSaveWhere([
                            company: company,
                            route: route,
                            startLocationCode: start.code,
                            endLocationCode: end.code
                        ]).save(validate:true)
                        log.info "Saved companyRoute: $companyRoute"
                        
                    
                }
            }
        }
        
    }
    
    List data = [
        [code: 'Alianza' , name: 'Alianza' , country: 'Chile'],
        [code: 'Allen' , name: 'Allen' , country: 'Chile'],
        [code: 'Allillay' , name: 'Allillay' , country: 'Chile'],
        [code: 'AltoHospicio' , name: 'Alto Hospicio' , country: 'Chile'],
        [code: 'Amol' , name: 'Amol' , country: 'Chile'],
        [code: 'Ancud' , name: 'Ancud' , country: 'Chile'],
        [code: 'Andacollo' , name: 'Andacollo' , country: 'Chile'],
        [code: 'Angol' , name: 'Angol' , country: 'Chile'],
        [code: 'Antihuala' , name: 'Antihuala' , country: 'Chile'],
        [code: 'Antofagasta' , name: 'Antofagasta' , country: 'Chile'],
        [code: 'Apacheta' , name: 'Apacheta' , country: 'Chile'],
        [code: 'Arica' , name: 'Arica' , country: 'Chile'],
        [code: 'ArtifSoprava' , name: 'Artificio Sopraval' , country: 'Chile'],
        [code: 'Ayquina' , name: 'Ayquina' , country: 'Chile'],
        [code: 'B.Arana' , name: 'Barros Arana' , country: 'Chile'],
        [code: 'Baquedano' , name: 'Baquedano' , country: 'Chile'],
        [code: 'Barranquilla' , name: 'Barranquilla' , country: 'Chile'],
        [code: 'Bulnes' , name: 'Bulnes' , country: 'Chile'],
        [code: 'Cr.LlayLlay' , name: 'Llay Llay' , country: 'Chile'],
        [code: 'Cr.Contulmo' , name: 'Contulmo' , country: 'Chile'],
        [code: 'Cr.Traiguen' , name: 'Traiguen' , country: 'Chile'],
        [code: 'Cr.Victoria' , name: 'Victoria' , country: 'Chile'],
        [code: 'Cabrero' , name: 'Cabrero' , country: 'Chile'],
        [code: 'Calama' , name: 'Calama' , country: 'Chile'],
        [code: 'Caldera' , name: 'Caldera' , country: 'Chile'],
        [code: 'Calquis' , name: 'Calquis' , country: 'Chile'],
        [code: 'Campanario' , name: 'Campanario' , country: 'Chile'],
        [code: 'Canelillo' , name: 'Canelillo' , country: 'Chile'],
        [code: 'Canelo' , name: 'Canelo' , country: 'Chile'],
        [code: 'Cañete' , name: 'Cañete' , country: 'Chile'],
        [code: 'C.Pastenes' , name: 'Capitán Pastene' , country: 'Chile'],
        [code: 'Carahue' , name: 'Carahue' , country: 'Chile'],
        [code: 'Carampangue' , name: 'Carampangue' , country: 'Chile'],
        [code: 'Casablanca' , name: 'Casablanca' , country: 'Chile'],
        [code: 'Caseron' , name: 'Caseron' , country: 'Chile'],
        [code: 'Castro' , name: 'Castro' , country: 'Chile'],
        [code: 'Catapilco' , name: 'Catapilco' , country: 'Chile'],
        [code: 'Catemu' , name: 'Catemu' , country: 'Chile'],
        [code: 'Cauquenes' , name: 'Cauquenes' , country: 'Chile'],
        [code: 'Ctro. Calera' , name: 'La Calera' , country: 'Chile'],
        [code: 'Cerrillos' , name: 'Cerrillos' , country: 'Chile'],
        [code: 'Cerro Alto' , name: 'Cerro Alto' , country: 'Chile'],
        [code: 'Cerro Blanco' , name: 'Cerro Blanco' , country: 'Chile'],
        [code: 'Chacabuco' , name: 'Chacabuco' , country: 'Chile'],
        [code: 'Chalinga' , name: 'Chalinga' , country: 'Chile'],
        [code: 'Chamonate' , name: 'Chamonate' , country: 'Chile'],
        [code: 'Chañaral' , name: 'Chañaral' , country: 'Chile'],
        [code: 'Chillan' , name: 'Chillan' , country: 'Chile'],
        [code: 'Chiloe' , name: 'Castro' , country: 'Chile'],
        [code: 'Chorrillos' , name: 'Chorrillos' , country: 'Chile'],
        [code: 'Chuchini' , name: 'Chuchini' , country: 'Chile'],
        [code: 'Cipolletti' , name: 'Cipolletti' , country: 'Chile'],
        [code: 'Cobal' , name: 'Cobal' , country: 'Chile'],
        [code: 'Coihue' , name: 'Coihueco' , country: 'Chile'],
        [code: 'Colchane' , name: 'Colchane' , country: 'Chile'],
        [code: 'Colina' , name: 'Colina' , country: 'Chile'],
        [code: 'Collipulli' , name: 'Collipulli' , country: 'Chile'],
        [code: 'Coñaripe' , name: 'Coñaripe' , country: 'Chile'],
        [code: 'Concepcion' , name: 'Concepción' , country: 'Chile'],
        [code: 'Concon' , name: 'Con Con' , country: 'Chile'],
        [code: 'Constitucion' , name: 'Constitución' , country: 'Chile'],
        [code: 'Contulmo' , name: 'Contulmo' , country: 'Chile'],
        [code: 'Copiapo' , name: 'Copiapo' , country: 'Chile'],
        [code: 'Coquimbo' , name: 'Coquimbo' , country: 'Chile'],
        [code: 'Coronel' , name: 'Coronel' , country: 'Chile'],
        [code: 'Coyhaique' , name: 'Coyhaique' , country: 'Chile'],
        [code: 'Casa Rosada' , name: 'Casa Rosada' , country: 'Chile'],
        [code: 'Cr.Caldera' , name: 'Caldera' , country: 'Chile'],
        [code: 'Cr.Chañaral' , name: 'Chañaral' , country: 'Chile'],
        [code: 'Cr.Laja' , name: 'Laja' , country: 'Chile'],
        [code: 'Cr.Taltal' , name: 'Taltal' , country: 'Chile'],
        [code: 'Cr.Vallenar' , name: 'Vallenar' , country: 'Chile'],
        [code: 'Cr.El radal' , name: 'El Radal' , country: 'Chile'],
        [code: 'Cr.San Jose' , name: 'San Jose' , country: 'Chile'],
        [code: 'Cr.Tambores' , name: 'Tambores' , country: 'Chile'],
        [code: 'Cunco' , name: 'Cunco' , country: 'Chile'],
        [code: 'Curacautin' , name: 'Curacautin' , country: 'Chile'],
        [code: 'Curacavi' , name: 'Curacavi' , country: 'Chile'],
        [code: 'Curanilahue' , name: 'Curanilahue' , country: 'Chile'],
        [code: 'Curarrehue' , name: 'Curarrehue' , country: 'Chile'],
        [code: 'Curico' , name: 'Curico' , country: 'Chile'],
        [code: 'Cutralco' , name: 'Cutralco' , country: 'Chile'],
        [code: 'Cuya' , name: 'Cuya' , country: 'Chile'],
        [code: 'Cuz Cuz' , name: 'Cuz Cuz' , country: 'Chile'],
        [code: 'Don Alfonso' , name: 'Don Alfonso' , country: 'Chile'],
        [code: 'Deliber' , name: 'Deliber' , country: 'Chile'],
        [code: 'Diego Almagr' , name: 'Diego de Almagro' , country: 'Chile'],
        [code: 'Dole' , name: 'Dole' , country: 'Chile'],
        [code: 'Elisa Bordos' , name: 'Elisa Bordos' , country: 'Chile'],
        [code: 'El Convento' , name: 'El Convento' , country: 'Chile'],
        [code: 'El Liuco' , name: 'El Liuco' , country: 'Chile'],
        [code: 'El Ajial' , name: 'El Ajial' , country: 'Chile'],
        [code: 'El Boldo' , name: 'El Boldo' , country: 'Chile'],
        [code: 'El Bosque' , name: 'El Bosque' , country: 'Chile'],
        [code: 'El Melon' , name: 'El Melón' , country: 'Chile'],
        [code: 'El Palto' , name: 'El Palto' , country: 'Chile'],
        [code: 'El Peñon' , name: 'El Peñon' , country: 'Chile'],
        [code: 'El Salvador' , name: 'El Salvador' , country: 'Chile'],
        [code: 'El Sauce' , name: 'El Sauce' , country: 'Chile'],
        [code: 'El Solar' , name: 'El Solar' , country: 'Chile'],
        [code: 'Entrelagos' , name: 'Entrelagos' , country: 'Chile'],
        [code: 'Ercilla' , name: 'Ercilla' , country: 'Chile'],
        [code: 'Est.Central' , name: 'Santiago' , country: 'Chile'],
        [code: 'Freire' , name: 'Freire' , country: 'Chile'],
        [code: 'Fresia' , name: 'Fresia' , country: 'Chile'],
        [code: 'Frutillar' , name: 'Frutillar' , country: 'Chile'],
        [code: 'Goyo Diaz' , name: 'Goyo Diaz' , country: 'Chile'],
        [code: 'Gorbea' , name: 'Gorbea' , country: 'Chile'],
        [code: 'Gral.Roca' , name: 'General Roca' , country: 'Chile'],
        [code: 'Guanaqueros' , name: 'Guanaqueros' , country: 'Chile'],
        [code: 'Halin' , name: 'Halin' , country: 'Chile'],
        [code: 'Hornitos' , name: 'Hornitos' , country: 'Chile'],
        [code: 'Hornopiren' , name: 'Hornopiren' , country: 'Chile'],
        [code: 'Hualpin' , name: 'Hualpin' , country: 'Chile'],
        [code: 'Huara' , name: 'Huara' , country: 'Chile'],
        [code: 'Huechuraba' , name: 'Huechuraba' , country: 'Chile'],
        [code: 'Huellahue' , name: 'Huellahue' , country: 'Chile'],
        [code: 'Huentelauque' , name: 'Huentelauque' , country: 'Chile'],
        [code: 'Illapel' , name: 'Illapel' , country: 'Chile'],
        [code: 'Inca de oro' , name: 'Inca de Oro' , country: 'Chile'],
        [code: 'Iquique' , name: 'Iquique' , country: 'Chile'],
        [code: 'Jotabeche' , name: 'Jotabeche' , country: 'Chile'],
        [code: 'Junin' , name: 'Junin' , country: 'Chile'],
        [code: 'Juntas' , name: 'Juntas' , country: 'Chile'],
        [code: 'La Boca' , name: 'La Boca' , country: 'Chile'],
        [code: 'La Cora' , name: 'La Cora' , country: 'Chile'],
        [code: 'La Cisterna' , name: 'La Cisterna' , country: 'Chile'],
        [code: 'La Paz' , name: 'La Paz' , country: 'Chile'],
        [code: 'La Variante' , name: 'La Variante' , country: 'Chile'],
        [code: 'La Ballena' , name: 'La Ballena' , country: 'Chile'],
        [code: 'Labranza' , name: 'Labranza' , country: 'Chile'],
        [code: 'La Calera' , name: 'La Calera' , country: 'Chile'],
        [code: 'La Cora' , name: 'La Cora' , country: 'Chile'],
        [code: 'La Florida' , name: 'La Florida' , country: 'Chile'],
        [code: 'La Ligua' , name: 'La Ligua' , country: 'Chile'],
        [code: 'Lanalhue' , name: 'Lanalhue' , country: 'Chile'],
        [code: 'Lanco' , name: 'Lanco' , country: 'Chile'],
        [code: 'La Palma' , name: 'La Palma' , country: 'Chile'],
        [code: 'La Pintana' , name: 'La Pintana' , country: 'Chile'],
        [code: 'Laraquete' , name: 'Laraquete' , country: 'Chile'],
        [code: 'La Reina' , name: 'La Reina' , country: 'Chile'],
        [code: 'Las Chilcas' , name: 'Las Chilcas' , country: 'Chile'],
        [code: 'Las Encinas' , name: 'Las Encinas' , country: 'Chile'],
        [code: 'La Serena' , name: 'La Serena' , country: 'Chile'],
        [code: 'Las Vacas' , name: 'Las Vacas' , country: 'Chile'],
        [code: 'Las Vegas' , name: 'Las Vegas' , country: 'Chile'],
        [code: 'La Tirana' , name: 'La Tirana' , country: 'Chile'],
        [code: 'La Union' , name: 'La Union' , country: 'Chile'],
        [code: 'Lautaro' , name: 'Lautaro' , country: 'Chile'],
        [code: 'La Cantera' , name: 'La Cantera' , country: 'Chile'],
        [code: 'Lebu' , name: 'Lebu' , country: 'Chile'],
        [code: 'Licancheu' , name: 'Licancheu' , country: 'Chile'],
        [code: 'Licanray' , name: 'Lican Ray' , country: 'Chile'],
        [code: 'Limache' , name: 'Limache' , country: 'Chile'],
        [code: 'Linares' , name: 'Linares' , country: 'Chile'],
        [code: 'Litueche' , name: 'Litueche' , country: 'Chile'],
        [code: 'Liucura' , name: 'Liucura' , country: 'Chile'],
        [code: 'Llanquihue' , name: 'Llanquihue' , country: 'Chile'],
        [code: 'Llanta' , name: 'Llanta' , country: 'Chile'],
        [code: 'Llay llay' , name: 'Llay llay' , country: 'Chile'],
        [code: 'Llolleo' , name: 'Llolleo' , country: 'Chile'],
        [code: 'Lloros' , name: 'Lloros' , country: 'Chile'],
        [code: 'Lo Aguirre' , name: 'Lo Aguirre' , country: 'Chile'],
        [code: 'Loncoche' , name: 'Loncoche' , country: 'Chile'],
        [code: 'Loncura' , name: 'Loncura' , country: 'Chile'],
        [code: 'Lonquimay' , name: 'Lonquimay' , country: 'Chile'],
        [code: 'Lo Prado' , name: 'Lo Prado' , country: 'Chile'],
        [code: 'Los Lagos' , name: 'Los Lagos' , country: 'Chile'],
        [code: 'Los Vilos' , name: 'Los Vilos' , country: 'Chile'],
        [code: 'Los Maitenes' , name: 'Los Maitenes' , country: 'Chile'],
        [code: 'Los Molles' , name: 'Los Molles' , country: 'Chile'],
        [code: 'Los Alamos' , name: 'Los álamos' , country: 'Chile'],
        [code: 'Los Andes' , name: 'Los Andes' , country: 'Chile'],
        [code: 'Los Angeles' , name: 'Los Angeles' , country: 'Chile'],
        [code: 'Los Pinos' , name: 'Los Pinos' , country: 'Chile'],
        [code: 'Los Sauces' , name: 'Los Sauces' , country: 'Chile'],
        [code: 'Lota' , name: 'Lota' , country: 'Chile'],
        [code: 'La Puerta' , name: 'La Puerta' , country: 'Chile'],
        [code: 'Lumaco' , name: 'Lumaco' , country: 'Chile'],
        [code: 'Mafil' , name: 'Mafil' , country: 'Chile'],
        [code: 'Malalhue' , name: 'Malalhue' , country: 'Chile'],
        [code: 'Mandinga' , name: 'Mandinga' , country: 'Chile'],
        [code: 'Manflas' , name: 'Manflas' , country: 'Chile'],
        [code: 'Maria pinto' , name: 'Maria pinto' , country: 'Chile'],
        [code: 'Matanzas' , name: 'Matanzas' , country: 'Chile'],
        [code: 'Mayorca' , name: 'Mayorca' , country: 'Chile'],
        [code: 'Mejillones' , name: 'Mejillones' , country: 'Chile'],
        [code: 'Melefquen' , name: 'Melefquen' , country: 'Chile'],
        [code: 'Melipeuco' , name: 'Melipeuco' , country: 'Chile'],
        [code: 'Melipilla' , name: 'Melipilla' , country: 'Chile'],
        [code: 'Mendoza' , name: 'Mendoza', country: 'Argentina'],
        [code: 'Millahue' , name: 'Millahue' , country: 'Chile'],
        [code: 'Mina Escondi' , name: 'Mina Escondida' , country: 'Chile'],
        [code: 'Mininco' , name: 'Mininco' , country: 'Chile'],
        [code: 'Montenegro' , name: 'Montenegro' , country: 'Chile'],
        [code: 'Mulchen' , name: 'Mulchen' , country: 'Chile'],
        [code: 'Nantoco' , name: 'Nantoco' , country: 'Chile'],
        [code: 'Nilhue' , name: 'Nilhue' , country: 'Chile'],
        [code: 'Nogales' , name: 'Nogales' , country: 'Chile'],
        [code: 'Noviciado' , name: 'Noviciado' , country: 'Chile'],
        [code: 'Nva.Imperial' , name: 'Nueva Imperial' , country: 'Chile'],
        [code: 'Ollague' , name: 'Ollague' , country: 'Chile'],
        [code: 'Olmue' , name: 'Olmue' , country: 'Chile'],
        [code: 'Osorno' , name: 'Osorno' , country: 'Chile'],
        [code: 'Ovalle' , name: 'Ovalle' , country: 'Chile'],
        [code: 'Pabellon' , name: 'Pabellon' , country: 'Chile'],
        [code: 'Paillaco' , name: 'Paillaco' , country: 'Chile'],
        [code: 'Pajaritos' , name: 'Santiago' , country: 'Chile'],
        [code: 'Panguiles' , name: 'Panguiles' , country: 'Chile'],
        [code: 'Panguipulli' , name: 'Panguipulli' , country: 'Chile'],
        [code: 'Parral' , name: 'Parral' , country: 'Chile'],
        [code: 'Pataguilla' , name: 'Pataguilla' , country: 'Chile'],
        [code: 'Peine' , name: 'Peine' , country: 'Chile'],
        [code: 'Pelchuquin' , name: 'Pelchuquin' , country: 'Chile'],
        [code: 'Peleco' , name: 'Peleco' , country: 'Chile'],
        [code: 'Pemuco' , name: 'Pemuco' , country: 'Chile'],
        [code: 'Peñablanca' , name: 'Peñablanca' , country: 'Chile'],
        [code: 'Peralillo' , name: 'Peralillo' , country: 'Chile'],
        [code: 'Perquenco' , name: 'Perquenco' , country: 'Chile'],
        [code: 'Pesenti' , name: 'Pesenti' , country: 'Chile'],
        [code: 'Pichicuy' , name: 'Pichicuy' , country: 'Chile'],
        [code: 'Pichidangui' , name: 'Pichidangui' , country: 'Chile'],
        [code: 'Pichilemu' , name: 'Pichilemu' , country: 'Chile'],
        [code: 'Pitrufquen' , name: 'Pitrufquen' , country: 'Chile'],
        [code: 'Pozo Almonte' , name: 'Pozo Almonte' , country: 'Chile'],
        [code: 'Providencia' , name: 'Providencia' , country: 'Chile'],
        [code: 'Pj.San Pedro' , name: 'San Pedro' , country: 'Chile'],
        [code: 'Pto.Saavedra' , name: 'Puerto Saavedra' , country: 'Chile'],
        [code: 'Puerto viejo' , name: 'Puerto Viejo' , country: 'Chile'],
        [code: 'Puchuncavi' , name: 'Puchuncavi' , country: 'Chile'],
        [code: 'Pucon' , name: 'Pucón' , country: 'Chile'],
        [code: 'Pueblo Seco' , name: 'Pueblo Seco' , country: 'Chile'],
        [code: 'Puente Quepe' , name: 'Puente Quepe' , country: 'Chile'],
        [code: 'Puerto Montt' , name: 'Puerto Montt' , country: 'Chile'],
        [code: 'Puerto Varas' , name: 'Puerto Varas' , country: 'Chile'],
        [code: 'Pullally' , name: 'Pullally' , country: 'Chile'],
        [code: 'Punta Arenas' , name: 'Punta Arenas' , country: 'Chile'],
        [code: 'Pupuya' , name: 'Pupuya' , country: 'Chile'],
        [code: 'Puren' , name: 'Puren' , country: 'Chile'],
        [code: 'Purmamarca' , name: 'Purmamarca' , country: 'Chile'],
        [code: 'Purranque' , name: 'Purranque' , country: 'Chile'],
        [code: 'Purulon' , name: 'Purulon' , country: 'Chile'],
        [code: 'Qucha' , name: 'Qucha' , country: 'Chile'],
        [code: 'Quicha' , name: 'Quicha' , country: 'Chile'],
        [code: 'Quidico' , name: 'Quidico' , country: 'Chile'],
        [code: 'Quilche' , name: 'Quilche' , country: 'Chile'],
        [code: 'Quilimari' , name: 'Quilimari' , country: 'Chile'],
        [code: 'Quillagua' , name: 'Quillagua' , country: 'Chile'],
        [code: 'Quillota' , name: 'Quillota' , country: 'Chile'],
        [code: 'Quilpue' , name: 'Quilpue' , country: 'Chile'],
        [code: 'Quintero' , name: 'Quintero' , country: 'Chile'],
        [code: 'Quinteros' , name: 'Quinteros' , country: 'Chile'],
        [code: 'Quiriquinas' , name: 'Quiriquinas' , country: 'Chile'],
        [code: 'Rancagua' , name: 'Rancagua' , country: 'Chile'],
        [code: 'Ranchillo' , name: 'Ranchillo' , country: 'Chile'],
        [code: 'Rapel' , name: 'Rapel' , country: 'Chile'],
        [code: 'Rio blanco' , name: 'Rio blanco' , country: 'Chile'],
        [code: 'Renaico' , name: 'Renaico' , country: 'Chile'],
        [code: 'Rio bueno' , name: 'Rio bueno' , country: 'Chile'],
        [code: 'Roble huacho' , name: 'Roble huacho' , country: 'Chile'],
        [code: 'Rodeo' , name: 'Rodeo' , country: 'Chile'],
        [code: 'Salamanca' , name: 'Salamanca' , country: 'Chile'],
        [code: 'Salta' , name: 'Salta' , country: 'Argentina'],
        [code: 'San Enrique' , name: 'San Enrique' , country: 'Chile'],
        [code: 'San Jose' , name: 'San Jose' , country: 'Chile'],
        [code: 'San Juan' , name: 'San Juan' , country: 'Chile'],
        [code: 'San Pablo' , name: 'San Pablo' , country: 'Chile'],
        [code: 'San Pedro' , name: 'San Pedro' , country: 'Chile'],
        [code: 'San Antonio' , name: 'San Antonio' , country: 'Chile'],
        [code: 'San Carlos' , name: 'San Carlos' , country: 'Chile'],
        [code: 'San Fernando' , name: 'San Fernando' , country: 'Chile'],
        [code: 'San Ignacio' , name: 'San Ignacio' , country: 'Chile'],
        [code: 'San Ramon' , name: 'San Ramon' , country: 'Chile'],
        [code: 'Santa ines' , name: 'Santa Ines' , country: 'Chile'],
        [code: 'Santiago' , name: 'Santiago' , country: 'Chile'],
        [code: 'San Vicente' , name: 'San Vicente' , country: 'Chile'],
        [code: 'Sierra gorda' , name: 'Sierra gorda' , country: 'Chile'],
        [code: 'San Isidro' , name: 'San Isidro' , country: 'Chile'],
        [code: 'Sn.Pedro Ata' , name: 'San Pedro de Atacama' , country: 'Chile'],
        [code: 'Socaire' , name: 'Socaire' , country: 'Chile'],
        [code: 'Sopraval' , name: 'Sopraval' , country: 'Chile'],
        [code: 'Sta. Barbara' , name: 'Santa Barbara' , country: 'Chile'],
        [code: 'Ter. Borja' , name: 'Santiago' , country: 'Chile'],
        [code: 'Ter. Calera' , name: 'La Calera' , country: 'Chile'],
        [code: 'Ter. Heroes' , name: 'Santiago' , country: 'Chile'],
        [code: 'Ter.Schmidt' , name: 'Teodoro Schmidt' , country: 'Chile'],
        [code: 'Ter.Santiago' , name: 'Santiago' , country: 'Chile'],
        [code: 'Tal tal' , name: 'Tal Tal' , country: 'Chile'],
        [code: 'Talca' , name: 'Talca' , country: 'Chile'],
        [code: 'Talcahuano' , name: 'Talcahuano' , country: 'Chile'],
        [code: 'Temuco' , name: 'Temuco' , country: 'Chile'],
        [code: 'Temuco rural' , name: 'Temuco' , country: 'Chile'],
        [code: 'TierraAmaril' , name: 'Tierra Amarilla' , country: 'Chile'],
        [code: 'Tijeral' , name: 'Tijeral' , country: 'Chile'],
        [code: 'Tirua' , name: 'Tirua' , country: 'Chile'],
        [code: 'Tque.Lautaro' , name: 'Lautaro' , country: 'Chile'],
        [code: 'Toconao' , name: 'Toconao' , country: 'Chile'],
        [code: 'Tocopilla' , name: 'Tocopilla' , country: 'Chile'],
        [code: 'Tolten' , name: 'Tolten' , country: 'Chile'],
        [code: 'Tongoy' , name: 'Tongoy' , country: 'Chile'],
        [code: 'Traiguen' , name: 'Traiguen' , country: 'Chile'],
        [code: 'Turbina' , name: 'Turbina' , country: 'Chile'],
        [code: 'Valdivia' , name: 'Valdivia' , country: 'Chile'],
        [code: 'Vallenar' , name: 'Vallenar' , country: 'Chile'],
        [code: 'Valparaiso' , name: 'Valparaiso' , country: 'Chile'],
        [code: 'Ventana' , name: 'Ventana' , country: 'Chile'],
        [code: 'Vialidad' , name: 'Vialidad' , country: 'Chile'],
        [code: 'Victoria' , name: 'Victoria' , country: 'Chile'],
        [code: 'Vicuña' , name: 'Vicuña' , country: 'Chile'],
        [code: 'Villa aleman' , name: 'Villa Alemana' , country: 'Chile'],
        [code: 'Villarrica' , name: 'Villarrica' , country: 'Chile'],
        [code: 'ViñadelMar' , name: 'Viña del Mar' , country: 'Chile'],
        [code: 'Vizcacha' , name: 'Vizcacha' , country: 'Chile'],
        [code: 'Villa Maria' , name: 'Villa Maria' , country: 'Chile'],
        [code: 'Yeso' , name: 'Yeso' , country: 'Chile'],
        [code: 'Yumbel' , name: 'Yumbel' , country: 'Chile'],
        [code: 'Yungay' , name: 'Yungay' , country: 'Chile'],
        [code: 'Zapala' , name: 'Zapala' , country: 'Chile'],
        [code: 'Perquenco Pu' , name: 'Perquenco', country: 'Chile']
    ]



    
}
