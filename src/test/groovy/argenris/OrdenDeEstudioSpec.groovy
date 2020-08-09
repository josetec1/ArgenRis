package argenris


import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import java.time.LocalDateTime

class OrdenDeEstudioSpec extends Specification implements DomainUnitTest<OrdenDeEstudio> {
    
    Medico medico
    Paciente paciente
    Prioridad prioridad
    LocalDateTime fechaDeCreacion
    String nota
    Procedimiento procedimiento
    
    def setup() {
        
        Medico medico = mockDomain Medico
        Paciente paciente = mockDomain Paciente
        Prioridad prioridad = Prioridad.NORMAL
        LocalDateTime fechaDeCreacion =LocalDateTime.of(2020,8,9,2,54)
        String nota = "una nota"
        Procedimiento procedimiento = mockDomain Procedimiento
        
    }

    def cleanup() {
    }
    
    void "test01 al crear una orden su estado inicial es registrada" (){
        given:
        
        when: 'una orden de estudio recien creada'
        
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(
                medico,
                paciente,
                prioridad,
                fechaDeCreacion,
                nota,
                procedimiento)
        
        then: "la orden queda en estado registrada"
        unaOrden.estadoDeLaOrden == new EstadoOrdenRegistrada()
        unaOrden.estadoDeLaOrden != new EstadoOrdenAsignada()  //ojo por que podes implementar mal el equals y da siempre true
        
    }
    
    void "test02 una orden de estudio sin cita, puedoAgregarCita debe dar true" (){
        
        given:'una orden de estudio recien creada'

        OrdenDeEstudio unaOrden= new OrdenDeEstudio(
                medico,
                paciente,
                prioridad,
                fechaDeCreacion,
                nota,
                procedimiento)
        
        when:
        def puedoAgregarCita = unaOrden.puedoAgregarCita()
        
        then: "A la orden se le puede agregar una cita"
        puedoAgregarCita == true
        
    }
    
  
    
}
