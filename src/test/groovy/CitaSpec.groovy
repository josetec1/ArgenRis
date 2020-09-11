

import argenris.Cita.Cita
import argenris.Cita.EstadoCita.CitaEstaCanceladaException
import argenris.Cita.EstadoCita.CitaEstaConcretadaException
import argenris.Cita.EstadoCita.CitaNoSePuedeArribarException
import argenris.Cita.EstadoCita.EstadoCitaConcretada
import argenris.Cita.EstadoCita.EstadoCitaPlanificada
import argenris.OrdenDeEstudio.OrdenDeEstudio
import argenris.Prioridad
import argenris.Cita.EstadoCita.EstadoCitaCancelada
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class CitaSpec extends Specification implements DomainUnitTest<Cita> {
    
    LocalDateTime fechaDeCita
	Prioridad prioridadNormal = Prioridad.NORMAL
    OrdenDeEstudio unaOrden
    def setup() {
        fechaDeCita = new LocalDateTime(new LocalDate(1998,1,23),new LocalTime(23,00,00,00))
        unaOrden = Mock OrdenDeEstudio
    }

    def cleanup() {
    }
    
 
    void "test01 al crear una cita, se encuentra en estado Planificada" (){
        
        when:'una cita recien creada'
        
            Cita unaCita= new Cita(fechaDeCita, prioridadNormal,unaOrden)
        
        then: "la cita tiene el estado Planificada"
        
            unaCita.estadoDeCita instanceof EstadoCitaPlanificada
        
        
    }
    
    void "test02 al crear una cita, almacena la orden" (){
        when:'una cita recien creada'
            Cita unaCita= new Cita(fechaDeCita, prioridadNormal,unaOrden)
        then: "guarda la orden"
            unaOrden == unaCita.ordenDeEstudio
    }
    
    
    void "test02-1 se crea una cita el 1-1-2020, y la cita no estaVencida el 1-1-2020" (){
        given:'se crea una cita con fecha 1-1-2020'
            LocalDateTime fechaDeCreacionCita
            fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
            Cita unaCita= new Cita (fechaDeCreacionCita, Prioridad.NORMAL,unaOrden)

        when:'cita estaVencida 1-1-2020'
            def vencida = unaCita.estaVencida(fechaDeCreacionCita)
        then: "la cita no esta vencida el 1-1-2020"
            vencida == false
    }
    
    
    
    
    
    
    
    
    
 
    

    
    
    /****************************************************************************
     * *********************PRUEBO CAMBIOS DE ESTADO ***************************
     ****************************************************************************/
    void "test03 al cancelar una cita en estado Planificada, queda con estado cancelada" (){
        
        given: 'una cita recien creada'
        Cita unaCita= new Cita (fechaDeCita,prioridadNormal,unaOrden)
        
        when:
        unaCita.cancelar(fechaDeCita)
        
        then: "la cita queda en estado cancelada"
        
        unaCita.estadoDeCita instanceof EstadoCitaCancelada
    }
    
    void "test03-1 al cancelar una cita en estado Planificada, avisa a la orden" (){
        
        given: 'una cita recien creada'
        LocalDateTime fechaDeCita = LocalDateTime.of(2020,8,1,12,54)
        Cita unaCita= new Cita (fechaDeCita,prioridadNormal,unaOrden)
        LocalDateTime fechaActual = LocalDateTime.of(2020,8,1,10,0)
        when:
        
        unaCita.cancelar(fechaActual)
        
        then: "la cita avisa a la orden"
        1 * unaOrden.notificarCitaCancelada(fechaActual)
    }
    
    
    
    
    
    void "test04 al cancelar una cita en estado Cancelada, debe lanzar excepcion" (){
        
        given: 'una cita cancelada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        unaCita.cancelar(fechaDeCita)
        
        when:
        unaCita.cancelar(fechaDeCita)
        
        then: "debe lanzar excepcion"
        
        thrown CitaEstaCanceladaException
        
    }
    
    void "test05 al cancelar una cita en estado Concretada, debe lanzar excepcion" (){
        
        given: 'una cita concretada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        unaCita.pacienteArribando(fechaDeCita)
        
        when:
        unaCita.cancelar(fechaDeCita)
        
        then: "debe lanzar excepcion"
        
        thrown CitaEstaConcretadaException
    }
    
    
    
    void "test06 pacienteArribando a una cita en estado Planificada, queda con estado concretada" (){
        
        given: 'una cita recien creada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        
        when:
        unaCita.pacienteArribando(fechaDeCita)
        
        then: "la cita queda en estado concretada"
        
        unaCita.estadoDeCita instanceof EstadoCitaConcretada
    }
    
    void "test07 pacienteArribando a una cita en estado Cancelada, debe lanzar excepcion" (){
        
        given: 'una cita cancelada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        unaCita.cancelar(fechaDeCita)
        
        when:
        unaCita.pacienteArribando()
        
        then: "debe lanzar excepcion"
        
        thrown CitaEstaCanceladaException
        
    }
    
    void "test08 pacienteArribando una cita en estado Concretada, debe lanzar excepcion" (){
        
        given: 'una cita concretada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
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
        
        Cita unaCita= new Cita (fechaDeCreacionCita, prioridadNormal,unaOrden)
        
        when:
        unaCita.cancelar(fechaDeCita)
        
        then: "la cita no se encuentra vencida"
        
        
        unaCita.estaVencida(fechaDeCreacionCita) == false
        
    }
    
    void "test10 una cita en estado concretada con fecha  1-1-2020 no estaVencida el 1-1-2020" (){
        
        given: 'una cita recien creada con fecha 1-1-2020'
        
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCreacionCita, prioridadNormal,unaOrden)
        
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
        
        Cita unaCita= new Cita (fechaDeCreacionCita, prioridadNormal,unaOrden)
        
        when:
        LocalDateTime fechaDeConsultaDeVigenciaDeCita= new LocalDateTime(new LocalDate(2020,2,27),new LocalTime(11,00,00,00))
        
        then: "la cita no se encuentra vencida"
        unaCita.estaVencida(fechaDeConsultaDeVigenciaDeCita) == false
        
    }
    
    void "test12 una cita creada con fecha para el 28-2-2020 a las 10 hs, el dia 28-2-2020 a las 10:30 hs no estaVencida" (){
        
        given: 'una cita  creada para el 28-2-2020 a las 10 hs'
        
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,2,28),new LocalTime(10,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCreacionCita, prioridadNormal,unaOrden)
        
        when:
        LocalDateTime fechaDeConsultaDeVigenciaDeCita= new LocalDateTime(new LocalDate(2020,2,28),new LocalTime(10,30,00,00))
        
        then: "la cita no se encuentra vencida"
        unaCita.estaVencida(fechaDeConsultaDeVigenciaDeCita) == false
        
    }
    
    
    
    void "test13 una cita creada el 28-2-2020 a las 10 hs, el dia 28-2-2020 a las 10:31 hs estaVencida" (){
        
        given: 'una cita  creada para el 28-2-2020 a las 10 hs'
        
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,2,28),new LocalTime(10,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCreacionCita, prioridadNormal,unaOrden)
        
        when:
        LocalDateTime fechaDeConsultaDeVigenciaDeCita= new LocalDateTime(new LocalDate(2020,2,28),new LocalTime(10,31,00,00))
        
        then: "la cita estaVencida"
        unaCita.estaVencida(fechaDeConsultaDeVigenciaDeCita) == true
        
    }
    
    
    void "test14 pacienteArribando a una cita en estado Planificada para el dia 10-3-2020, el dia 9-3-2020 debe lanzar excepcion" (){
        
        given: 'una cita Planificada con fechaDeCita y fechaDeArriboAnterior'
        
        LocalDateTime fechaDeCita =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,00,00,00))
        LocalDateTime fechaDeArriboAnterior =  new LocalDateTime(new LocalDate(2020,3,9),new LocalTime(10,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        
        when:
        unaCita.pacienteArribando(fechaDeArriboAnterior)
        
        then: "debe lanzar excepcion"
        
        thrown CitaNoSePuedeArribarException
        
    }
    
    
    void "test15 pacienteArribando a  una cita en estado Planificada para el dia 10-3-2020, el dia 11-3-2020 debe lanzar excepcion" (){
        
        given: 'una cita Planificada con fechaDeCita y fechaDeArriboPosterior'
        
        LocalDateTime fechaDeCita =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,00,00,00))
        LocalDateTime fechaDeArriboPosterior =  new LocalDateTime(new LocalDate(2020,3,11),new LocalTime(1,00,00,00))
        
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        
        when:
        unaCita.pacienteArribando(fechaDeArriboPosterior)
        
        then: "debe lanzar excepcion"
        thrown CitaNoSePuedeArribarException
        
    }
    
    void "test16 pacienteArribando a una cita en estado Planificada para el dia 10-3-2020 10 hs, el 10-3-2020 a las 10:31 debe lanzar excepcion" (){
        
        given: 'una cita Planificada con fechaDeCita y fechaDeArriboPosterior'
        
        LocalDateTime fechaDeCita =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,00,00,00))
        LocalDateTime fechaDeArriboPosterior=  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,31,00,00))
        
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        
        when:
        unaCita.pacienteArribando(fechaDeArriboPosterior)
        
        then: "debe lanzar excepcion"
        thrown CitaNoSePuedeArribarException
        
    }
    
    void "test17 pacienteArribando a una cita en estado Planificada para el dia 10-3-2020 10:00, el 10-3-2020 a las 10:30 debe quedar en estado concretada" (){
        
        given: 'una cita Planificada con fechaDeCita y fechaDeArribo'
        
        LocalDateTime fechaDeCita =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,00,00,00))
        LocalDateTime fechaDeArribo =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,30,00,00))
        
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        
        when:
        unaCita.pacienteArribando(fechaDeArribo)
        
        then: "debe quedar en estado concretada"
        unaCita.estadoDeCita instanceof EstadoCitaConcretada
        
    }
    
    void "test17-0 pacienteArribando a una cita en estado Planificada avisa a la orden que la cita se concreto" (){
        
        given: 'una cita Planificada con fechaDeCita y fechaDeArribo'
        
        LocalDateTime fechaDeCita =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,00,00,00))
        LocalDateTime fechaDeArribo =  new LocalDateTime(new LocalDate(2020,3,10),new LocalTime(10,30,00,00))
        
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        
        when:
        unaCita.pacienteArribando(fechaDeArribo)
        
        then: "avisa a la orden que la cita fue concretada"
        1 * unaOrden.notificarCitaConcretada(fechaDeArribo)
        unaCita.estadoDeCita instanceof EstadoCitaConcretada
        
    }
    
    void "test17-1 al cancelar una cita vencida  en estado Planificada, avisa a la orden con la fecha de vencimiento" (){
        
        given: 'una cita recien creada'
            LocalDateTime fechaDeVencimientoDeCita = LocalDateTime.of(2020,8,1,0,30)
            Cita unaCita= new Cita (fechaDeVencimientoDeCita,prioridadNormal,unaOrden)
            LocalDateTime fechaActual = LocalDateTime.of(2020,8,1,1,01)
        when:
          unaCita.cancelar(fechaActual)
        
        then: "la cita avisa a la orden pasandole la fecha de vencimiento"
        unaCita.estaVencida(fechaActual) == true
        1 * unaOrden.notificarCitaCancelada(fechaDeVencimientoDeCita)
        0 * unaOrden.notificarCitaCancelada(fechaActual)
    }
    
    void "test17-2 al cancelar una cita No vencida en estado Planificada, avisa a la orden con la fechaActual" (){
        
        given: 'una cita recien creada'
        LocalDateTime fechaDeVencimientoDeCita = LocalDateTime.of(2020,8,1,0,30)
        Cita unaCita= new Cita (fechaDeVencimientoDeCita,prioridadNormal,unaOrden)
        LocalDateTime fechaActual = LocalDateTime.of(2020,8,1,0,33)
        when:
        unaCita.cancelar(fechaActual)
        
        then: "la cita avisa a la orden pasandole la fecha de vencimiento"
        unaCita.estaVencida(fechaActual) == false
        1 * unaOrden.notificarCitaCancelada(fechaActual)
        0 * unaOrden.notificarCitaCancelada(fechaDeVencimientoDeCita)
    }
    
    
    
    
    
    
    
    /****************************************************************************************************
     ************************************************ superposicion de citas********************************
     *************************************************************************************************/
    
    void "test18 se crea una cita Normal el 1-1-2020, y la cita se superpone con el 1-1-2020" () {
        given:
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
        Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.NORMAL,unaOrden)
        when:'cita seSuperpone con fecha 1-1-2020'
        def seSuperpone = unaCita.seSuperponeCon(fechaDeCreacionCita)
        then: "la cita se superpone el 1-1-2020"
        seSuperpone == true
    }
    
    void "test19 se crea una cita Normal el 1-1-2020, y la cita no se superpone con el 1-2-2020" () {
        given:
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
        Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.NORMAL,unaOrden)
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
        Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.NORMAL,unaOrden)
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
        Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.URGENTE,unaOrden)
        when:'cita se superpone con fecha 1-15-2020'
        def seSuperpone = unaCita.seSuperponeCon(fechaDeCreacionCita)
        then: "la cita se superpone el 1-15-2020"
        seSuperpone == true
    }
    
    void "test22 se crea una cita Urgente el 1-1-2020, y la cita no se superpone con el 16-1-2020" () {
        given:
        LocalDateTime fechaDeCreacionCita
        fechaDeCreacionCita =  new LocalDateTime(new LocalDate(2020,1,1),new LocalTime(20,00,00,00))
        Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.URGENTE,unaOrden)
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
        Cita unaCita = new Cita (fechaDeCreacionCita, Prioridad.URGENTE,unaOrden)
        def fechaDeSuperposicion =  new LocalDateTime(new LocalDate(2019,12,16),new LocalTime(20,00,00,00))
        when:'cita no se se superpone con fecha 16-12-2019'
        def seSuperpone = unaCita.seSuperponeCon(fechaDeSuperposicion)
        then: "la cita no se superpone el 16-12-2019"
        seSuperpone == false
    }
    
    /*
                Paso del tiempo
     */
    
    
    void "test30 una cita en estado cancelada no es afectada por el paso del tiempo" () {
        given: 'una cita cancelada'
            Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
            unaCita.cancelar(fechaDeCita)
        when: 'se notifica el paso del tiempo'
            unaCita.notificarPasoDelTiempo(fechaDeCita)
        then: "continua en estado cancelada"
        unaCita.estadoDeCita instanceof EstadoCitaCancelada
    }
    
    void "test31 una cita en estado concretada no es afectada por el paso del tiempo" () {
        given: 'una cita concretada'
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        unaCita.pacienteArribando(fechaDeCita)
      
        when: 'se notifica el paso del tiempo'
        unaCita.notificarPasoDelTiempo(fechaDeCita)
        then: "continua en estado concretada"
        unaCita.estadoDeCita instanceof EstadoCitaConcretada
    }
    
    void "test32 una cita en estado planificada no vencida no es afectada por el paso del tiempo" () {
        given: 'una cita planificada no vencida'
        LocalDateTime fechaDeCita = LocalDateTime.of(2020,8,20,10,0)
        LocalDateTime fechaActualNoVencida = LocalDateTime.of(2020,8,20,9,0)
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        
        when: 'se notifica el paso del tiempo'
        unaCita.notificarPasoDelTiempo(fechaActualNoVencida)
        then: "continua en estado planificada"
        unaCita.estadoDeCita instanceof EstadoCitaPlanificada
        
        
    }
    void "test33 una cita en estado planificada vencida queda cancelada al llamar a notificarPasoDelTiempo" () {
        given: 'una cita planificada  vencida'
        LocalDateTime fechaDeCita = LocalDateTime.of(2020,8,20,10,0)
        LocalDateTime fechaActualVencida = LocalDateTime.of(2020,8,20,11,0)
        Cita unaCita= new Cita (fechaDeCita, prioridadNormal,unaOrden)
        def estadoCita = unaCita.getEstadoDeCita()
        when: 'se notifica el paso del tiempo'
        unaCita.notificarPasoDelTiempo(fechaActualVencida)
        then: "la cita queda Cancelada y notifica a la orden la cancelacion"
         unaCita.estadoDeCita instanceof EstadoCitaCancelada
        1 * unaOrden.notificarCitaCancelada(fechaActualVencida)
    }
}
