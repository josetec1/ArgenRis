package argenris.Cita.EstadoCita

import java.time.LocalDateTime

class EstadoCitaConcretada extends EstadoCita{


    @Override
    EstadoCita pacienteArribando(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual) {
        throw new CitaEstaConcretadaException()
    }

    @Override
    EstadoCita cancelar() {
        throw new CitaEstaConcretadaException()
    }
}
