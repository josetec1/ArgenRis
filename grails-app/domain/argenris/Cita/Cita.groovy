package argenris.Cita

import argenris.Cita.EstadoCita.EstadoCita
import argenris.Cita.EstadoCita.EstadoCitaRegistrada

import java.time.LocalDateTime

class Cita {

    EstadoCita estadoDeCita
    LocalDateTime fechaYHora

    static constraints = {

       // fechaYHora nullable: false
        // estadoDeCita nullable: false
    }

    Cita(LocalDateTime fechaYHora) {

        this.estadoDeCita = new EstadoCitaRegistrada()
        this.fechaYHora = fechaYHora

    }

    void cancelar() {
        this.estadoDeCita = this.estadoDeCita.cancelar()
    }

    boolean estaVencida(LocalDateTime fechaYHoraActual) {
        this.estadoDeCita.estaVencida(fechaYHora, fechaYHoraActual)

    }

    boolean pacienteArribando(LocalDateTime fechaYHoraActual) {
        this.estadoDeCita= this.estadoDeCita.pacienteArribando (fechaYHora, fechaYHoraActual)
    }
}