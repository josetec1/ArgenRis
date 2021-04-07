package argenris
//todo todo esto es para refactorizar
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

    //todo revisa que pasa si max es A  y ojo con params por que pueden pasar cualquier cosa  ?offset=B
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
      //  respond pacienteService.list(params), model:[pacienteCount: pacienteService.count()]
        respond Paciente.list(params), model:[pacienteCount: Paciente.count()]
    }
    
    //todo refactor ....
    /*
        Busca por nombre y NO ES case case sensitive
        los resultados entan odenados de la a a la z
        Maximos y offsets van de 1 a 1000
        Quiero buscar los que empiezan con d, maximo 1000 resulados, dame los primeros 1000
        http://localhost:8080/pacientes/buscarpornombre/?nombre=d&max=1000&offset=1

        dame to do (tiene un maximo de 1000)
        http://localhost:8080/pacientes/buscarpornombre/
    * */
    def buscarPorNombre(String nombre, Integer max,Integer offset) {
      
        max = Math.min(max ?: 10, 10000)
        offset = Math.min(offset ?: 0, 1000)
        
        if (!nombre) {
            respond Paciente.list(max: 1000,sort: "nombre" , order: "asc")
            return
        }
        
        def paciente =  Paciente.findAllByNombreIlike(nombre+ "%",[max: max, offset: offset, sort: "nombre", order: "asc"])
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

    def obtenerOrdenesDeEstudio (Long id){
       
        respond Paciente.get(id).ordenesDeEstudio
        
    }


}
