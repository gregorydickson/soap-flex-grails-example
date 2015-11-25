package pullman

class HomeController {

    static allowedMethods = [
        conciliar: 'POST'
    ]

    def index() {
        render(view:"/home/index")
    }

    def conciliar(){
        log.info params
    }
}