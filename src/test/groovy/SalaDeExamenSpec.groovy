

import argenris.Cita.Cita
import argenris.CitaFactory
import argenris.Prioridad
import argenris.SalaDeExamen
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


class SalaDeExamenSpec extends Specification implements DomainUnitTest<SalaDeExamen>{

    def setup() {
    }

    def cleanup() {
    }

    void "puedoAgregar cita en fecha 1-7-2020 devuelve true si no hay cita que se superponga"() {
        given:'una lista de citas que no se superponen con 1-1-2020'
            LocalDateTime fechaDeCreacionCita1 =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita2 =  new LocalDateTime(new LocalDate(2020,3,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita3 =  new LocalDateTime(new LocalDate(2020,5,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita4 =  new LocalDateTime(new LocalDate(2020,7,1),new LocalTime(20,00,00,00))
        Cita cita1 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita1,'NORMAL')
            Cita cita2 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita2,'NORMAL')
            Cita cita3 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita3,'NORMAL')
            List<Cita> listaDeCitas = new ArrayList<Cita>()
            listaDeCitas.add(cita1)
            listaDeCitas.add(cita2)
            listaDeCitas.add(cita3)
        SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas)
        when:'se llama a puedoCrearCita con fechaDeCreacion4 '
            def puedoGuardar = salaDeExamen.puedoAgregarCita(fechaDeCreacionCita4)
        then:'puedo guardar es true'
            puedoGuardar == true
    }

    void "puedoAgregar cita en fecha 1-1-2020 devuelve false si ya hay cita en esa fecha y hora"() {
        given:'una lista de citas que no se superponen con 1-1-2020'
            LocalDateTime fechaDeCreacionCita1 =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita2 =  new LocalDateTime(new LocalDate(2020,3,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita3 =  new LocalDateTime(new LocalDate(2020,5,1),new LocalTime(20,00,00,00))
            Cita cita1 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita1,'NORMAL')
            Cita cita2 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita2,'NORMAL')
            Cita cita3 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita3,'NORMAL')
            List<Cita> listaDeCitas = new ArrayList<Cita>()
            listaDeCitas.add(cita1)
            listaDeCitas.add(cita2)
            listaDeCitas.add(cita3)
            SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas)
        when:'se llama a puedoCrearCita con fechaDeCreacion1 '
            def puedoGuardar = salaDeExamen.puedoAgregarCita(fechaDeCreacionCita1)
        then:'puedo guardar es false'
            puedoGuardar == false
    }

    void " crearCita en fecha 1-7-2020 crea la cita y la guarda"() {
        given:'una lista de citas que no se superponen con 1-1-2020'
            LocalDateTime fechaDeCreacionCita1 =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita2 =  new LocalDateTime(new LocalDate(2020,3,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita3 =  new LocalDateTime(new LocalDate(2020,5,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita4 =  new LocalDateTime(new LocalDate(2020,7,1),new LocalTime(20,00,00,00))
            Cita cita1 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita1,'NORMAL')
            Cita cita2 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita2,'NORMAL')
            Cita cita3 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita3,'NORMAL')
            List<Cita> listaDeCitas = new ArrayList<Cita>()
            listaDeCitas.add(cita1)
            listaDeCitas.add(cita2)
            listaDeCitas.add(cita3)
            SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas)
        when:'se llama a crearCita con fechaDeCreacion4 y prioridad Normal'
            def cita = salaDeExamen.crearCita(fechaDeCreacionCita4, 'NORMAL')
        then:'se crea la citaNormal en fecha indicada y se la guarda en las citas'
            cita.getFechaYHora() == fechaDeCreacionCita4
            cita.getPrioridad() == Prioridad.NORMAL
            salaDeExamen.puedoAgregarCita(fechaDeCreacionCita4) == false
    }

    void " crearCita en fecha 1-1-2020 LanzaExcepcion si no se puede agregarCitaEn esa fecha"() {
        given:'una lista de citas que no se superponen con 1-1-2020'
            LocalDateTime fechaDeCreacionCita1 =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita2 =  new LocalDateTime(new LocalDate(2020,3,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita3 =  new LocalDateTime(new LocalDate(2020,5,1),new LocalTime(20,00,00,00))
            Cita cita1 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita1,'NORMAL')
            Cita cita2 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita2,'NORMAL')
            Cita cita3 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita3,'NORMAL')
            List<Cita> listaDeCitas = new ArrayList<Cita>()
            listaDeCitas.add(cita1)
            listaDeCitas.add(cita2)
            listaDeCitas.add(cita3)
            SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas)
        when:'se llama a crearCita con fechaDeCreacion1 y prioridad Normal'
            salaDeExamen.crearCita(fechaDeCreacionCita1, 'NORMAL')
        then:'devuelve Excepcion'
            Exception exception = thrown()
            exception.message == 'Error: No se puede agregar la cita en el dia y horario pactado'
            salaDeExamen.puedoAgregarCita(fechaDeCreacionCita1) == false
    }

    void " obtenerCitasDelDia en fecha 1-1-2020 devuelve la cita con fechaDeCreacion1"() {
        given:'una lista de citas que no se superponen con 1-1-2020'
            LocalDateTime fechaDeCreacionCita1 =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita2 =  new LocalDateTime(new LocalDate(2020,3,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita3 =  new LocalDateTime(new LocalDate(2020,5,1),new LocalTime(20,00,00,00))
            Cita cita1 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita1,'NORMAL')
            Cita cita2 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita2,'NORMAL')
            Cita cita3 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita3,'NORMAL')
            List<Cita> listaDeCitas = new ArrayList<Cita>()
            listaDeCitas.add(cita1)
            listaDeCitas.add(cita2)
            listaDeCitas.add(cita3)
            SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas)
        when:'se llama a obtenerCitasDelDia con fechaDeCreacion1'
            def citas = salaDeExamen.obtenerCitasDelDia(fechaDeCreacionCita1)
        then:'se obtiene una lista con la cita1'
            citas.get(0) == cita1
            citas.size() == 1
    }

    void " obtenerCitasDelDia en fecha 1-12-2020 devuelve lista vacia"() {
        given:'una lista de citas que no se superponen con 1-1-2020'
            LocalDateTime fechaDeCreacionCita1 =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita2 =  new LocalDateTime(new LocalDate(2020,3,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita3 =  new LocalDateTime(new LocalDate(2020,5,1),new LocalTime(20,00,00,00))
            LocalDateTime fechaDeCreacionCita4 =  new LocalDateTime(new LocalDate(2020,12,1),new LocalTime(20,00,00,00))
            Cita cita1 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita1,'NORMAL')
            Cita cita2 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita2,'NORMAL')
            Cita cita3 = CitaFactory.obtenerInstancia().crearCita(fechaDeCreacionCita3,'NORMAL')
            List<Cita> listaDeCitas = new ArrayList<Cita>()
            listaDeCitas.add(cita1)
            listaDeCitas.add(cita2)
            listaDeCitas.add(cita3)
            SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas)
        when:'se llama a obtenerCitasDelDia con fechaDeCreacion4'
            def citas = salaDeExamen.obtenerCitasDelDia(fechaDeCreacionCita4)
        then:'se obtiene una lista vacia'
            citas.size() == 0
    }

}
