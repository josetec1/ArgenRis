package argenris

import argenris.OrdenDeEstudio.OrdenDeEstudio
import grails.gorm.transactions.*
import static org.springframework.http.HttpStatus.*
import java.time.LocalDateTime

//A1  Defino un command para la validacion de los imputs que deben cargarse por la vista
class CreacionCommand{
    
    Long pacienteId
    Long procedimientoId
    String nota
    String prioridad  //todo pasar a prioridad
    String fecha //todo pasar a fecha
   // Date dateOfBirth
    
     static constraints = {
   //todo revisar estas validaciones
         pacienteId nullable: false   //en numero blank no tiene sentido
         procedimientoId nullable: false
         nota nullable: false, blank: false
         prioridad nullable: false, blank: false
         fecha nullable: true, blank: true
      //  dateOfBirth blank: false, date: true, validator: { val -> validateDate(val) } //todo mirar este metodo
    }
}

@Transactional(readOnly = true)  //todo 1-Rest esto esta en la guia de REST pero no me queda claro
class OrdenController {
    
    def ordenService  //primer letra minuscula para que haga la inyeccion
    
    static allowedMethods = [ save: 'POST'] //B2  Defino los metodos que voy a admitir  (puede que mucho sentido no tenga, por que ya desde el mapping le estoy diciendo segun el metodo que uso a que action mandarlo)
    static responseFormats = ['json', 'xml', 'text']  // defino los formatos con los cuales voy a responder
    
    
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
		  //el usuario actual es un medio  -imp trucha
		  
		  //2 validar el command
		  if (!command.hasErrors()){                               //todo fix prioridad y fecha
			  
			  //3 llamar al servicio para crear la orden
              //todo aca tiene que ir un try por que puede fallar por reglas de negocio
			    def  orden = ordenService.crear(command.pacienteId,Prioridad.NORMAL, LocalDateTime.now(),command.nota,command.procedimientoId)
			  //4 mostrar una pantalla de ok
              respond  orden, [status: CREATED, view:"show"]   //todo tenes que ver lo que le pasas a la vista.
              
              //4.1 mostrar una pantalla de error
		 }else {
              respond command.errors, view:'create'
              
          }
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
  /*
    
    def nuevaOrden() {
        [
         procedimientos:Procedimiento.list(), pacientes:Paciente.list()  //provisorio no usar esto
        ]
    }
 */
    /*
    def medicos() {
        
        def medicos = Medico.list()
    
        [
                medicos: medicos.collect { medico ->
                    [
                            id: medico.id,
                            nombre: medico.nombre
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
    def nuevaCita() {
        [
                ordenes:OrdenDeEstudio.list(), salas:SalaDeExamen.list()  //provisorio no usar esto
        ]
    }
    
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
