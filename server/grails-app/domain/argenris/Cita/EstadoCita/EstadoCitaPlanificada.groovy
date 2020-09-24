package argenris.Cita.EstadoCita

import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDateTime

class EstadoCitaPlanificada extends EstadoCita  {


    @Override
    EstadoCita pacienteArribando(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden) {

        // fuera del dia o que  exceda los 30 minutos de la fecha de la cita
        if (!puedeArribar(fechaYHoraDeCita, fechaYHoraActual)) { throw new CitaNoSePuedeArribarException()}
        unaOrden.notificarCitaConcretada(fechaYHoraActual)
        return new EstadoCitaConcretada()
    }

    @Override
    EstadoCita cancelar(LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden) {
        unaOrden.notificarCitaCancelada(fechaYHoraActual)
        return new EstadoCitaCancelada()
    }
    
    @Override
    EstadoCita notificarPasoDelTiempo(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden) {
        if (super.estaVencida(fechaYHoraDeCita,fechaYHoraActual) ) {return this.cancelar(fechaYHoraActual,unaOrden)}
        return this
    }
    
    boolean puedeArribar (fechaYHoraDeCita, fechaYHoraActual){

        if (!super.estaVencida(fechaYHoraDeCita, fechaYHoraActual) &  fechaYHoraDeCita.toLocalDate().isEqual(fechaYHoraActual.toLocalDate())){return true}

        return false
    }
}
