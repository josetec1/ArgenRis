package argenris

import java.time.Duration
import java.time.LocalDateTime

class Cita {

    EstadoCita estadoDeCita
    LocalDateTime fechaYHora
    Prioridad prioridad

    static constraints = {
    }

    Cita ( LocalDateTime fechaYHora, Prioridad prioridad){

        this.estadoDeCita = new EstadoCitaRegistrada()
        this.fechaYHora = fechaYHora
        this.prioridad = prioridad

    }

    void cancelar(){
        this.estadoDeCita = new EstadoCitaCancelada()
    }

    boolean estaVencida(LocalDateTime fechaYHoraActual){

        fechaYHoraActual != this.fechaYHora
    }

    boolean seSuperponeCon(LocalDateTime fechaYHoraActual) {
        if(fechaYHoraActual >= fechaYHora) {
            return Duration.between(this.fechaYHora, fechaYHoraActual).toMinutes() <= prioridad.obtenerRango()
        }
        Duration.between(fechaYHoraActual, this.fechaYHora).toMinutes() <= prioridad.obtenerRango()
    }

}
