package argenris.Cita.EstadoCita

import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDateTime


abstract class EstadoCita {


     boolean estaVencida(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual) {
            fechaYHoraActual.isAfter(fechaYHoraDeCita.plusMinutes(30))
     }

   abstract EstadoCita pacienteArribando(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden)
   abstract EstadoCita cancelar(LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden)
    
    abstract  EstadoCita notificarPasoDelTiempo(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual, OrdenDeEstudio unaOrden)
}
