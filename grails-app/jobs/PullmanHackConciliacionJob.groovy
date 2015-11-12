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

class PullmanHackConciliacionJob  {

    
    def pullmanService

    
    static triggers = {
        //cron name: 'pullmanConTrigger', cronExpression: "0 0 6 * * ?"
        //simple name: 'pullmanConTrigger', startDelay: 1000, repeatInterval: 1000*60*60*24
    }

    def group = "LatAmGroup"
    def description = "Pullman Conciliacion"


    public void execute(JobExecutionContext context) {
        Conciliacion con = new Conciliacion()
        try{


            Date date = new Date()-9
            date.clearTime()
            
            log.info "date:"+date
            
            
            def result = pullmanService.conciliacionComercioIntegrado(date,date, '82200', '22')
            
            
            con.dateFor = date
            con.dateRun = new Date()
            if (result?.exito ==  "1"){
                con.exitoso = true
                log.info"exito"
            } else {
                con.exitoso = false
            }
            
            
            con.totalCanceledPrice = -1
            con.totalNumberOfTickets = 22
            con.totalTicketsPrice = 82200
            con.mensaje = 'hacked'
            con.save()

            if( result?.exito ==  "1") {
                log.info "exito "
            } 
            
        
            
        } catch(Throwable e){
            log.error ("Exception in Pullman Conciliacion: ",  e)
            con.exception = e.message
            con.save()

        }
    }
    

    


    
}
