package argenris.Cita

import argenris.Cita.EstadoCita.EstadoCita
import argenris.Cita.EstadoCita.EstadoCitaPlanificada
import argenris.OrdenDeEstudio.OrdenDeEstudio
import argenris.Prioridad


import java.time.Duration
import java.time.LocalDateTime

class Cita {

    EstadoCita estadoDeCita
    LocalDateTime fechaYHora
    Prioridad prioridad
    OrdenDeEstudio ordenDeEstudio //todo habria que implementar nullPatern o pasarla por constructor

    static constraints = {
        fechaYHora nullable: false
        prioridad nullable: false
    
    }

    Cita(LocalDateTime fechaYHora, Prioridad prioridad) {
        this.prioridad = prioridad
        this.estadoDeCita = new EstadoCitaPlanificada()
        this.fechaYHora = fechaYHora

    }

    void cancelar() {
        this.estadoDeCita = this.estadoDeCita.cancelar()
    }

    boolean estaVencida(LocalDateTime fechaYHoraActual) {
        this.estadoDeCita.estaVencida(fechaYHora, fechaYHoraActual)

    }

    void pacienteArribando(LocalDateTime fechaYHoraActual) {
        this.estadoDeCita= this.estadoDeCita.pacienteArribando (fechaYHora, fechaYHoraActual)
    }

    boolean seSuperponeCon(LocalDateTime fechaYHoraActual) {
        if(fechaYHoraActual >= fechaYHora) {
            return Duration.between(this.fechaYHora, fechaYHoraActual).toMinutes() <= prioridad.obtenerRango()
        }
        Duration.between(fechaYHoraActual, this.fechaYHora).toMinutes() <= prioridad.obtenerRango()
    }
    
    void agregarOrden (OrdenDeEstudio orden){
        
        this.ordenDeEstudio = orden
    }
}