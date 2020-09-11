package argenris.Cita

import argenris.Cita.EstadoCita.EstadoCita
import argenris.Cita.EstadoCita.EstadoCitaConcretada
import argenris.Cita.EstadoCita.EstadoCitaPlanificada
import argenris.OrdenDeEstudio.OrdenDeEstudio
import argenris.Prioridad


import java.time.Duration
import java.time.LocalDateTime

class Cita {

    EstadoCita estadoDeCita
    LocalDateTime fechaYHoraDeCita
    
    Prioridad prioridad
    OrdenDeEstudio ordenDeEstudio

    static constraints = {
        fechaYHoraDeCita nullable: false
        prioridad nullable: false
    
    }
    //todo habria que validad que no se puedan crear citas con fechas anterior a la actual
    // y luego validar que la fecha de cancelacion no sea anterior a la de creacion
    Cita(LocalDateTime fechaYHoraDeCita, Prioridad prioridad, OrdenDeEstudio unaOrden) {
        this.prioridad = prioridad
        this.estadoDeCita = new EstadoCitaPlanificada()
        this.fechaYHoraDeCita = fechaYHoraDeCita
        this.ordenDeEstudio = unaOrden

    }
    //todo hay que revisar todas las firmas de un solo parametro por que te puede dar null pointer
    void cancelar(LocalDateTime fechaYHoraActual) {
        //todo no esta chequeando que envie fechaYHoraActual
        
        def fechaApasar  //todo mover a estados
        if (this.estaVencida(fechaYHoraActual)) {
            fechaApasar = this.fechaYHoraDeCita
        }else{
            fechaApasar = fechaYHoraActual
        }
        
       
        this.estadoDeCita = this.estadoDeCita.cancelar(fechaApasar, this.ordenDeEstudio)
    }

    boolean estaVencida(LocalDateTime fechaYHoraActual) {
        this.estadoDeCita.estaVencida(fechaYHoraDeCita, fechaYHoraActual)

    }

    void pacienteArribando(LocalDateTime fechaYHoraActual) {
        this.estadoDeCita= this.estadoDeCita.pacienteArribando (fechaYHoraDeCita, fechaYHoraActual, this.ordenDeEstudio)
       
    }

    boolean seSuperponeCon(LocalDateTime fechaYHoraActual) {
        if(fechaYHoraActual >= fechaYHoraDeCita) {
            return Duration.between(this.fechaYHoraDeCita, fechaYHoraActual).toMinutes() <= prioridad.obtenerRango()
        }
        Duration.between(fechaYHoraActual, this.fechaYHoraDeCita).toMinutes() <= prioridad.obtenerRango()
    }
    
    
     void notificarPasoDelTiempo(LocalDateTime fechaYHoraActual) {
           this.estadoDeCita= this.estadoDeCita.notificarPasoDelTiempo(fechaYHoraDeCita, fechaYHoraActual, this.ordenDeEstudio)
    }
}