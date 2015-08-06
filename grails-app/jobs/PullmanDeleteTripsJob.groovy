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

class PullmanDeleteTripsJob  {

    
    def pullmanService

    
    static triggers = {
        //cron name: 'pullmanConTrigger', cronExpression: "0 0 6 * * ?"
        //simple name: 'pullmanDeleteTrigger', startDelay: 1000, repeatInterval: 1000*60*60*24
    }

    def group = "LatAmGroup"
    def description = "Pullman Delete trips"


    public void execute(JobExecutionContext context) {
        
        try{
            log.info 'delete trips Pullman  Job'
            List companies = []
            companies << Company.findByName("pullman bus")
            companies << Company.findByName("elqui bus")
            companies << Company.findByName("atacama vip")
            companies << Company.findByName("los corsarios")
            companies << Company.findByName("fichtur vip")
            companies << Company.findByName("los libertadores")
            companies << Company.findByName("los conquistadores")
            companies << Company.findByName("cidher")

            List cr = CompanyRoute.findAllByCompanyInList(companies)
            
            def trips = Trip.findAllByIsTicketableAndCompanyRouteInList(
                    false,
                    cr
            )
            def count = trips.size()
            log.info "deleting: "+ count
            trips*.delete(flush:true)
            log.info "DELETED"
            
            
        } catch(Throwable e){
            log.error ("Exception in Pullman Conciliacion: ",  e.message)
            
            

        }
    }
    

    


    
}
