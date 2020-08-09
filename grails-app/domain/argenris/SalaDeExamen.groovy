package argenris

import java.time.LocalDateTime

class SalaDeExamen extends AreaDeExamen{


    SalaDeExamen(List<Cita> citas) {
        this.citas = citas
    }

    @Override
    List<Cita> obtenerCitasDelDia(LocalDateTime dia) {
        this.citas.findAll() {cita -> cita.getFechaYHora().getDayOfWeek() == dia.getDayOfWeek()}
    }

    //TODO: VER CASOS BORDE
    @Override
    boolean puedoAgregarCita(LocalDateTime fechaCita) {
        this.citas.every {cita -> !cita.seSuperponeCon(fechaCita) }
    }

    @Override
    Cita crearCita(LocalDateTime fechaYHoraCita, String prioridad) {

        if(puedoAgregarCita(fechaYHoraCita)){
            Cita nuevaCita = super.crearCita(fechaYHoraCita, prioridad)
            this.citas.add(nuevaCita)
            return nuevaCita
        }else{
            throw new Exception("Error: No se puede agregar la cita en el dia y horario pactado")
        }
    }
}
