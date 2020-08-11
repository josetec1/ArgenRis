package argenris

import java.time.LocalDateTime

//todo falta implementar. constructor etc.

//todo la anotacion hay que quitarla, la uso momentaneamente
@groovy.transform.ToString
class Medico {

    String nombre
  //  OrdenDeEstudio ordenDeEstudio //todo esto tiene que ser una coleccion
    static constraints = {
    }
    
    Medico (String nombre) {this.nombre = nombre}
    
    OrdenDeEstudio crearOrdenDeEstudio (Paciente paciente,
                                        Prioridad prioridad,
                                        LocalDateTime fechaDeCreacion,
                                        String nota,
                                        Procedimiento procedimiento){
    
      
        
      //  this.ordenDeEstudio = new OrdenDeEstudio(this,paciente,prioridad,fechaDeCreacion,nota,procedimiento)
        
        
    }
}


