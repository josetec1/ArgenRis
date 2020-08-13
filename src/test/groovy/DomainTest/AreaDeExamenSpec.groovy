package DomainTest

import argenris.Prioridad
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AreaDeExamenSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "AreaDeExamen crear cita Normal devuelve la isntancia de cita esperada"() {
        given:'se inicializa un area de examen'
            LocalDateTime fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            AreaDeExamenDummy dummyArea = new AreaDeExamenDummy()
        when:'se llama AreaDeExamen.crearCita en la fecha indicada y prioridad NORMAL'
            def cita = dummyArea.crearCita(fechaDeCreacionCita, 'NORMAL')
        then:'se devuelve la cita con la fecha indicada y la prioridad NORMAL'
            cita.getFechaYHora() == fechaDeCreacionCita
            cita.getPrioridad() == Prioridad.NORMAL
    }

    void "AreaDeExamen crear cita URGENTE devuelve la isntancia de cita esperada"() {
        given:'se inicializa un area de examen'
            LocalDateTime fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            AreaDeExamenDummy dummyArea = new AreaDeExamenDummy()
        when:'se llama AreaDeExamen.crearCita en la fecha indicada y prioridad URGENTE'
            def cita = dummyArea.crearCita(fechaDeCreacionCita, 'URGENTE')
        then:'se devuelve la cita con la fecha indicada y la prioridad URGENTE'
            cita.getFechaYHora() == fechaDeCreacionCita
            cita.getPrioridad() == Prioridad.URGENTE
    }
    void "AreaDeExamen crear cita INEXISTENTE lanza Excepcion"() {
        given:'se inicializa un area de examen'
            LocalDateTime fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            AreaDeExamenDummy dummyArea = new AreaDeExamenDummy()
        when:'se llama AreaDeExamen.crearCita en la fecha indicada y prioridad INEXISTENTE'
            dummyArea.crearCita(fechaDeCreacionCita, 'INEXISTENTE')
        then:
            Exception exception = thrown()
            exception.message == 'La prioridad recibida no existe'
    }
}
