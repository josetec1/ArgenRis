package argenris

import argenris.Cita.Cita
import argenris.OrdenDeEstudio.OrdenDeEstudio
import grails.gorm.transactions.*
import org.apache.tools.ant.taskdefs.condition.Or

import java.time.Instant
import java.time.ZoneId

import static org.springframework.http.HttpStatus.*
import java.time.LocalDateTime

//todo todo esto es para refactorizar
//A1  Defino un command para la validacion de los imputs que deben cargarse por la vista
class CreacionCommand{
    
    Long pacienteId
    Long procedimientoId
    String nota
    Prioridad prioridad  // todo podria ser id y buscarla en el repositorio (?)
    Date fecha
   // Date dateOfBirth
    
     static constraints = {
   //todo revisar estas validaciones
         pacienteId nullable: false   //en numero blank no tiene sentido
         procedimientoId nullable: false
         nota nullable: false, blank: false
         prioridad nullable: false, blank: false
         fecha nullable: false, blank: false, date: true
      //  dateOfBirth blank: false, date: true, validator: { val -> validateDate(val) } //todo mirar este metodo
    }
}

class CitaCommand{
    
    Long salaId
    Date fechaYHoraActual
    Date fechaYHoraDeCita
    
    
    
    static constraints = {
        salaId nullable: false   //en numero blank no tiene sentido
        fechaYHoraActual nullable: false, blank: false, date: true
        fechaYHoraDeCita nullable: false, blank: false, date: true
    }
}





@Transactional(readOnly = true)  //todo 1-Rest esto esta en la guia de REST pero no me queda claro
class OrdenController {
    
    def ordenService  //primer letra minuscula para que haga la inyeccion
    
    static allowedMethods = [ save: 'POST'] //B2  Defino los metodos que voy a admitir  (puede que mucho sentido no tenga, por que ya desde el mapping le estoy diciendo segun el metodo que uso a que action mandarlo)
    static responseFormats = ['json', 'xml', 'text']  // defino los formatos con los cuales voy a responder
    
    // ojo con la variable params por que tenes que validad que llega.
    // como esta armado pueden hacer un http://localhost:8080/ordenes/?max=10&offset=a
    // y te explota.
    def index(Integer max) {
        //todo faltan validacion de: 1) esta logueado? 2)esta autorizado a ver esto?
        params.max = Math.min(max ?: 10, 100)   //lo uso para paginar
        respond OrdenDeEstudio.list(params), model:[OrdenDeEstudioCount: OrdenDeEstudio.count()]   //todo implementar service
    }
    
    
    def show(Long id ) {
        if(id == null) {render status: BAD_REQUEST}
        else {
            //si no lo encuentra devuelve NOT_FOUND automaticamente.... no se si esta bueno dejarlo asi
           respond OrdenDeEstudio.findById(id)      //todo revisar, si esta autorizado a verlo, que datos le pasas a la vista....
        }
    }
    
    
    
	 
	  //@secure  ( rol del usuario --  y quien puede llamar a esto)
   @Transactional   //le avisas que es transaccional por que vas a guardar algo // hay que ver bien esto.. por que el service ya tiene uno, capaz si haces varias cosas aca puede ser que vaya
    def save (CreacionCommand command){
		  //el usuario actual es un medico  -imp trucha
		  
		  //2 validar el command
		  if (!command.hasErrors()){
            
              LocalDateTime fechaConvertida = LocalDateTime.ofInstant(command.fecha.toInstant(),ZoneId.systemDefault())
              
              
			  //3 llamar al servicio para crear la orden
              //todo aca tiene que ir un try por que puede fallar por reglas de negocio
			    def  orden = ordenService.crear(command.pacienteId,command.prioridad, fechaConvertida,command.nota,command.procedimientoId)
			  //4 mostrar una pantalla de ok
              respond  orden, [status: CREATED, view:"show"]   //todo tenes que ver lo que le pasas a la vista.
              
              //4.1 mostrar una pantalla de error
		 }else {
              respond command.errors, view:'create'
             
          }
	  }
   
 
    //todo refactor
    //validar quien puede entrar
    //validar que la orden exista
    //atrapar excepciones
    
    def cancelar(Long id ) {
        if(id == null) {render status: BAD_REQUEST}
        else {
            def orden = ordenService.cancelar(id)
            
            //si no lo encuentra devuelve NOT_FOUND automaticamente.... no se si esta bueno dejarlo asi
            respond orden
        }
    }
    
 
    
    
    @Transactional   //todo hacer bien
    def crearCita (CitaCommand cmd) {
    
        // (el id de orden esta en url, por lo tanto no puede no existir  params.id)
        // solo valido que sea numero
        
        def ordenID
        if ( params.id.isNumber()  && params.id.toLong() >0){
            ordenID = params.id.toLong()
        }else {
            render status: BAD_REQUEST
            return
        }
    
    
        //2 validar el command
        if (!cmd.hasErrors()) {
            
            //todo refactor metodo
            LocalDateTime fechaYHoraActualConvertida = LocalDateTime.ofInstant(cmd.fechaYHoraActual.toInstant(),ZoneId.systemDefault());
            LocalDateTime fechaYHoraDeCitaConvertida = LocalDateTime.ofInstant(cmd.fechaYHoraDeCita.toInstant(),ZoneId.systemDefault());
            //atrapar exepciones del dominio
            //3 llamar al servicio para crear la cita
            try {
                def orden = OrdenDeEstudio.get(ordenID)  //validar que existan las cosas
                if (!orden) throw new Exception("Error: no existe la orden")
                def sala = SalaDeExamen.get(cmd.salaId)
                if (!sala) throw new Exception("Error: no existe la sala")
                Cita cita = orden.agregarCita(fechaYHoraActualConvertida,sala,fechaYHoraDeCitaConvertida)
                cita.save(failOnError : true)
                //4 mostrar una pantalla de ok
                respond  cita, [status: CREATED, view:"show"]
            } catch (e) {
               respond (text: e.message, status: BAD_REQUEST)
            }

        }else {     //4.1 mostrar una pantalla de error
            respond cmd.errors, view: 'create'
        }
   
    }
    
    /*
            Que reciba id de paciente
            Y me traiga todas las órdenes con ese paciente asociado
                Calculo que sería solo una linda con diferencia al show.
     */
    def buscarPorPacienteId (String pacienteId) {
        def paciente
        
        if (!pacienteId){render status: BAD_REQUEST
            return
        }
        
        if ( pacienteId.isNumber()  && pacienteId.toLong() >0){
            paciente = Paciente.get(pacienteId)
        }else {
            render status: BAD_REQUEST
            return
        }
        
        if (!paciente) {
            render status: NOT_FOUND
            return
        }
        
       def  ordenes = OrdenDeEstudio.findAllByPaciente(paciente)
        respond ordenes
    
    }
   
    /*
     obtener citaPorIdOrden
        Le pasó el id de la orden y me trae todas las citas de esa orden
        */
    def buscarcitaPorIdOrden(String ordenId) {
        def orden
        
        if (!ordenId){render status: BAD_REQUEST
            return
        }
        
        if ( ordenId.isNumber()  && ordenId.toLong() >0){
            orden = OrdenDeEstudio.get(ordenId)
        }else {
            render status: BAD_REQUEST
            return
        }
        
        if (!orden) {
            render status: NOT_FOUND
            return
        }
        respond orden.citas
        
        
    }
    
    
	  
	  /*
	  def ordenes() {
		  
		  def ordenes = OrdenDeEstudio.list()
		  
		  [
				  ordenes: ordenes.collect { orden ->
					  [
							  id: orden.id,
							  nota:orden.notaAdicional
					  
					  ]
				  },
		  ]
	  }
	  
	  
	  */
   
  /*
    // pantalla inicial de administracion de ordenes
    def index() {
        //todo faltan validacion de: 1) esta logueado? 2)esta autorizado a ver esto?
        
        //todo, en esta parte supongo que habria que ponerle un cuadro de busqueda, que ingrese parte del nombre
        // de un paciente, hacemos una busqueda con filter y le devolvemos una lista de lo encontrado
        // o no hay pacientes si esta vacia
        def pacientes = Paciente.list()    //provisorio no usar esto
        
        
        [
          pacientes: pacientes.collect { paciente ->
              [
                      id: paciente.id,
                      nombre: paciente.nombre,
                      apellido: paciente.apellido,
                      email: paciente.email,
              ]
          },
        ]
        
    }
  */
 
    
    
    
    
    /************************ ULTIMA ENTREGA ***************************************************************************
    ************************************ ***********************************************************************
     *********************************************************************************************************/
    //todo esto es provisorio
 /*
    
    def crearCita (Long ordenId, Long salaId){
        
        //2 validar el command
        
            //3 llamar al servicio para crear la cita
            def orden = OrdenDeEstudio.get(ordenId)
            def sala = SalaDeExamen.get(salaId)
            
            Cita cita = orden.agregarCita(sala,LocalDateTime.now())
            cita.save(failOnError : true)
        
        
            //4 mostrar una pantalla de ok
            render view: "creacionOK"
            
            //4.1 mostrar una pantalla de error
        
        
    }
    
    def citas() {
        
        def citas = Cita.list()
        
        [
                citas: citas.collect { cita ->
                    [
                            id: cita.id,
                            fecha:cita.fechaYHoraDeCita
                    
                    ]
                },
        ]
        
    
        
    }
    */
}
