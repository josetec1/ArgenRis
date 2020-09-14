package argenris

import argenris.OrdenDeEstudio.OrdenDeEstudio
import argenris.Cita.Cita

import grails.gorm.transactions.*
import static org.springframework.http.HttpStatus.*

import java.time.LocalDateTime



/*
HTTP Method	        URI     	    Controller Action
GET             /Orden                    index

GET            /Orden/${id}                show

GET           /Orden/create               create

GET          /Orden/${id}/edit             edit

POST         /Orden                          save

PUT         /Orden/${id}                  update

DELETE      /Orden/${id}                   delete

 */


/*
//A1  Defino un command para la validacion de los imputs que deben cargarse por la vista
class CreacionCommand{
    
    Long pacienteId
    Long procedimientoId
    String nota
    String prioridad  //todo pasar a prioridad
    String fecha //todo pasar a fecha
    
    
    Date dateOfBirth
    static constraints = {
    dateOfBirth blank: false, date: true, validator: { val -> validateDate(val) }
        }
     
    
    static constraints = {
   //todo revisar estas validaciones
         pacienteId nullable: false   //en numero blank no tiene sentido
         procedimientoId nullable: false
         nota nullable: false, blank: false
         prioridad nullable: false, blank: false
         fecha nullable: true, blank: true

    }
    
}

*/


@Transactional(readOnly = true)  //1-Rest esto es para implementar el controlador rest por mi cuenta
class OrdenController {

    
    def ordenService  //primer letra minuscula para que haga la inyeccion
 
  /*
    //B2  Defino los metodos que voy a admitir
    static allowedMethods = [
            crear: 'POST'
    ]
 */
    // aca voy a mostrar la lista de ordenes (es por el estandar)
    //2-Rest defino la indexacion
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)   //lo uso para paginar
        respond OrdenDeEstudio.list(params), model:[OrdenDeEstudioCount: OrdenDeEstudio.count()]
    
    
      
        //respond OrdenDeEstudioService.list(params), model:[OrdenDeEstudioCount: OrdenDeEstudioService.count()]
        
        
    }
    
    //3-Rest
    // Hay que ver por que podes hacer un matching de objetos y q grails busca automaticamente la instancia (recibe un id que lo convierte en instancia escribiendo esto asi)
    def show(Long id ) {
        if(id == null) {
            render status:404
        }
        else {
            respond OrdenDeEstudio.get(id)
          //  return [OrdenDeEstudio: orden]
        }
    }
    
    
    
    
    @Transactional   //le avisas que es transaccional por que vas a guardar algo
    // asi como esta sin definir el json, te valida que lo que reciba tenga todos los parametros especificos para crear la orden
    // ahora falla por que no se como pasarle la fecha
    
    def save(OrdenDeEstudio orden) {    //esto se comporta como un command
    
        if(orden.hasErrors()) {
            respond orden.errors, view:'create'
        }
        else {
           orden.save()      //Esta linea es provisora, muy mal hecho
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
    //todo fix prioridad y fecha
  /*
    // creacion de ordenes
    // 1 Los datos deben llegar por un command
    //@secure  ( rol del usuario --  y quien puede llamar a esto)
    def crear (CreacionCommand command){
        //el usuario actual es un medio  -imp trucha
        
        //2 validar el command
        if (!command.hasErrors()){
            
            //3 llamar al servicio para crear la orden
            ordenService.crear(command.pacienteId,Prioridad.NORMAL, LocalDateTime.now(),command.nota,command.procedimientoId)
            
            //4 mostrar una pantalla de ok
            render view: "creacionOK", model: [pacienteId: command.pacienteId]
    
            //4.1 mostrar una pantalla de error
       }else {render view: "nuevaOrden", model: [faltanParametros : 1]} //todo mostrar el erro y mantener los datos ingresados. y rompe los select
       
    }
    
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