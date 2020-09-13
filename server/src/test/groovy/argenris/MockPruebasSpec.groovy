import argenris.Cita.Cita
import argenris.Cita.EstadoCita.CitaEstaCanceladaException
import argenris.Cita.EstadoCita.CitaEstaConcretadaException
import argenris.Medico
import argenris.OrdenDeEstudio.OrdenDeEstudio
import argenris.Prioridad
import argenris.SalaDeExamen
import grails.testing.gorm.DomainUnitTest
import org.grails.plugins.testing.MockPart
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

/*
		Mas especificaciones https://medium.com/@xala3pa/mocking-with-spock-51c8e2fb6cb6

 */

class MockPruebasSpec extends Specification implements DomainUnitTest<OrdenDeEstudio> {
	
	
	void "Asi se crea un Mock" (){
		given: 'un mock de la clase Orden'
				def mockOrden = Mock OrdenDeEstudio
		expect: 'obtener una instancia mock'
				mockOrden instanceof OrdenDeEstudio
	}
	
	void "Contar las llamadas a un metodo de un mock" (){
		
		given: 'un mock de una orden de estudio'
			def mockOrden = Mock OrdenDeEstudio
			def fecha = LocalDateTime.now()
		when:'cuando llamo al metodo 2 veces'
			mockOrden.puedoAgregarCita(fecha) //1
			mockOrden.puedoAgregarCita(fecha) //2
		then:'efectivamente llamo al metodo 2 veces'
			2 * mockOrden.puedoAgregarCita(fecha)
	}
	
	
	// NO USAR def EN EL BLOQUE WHEN POR QUE LANZA EXCEPTION MissingPropertyException
	void "Spock ignora todas las llamadas que hagas a los metodos del mock que hagas antes del when" (){
		def fecha = LocalDateTime.now()
		def mockOrden = Mock OrdenDeEstudio
			mockOrden.puedoAgregarCita(fecha) //ignora
			mockOrden.puedoAgregarCita(fecha) //ignora
			mockOrden.puedoAgregarCita(fecha) //ignora
			mockOrden.puedoAgregarCita(fecha) //ignora
		when:
			mockOrden.puedoAgregarCita(fecha) //1
			mockOrden.puedoAgregarCita(fecha) //2
		then:
		2 * mockOrden.puedoAgregarCita(fecha)
	}
	
	void "Lanzar una excepcion desde un Stub objet si falta un parametro no lo lanza" (){
		LocalDateTime fecha = LocalDateTime.of(2020,8,1,4,4)
		def mockCita = Stub Cita
		mockCita.cancelar() >> {throw new CitaEstaCanceladaException()}
		when:
		mockCita.cancelar(fecha)
		then: 'como falta la fecha en cancelar, no va a lanzar exepcion'
		true == true
	}
	
	void "Lanzar una excepcion desde un Stub objet" (){
			LocalDateTime fecha = LocalDateTime.of(2020,8,1,4,4)
			def mockCita = Stub Cita
			mockCita.cancelar(fecha) >> {throw new CitaEstaCanceladaException()}
		when:
			mockCita.cancelar(fecha)
		then:
			thrown CitaEstaCanceladaException
	}
	
	
	
	// esta es de spock
	@Unroll
	def "El método plus funciona con el sumador #sumador y el sumando #sumando"() {
		when:
		def result = sumador.plus(sumando)
		then:
		result == resultadoEsperado
		where:
		sumador |   sumando     |   resultadoEsperado
		0       |   0           |   0
		0       |   1           |   1
		1       |   1           |   2
		-1      |   0           |   -1
		0       |   -1          |   -1
		-1      |   -1          |   -2
		2       |   1           |   3
	}
	
	
	
}