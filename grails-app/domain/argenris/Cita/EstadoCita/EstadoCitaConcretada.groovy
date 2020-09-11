package argenris.Cita.EstadoCita

import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDateTime

class EstadoCitaConcretada extends EstadoCita{


    @Override
    EstadoCita pacienteArribando(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden) {
        throw new CitaEstaConcretadaException()
    }

    @Override
    EstadoCita cancelar(LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden) {
        throw new CitaEstaConcretadaException()
    }
    
    @Override
    EstadoCita notificarPasoDelTiempo(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden) {
        return this
    }
}
