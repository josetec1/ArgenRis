package argenris

import argenris.Cita.Cita
import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDateTime

class CitaFactory {

    static constraints = {
    }
    
    
    private static CitaFactory INSTANCIA = new CitaFactory()
    private CitaFactory() {

    }

    static CitaFactory obtenerInstancia() {
        return INSTANCIA
    }
    
    Cita crearCita(LocalDateTime fechaCita, String prioridad, OrdenDeEstudio unaOrden) throws Exception{
        if(prioridad == 'URGENTE') {
            return new Cita(fechaCita, Prioridad.URGENTE,unaOrden)
        }
        if(prioridad == 'NORMAL') {
            return new Cita(fechaCita, Prioridad.NORMAL,unaOrden)
        }
        else {
            throw new Exception('La prioridad recibida no existe')
        }
    }
}
