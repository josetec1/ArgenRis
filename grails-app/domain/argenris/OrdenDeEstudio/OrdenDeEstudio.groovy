package argenris.OrdenDeEstudio

import argenris.AreaDeExamen
import argenris.Cita.Cita
import argenris.OrdenDeEstudio.EstadoOrden.EstadoDeLaOrden
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenRegistrada
import argenris.Medico
import argenris.Paciente
import argenris.Prioridad
import argenris.Procedimiento

import java.time.LocalDateTime

class OrdenDeEstudio {
    
    //TODO implementar
    
    Medico medico
    Paciente paciente
    Prioridad prioridad
    LocalDateTime dateCreated  //fecha real en que se ingresa en el sistema la orden
    LocalDateTime fecha       // fecha de creacion que ingresa el medico
    String notaAdicional
    Set<Cita> citas =[]
    EstadoDeLaOrden estadoDeLaOrden
    Procedimiento procedimiento
    
    
    
    static hasMany = [
            citas: Cita
    ]
    
    static constraints = {
        medico nullable: false
        paciente nullable: false
        prioridad nullable: false
        fecha nullable: false
        notaAdicional nullable: true, blank: true
        procedimiento nullable: false
        
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
        
    
        this.estadoDeLaOrden =  new EstadoOrdenRegistrada()
        
        
    }
    //todo
    boolean puedoAgregarCita (){
        this.citas.isEmpty()
    
    }
    
    //todo
    void agregarCita (AreaDeExamen salaDeExamen, LocalDateTime fechaDeCita){
                this.citas.add(new Cita(fechaDeCita,Prioridad.URGENTE))
    }
}
