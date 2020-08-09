package argenris

import argenris.Cita.Cita
import argenris.Cita.EstadoCita.EstadoCitaRegistrada
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
         fechaDeCreacionCita =  new LocalDateTime(new LocalDate(1998,1,23), new LocalTime(23,00,00,00))
        when:'una cita recien creada'

        Cita unaCita= new Cita (fechaDeCreacionCita, Prioridad.NORMAL)

	    then: "la cita tiene el estado registrado"	

        unaCita.estadoDeCita instanceof EstadoCitaRegistrada

    }

    void "test se crea una cita el 1-1-2020, y la cita no estaVencida el 1-1-2020" (){
        given:'se crea una cita con fecha 1-1-2020'
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            Cita unaCita= new Cita (fechaDeCreacionCita, Prioridad.NORMAL)

        when:'cita estaVencida 1-1-2020'
            def vencida = unaCita.estaVencida(fechaDeCreacionCita)
        then: "la cita no esta vencida el 1-1-2020"
            vencida == false
    }

    void "test se crea una cita Normal el 1-1-2020, y la cita se superpone con el 1-1-2020" () {
       given:
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.NORMAL)
       when:'cita seSuperpone con fecha 1-1-2020'
            def seSuperpone = unaCita.seSuperponeCon(fechaDeCreacionCita)
       then: "la cita se superpone el 1-1-2020"
            seSuperpone == true
    }

    void "test se crea una cita Normal el 1-1-2020, y la cita no se superpone con el 1-2-2020" () {
       given:
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.NORMAL)
            def fechaDeSuperposicion =  new LocalDateTime(new LocalDate(2020,2,1),new LocalTime(20,00,00,00))
       when:'cita sesuperpone con fecha 1-2-2020'
            def seSuperpone = unaCita.seSuperponeCon(fechaDeSuperposicion)
       then: "la cita no se superpone el 1-2-2020"
            seSuperpone == false
    }

    void "test se crea una cita Normal el 1-1-2020, y la cita no se superpone con el 1-12-2019" () {
       given:
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.NORMAL)
            def fechaDeSuperposicion =  new LocalDateTime(new LocalDate(2019,12,1),new LocalTime(20,00,00,00))
       when:'cita no se se superpone con fecha 1-12-2019'
           def seSuperpone = unaCita.seSuperponeCon(fechaDeSuperposicion)
       then: "la cita no se superpone el 1-12-2019"
           seSuperpone == false
    }

    void "test se crea una cita Urgente el 1-1-2020, y la cita se superpone con el 1-15-2020" () {
       given:
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,15),new LocalTime(20,00,00,00))
            Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.URGENTE)
       when:'cita se superpone con fecha 1-15-2020'
            def seSuperpone = unaCita.seSuperponeCon(fechaDeCreacionCita)
       then: "la cita se superpone el 1-15-2020"
            seSuperpone == true
    }

    void "test se crea una cita Urgente el 1-1-2020, y la cita no se superpone con el 16-1-2020" () {
       given:
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.URGENTE)
            def fechaDeSuperposicion =  new LocalDateTime(new LocalDate(2020,1,16),new LocalTime(20,00,00,00))
       when:'cita seSuperpone con fecha 16-1-2020'
            def seSuperpone = unaCita.seSuperponeCon(fechaDeSuperposicion)
       then: "la cita no se superpone el 16-1-2020"
            seSuperpone == false
    }

    void "test se crea una cita Urgente el 1-1-2020, y la cita no se superpone con el 16-12-2019" () {
       given:
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.URGENTE)
            def fechaDeSuperposicion =  new LocalDateTime(new LocalDate(2019,12,16),new LocalTime(20,00,00,00))
       when:'cita no se se superpone con fecha 16-12-2019'
           def seSuperpone = unaCita.seSuperponeCon(fechaDeSuperposicion)
       then: "la cita no se superpone el 16-12-2019"
           seSuperpone == false
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
