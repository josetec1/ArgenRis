package argenris

import argenris.Cita.Cita
import argenris.Cita.EstadoCita.CitaEstaCanceladaException
import argenris.Cita.EstadoCita.CitaEstaConcretadaException
import argenris.Cita.EstadoCita.CitaNoSePuedeArribarException
import argenris.Cita.EstadoCita.EstadoCitaCancelada
import argenris.Cita.EstadoCita.EstadoCitaConcretada
import argenris.Cita.EstadoCita.EstadoCitaRegistrada
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class CitaSpec extends Specification implements DomainUnitTest<Cita> {
    
    LocalDateTime fechaDeCita
    Prioridad prioridadNormal = Prioridad.NORMAL

    def setup() {
        fechaDeCita = new LocalDateTime(new LocalDate(1998,1,23),new LocalTime(23,00,00,00))
        
    }

    def cleanup() {
    }

    
    void "test01 al crear una cita, se encuentra en estado registrada" (){
        
        when:'una cita recien creada'
        
        Cita unaCita= new Cita(fechaDeCita, prioridadNormal)
        
        then: "la cita tiene el estado registrado"
        
        unaCita.estadoDeCita instanceof EstadoCitaRegistrada
        
        
    }
    
    
    void "test02 se crea una cita el 1-1-2020, y la cita no estaVencida el 1-1-2020" (){
        given:'se crea una cita con fecha 1-1-2020'
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            Cita unaCita= new Cita (fechaDeCreacionCita, Prioridad.NORMAL)

        when:'cita estaVencida 1-1-2020'
            def vencida = unaCita.estaVencida(fechaDeCreacionCita)
        then: "la cita no esta vencida el 1-1-2020"
            vencida == false
    }
    
    
    
    
    void "test18 se crea una cita Normal el 1-1-2020, y la cita se superpone con el 1-1-2020" () {
       given:
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.NORMAL)
       when:'cita seSuperpone con fecha 1-1-2020'
            def seSuperpone = unaCita.seSuperponeCon(fechaDeCreacionCita)
       then: "la cita se superpone el 1-1-2020"
            seSuperpone == true
    }

    void "test19 se crea una cita Normal el 1-1-2020, y la cita no se superpone con el 1-2-2020" () {
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

    void "test20 se crea una cita Normal el 1-1-2020, y la cita no se superpone con el 1-12-2019" () {
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

    void "test21 se crea una cita Urgente el 1-1-2020, y la cita se superpone con el 1-15-2020" () {
       given:
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,15),new LocalTime(20,00,00,00))
            Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.URGENTE)
       when:'cita se superpone con fecha 1-15-2020'
            def seSuperpone = unaCita.seSuperponeCon(fechaDeCreacionCita)
       then: "la cita se superpone el 1-15-2020"
            seSuperpone == true
    }

    void "test22 se crea una cita Urgente el 1-1-2020, y la cita no se superpone con el 16-1-2020" () {
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

    void "test23 se crea una cita Urgente el 1-1-2020, y la cita no se superpone con el 16-12-2019" () {
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
    
    
    
    
 
    

    
    
    /****************************************************************************
     * *********************PRUEBO CAMBIOS DE ESTADO ***************************
     ****************************************************************************/
    void "test03 al cancelar una cita en estado registrada, queda con estado cancelada" (){
        
        given: 'una cita recien creada'
        Cita unaCita= new Cita (fechaDeCita,prioridadNormal)
        
        when:
        unaCita.cancelar()
        
        then: "la cita queda en estado cancelada"
        
        unaCita.estadoDeCita instanceof EstadoCitaCancelada
    }
    
    void "test04 al querer cancelar una cita en estado Cancelada, debe lanzar excepcion" (){
        
        given: 'una cita cancelada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal)
        unaCita.cancelar()
        
        when:
        unaCita.cancelar()
        
        then: "debe lanzar excepcion"
        
        thrown CitaEstaCanceladaException
        
    }
    
    void "test05 al querer cancelar una cita en estado Concretada, debe lanzar excepcion" (){
        
        given: 'una cita concretada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal)
        unaCita.pacienteArribando(fechaDeCita)
        
        when:
        unaCita.cancelar()
        
        then: "debe lanzar excepcion"
        
        thrown CitaEstaConcretadaException
    }
    
    
    
    void "test06 al concretar una cita en estado registrada, queda con estado concretada" (){
        
        given: 'una cita recien creada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal)
        
        when:
        unaCita.pacienteArribando(fechaDeCita)
        
        then: "la cita queda en estado concretada"
        
        
        unaCita.estadoDeCita instanceof EstadoCitaConcretada
    }
    
    void "test07 al querer concretar una cita en estado Cancelada, debe lanzar excepcion" (){
        
        given: 'una cita cancelada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal)
        unaCita.cancelar()
        
        when:
        unaCita.pacienteArribando()
        
        then: "debe lanzar excepcion"
        
        thrown CitaEstaCanceladaException
        
    }
    
    void "test08 al querer concretar una cita en estado Concretada, debe lanzar excepcion" (){
        
        given: 'una cita concretada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal)
        unaCita.pacienteArribando(fechaDeCita)
        
        when:
        unaCita.pacienteArribando(fechaDeCita)
        
        then: "debe lanzar excepcion"
        
        thrown CitaEstaConcretadaException
    }
    
    void "test09 una cita en estado cancelada con fecha  1-1-2020 no estaVencida el 1-1-2020" (){
        
        
        given: 'una cita recien creada con fecha 1-1-2020'
        
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCreacionCita, prioridadNormal)
        
        when:
        unaCita.cancelar()
        
        then: "la cita no se encuentra vencida"
        
        
        unaCita.estaVencida(fechaDeCreacionCita) == false
        
    }
    
    void "test10 una cita en estado concretada con fecha  1-1-2020 no estaVencida el 1-1-2020" (){
        
        given: 'una cita recien creada con fecha 1-1-2020'
        
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCreacionCita, prioridadNormal)
        
        when:
        unaCita.pacienteArribando(fechaDeCreacionCita)
        
        then: "la cita no se encuentra vencida"
        
        
        unaCita.estaVencida(fechaDeCreacionCita) == false
        
    }
    
    
    
    /******************************************************************************************************************
     ********************************************* VALIDACION DE HORARIOS Y FECHAS *************************************
     ******************************************************************************************************************/
/*
Cuando no esta vigente una fecha
1) vigente hasta 30 minutos despues de la fecha de cita
*/
    
    
    void "test11 una cita creada para el 28-2-2020 a las 10 hs, el dia 27-2-2020 a las 11 hs no estaVencida" (){
        
        given: 'una cita creada para el 28-2-2020 a las 10 hs'
        
        LocalDateTime fechaDeCreacionCita
        
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,2,28),new LocalTime(10,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCreacionCita, prioridadNormal)
        
        when:
        LocalDateTime fechaDeConsultaDeVigenciaDeCita= new LocalDateTime(new LocalDate(2020,2,27),new LocalTime(11,00,00,00))
        
        then: "la cita no se encuentra vencida"
        unaCita.estaVencida(fechaDeConsultaDeVigenciaDeCita) == false
        
    }
    
    void "test12 una cita creada con fecha para el 28-2-2020 a las 10 hs, el dia 28-2-2020 a las 10:30 hs no estaVencida" (){
        
        given: 'una cita  creada para el 28-2-2020 a las 10 hs'
        
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,2,28),new LocalTime(10,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCreacionCita, prioridadNormal)
        
        when:
        LocalDateTime fechaDeConsultaDeVigenciaDeCita= new LocalDateTime(new LocalDate(2020,2,28),new LocalTime(10,30,00,00))
        
        then: "la cita no se encuentra vencida"
        unaCita.estaVencida(fechaDeConsultaDeVigenciaDeCita) == false
        
    }
    
    
    
    void "test13 una cita creada el 28-2-2020 a las 10 hs, el dia 28-2-2020 a las 10:31 hs estaVencida" (){
        
        given: 'una cita  creada para el 28-2-2020 a las 10 hs'
        
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,2,28),new LocalTime(10,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCreacionCita, prioridadNormal)
        
        when:
        LocalDateTime fechaDeConsultaDeVigenciaDeCita= new LocalDateTime(new LocalDate(2020,2,28),new LocalTime(10,31,00,00))
        
        then: "la cita estaVencida"
        unaCita.estaVencida(fechaDeConsultaDeVigenciaDeCita) == true
        
    }
    
    
    void "test14 al intentar arribar a una cita en estado registrada para el dia 10-3-2020, el dia 9-3-2020 debe lanzar excepcion" (){
        
        given: 'una cita registrada con fechaDeCita y fechaDeArriboAnterior'
        
        LocalDateTime fechaDeCita =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,00,00,00))
        LocalDateTime fechaDeArriboAnterior =  new LocalDateTime(new LocalDate(2020,3,9),new LocalTime(10,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal)
        
        when:
        unaCita.pacienteArribando(fechaDeArriboAnterior)
        
        then: "debe lanzar excepcion"
        
        thrown CitaNoSePuedeArribarException
        
    }
    
    
    void "test15 al intentar arribar a  una cita en estado registrada para el dia 10-3-2020, el dia 11-3-2020 debe lanzar excepcion" (){
        
        given: 'una cita registrada con fechaDeCita y fechaDeArriboPosterior'
        
        LocalDateTime fechaDeCita =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,00,00,00))
        LocalDateTime fechaDeArriboPosterior =  new LocalDateTime(new LocalDate(2020,3,11),new LocalTime(1,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal)
        
        when:
        unaCita.pacienteArribando(fechaDeArriboPosterior)
        
        then: "debe lanzar excepcion"
        thrown CitaNoSePuedeArribarException
        
    }
    
    void "test16 al intentar arribar a una cita en estado registrada para el dia 10-3-2020 10 hs, el 10-3-2020 a las 10:31 debe lanzar excepcion" (){
        
        given: 'una cita registrada con fechaDeCita y fechaDeArriboPosterior'
        
        LocalDateTime fechaDeCita =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,00,00,00))
        LocalDateTime fechaDeArriboPosterior=  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,31,00,00))
        
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal)
        
        when:
        unaCita.pacienteArribando(fechaDeArriboPosterior)
        
        then: "debe lanzar excepcion"
        thrown CitaNoSePuedeArribarException
        
    }
    
    void "test17 al intentar arribar a una cita en estado registrada para el dia 10-3-2020 10:00, el 10-3-2020 a las 10:30 debe quedar en estado concretada" (){
        
        given: 'una cita registrada con fechaDeCita y fechaDeArribo'
        
        LocalDateTime fechaDeCita =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,00,00,00))
        LocalDateTime fechaDeArribo =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,30,00,00))
        
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal)
        
        when:
        unaCita.pacienteArribando(fechaDeArribo)
        
        then: "debe quedar en estado concretada"
        unaCita.estadoDeCita instanceof EstadoCitaConcretada
        
    }
    

}
