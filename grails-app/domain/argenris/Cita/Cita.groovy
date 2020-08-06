package argenris.Cita

import argenris.Cita.EstadoCita.EstadoCita
import argenris.Cita.EstadoCita.EstadoCitaRegistrada

import java.time.LocalDateTime

class Cita {

    EstadoCita estadoDeCita
    LocalDateTime fechaYHora

    static constraints = {

    
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

    void pacienteArribando(LocalDateTime fechaYHoraActual) {
        this.estadoDeCita= this.estadoDeCita.pacienteArribando (fechaYHora, fechaYHoraActual)
    }
}