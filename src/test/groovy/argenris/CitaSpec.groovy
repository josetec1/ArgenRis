package argenris

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class CitaSpec extends Specification implements DomainUnitTest<Cita> {

    def setup() {
    }

    def cleanup() {
    }

     void "test al crear una cita, se encuentra vigente" (){
	
        when:'una cita recien creada'

	        def Cita unaCita= new Cita ()
	
	    then: "la cita tiene el estado registrado"	

            //unaCita.estadoDeCita == new EstadoCitaRegistrada()
            unaCita.estaVigente() == true

    }

    void "test al cancelar una cita,deberia quedar no vigente" (){
	
        given: 'una cita recien creada'
            def Cita unaCita= new Cita ()

        when:
        unaCita.cancelar()
	
	    then: "la cita tiene el estado cancelado"	


        unaCita.estaVigente() == false

    }


}
