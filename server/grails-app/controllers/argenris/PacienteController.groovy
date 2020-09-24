package argenris
//todo implementar
import grails.validation.ValidationException

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

@ReadOnly
class PacienteController {

  //  PacienteService pacienteService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
      //  respond pacienteService.list(params), model:[pacienteCount: pacienteService.count()]
        respond Paciente.list(params), model:[pacienteCount: Paciente.count()]
    }
    
    //todo refactor ....
    //si no encuentra nada devuelve lista vacia, sino lista de lo que encontro
    def buscarPorNombre(String nombre) {
        //  respond pacienteService.list(params), model:[pacienteCount: pacienteService.count()]
       // render "hola"
        if (!nombre) {
            render status: BAD_REQUEST
            return
        }
       
        def paciente=  Paciente.findAllByNombreLike(nombre + "%")
        
       // Book.findAllByTitleLike("Harry Pot%",
     //           [max: 3, offset: 2, sort: "title", order: "desc"])
        
            respond paciente
    }
    

    def show(Long id) {
        //respond pacienteService.get(id)
        respond Paciente.get(id)
    }

    @Transactional
    def save(Paciente paciente) {
        if (paciente == null) {
            render status: NOT_FOUND
            return
        }
        if (paciente.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond paciente.errors
            return
        }

        try {
            //pacienteService.save(paciente)
            paciente.save()
        } catch (ValidationException e) {
            respond paciente.errors
            return
        }

        respond paciente, [status: CREATED, view:"show"]
    }




}
