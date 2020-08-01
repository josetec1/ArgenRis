package argenris

class Cita {

    EstadoCita estadoDeCita
    static constraints = {
    }

    Cita (){

        this.estadoDeCita = new EstadoCitaRegistrada()

    }

    void cancelar(){
        this.estadoDeCita = new EstadoCitaCancelada()
    }

    boolean estaVigente(){
        return this.estadoDeCita.estaVigente()
    }

}
