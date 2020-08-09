package argenris

import argenris.Cita.Cita

import java.time.LocalDateTime

abstract class AreaDeExamen {

    static constraints = {
    }

    List<Cita> citas

    Cita crearCita(LocalDateTime fechaYHoraCita, String prioridad) throws Exception{
        CitaFactory.obtenerInstancia().crearCita(fechaYHoraCita, prioridad)
    }

    abstract List<Cita> obtenerCitasDelDia(LocalDateTime dia)

    abstract boolean puedoAgregarCita(LocalDateTime fechaCita)

}
