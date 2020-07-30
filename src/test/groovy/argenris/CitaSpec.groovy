package argenris

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class CitaSpec extends Specification implements DomainUnitTest<Cita> {

    def setup() {
    }

    def cleanup() {
    }

     void "test al crear una cita, su estado es registrada" (){
	
        when:'una cita recien creada'

	        def Cita unaCita= new Cita ()
	
	    then: "la cita tiene el estado registrado"	

            //unaCita.estadoDeCita == new EstadoCitaRegistrada()
            unaCita.estaVencida() == false

    }
/*
    void "test al cancelar una cita, su estado es cancelada" (){
	
        given: 'una cita recien creada'
            def Cita unaCita= new Cita ()

        when:
            unaCita.cancelar()      
	
	    then: "la cita tiene el estado cancelado"	
            println unaCita.estadoDeCita.
           // unaCita.estadoDeCita == new EstadoCitaRegistrada()
            unaCita.estaVencida() == true

    }
*/

}
