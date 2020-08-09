package argenris


import argenris.Cita.Cita


import java.time.LocalDateTime

class OrdenDeEstudio {
    
    //TODO implementar
    
    Medico medico
    Paciente paciente
    Prioridad prioridad
    LocalDateTime dateCreated  //fecha real en que se ingresa en el sistema la orden
    LocalDateTime fecha       // fecha de creacion que ingresa el medico
    String notaAdicional
    Cita cita
    EstadoDeLaOrden estadoDeLaOrden
    Procedimiento procedimiento
    
    
    
    static constraints = {
    }
    
    OrdenDeEstudio (Medico medico,
                    Paciente paciente,
                    Prioridad prioridad,
                    LocalDateTime fechaDeCreacion,
                    String nota,
                    Procedimiento procedimiento){
        
        this.medico = medico
        this.paciente = paciente
        this.prioridad = prioridad
        this.fecha = fechaDeCreacion
        this.notaAdicional = nota
        this.procedimiento = procedimiento
        
        this.cita = null  //todo deberia haber un NullPattern
        this.estadoDeLaOrden =  new EstadoOrdenRegistrada()
        
        
    }
    
    boolean puedoAgregarCita (){
        //todo implementar
        if (this.cita==null) return true
     return false
    
    }
}
