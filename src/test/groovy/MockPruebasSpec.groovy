import argenris.Cita.Cita
import argenris.Cita.EstadoCita.CitaEstaCanceladaException
import argenris.Cita.EstadoCita.CitaEstaConcretadaException
import argenris.Medico
import argenris.OrdenDeEstudio.OrdenDeEstudio
import grails.testing.gorm.DomainUnitTest
import org.grails.plugins.testing.MockPart
import spock.lang.Specification
import spock.lang.Unroll

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
		when:'cuando llamo al metodo 2 veces'
			mockOrden.puedoAgregarCita() //1
			mockOrden.puedoAgregarCita() //2
		then:'efectivamente llamo al metodo 2 veces'
			2 * mockOrden.puedoAgregarCita()
	}
	
	
	// NO USAR def EN EL BLOQUE WHEN POR QUE LANZA EXCEPTION MissingPropertyException
	void "Spock ignora todas las llamadas que hagas a los metodos del mock que hagas antes del when" (){
		def mockOrden = Mock OrdenDeEstudio
			mockOrden.puedoAgregarCita() //ignora
			mockOrden.puedoAgregarCita() //ignora
			mockOrden.puedoAgregarCita() //ignora
			mockOrden.puedoAgregarCita() //ignora
		when:
			mockOrden.puedoAgregarCita() //1
			mockOrden.puedoAgregarCita() //2
		then:
		2 * mockOrden.puedoAgregarCita()
	}
	
	void "Lanzar una excepcion desde un Stub objet" (){
		
			def mockCita = Stub Cita
			mockCita.cancelar() >> {throw new CitaEstaCanceladaException()}
		when:
			mockCita.cancelar()
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