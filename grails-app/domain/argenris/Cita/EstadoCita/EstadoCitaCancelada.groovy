package argenris.Cita.EstadoCita


import java.time.LocalDateTime

class EstadoCitaCancelada extends EstadoCita  {



    @Override
    EstadoCita pacienteArribando(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual) {
        throw new CitaEstaCanceladaException()
    }

    @Override
    EstadoCita cancelar() {
        throw new CitaEstaCanceladaException()
    }
}
