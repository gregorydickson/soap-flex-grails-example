import latambuses.*
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.commons.lang.WordUtils
import org.quartz.JobExecutionException
import org.quartz.Job
import org.quartz.JobExecutionContext



import grails.converters.JSON
import grails.converters.XML

import groovy.time.TimeCategory


import java.text.SimpleDateFormat
import javax.xml.transform.stream.StreamResult
import java.text.SimpleDateFormat

import grails.transaction.Transactional

class AlertPaymentOrderErrorsJob  {

    def emailService
    
    static triggers = {
        cron name: 'alertPaymentOrderTrigger', cronExpression: "0 0/10 * * * ?"
        //simple name: 'alertPaymentOrderTrigger', startDelay: 1000, repeatInterval: 1000*60*60*24
    }

    def group = "LatAmGroup"
    def description = "Alert Payment OrderErrors "


    public void execute(JobExecutionContext context) {
        
        try{
            log.info 'alert pament order error   Job'
            


            Date endTime = new Date()
            
            Date startTime
            use(TimeCategory) {
                startTime = endTime - 10000000.minutes
            }
            
            def errors = ['Locked','Paid but failed in the company']
            
            def paymentOrders = PaymentOrder.findAllByLastUpdatedBetweenAndStatusInList(
                    startTime,
                    endTime,
                    errors
            )

            Integer count = paymentOrders.size()
            log.info "returned from db findAll with:"+count
            def message = ''
            paymentOrders.each{po ->
                message = message +  "id:"+po.id+"<br>"
                message = message + "status:"+po.status+"<br>"
                message = message +  "updated:"+po.lastUpdated+"<br>" 
                message = message +"<br>"
            }

            if(count > 0)
                log.info "sending error email"
                emailService.paymentOrderError(message)

            log.info "alert payment order error job complete"
        } catch(Throwable e){
            log.error ("Exception in  Payment Orde Errrors Job: ",  e)

        }
    }
    

    


    
}
