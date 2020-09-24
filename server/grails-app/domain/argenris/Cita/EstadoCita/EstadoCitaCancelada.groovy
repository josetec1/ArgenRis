package argenris.Cita.EstadoCita

import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDateTime

class EstadoCitaCancelada extends EstadoCita  {



    @Override
    EstadoCita pacienteArribando(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden){
        throw new CitaEstaCanceladaException()
    }

    @Override
    EstadoCita cancelar(LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden) {
        throw new CitaEstaCanceladaException()
    }
    
    @Override
    EstadoCita notificarPasoDelTiempo(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden) {
        return this
    }
}
