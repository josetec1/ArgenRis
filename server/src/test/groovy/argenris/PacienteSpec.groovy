import argenris.Medico
import argenris.OrdenDeEstudio.OrdenDeEstudio
import argenris.Paciente
import argenris.Prioridad
import argenris.Procedimiento
import argenris.SalaDeExamen
import grails.testing.gorm.DomainUnitTest
import org.mockito.Mock
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime


class PacienteSpec extends Specification implements DomainUnitTest<Paciente> {
    
    String nombre
    String apellido
    String direccion
    Long dni
    LocalDate fechaDeNacimiento
    String email
    String telefono
    Paciente unPaciente
    
    def setup() {
    
           nombre = "Diego"
           apellido = "Maradona"
           direccion = "Segurola y Habana"
           dni = 14276579
           fechaDeNacimiento = LocalDate.of(1960,10,30)
           email = "diego@maradona.com"
           telefono  = "48426598"
           unPaciente = new Paciente (nombre,apellido,direccion,dni,fechaDeNacimiento,email,telefono)
    }

    def cleanup() {
    }

    void "test 01 al crear un paciente, se crea con los datos que le paso"() {
    
        when: 'una Paciente recien creado'
            String nombre = "Diego"
            String apellido = "Maradona"
            String direccion = "Segurola y Habana"
            Long dni = 14276579
            LocalDate fechaDeNacimiento = LocalDate.of(1960,10,30)
            String email = "diego@maradona.com"
            String telefono  = "48426598"
            Paciente paciente = new Paciente (nombre,apellido,direccion,dni,fechaDeNacimiento,email,telefono)
        then: "El Paciente se crea con los datos asignados"
            paciente.nombre == nombre
            paciente.apellido== apellido
            paciente.direccion== direccion
            paciente.dni== dni
            paciente.fechaDeNacimiento == fechaDeNacimiento
            paciente.email == email
            paciente.telefono == telefono
            paciente.ordenesDeEstudio.empty == true
    }
    
    void "test 02 agregarOrden agrega la orden que le paso"() {
        given: 'una paciente recien creado'
        unPaciente
        def unaOrden = Mock OrdenDeEstudio
        when: 'agrego una orden al paciente'
            unPaciente.agregarOrden(unaOrden)
        then: "el paciente guarda la orden"
            unPaciente.ordenesDeEstudio.first() == unaOrden
    }
    
    
}
