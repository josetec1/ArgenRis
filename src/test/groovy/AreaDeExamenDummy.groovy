

import argenris.AreaDeExamen
import argenris.Cita.Cita
import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDateTime

class AreaDeExamenDummy extends AreaDeExamen{

    @Override
    List<Cita> obtenerCitasDelDia(LocalDateTime dia) {
        null
    }

    Cita crearCita(LocalDateTime fechaYHoraCita, String prioridad, OrdenDeEstudio unaOrden) {
        super.crearCita(fechaYHoraCita ,prioridad, unaOrden)
    }

    @Override
    boolean puedoAgregarCita(LocalDateTime fechaCita) {
        false
    }
}
