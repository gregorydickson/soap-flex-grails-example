package pullman

class HomeController {

    static allowedMethods = [
        conciliar: 'POST'
    ]

    def index() {
        render(view:"/home/index")
    }

    def conciliar(){
        String startDate = params.starDate
        String endDate = params.endDate
        Integer amount = Integer.valueOf(((String)params.amount).replace(".", ""))
        Integer pax = Integer.valueOf(((String)params.pax).replace(".", ""))
        log.info params
    }
}