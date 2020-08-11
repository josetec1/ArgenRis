package argenris

import java.time.LocalDateTime

class OrdenController {

    
    def ordenService  //primer letra minuscula para que haga la injeccion
    
    
    
    
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
    
    
    // creacion de ordenes
    // que me lleguen los datos por un command
    def crear (){
        //validar el command
        //llamar al servicio para crear la orden
            //ordenService.crear(pacienteID,Prioridad.NORMAL,fechaCreacion,nota,procedimientoID)
            ordenService.crear(1,Prioridad.NORMAL, LocalDateTime.now(),"Creada por el service",1)
    
    
        
    
    
        //mostrar una pantalla (de error o de ok)
        
        
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
