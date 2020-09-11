

import argenris.Cita.Cita
import argenris.CitaFactory
import argenris.Prioridad
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class CitaFactorySpec extends Specification implements DomainUnitTest<CitaFactory> {

    def setup() {
    }

    def cleanup() {
    }

    void "cita Factory se llama dos veces CitaFactory.obtenerInstancia y se obtiene la misma instancia "() {
        given:'se obtiene la instancia de CitaFactory'
            CitaFactory factory = CitaFactory.obtenerInstancia()
        when:'se obtiene una nueva isntancia de CitaFactory'
            CitaFactory nuevaFactory = CitaFactory.obtenerInstancia()
        then:'Es la misma instancia'
            factory == nuevaFactory
    }

    void "cita Factory crea una cita normal cuando recibe prioridad normal"() {
        given:'Cita factory es creada, fechaCita es creada'
            CitaFactory factory = CitaFactory.obtenerInstancia()
            LocalDateTime fechaCita = new LocalDateTime(new LocalDate(1998, 1, 23), new LocalTime(23, 00, 00, 00))
        when:'se llama a citaFactory crear cita con la fecha creada'
        Cita nuevaCita = factory.crearCita(fechaCita, 'NORMAL')
        then:'debuelve la cita normal esperada'
            nuevaCita.fechaYHora == fechaCita
            nuevaCita.prioridad == Prioridad.NORMAL
    }

    void "cita Factory crea una cita Urgente cuando recibe prioridad normal"() {
        given:'Cita factory es creada, fechaCita es creada'
            CitaFactory factory = CitaFactory.obtenerInstancia()
            LocalDateTime fechaCita = new LocalDateTime(new LocalDate(1998, 1, 23), new LocalTime(23, 00, 00, 00))
        when:'se llama a citaFactory crear cita con la fecha creada y prioridad URGENTE'
            Cita nuevaCita = factory.crearCita(fechaCita, 'URGENTE')
        then:'debuelve la cita normal esperada'
            nuevaCita.fechaYHora == fechaCita
            nuevaCita.prioridad == Prioridad.URGENTE
    }

    void "cita Factory devuelve Excepcion cuando recibe prioridad INEXISTENTE"() throws Exception{
        given:'Cita factory es creada, fechaCita es creada'
            CitaFactory factory = CitaFactory.obtenerInstancia()
            LocalDateTime fechaCita = new LocalDateTime(new LocalDate(1998, 1, 23), new LocalTime(23, 00, 00, 00))
        when:'se llama a citaFactory crear cita con la fecha creada y prioridad INEXISTENTE'
            factory.crearCita(fechaCita, 'INEXISTENTE')
        then:'debuelve la cita normal esperada'
            Exception exception = thrown()
            exception.message == 'La prioridad recibida no existe'
        }
}
