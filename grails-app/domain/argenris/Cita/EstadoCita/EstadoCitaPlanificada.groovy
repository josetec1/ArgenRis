package argenris.Cita.EstadoCita


import java.time.LocalDateTime

class EstadoCitaPlanificada extends EstadoCita  {


    @Override
    EstadoCita pacienteArribando(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual) {

        // fuera del dia o que  exceda los 30 minutos de la fecha de la cita
        if (!puedeArribar(fechaYHoraDeCita, fechaYHoraActual)) { throw new CitaNoSePuedeArribarException()}

        return new EstadoCitaConcretada()
    }

    @Override
    EstadoCita cancelar() {
        return new EstadoCitaCancelada()
    }

    boolean puedeArribar (fechaYHoraDeCita,  fechaYHoraActual){

        if (!estaVencida(fechaYHoraDeCita, fechaYHoraActual) &  fechaYHoraDeCita.toLocalDate().isEqual(fechaYHoraActual.toLocalDate())){return true}

        return false
    }
}
