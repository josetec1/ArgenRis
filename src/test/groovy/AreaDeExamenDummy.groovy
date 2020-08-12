

import argenris.AreaDeExamen
import argenris.Cita.Cita

import java.time.LocalDateTime

class AreaDeExamenDummy extends AreaDeExamen{

    @Override
    List<Cita> obtenerCitasDelDia(LocalDateTime dia) {
        null
    }

    Cita crearCita(LocalDateTime fechaYHoraCita, String prioridad) {
        super.crearCita(fechaYHoraCita ,prioridad)
    }

    @Override
    boolean puedoAgregarCita(LocalDateTime fechaCita) {
        false
    }
}
