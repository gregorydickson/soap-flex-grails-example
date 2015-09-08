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
            /*
para el día 6 tengo

ventas :14.300
transacciones:12


para el día 7 tengo 69.100 
transacciones: 15

            */

            Date date = new Date()-14
            date.clearTime()
            
            log.info "date:"+date
            
            
            def result = pullmanService.conciliacionComercioIntegrado(date,date, '56700', '14')
            
            
            con.dateFor = date
            con.dateRun = new Date()
            if (result?.exito ==  "1"){
                con.exitoso = true
                log.info"exito"
            } else {
                con.exitoso = false
            }
            
            
            con.totalCanceledPrice = -1
            con.totalNumberOfTickets = -1
            con.totalTicketsPrice = 73600
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
