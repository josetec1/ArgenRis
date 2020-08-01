package argenris

import java.time.LocalDateTime

class Cita {

    EstadoCita estadoDeCita
    LocalDateTime fechaYHora

    static constraints = {
    }

    Cita ( LocalDateTime fechaYHora){

        this.estadoDeCita = new EstadoCitaRegistrada()
        this.fechaYHora = fechaYHora

    }

    void cancelar(){
        this.estadoDeCita = new EstadoCitaCancelada()
    }

    boolean estaVencida(LocalDateTime fechaYHoraActual){

        fechaYHoraActual != this.fechaYHora

    }

}
