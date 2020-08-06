package argenris.Cita.EstadoCita

import argenris.Cita.EstadoCita.EstadoCita
import com.sun.media.sound.InvalidDataException

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
