package argenris

import argenris.Cita.Cita
import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDateTime

abstract class AreaDeExamen {

    static constraints = {
    }

    List<Cita> citas
    
    Cita crearCita(LocalDateTime fechaYHoraCita, String prioridad, OrdenDeEstudio unaOrden) throws Exception{
        CitaFactory.obtenerInstancia().crearCita(fechaYHoraCita, prioridad, unaOrden)
    }

    abstract List<Cita> obtenerCitasDelDia(LocalDateTime dia)

    abstract boolean puedoAgregarCita(LocalDateTime fechaCita)

}