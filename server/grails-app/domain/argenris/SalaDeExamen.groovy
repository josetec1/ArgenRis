package argenris

import argenris.Cita.Cita
import argenris.OrdenDeEstudio.OrdenDeEstudio
import grails.compiler.GrailsCompileStatic

import java.time.LocalDateTime


class SalaDeExamen  extends AreaDeExamen {
    
    
    SalaDeExamen(List<Cita> citas) {
        this.citas = citas
    }
    
    @Override
    List<Cita> obtenerCitasDelDia(LocalDateTime dia) {
        this.citas.findAll() { cita -> cita.getFechaYHoraDeCita().getDayOfWeek() == dia.getDayOfWeek() }
    }
    
    //TODO: VER CASOS BORDE
    //TODO: la sala tiene que quedar disponible cuando hay citas canceladas
    @Override
    boolean puedoAgregarCita(LocalDateTime fechaCita) {
        this.citas.every { cita -> !cita.seSuperponeCon(fechaCita) }
    }
    
    @Override
    Cita crearCita(LocalDateTime fechaYHoraCita, String prioridad, OrdenDeEstudio unaOrden) {
        
        if(puedoAgregarCita(fechaYHoraCita)){
            Cita nuevaCita = super.crearCita(fechaYHoraCita, prioridad, unaOrden)
            this.citas.add(nuevaCita)
            return nuevaCita
        }else{
            throw new Exception("Error: No se puede agregar la cita en el dia y horario pactado")
        }
    }
}


