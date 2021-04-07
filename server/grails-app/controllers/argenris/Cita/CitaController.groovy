package argenris.Cita

import grails.validation.ValidationException

import java.time.LocalDateTime
import java.time.ZoneId

import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional



class CitaCommand{
    
    Date fechaYHoraActual
    
    static constraints = {
        fechaYHoraActual nullable: false, blank: false, date: true
    }
}





@ReadOnly
class CitaController {

   // CitaService citaService

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
      //  respond citaService.list(params), model:[citaCount: citaService.count()]
        respond Cita.list(params), model:[citaCount: Cita.count()]
    }

    def show(Long id) {
       // respond citaService.get(id)
        respond Cita.get(id)
    }

    //todo este controller no va, hay que sacarlo. la cita se crea por la orden.agregarCita
    @Transactional
    def save(Cita cita) {
        if (cita == null) {
            render status: NOT_FOUND
            return
        }
        if (cita.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond cita.errors
            return
        }

        try {
          //  citaService.save(cita)
            cita.save(failOnError : true)
        } catch (ValidationException e) {
            respond cita.errors
            return
        }

        respond cita, [status: CREATED, view:"show"]
    }
    
    @Transactional  //todo hacer bien
    def cancelar(CitaCommand cmd ) {
    
        def citaID
        if ( params.id.isNumber()  && params.id.toLong() >0){
            citaID = params.id.toLong()
        }else {
            render status: BAD_REQUEST
            return
        }
    
    
        if(!cmd.hasErrors()) {
    
            LocalDateTime fechaYHoraActualConvertida = LocalDateTime.ofInstant(cmd.fechaYHoraActual.toInstant(), ZoneId.systemDefault());
            try {
        
                def cita = Cita.get(citaID)
        
                if (!cita) throw new Exception("Error: no existe la cita")
                cita.cancelar(fechaYHoraActualConvertida)
        
        
                //si no lo encuentra devuelve NOT_FOUND automaticamente.... no se si esta bueno dejarlo asi
                respond cita
        
            } catch (e) {
                respond (text: e.message, status: BAD_REQUEST)
            }
           
        }
        else {
            respond cmd.errors, view:'create'
        }
    }
    
    
    
/*
    @Transactional
    def update(Cita cita) {
        if (cita == null) {
            render status: NOT_FOUND
            return
        }
        if (cita.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond cita.errors
            return
        }

        try {
            citaService.save(cita)
        } catch (ValidationException e) {
            respond cita.errors
            return
        }

        respond cita, [status: OK, view:"show"]
    }

    @Transactional
    def cancelar(Long id) {
        if (id == null || citaService.cancelar(id) == null) {
            render status: NOT_FOUND
            return
        }

        render status: NO_CONTENT
    }
  */
 
}
