package argenris

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class CitaSpec extends Specification implements DomainUnitTest<Cita> {

    def setup() {

    }

    def cleanup() {
    }


     void "test al crear una cita, se encuentra en estado registrada" (){
         LocalDateTime fechaDeCreacionCita
         fechaDeCreacionCita =  new LocalDateTime(new LocalDate(1998,1,23),new LocalTime(23,00,00,00))
        when:'una cita recien creada'

        Cita unaCita= new Cita (fechaDeCreacionCita)

	    then: "la cita tiene el estado registrado"	

        unaCita.estadoDeCita instanceof EstadoCitaRegistrada
         //   unaCita.estadoDeCita == new EstadoCitaRegistrada()

    }

    void "test se crea una cita el 1-1-2020, y la cita no estaVencida el 1-1-2020" (){
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))

        when:'se crea una cita con fecha 1-1-2020'
        Cita unaCita= new Cita (fechaDeCreacionCita)


        then: "la cita no esta vencida el 1-1-2020"

        !unaCita.estaVencida(fechaDeCreacionCita)

    }


/*
    void "test al cancelar una cita,deberia quedar no vigente" (){
	
        given: 'una cita recien creada'
            def Cita unaCita= new Cita ()

        when:
        unaCita.cancelar()
	
	    then: "la cita tiene el estado cancelado"	


        unaCita.estaVencida() == true

    }
*/

}
