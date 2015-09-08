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

class AltanetJob  {

    static triggers = {
        //simple name: 'altanetTrigger', startDelay: 1000, repeatInterval: 1000*60*60*24
        //simple name: 'altanetTrigger', startDelay: 1000, repeatInterval: 1000*60*60*24
    }

    def group = "LatAmGroup"
    def description = " Altanet Integration"
    
    Country colombia = Country.findByNameAndCodeAndCurrency("Colombia", "CO", "COP")
    Country chile = Country.findByNameAndCodeAndCurrency("Chile", "CL", "CLP")
    Country peru = Country.findByNameAndCodeAndCurrency("Perú", "PE", "PEN")
    Country argentina = Country.findByNameAndCodeAndCurrency("Argentina", "AR", "ARS")
    Country ecuador = Country.findByNameAndCodeAndCurrency("Ecuador", "EC", "ECS")
    Country bolivia = Country.findByNameAndCodeAndCurrency("Bolivia","BO","BOB")
    
    public void execute(JobExecutionContext context) {
        
        try{
            log.info ' Altanet Update Routes Job'
            def startTime = System.currentTimeMillis()

            def altanet = Company.findByName("Altanet Pasajes")
            createRoutesIfNeeded(altanet)
            

        
        } catch(Throwable e){
            log.error ("Exception in ALTANET Scraper: ",  e)
        }
    }
    

    
    def createRoutesIfNeeded(Company company) {
        // check if there are missing routes
        if(true) {
            log.info "### CREATING NEEDED ROUTES FOR Altanet"
            
            data.each { start ->
                data.each { end ->
                    if(start != end) {
                        //CompanyRoute.withTransaction{
                            //log.info "start.country: " + start.country
                            //log.info "end.country: " + end.country
                            def startLocation = Location.findOrSaveByNameAndCountryAndDescription(start.name, start.country, start.name).save(validate:true)
                            if(startLocation == null){
                                startLocation = new Location()
                                startLocation.name = start.name
                                startLocation.country = start.country
                                startLocation.description = start.name
                                startLocation.save(validate:true)
                            }
                            log.info "saved/found start location ${startLocation}"
                            def endLocation = Location.findOrSaveByNameAndCountryAndDescription(end.name, end.country, end.name).save(validate:true)
                            if(endLocation == null){
                                endLocation = new Location()
                                endLocation.name = start.name
                                endLocation.country = start.country
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
                        //}
                    }
                }
            }
        }
    }
    
    List data = [
        [code: 'Alianza' , name: 'Alianza' , country: chile],
        [code: 'Allen' , name: 'Allen' , country: chile],
        [code: 'Allillay' , name: 'Allillay' , country: chile],
        [code: 'AltoHospicio' , name: 'Alto Hospicio' , country: chile],
        [code: 'Amol' , name: 'Amol' , country: chile],
        [code: 'Ancud' , name: 'Ancud' , country: chile],
        [code: 'Andacollo' , name: 'Andacollo' , country: chile],
        [code: 'Angol' , name: 'Angol' , country: chile],
        [code: 'Antihuala' , name: 'Antihuala' , country: chile],
        [code: 'Antofagasta' , name: 'Antofagasta' , country: chile],
        [code: 'Apacheta' , name: 'Apacheta' , country: chile],
        [code: 'Arica' , name: 'Arica' , country: chile],
        [code: 'ArtifSoprava' , name: 'Artificio Sopraval' , country: chile],
        [code: 'Ayquina' , name: 'Ayquina' , country: chile],
        [code: 'B.Arana' , name: 'Barros Arana' , country: chile],
        [code: 'BuenosAires' , name: 'Buenos Aires', country: argentina],
        [code: 'Baquedano' , name: 'Baquedano' , country: chile],
        [code: 'Barranquilla' , name: 'Barranquilla' , country: chile],
        [code: 'Bulnes' , name: 'Bulnes' , country: chile],
        [code: 'Cr.LlayLlay' , name: 'Llay Llay' , country: chile],
        [code: 'Cr.Contulmo' , name: 'Contulmo' , country: chile],
        [code: 'Cr.Traiguen' , name: 'Traiguen' , country: chile],
        [code: 'Cr.Victoria' , name: 'Victoria' , country: chile],
        [code: 'Cabrero' , name: 'Cabrero' , country: chile],
        [code: 'Calama' , name: 'Calama' , country: chile],
        [code: 'Caldera' , name: 'Caldera' , country: chile],
        [code: 'Calquis' , name: 'Calquis' , country: chile],
        [code: 'Campanario' , name: 'Campanario' , country: chile],
        [code: 'Canelillo' , name: 'Canelillo' , country: chile],
        [code: 'Canelo' , name: 'Canelo' , country: chile],
        [code: 'Cañete' , name: 'Cañete' , country: chile],
        [code: 'C.Pastenes' , name: 'Capitán Pastene' , country: chile],
        [code: 'Carahue' , name: 'Carahue' , country: chile],
        [code: 'Carampangue' , name: 'Carampangue' , country: chile],
        [code: 'Casablanca' , name: 'Casablanca' , country: chile],
        [code: 'Caseron' , name: 'Caseron' , country: chile],
        [code: 'Castro' , name: 'Castro' , country: chile],
        [code: 'Catapilco' , name: 'Catapilco' , country: chile],
        [code: 'Catemu' , name: 'Catemu' , country: chile],
        [code: 'Cauquenes' , name: 'Cauquenes' , country: chile],
        [code: 'Ctro. Calera' , name: 'La Calera' , country: chile],
        [code: 'Cerrillos' , name: 'Cerrillos' , country: chile],
        [code: 'Cerro Alto' , name: 'Cerro Alto' , country: chile],
        [code: 'Cerro Blanco' , name: 'Cerro Blanco' , country: chile],
        [code: 'Chacabuco' , name: 'Chacabuco' , country: chile],
        [code: 'Chalinga' , name: 'Chalinga' , country: chile],
        [code: 'Chamonate' , name: 'Chamonate' , country: chile],
        [code: 'Chañaral' , name: 'Chañaral' , country: chile],
        [code: 'Chillan' , name: 'Chillan' , country: chile],
        [code: 'Chiloe' , name: 'Castro' , country: chile],
        [code: 'Chorrillos' , name: 'Chorrillos' , country: chile],
        [code: 'Chuchini' , name: 'Chuchini' , country: chile],
        [code: 'Cipolletti' , name: 'Cipolletti' , country: chile],
        [code: 'Cobal' , name: 'Cobal' , country: chile],
        [code: 'Coihue' , name: 'Coihueco' , country: chile],
        [code: 'Colchane' , name: 'Colchane' , country: chile],
        [code: 'Colina' , name: 'Colina' , country: chile],
        [code: 'Collipulli' , name: 'Collipulli' , country: chile],
        [code: 'Coñaripe' , name: 'Coñaripe' , country: chile],
        [code: 'Concepcion' , name: 'Concepción' , country: chile],
        [code: 'Concon' , name: 'Con Con' , country: chile],
        [code: 'Constitucion' , name: 'Constitución' , country: chile],
        [code: 'Contulmo' , name: 'Contulmo' , country: chile],
        [code: 'Copiapo' , name: 'Copiapo' , country: chile],
        [code: 'Coquimbo' , name: 'Coquimbo' , country: chile],
        [code: 'Coronel' , name: 'Coronel' , country: chile],
        [code: 'Coyhaique' , name: 'Coyhaique' , country: chile],
        [code: 'Casa Rosada' , name: 'Casa Rosada' , country: chile],
        [code: 'Cr.Caldera' , name: 'Caldera' , country: chile],
        [code: 'Cr.Chañaral' , name: 'Chañaral' , country: chile],
        [code: 'Cr.Laja' , name: 'Laja' , country: chile],
        [code: 'Cr.Taltal' , name: 'Taltal' , country: chile],
        [code: 'Cr.Vallenar' , name: 'Vallenar' , country: chile],
        [code: 'Cr.El radal' , name: 'El Radal' , country: chile],
        [code: 'Cr.San Jose' , name: 'San Jose' , country: chile],
        [code: 'Cr.Tambores' , name: 'Tambores' , country: chile],
        [code: 'Cunco' , name: 'Cunco' , country: chile],
        [code: 'Curacautin' , name: 'Curacautin' , country: chile],
        [code: 'Curacavi' , name: 'Curacavi' , country: chile],
        [code: 'Curanilahue' , name: 'Curanilahue' , country: chile],
        [code: 'Curarrehue' , name: 'Curarrehue' , country: chile],
        [code: 'Curico' , name: 'Curico' , country: chile],
        [code: 'Cutralco' , name: 'Cutralco' , country: chile],
        [code: 'Cuya' , name: 'Cuya' , country: chile],
        [code: 'Cuz Cuz' , name: 'Cuz Cuz' , country: chile],
        [code: 'Don Alfonso' , name: 'Don Alfonso' , country: chile],
        [code: 'Deliber' , name: 'Deliber' , country: chile],
        [code: 'Diego Almagr' , name: 'Diego de Almagro' , country: chile],
        [code: 'Dole' , name: 'Dole' , country: chile],
        [code: 'Elisa Bordos' , name: 'Elisa Bordos' , country: chile],
        [code: 'El Convento' , name: 'El Convento' , country: chile],
        [code: 'El Liuco' , name: 'El Liuco' , country: chile],
        [code: 'El Ajial' , name: 'El Ajial' , country: chile],
        [code: 'El Boldo' , name: 'El Boldo' , country: chile],
        [code: 'El Bosque' , name: 'El Bosque' , country: chile],
        [code: 'El Melon' , name: 'El Melón' , country: chile],
        [code: 'El Palto' , name: 'El Palto' , country: chile],
        [code: 'El Peñon' , name: 'El Peñon' , country: chile],
        [code: 'El Salvador' , name: 'El Salvador' , country: chile],
        [code: 'El Sauce' , name: 'El Sauce' , country: chile],
        [code: 'El Solar' , name: 'El Solar' , country: chile],
        [code: 'Entrelagos' , name: 'Entrelagos' , country: chile],
        [code: 'Ercilla' , name: 'Ercilla' , country: chile],
        [code: 'Est.Central' , name: 'Santiago' , country: chile],
        [code: 'Freire' , name: 'Freire' , country: chile],
        [code: 'Fresia' , name: 'Fresia' , country: chile],
        [code: 'Frutillar' , name: 'Frutillar' , country: chile],
        [code: 'Goyo Diaz' , name: 'Goyo Diaz' , country: chile],
        [code: 'Gorbea' , name: 'Gorbea' , country: chile],
        [code: 'Gral.Roca' , name: 'General Roca' , country: chile],
        [code: 'Guanaqueros' , name: 'Guanaqueros' , country: chile],
        [code: 'Halin' , name: 'Halin' , country: chile],
        [code: 'Hornitos' , name: 'Hornitos' , country: chile],
        [code: 'Hornopiren' , name: 'Hornopiren' , country: chile],
        [code: 'Hualpin' , name: 'Hualpin' , country: chile],
        [code: 'Huara' , name: 'Huara' , country: chile],
        [code: 'Huechuraba' , name: 'Huechuraba' , country: chile],
        [code: 'Huellahue' , name: 'Huellahue' , country: chile],
        [code: 'Huentelauque' , name: 'Huentelauque' , country: chile],
        [code: 'Illapel' , name: 'Illapel' , country: chile],
        [code: 'Inca de oro' , name: 'Inca de Oro' , country: chile],
        [code: 'Iquique' , name: 'Iquique' , country: chile],
        [code: 'Jotabeche' , name: 'Jotabeche' , country: chile],
        [code: 'Jujuy' , name: 'Jujuy', country: argentina],
        [code: 'Junin' , name: 'Junin' , country: chile],
        [code: 'Juntas' , name: 'Juntas' , country: chile],
        [code: 'La Boca' , name: 'La Boca' , country: chile],
        [code: 'La Cora' , name: 'La Cora' , country: chile],
        [code: 'La Cisterna' , name: 'La Cisterna' , country: chile],
        [code: 'La Paz' , name: 'La Paz' , country: chile],
        [code: 'La Variante' , name: 'La Variante' , country: chile],
        [code: 'La Ballena' , name: 'La Ballena' , country: chile],
        [code: 'Labranza' , name: 'Labranza' , country: chile],
        [code: 'La Calera' , name: 'La Calera' , country: chile],
        [code: 'La Cora' , name: 'La Cora' , country: chile],
        [code: 'La Florida' , name: 'La Florida' , country: chile],
        [code: 'La Ligua' , name: 'La Ligua' , country: chile],
        [code: 'Lanalhue' , name: 'Lanalhue' , country: chile],
        [code: 'Lanco' , name: 'Lanco' , country: chile],
        [code: 'La Palma' , name: 'La Palma' , country: chile],
        [code: 'La Pintana' , name: 'La Pintana' , country: chile],
        [code: 'Laraquete' , name: 'Laraquete' , country: chile],
        [code: 'La Reina' , name: 'La Reina' , country: chile],
        [code: 'Las Chilcas' , name: 'Las Chilcas' , country: chile],
        [code: 'Las Encinas' , name: 'Las Encinas' , country: chile],
        [code: 'Las Lajas' , name: 'Las Lajas', country: argentina],
        [code: 'La Serena' , name: 'La Serena' , country: chile],
        [code: 'Las Vacas' , name: 'Las Vacas' , country: chile],
        [code: 'Las Vegas' , name: 'Las Vegas' , country: chile],
        [code: 'La Tirana' , name: 'La Tirana' , country: chile],
        [code: 'La Union' , name: 'La Union' , country: chile],
        [code: 'Lautaro' , name: 'Lautaro' , country: chile],
        [code: 'La Cantera' , name: 'La Cantera' , country: chile],
        [code: 'Lebu' , name: 'Lebu' , country: chile],
        [code: 'Licancheu' , name: 'Licancheu' , country: chile],
        [code: 'Licanray' , name: 'Lican Ray' , country: chile],
        [code: 'Limache' , name: 'Limache' , country: chile],
        [code: 'Linares' , name: 'Linares' , country: chile],
        [code: 'Litueche' , name: 'Litueche' , country: chile],
        [code: 'Liucura' , name: 'Liucura' , country: chile],
        [code: 'Llanquihue' , name: 'Llanquihue' , country: chile],
        [code: 'Llanta' , name: 'Llanta' , country: chile],
        [code: 'Llay llay' , name: 'Llay llay' , country: chile],
        [code: 'Llolleo' , name: 'Llolleo' , country: chile],
        [code: 'Lloros' , name: 'Lloros' , country: chile],
        [code: 'Lo Aguirre' , name: 'Lo Aguirre' , country: chile],
        [code: 'Loncoche' , name: 'Loncoche' , country: chile],
        [code: 'Loncura' , name: 'Loncura' , country: chile],
        [code: 'Lonquimay' , name: 'Lonquimay' , country: chile],
        [code: 'Lo Prado' , name: 'Lo Prado' , country: chile],
        [code: 'Los Lagos' , name: 'Los Lagos' , country: chile],
        [code: 'Los Vilos' , name: 'Los Vilos' , country: chile],
        [code: 'Los Maitenes' , name: 'Los Maitenes' , country: chile],
        [code: 'Los Molles' , name: 'Los Molles' , country: chile],
        [code: 'Los Alamos' , name: 'Los álamos' , country: chile],
        [code: 'Los Andes' , name: 'Los Andes' , country: chile],
        [code: 'Los Angeles' , name: 'Los Angeles' , country: chile],
        [code: 'Los Pinos' , name: 'Los Pinos' , country: chile],
        [code: 'Los Sauces' , name: 'Los Sauces' , country: chile],
        [code: 'Lota' , name: 'Lota' , country: chile],
        [code: 'La Puerta' , name: 'La Puerta' , country: chile],
        [code: 'Lumaco' , name: 'Lumaco' , country: chile],
        [code: 'Mafil' , name: 'Mafil' , country: chile],
        [code: 'Malalhue' , name: 'Malalhue' , country: chile],
        [code: 'Mandinga' , name: 'Mandinga' , country: chile],
        [code: 'Manflas' , name: 'Manflas' , country: chile],
        [code: 'Maria pinto' , name: 'Maria pinto' , country: chile],
        [code: 'Matanzas' , name: 'Matanzas' , country: chile],
        [code: 'Mayorca' , name: 'Mayorca' , country: chile],
        [code: 'Mejillones' , name: 'Mejillones' , country: chile],
        [code: 'Melefquen' , name: 'Melefquen' , country: chile],
        [code: 'Melipeuco' , name: 'Melipeuco' , country: chile],
        [code: 'Melipilla' , name: 'Melipilla' , country: chile],
        [code: 'Mendoza' , name: 'Mendoza', country: argentina],
        [code: 'Millahue' , name: 'Millahue' , country: chile],
        [code: 'Mina Escondi' , name: 'Mina Escondida' , country: chile],
        [code: 'Mininco' , name: 'Mininco' , country: chile],
        [code: 'Ma. Isabel' , name: '' , country: chile],
        [code: 'Montenegro' , name: 'Montenegro' , country: chile],
        [code: 'Mulchen' , name: 'Mulchen' , country: chile],
        [code: 'Nantoco' , name: 'Nantoco' , country: chile],
        [code: 'Navidad' , name: '' , country: chile],
        [code: 'Neuquen' , name: 'Neuquen', country: argentina],
        [code: 'Nilhue' , name: 'Nilhue' , country: chile],
        [code: 'Nogales' , name: 'Nogales' , country: chile],
        [code: 'Noviciado' , name: 'Noviciado' , country: chile],
        [code: 'Nva.Imperial' , name: 'Nueva Imperial' , country: chile],
        [code: 'Ollague' , name: 'Ollague' , country: chile],
        [code: 'Olmue' , name: 'Olmue' , country: chile],
        [code: 'Osorno' , name: 'Osorno' , country: chile],
        [code: 'Ovalle' , name: 'Ovalle' , country: chile],
        [code: 'Pabellon' , name: 'Pabellon' , country: chile],
        [code: 'Paillaco' , name: 'Paillaco' , country: chile],
        [code: 'Pajaritos' , name: 'Santiago' , country: chile],
        [code: 'Panguiles' , name: 'Panguiles' , country: chile],
        [code: 'Panguipulli' , name: 'Panguipulli' , country: chile],
        [code: 'Parral' , name: 'Parral' , country: chile],
        [code: 'Pataguilla' , name: 'Pataguilla' , country: chile],
        [code: 'Peine' , name: 'Peine' , country: chile],
        [code: 'Pelchuquin' , name: 'Pelchuquin' , country: chile],
        [code: 'Peleco' , name: 'Peleco' , country: chile],
        [code: 'Pemuco' , name: 'Pemuco' , country: chile],
        [code: 'Peñablanca' , name: 'Peñablanca' , country: chile],
        [code: 'Peralillo' , name: 'Peralillo' , country: chile],
        [code: 'Perquenco' , name: 'Perquenco' , country: chile],
        [code: 'Pesenti' , name: 'Pesenti' , country: chile],
        [code: 'Pichicuy' , name: 'Pichicuy' , country: chile],
        [code: 'Pichidangui' , name: 'Pichidangui' , country: chile],
        [code: 'Pichilemu' , name: 'Pichilemu' , country: chile],
        [code: 'Pitrufquen' , name: 'Pitrufquen' , country: chile],
        [code: 'Plotier' , name: 'Plotier', country: argentina],
        [code: 'Pozo Almonte' , name: 'Pozo Almonte' , country: chile],
        [code: 'Providencia' , name: 'Providencia' , country: chile],
        [code: 'Pj.San Pedro' , name: 'San Pedro' , country: chile],
        [code: 'Pto.Saavedra' , name: 'Puerto Saavedra' , country: chile],
        [code: 'Puerto viejo' , name: 'Puerto Viejo' , country: chile],
        [code: 'Puchuncavi' , name: 'Puchuncavi' , country: chile],
        [code: 'Pucon' , name: 'Pucón' , country: chile],
        [code: 'Pueblo Seco' , name: 'Pueblo Seco' , country: chile],
        [code: 'Puente Quepe' , name: 'Puente Quepe' , country: chile],
        [code: 'Puerto Montt' , name: 'Puerto Montt' , country: chile],
        [code: 'Puerto Varas' , name: 'Puerto Varas' , country: chile],
        [code: 'Pullally' , name: 'Pullally' , country: chile],
        [code: 'Punta Arenas' , name: 'Punta Arenas' , country: chile],
        [code: 'Pupuya' , name: 'Pupuya' , country: chile],
        [code: 'Puren' , name: 'Puren' , country: chile],
        [code: 'Purmamarca' , name: 'Purmamarca' , country: chile],
        [code: 'Purranque' , name: 'Purranque' , country: chile],
        [code: 'Purulon' , name: 'Purulon' , country: chile],
        [code: 'Qucha' , name: 'Qucha' , country: chile],
        [code: 'Quicha' , name: 'Quicha' , country: chile],
        [code: 'Quidico' , name: 'Quidico' , country: chile],
        [code: 'Quilche' , name: 'Quilche' , country: chile],
        [code: 'Quilimari' , name: 'Quilimari' , country: chile],
        [code: 'Quillagua' , name: 'Quillagua' , country: chile],
        [code: 'Quillota' , name: 'Quillota' , country: chile],
        [code: 'Quilpue' , name: 'Quilpue' , country: chile],
        [code: 'Quintero' , name: 'Quintero' , country: chile],
        [code: 'Quinteros' , name: 'Quinteros' , country: chile],
        [code: 'Quiriquinas' , name: 'Quiriquinas' , country: chile],
        [code: 'Rancagua' , name: 'Rancagua' , country: chile],
        [code: 'Ranchillo' , name: 'Ranchillo' , country: chile],
        [code: 'Rapel' , name: 'Rapel' , country: chile],
        [code: 'Rio blanco' , name: 'Rio blanco' , country: chile],
        [code: 'Renaico' , name: 'Renaico' , country: chile],
        [code: 'Rio bueno' , name: 'Rio bueno' , country: chile],
        [code: 'Roble huacho' , name: 'Roble huacho' , country: chile],
        [code: 'Rodeo' , name: 'Rodeo' , country: chile],
        [code: 'Salamanca' , name: 'Salamanca' , country: chile],
        [code: 'Salta' , name: 'Salta' , country: chile],
        [code: 'San martin' , name: 'San Martin', country: argentina],
        [code: 'San Enrique' , name: 'San Enrique' , country: chile],
        [code: 'San Jose' , name: 'San Jose' , country: chile],
        [code: 'San Juan' , name: 'San Juan' , country: chile],
        [code: 'San Pablo' , name: 'San Pablo' , country: chile],
        [code: 'San Pedro' , name: 'San Pedro' , country: chile],
        [code: 'San Antonio' , name: 'San Antonio' , country: chile],
        [code: 'San Carlos' , name: 'San Carlos' , country: chile],
        [code: 'San Fernando' , name: 'San Fernando' , country: chile],
        [code: 'San Ignacio' , name: 'San Ignacio' , country: chile],
        [code: 'San Ramon' , name: 'San Ramon' , country: chile],
        [code: 'Santa ines' , name: 'Santa Ines' , country: chile],
        [code: 'Santiago' , name: 'Santiago' , country: chile],
        [code: 'San Vicente' , name: 'San Vicente' , country: chile],
        [code: 'Sierra gorda' , name: 'Sierra gorda' , country: chile],
        [code: 'San Isidro' , name: 'San Isidro' , country: chile],
        [code: 'Sn.Pedro Ata' , name: 'San Pedro de Atacama' , country: chile],
        [code: 'Socaire' , name: 'Socaire' , country: chile],
        [code: 'Sopraval' , name: 'Sopraval' , country: chile],
        [code: 'Sta. Barbara' , name: 'Santa Barbara' , country: chile],
        [code: 'Santa Rosa' , name: 'Santa Rosa', country: argentina],
        [code: 'Ter. Borja' , name: 'Santiago' , country: chile],
        [code: 'Ter. Calera' , name: 'La Calera' , country: chile],
        [code: 'Ter. Heroes' , name: 'Santiago' , country: chile],
        [code: 'Ter.Schmidt' , name: 'Teodoro Schmidt' , country: chile],
        [code: 'Ter.Santiago' , name: 'Santiago' , country: chile],
        [code: 'Tal tal' , name: 'Tal Tal' , country: chile],
        [code: 'Talca' , name: 'Talca' , country: chile],
        [code: 'Talcahuano' , name: 'Talcahuano' , country: chile],
        [code: 'Temuco' , name: 'Temuco' , country: chile],
        [code: 'Temuco rural' , name: 'Temuco' , country: chile],
        [code: 'TierraAmaril' , name: 'Tierra Amarilla' , country: chile],
        [code: 'Tijeral' , name: 'Tijeral' , country: chile],
        [code: 'Tirua' , name: 'Tirua' , country: chile],
        [code: 'Tque.Lautaro' , name: 'Lautaro' , country: chile],
        [code: 'Toconao' , name: 'Toconao' , country: chile],
        [code: 'Tocopilla' , name: 'Tocopilla' , country: chile],
        [code: 'Tolten' , name: 'Tolten' , country: chile],
        [code: 'Tongoy' , name: 'Tongoy' , country: chile],
        [code: 'Traiguen' , name: 'Traiguen' , country: chile],
        [code: 'Turbina' , name: 'Turbina' , country: chile],
        [code: 'Uyuni' , name: 'Uyuni', country: bolivia],
        [code: 'Valdivia' , name: 'Valdivia' , country: chile],
        [code: 'Vallenar' , name: 'Vallenar' , country: chile],
        [code: 'Valparaiso' , name: 'Valparaiso' , country: chile],
        [code: 'Ventana' , name: 'Ventana' , country: chile],
        [code: 'Vialidad' , name: 'Vialidad' , country: chile],
        [code: 'Victoria' , name: 'Victoria' , country: chile],
        [code: 'Vicuña' , name: 'Vicuña' , country: chile],
        [code: 'Villa aleman' , name: 'Villa Alemana' , country: chile],
        [code: 'Villarrica' , name: 'Villarrica' , country: chile],
        [code: 'ViñadelMar' , name: 'Viña del Mar' , country: chile],
        [code: 'Vizcacha' , name: 'Vizcacha' , country: chile],
        [code: 'Villa Maria' , name: 'Villa Maria' , country: chile],
        [code: 'Yeso' , name: 'Yeso' , country: chile],
        [code: 'Yumbel' , name: 'Yumbel' , country: chile],
        [code: 'Yungay' , name: 'Yungay' , country: chile],
        [code: 'Zapala' , name: 'Zapala' , country: chile],
        [code: 'Perquenco Pu' , name: 'Perquenco', country: chile],
        [code: 'Zapala' , name: 'Zapala', country: argentina]
    ]



    
}
