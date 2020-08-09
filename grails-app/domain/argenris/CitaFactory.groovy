package argenris

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

    Cita crearCita(LocalDateTime fechaCita, String prioridad) throws Exception{
        if(prioridad == 'URGENTE') {
            return new Cita(fechaCita, Prioridad.URGENTE)
        }
        if(prioridad == 'NORMAL') {
            return new Cita(fechaCita, Prioridad.NORMAL)
        }
        else {
            throw new Exception('La prioridad recibida no existe')
        }
    }
}
