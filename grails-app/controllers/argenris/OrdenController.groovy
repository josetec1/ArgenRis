package argenris

import java.time.LocalDateTime


//A  Defino un command para la validacion de los imputs que deben cargarse por la vista
class CreacionCommand{
    
    Long pacienteId
    Long procedimientoId
    String nota
    String prioridad
    String fecha
    
    static constraints = {
   
         pacienteId nullable: false, blank: false
         procedimientoId nullable: false, blank: false
         nota nullable: false, blank: false
         prioridad nullable: false, blank: false
         fecha nullable: false, blank: true

    }
    
}





class OrdenController {

    
    def ordenService  //primer letra minuscula para que haga la injeccion
    
    //B  Defino los metodos que voy a admitir
    static allowedMethods = [
            crear: 'POST'
    ]
    
    
    
    // pantalla inicial de administracion de ordenes
    def index() {
        
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
    
    //todo fix prioridad y fecha
    
    // creacion de ordenes
    // 1 Los datos deben llegar por un command
    def crear (CreacionCommand command){
        
        //2 validar el command
        if (!command.hasErrors()){
            
            //3 llamar al servicio para crear la orden
            ordenService.crear(command.pacienteId,Prioridad.NORMAL, LocalDateTime.now(),command.nota,command.procedimientoId)
            
            //4 mostrar una pantalla de ok
            render view: "creacionOK", model: [pacienteId: command.pacienteId]
    
            //4.1 mostrar una pantalla de error
        }else {render view: "nuevaOrden", model: [faltanParametros : 1]}
        
    }
    
    def nuevaOrden() {
    }
    
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
    
}
