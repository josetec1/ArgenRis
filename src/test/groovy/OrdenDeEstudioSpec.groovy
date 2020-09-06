import argenris.Cita.Cita
import argenris.Cita.EstadoCita.CitaEstaCanceladaException
import argenris.OrdenDeEstudio.EstadoOrden.EstadoDeLaOrden
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenAsignada
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenCancelada
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenEsperaEstudio
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenEsperaInforme
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenEsperaRepro
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenFinalizado
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
	Procedimiento procedimiento
    SalaDeExamen salaDeExamen
    Prioridad prioridad
    String nota
    LocalDateTime fechaDeCreacion
    OrdenDeEstudio unaOrden
    
    def setup() {
         medico = mockDomain Medico
         paciente = mockDomain Paciente
         procedimiento = mockDomain Procedimiento
         salaDeExamen = mockDomain SalaDeExamen
         
         prioridad = Prioridad.NORMAL
         nota = "una nota"
         fechaDeCreacion = LocalDateTime.of(2020,8,9,2,54)
    
         unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaDeCreacion,nota,procedimiento)
    }
    
    def cleanup() {
    }
    
    /*  ************************************************
    ************ Logicas del paso del tiempo:
    *  Finalizada, espera informe, espera estudio, cancelada --> no hace nada
    * Registrada --> si pasan 30 dias cancela
    * Asignada, no hace nada, el job de la cita es la que lo tiene que pasar a esperando repro
    * Esperando repro --> revisas si pasaron 48 hs y la cancelas.
     */
    
    
    
    
    
   
    /*
    Escenario A: Creación de órdenes.
    
    Dado que el usuario utilizando la aplicación tiene el perfil de médico,
    y la misma orden no fue realizada anteriormente,
    y el paciente ya está registrado en el sistema,
    Cuando el médico cree una orden indicando nombre del médico, paciente, departamento que solicita el estudio médico, prioridad de la orden,
    y el procedimiento, fecha de creación de orden y una nota con información adicional,
    Entonces se creará una nueva orden que quedará en estado registrada.
    */
    
    
    void "test01 al crear una orden su estado inicial es registrada" (){
        when: 'una orden de estudio recien creada'
            unaOrden
        then: "la orden queda en estado registrada"
            unaOrden.estadoDeLaOrden == new EstadoOrdenRegistrada()
            unaOrden.estadoDeLaOrden != new EstadoOrdenAsignada()  //ojo por que podes implementar mal el equals y da siempre true
    }
    
    
    /*
    Escenario B: Cancelación de orden por decisión el Médico solicitante en estado
    Registrada o Esperando reprogramación
    Dado que una orden para un estudio médico registrada o esperando reprogramación
    Cuando el médico que la creó cancele la orden,
    entonces  La orden quedará como cancelada, y no se le permitirá al paciente realizarse el estudio médico sin asistir nuevamente al médico, para que le gestione una nueva orden
    */
    
    void " test02 Una orden en estado registrada debe quedar en estado cancelada cuando el medico la cancela" (){
        given: 'una orden recien en estado registrada'
            unaOrden
        when: 'el medico intenta cancelarla'
            unaOrden.cancelar()
        then: 'la orden queda en estado cancelada'
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
            unaOrden.estadoDeLaOrden != new EstadoOrdenRegistrada()
    }
    
    void "test03  Una orden en estado EsperandoReprogramacion debe quedar en estado cancelada cuando el medico la cancela" (){
        given: 'una orden en estado EsperandoReprogramacion'
            
            unaOrden.estadoDeLaOrden = new EstadoOrdenEsperaRepro()
            // unaOrden.notificarCitaCancelada() //todo verificar este metodo
        when: 'el medico decide cancelarla'
            unaOrden.cancelar()
        then: 'la orden queda en estado cancelada'
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
            unaOrden.estadoDeLaOrden != new EstadoOrdenEsperaRepro()
    }
    
    
    /*
    Escenario C: Cancelación de orden por decisión el Médico solicitante en estado Finalizada, Esperando informe, Esperando Estudio o cancelada
    Dado que una orden para un estudio médico finalizada, esperando informe, esperando estudio o cancelada
    Cuando el médico que la creó cancele la orden,
    entonces  la orden no podrá ser cancelada.
    */
    
    
    void "test04 Una orden en estado Finalizado no puede ser cancelada y debe lanzar excepcion" (){
        given: 'una orden en estado Finalizado'
            unaOrden.estadoDeLaOrden = new EstadoOrdenFinalizado()
        when: 'el medico decide cancelarla'
            unaOrden.cancelar()
        then: "debe lanzar excepcion"
            Exception exception = thrown()
            exception.message == 'Error: No se puede cancelar una Orden en estado Finalizado'
    }
    
    void "test05 Una orden en estado Espera informe no puede ser cancelada y debe lanzar excepcion" (){
        given: 'una orden en estado Espera informe'
        unaOrden.estadoDeLaOrden = new EstadoOrdenEsperaInforme()
        when: 'el medico decide cancelarla'
        unaOrden.cancelar()
        then: "debe lanzar excepcion"
        Exception exception = thrown()
        exception.message == 'Error: No se puede cancelar una Orden en estado Espera Informe'
    }
    
    void "test06 Una orden en estado Espera Estudio no puede ser cancelada y debe lanzar excepcion" (){
        given: 'una orden en estado Espera Estudio'
        unaOrden.estadoDeLaOrden = new EstadoOrdenEsperaEstudio()
        when: 'el medico decide cancelarla'
        unaOrden.cancelar()
        then: "debe lanzar excepcion"
        Exception exception = thrown()
        exception.message == 'Error: No se puede cancelar una Orden en estado Espera Estudio'
    }
    
    void "test07 Una orden en estado cancelada no puede ser cancelada y debe lanzar excepcion" (){
        given: 'una orden en estado cancelada'
        unaOrden.estadoDeLaOrden = new EstadoOrdenCancelada()
        when: 'el medico decide cancelarla'
        unaOrden.cancelar()
        then: "debe lanzar excepcion"
        Exception exception = thrown()
        exception.message == 'Error: No se puede cancelar una Orden en estado Cancelada'
    }
    
    
    
    /*
    Escenario D: Cancelación de orden por decisión el Médico solicitante en estado Asignada
    Dado que una orden para un estudio médico asignada
    Cuando el médico que la creó cancele la orden,
    entonces  La orden quedará como cancelada, la cita quedará en estado cancelada
    y no se le permitirá al paciente realizarse el estudio médico sin asistir nuevamente al médico, para que le gestione una nueva orden
    */
    
    void "test08 Una orden en estado asignada cuando el medico la cancela debe quedar cancelada " (){
        given: 'una orden en estado asignada'
            unaOrden.estadoDeLaOrden = new EstadoOrdenAsignada()
        when: 'el medico decide cancelarla'
            unaOrden.cancelar()
        then: "la orden queda en estado cancelada y las citas"
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
   }
    
    void "test09 Una orden en estado asignada con una cita registrada cuando el medico la cancela debe enviar el mensaje cancelar a la cita" (){
        given: 'una orden en estado asignada con una cita registrada'
            def mockCita = Mock Cita
            unaOrden.estadoDeLaOrden = new EstadoOrdenAsignada()
            unaOrden.citas.add(mockCita)
        when: 'el medico decide cancelarla'
            unaOrden.cancelar()
        then: "la cita recibio el mensaje de cancelar"
            1 * mockCita.cancelar()
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
        
    }
    
    void "test10 una orden con estado asignada y con una cita registrada y otra cancelada cuando el medico la cancela debe enviar mensaje de cancelar a todas sus citas ignorando excepciones de las citas" (){
    
        given: 'una orden en estado asignada con una cita cancelada y una registrada'
            def mockCitaRegistrada = Mock Cita
            def stubCitaCancelada = Stub Cita
            stubCitaCancelada.cancelar() >> {throw new CitaEstaCanceladaException()}
            unaOrden.estadoDeLaOrden = new EstadoOrdenAsignada()
            unaOrden.citas.add(mockCitaRegistrada)
            unaOrden.citas.add(stubCitaCancelada)
        when: 'el medico decide cancelarla'
            unaOrden.cancelar()
        then: "las citas reciben el mensaje de cancelar y se ignora la excepcion lanzada por la cita cancelada"
             1 * mockCitaRegistrada.cancelar()
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
    }
    
    
    /********************************************************************************************************************
		   El sistema cancela órdenes por el paso del tiempo
	  *****************************************************************************************************************
	  Escenario A: Cancelación de órdenes por no solicitar cita médica
	  
	  Dado que un paciente tiene una orden registrada
	  y, a la cual  no se le asignó cita médica para realizar el estudio médico
	  Cuando pasen 1 mes desde que la orden fue emitida,
	  entonces La orden quedará como cancelada,
	  y no se le permitirá al paciente realizarse el estudio médico sin asistir nuevamente al médico, para que le gestione una nueva orden
	  */
    
    void " test11 una orden en estado registrada, queda en estado cancelada cuando pasan 30 dias desde que se creo y no se asigno cita" (){
        
        given: 'un paciente tiene una orden registrada el 1-8-2020 a las 23 hs y 59 minutos'
        and: 'no se le asigno cita medica para realizar el estudio medico'
            LocalDateTime fecha30DiasDespues = LocalDateTime.of(2020,8,31,0,0)
            LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,23,59)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        when: 'pasan 30 dias desde que fue creada la orden (31-8-2020 a las 00:00 hs)'
            unaOrden.notificarPasoDelTiempo (fecha30DiasDespues)
        then: 'la orden queda en estado cancelada'
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
            unaOrden.estadoDeLaOrden != new EstadoOrdenRegistrada()
    }
    
    
    
    void "test12 una orden en estado registrada, queda en estado cancelada cuando pasan 31 dias desde que se creo y no se asigno cita" (){
        
        given: 'una orden registrada el 1-8-2020'
        and: 'no se le asigno cita medica para realizar el estudio medico'
            LocalDateTime fecha31DiasDespues = LocalDateTime.of(2020,9,1,0,0)
            LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,0,0)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        when: 'pasan 31 dias desde que fue creada la orden (1-9-2020)'
            unaOrden.notificarPasoDelTiempo (fecha31DiasDespues)
        then: 'la orden queda en estado cancelada'
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
            unaOrden.estadoDeLaOrden != new EstadoOrdenRegistrada()
    }
    
    
    void "test13 una orden en estado registrada, continua en estado registrada cuando pasan 29 dias  desde que se creo aunque no tenga cita" (){
        
        given: 'una orden registrada el 1-8-2020'
        and: 'no se le asigno cita medica para realizar el estudio medico'
        LocalDateTime fecha29DiasDespues = LocalDateTime.of(2020,8,30,0,00)
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,23,59)
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        when: 'pasan 29 dias desde que fue creada la orden (30-8-2020)'
        unaOrden.notificarPasoDelTiempo (fecha29DiasDespues)
        then: 'la orden continua en estado registrada'
        unaOrden.estadoDeLaOrden == new EstadoOrdenRegistrada()
        unaOrden.estadoDeLaOrden != new EstadoOrdenCancelada()
    }
    
    void " test14 una orden en estado finalizada, no es afectada por el paso del tiempo" (){
        
        given: 'una orden finalizada con fecha de creacion el 1-8-2020 a las 23 hs y 59 minutos'
        
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,23,59)
        OrdenDeEstudio unaOrden1= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        OrdenDeEstudio unaOrden2= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        OrdenDeEstudio unaOrden3= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        
        unaOrden1.estadoDeLaOrden = new EstadoOrdenFinalizado()
        unaOrden2.estadoDeLaOrden = new EstadoOrdenFinalizado()
        unaOrden3.estadoDeLaOrden = new EstadoOrdenFinalizado()

        LocalDateTime fecha30DiasDespues = LocalDateTime.of(2020,8,31,0,0)
        LocalDateTime fecha31DiasDespues = LocalDateTime.of(2020,9,1,0,0)
        LocalDateTime fecha29DiasDespues = LocalDateTime.of(2020,8,30,0,00)
        
        when: 'pasan distintos dias desde que fue creada la orden'
        unaOrden1.notificarPasoDelTiempo (fecha30DiasDespues)
        unaOrden2.notificarPasoDelTiempo (fecha31DiasDespues)
        unaOrden3.notificarPasoDelTiempo (fecha29DiasDespues)
        
        then: 'la orden sigue en estado finalizada'
        unaOrden1.estadoDeLaOrden == new EstadoOrdenFinalizado()
        unaOrden1.estadoDeLaOrden != new EstadoOrdenCancelada()
        unaOrden2.estadoDeLaOrden == new EstadoOrdenFinalizado()
        unaOrden2.estadoDeLaOrden != new EstadoOrdenCancelada()
        unaOrden3.estadoDeLaOrden == new EstadoOrdenFinalizado()
        unaOrden3.estadoDeLaOrden != new EstadoOrdenCancelada()
        
    }
    
    void " test15 una orden en estado cancelada, no es afectada por el paso del tiempo" (){
        
        given: 'una orden cancelada con fecha de creacion el 1-8-2020 a las 23 hs y 59 minutos'
        
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,23,59)
        OrdenDeEstudio unaOrden1= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        OrdenDeEstudio unaOrden2= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        OrdenDeEstudio unaOrden3= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        
        unaOrden1.estadoDeLaOrden = new EstadoOrdenCancelada()
        unaOrden2.estadoDeLaOrden = new EstadoOrdenCancelada()
        unaOrden3.estadoDeLaOrden = new EstadoOrdenCancelada()
        
        LocalDateTime fecha30DiasDespues = LocalDateTime.of(2020,8,31,0,0)
        LocalDateTime fecha31DiasDespues = LocalDateTime.of(2020,9,1,0,0)
        LocalDateTime fecha29DiasDespues = LocalDateTime.of(2020,8,30,0,00)
        
        when: 'pasan distintos dias desde que fue creada la orden'
        unaOrden1.notificarPasoDelTiempo (fecha30DiasDespues)
        unaOrden2.notificarPasoDelTiempo (fecha31DiasDespues)
        unaOrden3.notificarPasoDelTiempo (fecha29DiasDespues)
        
        then: 'la orden sigue en estado cancelada'
      
        unaOrden1.estadoDeLaOrden == new EstadoOrdenCancelada()
        unaOrden2.estadoDeLaOrden == new EstadoOrdenCancelada()
        unaOrden3.estadoDeLaOrden == new EstadoOrdenCancelada()
        
    }
    
    void " test16 una orden en estado espera informe, no es afectada por el paso del tiempo" (){
        
        given: 'una orden con estado de  espera informe con fecha de creacion el 1-8-2020 a las 23 hs y 59 minutos'
        
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,23,59)
        OrdenDeEstudio unaOrden1= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        OrdenDeEstudio unaOrden2= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        OrdenDeEstudio unaOrden3= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        
        unaOrden1.estadoDeLaOrden = new EstadoOrdenEsperaInforme()
        unaOrden2.estadoDeLaOrden = new EstadoOrdenEsperaInforme()
        unaOrden3.estadoDeLaOrden = new EstadoOrdenEsperaInforme()
        
        LocalDateTime fecha30DiasDespues = LocalDateTime.of(2020,8,31,0,0)
        LocalDateTime fecha31DiasDespues = LocalDateTime.of(2020,9,1,0,0)
        LocalDateTime fecha29DiasDespues = LocalDateTime.of(2020,8,30,0,00)
        
        when: 'pasan distintos dias desde que fue creada la orden'
        unaOrden1.notificarPasoDelTiempo (fecha30DiasDespues)
        unaOrden2.notificarPasoDelTiempo (fecha31DiasDespues)
        unaOrden3.notificarPasoDelTiempo (fecha29DiasDespues)
        
        then: 'la orden sigue en estado espera informe'
        unaOrden1.estadoDeLaOrden == new EstadoOrdenEsperaInforme()
        unaOrden1.estadoDeLaOrden != new EstadoOrdenCancelada()
        unaOrden2.estadoDeLaOrden == new EstadoOrdenEsperaInforme()
        unaOrden2.estadoDeLaOrden != new EstadoOrdenCancelada()
        unaOrden3.estadoDeLaOrden == new EstadoOrdenEsperaInforme()
        unaOrden3.estadoDeLaOrden != new EstadoOrdenCancelada()
        
    }
    
    void " test17 una orden en estado espera estudio, no es afectada por el paso del tiempo" (){
        
        given: 'una orden con estado de espera estudio con fecha de creacion el 1-8-2020 a las 23 hs y 59 minutos'
        
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,23,59)
        OrdenDeEstudio unaOrden1= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        OrdenDeEstudio unaOrden2= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        OrdenDeEstudio unaOrden3= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        
        unaOrden1.estadoDeLaOrden = new EstadoOrdenEsperaEstudio()
        unaOrden2.estadoDeLaOrden = new EstadoOrdenEsperaEstudio()
        unaOrden3.estadoDeLaOrden = new EstadoOrdenEsperaEstudio()
        
        LocalDateTime fecha30DiasDespues = LocalDateTime.of(2020,8,31,0,0)
        LocalDateTime fecha31DiasDespues = LocalDateTime.of(2020,9,1,0,0)
        LocalDateTime fecha29DiasDespues = LocalDateTime.of(2020,8,30,0,00)
        
        when: 'pasan distintos dias desde que fue creada la orden'
        unaOrden1.notificarPasoDelTiempo (fecha30DiasDespues)
        unaOrden2.notificarPasoDelTiempo (fecha31DiasDespues)
        unaOrden3.notificarPasoDelTiempo (fecha29DiasDespues)
        
        then: 'la orden sigue en estado espera estudio'
        unaOrden1.estadoDeLaOrden == new EstadoOrdenEsperaEstudio()
        unaOrden1.estadoDeLaOrden != new EstadoOrdenCancelada()
        unaOrden2.estadoDeLaOrden == new EstadoOrdenEsperaEstudio()
        unaOrden2.estadoDeLaOrden != new EstadoOrdenCancelada()
        unaOrden3.estadoDeLaOrden == new EstadoOrdenEsperaEstudio()
        unaOrden3.estadoDeLaOrden != new EstadoOrdenCancelada()
        
    }
    
    void " test18 una orden en estado asignada, no es afectada por el paso del tiempo" (){
        
        given: 'una orden con estado asignada con fecha de creacion el 1-8-2020 a las 23 hs y 59 minutos'
        
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,23,59)
        OrdenDeEstudio unaOrden1= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        OrdenDeEstudio unaOrden2= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        OrdenDeEstudio unaOrden3= new OrdenDeEstudio( medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        
        unaOrden1.estadoDeLaOrden = new EstadoOrdenAsignada()
        unaOrden2.estadoDeLaOrden = new EstadoOrdenAsignada()
        unaOrden3.estadoDeLaOrden = new EstadoOrdenAsignada()
        
        LocalDateTime fecha30DiasDespues = LocalDateTime.of(2020,8,31,0,0)
        LocalDateTime fecha31DiasDespues = LocalDateTime.of(2020,9,1,0,0)
        LocalDateTime fecha29DiasDespues = LocalDateTime.of(2020,8,30,0,00)
        
        when: 'pasan distintos dias desde que fue creada la orden'
        unaOrden1.notificarPasoDelTiempo (fecha30DiasDespues)
        unaOrden2.notificarPasoDelTiempo (fecha31DiasDespues)
        unaOrden3.notificarPasoDelTiempo (fecha29DiasDespues)
        
        then: 'la orden sigue en estado espera estudio'
        unaOrden1.estadoDeLaOrden == new EstadoOrdenAsignada()
        unaOrden1.estadoDeLaOrden != new EstadoOrdenCancelada()
        unaOrden2.estadoDeLaOrden == new EstadoOrdenAsignada()
        unaOrden2.estadoDeLaOrden != new EstadoOrdenCancelada()
        unaOrden3.estadoDeLaOrden == new EstadoOrdenAsignada()
        unaOrden3.estadoDeLaOrden != new EstadoOrdenCancelada()
        
    }
   
    /*
    Escenario B : Cancelación de órdenes por no reprogramacion

    Dado que un paciente no se presentó a su cita, o bien, la cancelo anticipadamente
    y  tiene una orden esperando reprogramación,
    y la cita no se reprogramo para otro dia
    cuando cuando pasen 48 hs desde desde la fecha en que la orden quedó reprogramada
    entonces  La orden quedará como cancelada,
     y no se le permitirá al cliente realizarse el estudio médico sin asistir nuevamente al médico, para que le gestione una nueva orden

    */
    
    
    void "test19 una orden en estado Esperando Reprogramacion, reprogramada el dia 20-8-2020 queda en estado cancelada 48 hs despues" (){
        
        given: 'un paciente tiene una orden esperando reprogramacion desde el 20-8-2020'
        and: 'no se le asigno cita medica nueva'
            LocalDateTime fechaDeCreacionDeOrden = LocalDateTime.of(2020,8,1,0,0)
            LocalDateTime fechaDeEntradaEnReprogramacion = LocalDateTime.of(2020,8,20,0,0)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacionDeOrden, nota, procedimiento)
            unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
        when: 'pasen 48 hs (22-8-2020)'
            LocalDateTime fecha48HsPosteriores = LocalDateTime.of(2020,8,22,0,0)
            unaOrden.notificarPasoDelTiempo(fecha48HsPosteriores)
        then: 'la orden queda en estado cancelada'
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
            unaOrden.estadoDeLaOrden != new EstadoOrdenEsperaRepro()
    }
    
    void "test20 una orden en estado Esperando Reprogramacion, reprogramada el dia 20-8-2020 queda en estado cancelada 48 hs y 1 minuto despues" (){
        
        given: 'un paciente tiene una orden esperando reprogramacion desde el 20-8-2020'
        and: 'no se le asigno cita medica nueva'
        LocalDateTime fechaDeCreacionDeOrden = LocalDateTime.of(2020,8,1,0,0)
        LocalDateTime fechaDeEntradaEnReprogramacion = LocalDateTime.of(2020,8,20,0,0)
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacionDeOrden, nota, procedimiento)
        unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
        when: 'pasen 48 hs (22-8-2020)'
        LocalDateTime fecha48HsY1MinPosteriores = LocalDateTime.of(2020,8,22,0,1)
        unaOrden.notificarPasoDelTiempo(fecha48HsY1MinPosteriores)
        then: 'la orden queda en estado cancelada'
        unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
        unaOrden.estadoDeLaOrden != new EstadoOrdenEsperaRepro()
    }
    
    void "test21 una orden en estado Esperando Reprogramacion, reprogramada el dia 20-8-2020 continua en estado esperando reprogramacion 47 hs 59 minutos despues" (){
        
        given: 'un paciente tiene una orden esperando reprogramacion desde el 20-8-2020'
        and: 'no se le asigno cita medica nueva'
        LocalDateTime fechaDeCreacionDeOrden = LocalDateTime.of(2020,8,1,0,0)
        LocalDateTime fechaDeEntradaEnReprogramacion = LocalDateTime.of(2020,8,20,0,1)
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacionDeOrden, nota, procedimiento)
        unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
        when: 'pasen 48 hs (22-8-2020)'
        LocalDateTime fecha47HsY59MinPosteriores = LocalDateTime.of(2020,8,22,0,0)
        unaOrden.notificarPasoDelTiempo(fecha47HsY59MinPosteriores)
        then: 'la orden queda en estado cancelada'
        unaOrden.estadoDeLaOrden == new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
        unaOrden.estadoDeLaOrden != new EstadoOrdenCancelada()
    }

    
    
    
    
    
    
    
    
    /* **********                 *********************************************/
  
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
        
       // salaDeExamen = new SalaDeExamen();
      //  Medico medico = mockDomain Medico
      //  Paciente paciente = mockDomain Paciente
      //  Prioridad prioridad = Prioridad.NORMAL
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
        
      //  salaDeExamen = new SalaDeExamen();
      //  Medico medico = mockDomain Medico
      //  Paciente paciente = mockDomain Paciente
      //  Prioridad prioridad = Prioridad.NORMAL
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
        salaDeExamen = new SalaDeExamen()
        //Medico medico = mockDomain Medico
       // Paciente paciente = mockDomain Paciente
       // Prioridad prioridad = Prioridad.NORMAL
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
