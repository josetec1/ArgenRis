

import argenris.AreaDeExamen
import argenris.Cita.Cita
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenAsignada
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenRegistrada
import argenris.Medico
import argenris.OrdenDeEstudio.OrdenDeEstudio
import argenris.Paciente
import argenris.Prioridad
import argenris.Procedimiento
import argenris.SalaDeExamen
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
    SalaDeExamen salaDeExamen
    
    def setup() {
        
        Medico medico = mockDomain Medico
        Paciente paciente = mockDomain Paciente
       // Prioridad prioridad = Prioridad.NORMAL
       // LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,9,2,54)
        String nota = "una nota"
        Procedimiento procedimiento = mockDomain Procedimiento
        //SalaDeExamen salaDeExamen = mockDomain SalaDeExamen
    
       
       
        Prioridad prioridad = Prioridad.NORMAL
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,9,2,54)
        
        SalaDeExamen salaDeExamen = new SalaDeExamen()
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
    
    void "test03 una orden de estudio con cita, puedoAgregarCita debe dar false" (){
        
        given:'una orden de estudio recien creada'
        
        salaDeExamen = new SalaDeExamen();
        Medico medico = mockDomain Medico
        Paciente paciente = mockDomain Paciente
        Prioridad prioridad = Prioridad.NORMAL
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,9,2,54)
        List<Cita> listaDeCitas = new ArrayList<Cita>()
        SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas )
        
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(
                medico,
                paciente,
                prioridad,
                fechaDeCreacion,
                nota,
                procedimiento)
        
        when: 'agrego una cita'
        unaOrden.agregarCita (salaDeExamen,fechaDeCreacion)
        
        then: "A la orden no se le puede agregar una cita"
        unaOrden.puedoAgregarCita() == false
        
    }
    
    void "test04 una orden de estudio sin cita, puede asignarse una" (){
        
        given:'una orden de estudio recien creada'
        
        salaDeExamen = new SalaDeExamen();
        Medico medico = mockDomain Medico
        Paciente paciente = mockDomain Paciente
        Prioridad prioridad = Prioridad.NORMAL
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,9,2,54)
        List<Cita> listaDeCitas = new ArrayList<Cita>()
        SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas )
        
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(
                medico,
                paciente,
                prioridad,
                fechaDeCreacion,
                nota,
                procedimiento)
        
        when: 'agrego una cita'
        unaOrden.agregarCita (salaDeExamen,fechaDeCreacion)
        
        then: "La orden queda en estado asignada"
        unaOrden.estadoDeLaOrden == new EstadoOrdenAsignada()
        unaOrden.estadoDeLaOrden != new EstadoOrdenRegistrada()
       
        
    }
    
    
    void "test05 una orden de estudio con cita, no se le puede asignar otra" (){
        
        given:'una orden de estudio recien creada'
        salaDeExamen = new SalaDeExamen();
        Medico medico = mockDomain Medico
        Paciente paciente = mockDomain Paciente
        Prioridad prioridad = Prioridad.NORMAL
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,9,2,54)
        List<Cita> listaDeCitas = new ArrayList<Cita>()
        SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas )
        
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(
                medico,
                paciente,
                prioridad,
                fechaDeCreacion,
                nota,
                procedimiento)
        
        unaOrden.agregarCita (salaDeExamen,fechaDeCreacion)
        
        when: 'agrego otra cita'
        unaOrden.agregarCita (salaDeExamen,LocalDateTime.now())
        
        then: "devuelve Excepcion"
        Exception exception = thrown()
        exception.message == 'Error: la orden ya tiene una cita asignada'
        
      
    }
    
    
    
    
    
}
