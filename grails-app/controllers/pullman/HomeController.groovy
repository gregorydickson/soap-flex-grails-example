package pullman

import grails.converters.JSON
import groovy.time.TimeCategory
import latambuses.Conciliacion

import java.text.SimpleDateFormat

class HomeController {

    static allowedMethods = [
        conciliar: 'POST'
    ]

    def pullmanService
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd")

    def index() {
        render(view:"/home/index")
    }

    def conciliar(){
        Conciliacion con = new Conciliacion()
        String conciliacionMessage = "ups! Something went wrong"
        Boolean response = false
        try{

            Date date = dateFormat.parse((String)params.date)
            date.clearTime()

            String amount = ((String)params.amount).replace(".", "")
            String amountPaid = ((String)params.amountPaid).replace(".", "")
            String pax = ((String)params.pax).replace(".", "")

            Map result = pullmanService.conciliacionComercioIntegrado(date, date, amount, pax)
            conciliacionMessage = result?.mensaje ? result?.mensaje : "ups! Something went wrong"
            response = result?.exito == "1"

            Date now = new Date()
            //Add chilean time
            use(TimeCategory) {
                now = date - 3.hours
            }

            con.dateFor = date
            con.dateRun = now
            con.exitoso = response
            con.totalCanceledPrice = Integer.valueOf(amountPaid) ? Integer.valueOf(amountPaid) : -1
            con.totalNumberOfTickets = Integer.valueOf(amount) ? Integer.valueOf(amount) : -1
            con.totalTicketsPrice = Integer.valueOf(pax) ? Integer.valueOf(pax) : -1
            con.mensaje = 'hacked'
            con.save()

        } catch(Throwable e){
            log.error ("Exception in Pullman Conciliacion: ",  e)
            con.exception = e.message
            con.save()
        }

        render(['response': response, 'responseMessage': conciliacionMessage] as JSON)
    }
}