package argenris.cita

import argenris.Prioridad
import argenris.cita.estadoCita.EstadoCita
import argenris.cita.estadoCita.EstadoCitaRegistrada

import java.time.Duration
import java.time.LocalDateTime

class Cita {

    EstadoCita estadoDeCita
    LocalDateTime fechaYHora
    Prioridad prioridad

    static constraints = {

    
    }

    Cita(LocalDateTime fechaYHora, Prioridad prioridad) {
        this.prioridad = prioridad
        this.estadoDeCita = new EstadoCitaRegistrada()
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
}