import latambuses.*
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.commons.lang.WordUtils
import org.quartz.JobExecutionException
import org.quartz.Job
import org.quartz.JobExecutionContext


import cl.pullman.webservices.*
import grails.converters.JSON
import grails.converters.XML

import groovy.time.TimeCategory


import java.text.SimpleDateFormat
import javax.xml.transform.stream.StreamResult
import java.text.SimpleDateFormat

import grails.transaction.Transactional

class PullmanConciliacionJob  {

    
    def pullmanService

    
    static triggers = {
        cron name: 'pullmanXMLTrigger', cronExpression: "0 0 6 * * ?"
        //simple name: 'pullmanXMLTrigger', startDelay: 1000, repeatInterval: 1000*60*60*24
    }

    def group = "LatAmGroup"
    def description = "Pullman Conciliacion"


    public void execute(JobExecutionContext context) {
        
        try{
            log.info 'Conciliacion PullmanXML  Job'
            List companies = []
            companies << Company.findByName("pullman bus")
            companies << Company.findByName("elqui bus")
            companies << Company.findByName("atacama vip")
            companies << Company.findByName("los corsarios")
            companies << Company.findByName("fichtur vip")
            companies << Company.findByName("los libertadores")
            companies << Company.findByName("los conquistadores")
            companies << Company.findByName("cidher")

            Date date = new Date()-1
            date.clearTime()
            Date upperDepartureTime
            use(TimeCategory) {
                upperDepartureTime = date + 23.hours + 59.minutes
            }

            log.info "date:"+date
            log.info "upperDepartureTime:"+upperDepartureTime
            def tickets = Ticket.findAllByPullmanIssueDateBetweenAndCompanyInList(
                    date,
                    upperDepartureTime,
                    companies
            )
            
            def result = [exito:'']
            
            Integer total = 0
            if(tickets){
                total = tickets.price.sum()
            } 

            def canceled = tickets.findAll{it.status == 'Canceled'}
            Integer totalCanceled = 0
            if(canceled){
                totalCanceled = canceled.price.sum()
            }
            String monto = (total - totalCanceled).toString()
            String cantidad = tickets.size().toString()
            log.info "total canceled:"+totalCanceled
            log.info "total:"+ total
            log.info "monto:"+monto
            log.info "cantidad:"+cantidad
            result = pullmanService.conciliacionComercioIntegrado(date,date, monto, cantidad)

            if( result?.exito ==  "1") {
                log.info "exito "
            } else {
                def codConciliacion = result?.codConciliacion
                log.info "running detalle conciliacion con codigo:"+codConciliacion
                //detalle conciliacion
                def detalle = "<ventacomercio>"
                
                tickets.each{Ticket ticket ->
                    def template = """
                        <detalleventa>
                            <codigo>INT0000010</codigo>
                            <punto>VOY01</punto>
                            <boleto>${ticket.ticketNumber}</boleto>
                            <origen>${ticket.trip.companyRoute.startLocationCode}</origen>
                            <destino>${ticket.trip.companyRoute.endLocationCode}</destino> 
                            <fechatransaccion>${ticket.pullmanIssueDate.format('dd-MM-YYYY')}</fechatransaccion>
                            <valor>${ticket.pricePaid.toInteger().toString()}</valor> 
                            <idconciliacion>${codConciliacion}</idconciliacion>
                        </detalleventa>
                    """
                    detalle = detalle + template 
                }
                detalle = detalle + "</ventacomercio>"
                
                result = pullmanService.detalleConciliacion(detalle)
                log.info "detalle:"+ result
            }
        
       
            
        } catch(Throwable e){
            log.error ("Exception in Pullman Conciliacion: ",  e)
        }
    }
    

    


    
}
