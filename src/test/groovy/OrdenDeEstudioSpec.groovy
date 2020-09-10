import argenris.AreaDeExamen
import argenris.Cita.Cita
import argenris.Cita.EstadoCita.CitaEstaCanceladaException
import argenris.OrdenDeEstudio.EstadoOrden.*

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
    
    /*  ***************************************************************************************************************
    ***************************************** Logicas del paso del tiempo: ********************************************
    *  Finalizada, espera informe, espera estudio, cancelada --> no hace nada
    * Registrada --> si pasan 30 dias cancela
    * Asignada, no hace nada, el job de la cita es la que lo tiene que  enviarnos el mensaje para pasar a esperando repro
    * Esperando repro --> revisas si pasaron 2 dias y la cancelas.
    * *****************************************************************************************************************
     */
    
    /*  ***************************************************************************************************************
    ***************************************** Logicas para agregar cita: ********************************************
    *  Finalizada, espera informe, espera estudio, cancelada --> no hace nada
    * Registrada --> revisa si pasaron 30 dias por si el sistema no transacciono (el job)
    * Asignada, si la cancelo el usuario te enteras pq llega un mensaje. Pero si la cita vencio y no transacciono el job
    *  (esto puede pasar si vencio durante el dia). Decido: Va a tener que cancelar la cita el recepcionista.
    *
    * Esperando repro --> revisas si pasaron 2 dias por si el sistema no transacciono  (el job)
    * *****************************************************************************************************************
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
    
    void "test02 Una orden en estado registrada debe quedar en estado cancelada cuando el medico la cancela" (){
        given: 'una orden recien en estado registrada'
            unaOrden
        when: 'el medico intenta cancelarla'
            unaOrden.cancelar()
        then: 'la orden queda en estado cancelada'
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
            unaOrden.estadoDeLaOrden != new EstadoOrdenRegistrada()
    }
    
    void "test03 Una orden en estado EsperandoReprogramacion debe quedar en estado cancelada cuando el medico la cancela" (){
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
    
    void "test10 una orden en estado asignada y con una cita registrada y otra cancelada cuando el medico la cancela debe enviar mensaje de cancelar a todas sus citas ignorando excepciones de las citas" (){
    
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
    
    void "test11 una orden en estado registrada, queda en estado cancelada cuando pasan 30 dias desde que se creo y no se asigno cita" (){
        
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
    
    void "test14 una orden en estado finalizada, no es afectada por el paso del tiempo" (){
        
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
    
    void "test15 una orden en estado cancelada, no es afectada por el paso del tiempo" (){
        
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
    
    void "test16 una orden en estado espera informe, no es afectada por el paso del tiempo" (){
        
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
    
    void "test17 una orden en estado espera estudio, no es afectada por el paso del tiempo" (){
        
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
    
    void "test18 una orden en estado asignada, no es afectada por el paso del tiempo" (){
        
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
    cuando cuando pasen 2 dias  desde desde la fecha en que la orden quedó reprogramada
    entonces  La orden quedará como cancelada,
     y no se le permitirá al cliente realizarse el estudio médico sin asistir nuevamente al médico, para que le gestione una nueva orden

    */
    
    
    void "test19 una orden en estado Esperando Reprogramacion, reprogramada el dia 20-8-2020 queda en estado cancelada el 22-8-2020 hs despues" (){
        
        given: 'un paciente tiene una orden esperando reprogramacion desde el 20-8-2020'
        and: 'no se le asigno cita medica nueva'
            LocalDateTime fechaDeCreacionDeOrden = LocalDateTime.of(2020,8,1,0,0)
            LocalDateTime fechaDeEntradaEnReprogramacion = LocalDateTime.of(2020,8,20,23,0)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacionDeOrden, nota, procedimiento)
            unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
        when: 'pasen 2 dias (22-8-2020)'
            LocalDateTime fecha2diasPosteriores = LocalDateTime.of(2020,8,22,0,0)
            unaOrden.notificarPasoDelTiempo(fecha2diasPosteriores)
        then: 'la orden queda en estado cancelada'
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
            unaOrden.estadoDeLaOrden != new EstadoOrdenEsperaRepro()
    }
    
    void "test20 una orden en estado Esperando Reprogramacion, reprogramada el dia 20-8-2020 queda en estado cancelada el 23-8-2020" (){
        
        given: 'un paciente tiene una orden esperando reprogramacion desde el 20-8-2020'
        and: 'no se le asigno cita medica nueva'
            LocalDateTime fechaDeCreacionDeOrden = LocalDateTime.of(2020,8,1,0,0)
            LocalDateTime fechaDeEntradaEnReprogramacion = LocalDateTime.of(2020,8,20,0,0)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacionDeOrden, nota, procedimiento)
            unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
        when: 'pasen 3 dias (23-8-2020)'
            LocalDateTime fecha3DiasPosteriores = LocalDateTime.of(2020,8,23,0,1)
            unaOrden.notificarPasoDelTiempo(fecha3DiasPosteriores)
        then: 'la orden queda en estado cancelada'
            unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
            unaOrden.estadoDeLaOrden != new EstadoOrdenEsperaRepro()
    }
    
    void "test21 una orden en estado Esperando Reprogramacion, reprogramada el dia 20-8-2020 continua en estado esperando reprogramacion el 21-8-2020 despues" (){
        
        given: 'un paciente tiene una orden esperando reprogramacion desde el 20-8-2020'
        and: 'no se le asigno cita medica nueva'
            LocalDateTime fechaDeCreacionDeOrden = LocalDateTime.of(2020,8,1,0,0)
            LocalDateTime fechaDeEntradaEnReprogramacion = LocalDateTime.of(2020,8,20,0,1)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacionDeOrden, nota, procedimiento)
            unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
        when: 'pase 1 dia (21-8-2020)'
            LocalDateTime fecha1DiaPosteriores = LocalDateTime.of(2020,8,21,23,59)
            unaOrden.notificarPasoDelTiempo(fecha1DiaPosteriores)
        then: 'la orden continua en estado esperando reprogramacion'
            unaOrden.estadoDeLaOrden == new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
            unaOrden.estadoDeLaOrden != new EstadoOrdenCancelada()
    }

    
    /******************************************************************************************************************
     **************************************************Cita Medica ****************************************************
     ******************************************************************************************************************
     
     Escenario A: Creación de cita médica en una orden Registrada.
     
 
     Dado que el usuario utilizando la aplicación tiene el perfil de recepcionista,
     y la orden seleccionada está registrada
     Cuando el recepcionista
     asigne una cita para el estudio médico indicando la fecha, horario y sala en la cual se realizará el estudio médico
     Entonces
     se creará una nueva cita
     y la orden médica pasará a estar asignada.
 
     */
    
    
    /*
     METODO PUEDO AGREGAR CITA
     */
    
    void "test22 una orden en estado asignada, puedoAgregarCita debe dar false " (){
        given:'una orden de estudio en estado asignada'
        unaOrden.estadoDeLaOrden= new EstadoOrdenAsignada()
        when: 'pregunto si puedoAgregarCita'
        def puedoAgregarCita = unaOrden.puedoAgregarCita()
        then: "A la orden no se le puede agregar una cita"
        puedoAgregarCita == false
    }
    
    
    
    
    void "test23 una orden en estado finalizado, puedoAgregarCita debe dar false" (){
        given:'una orden de estudio en estado finalizado'
        unaOrden.estadoDeLaOrden= new EstadoOrdenFinalizado()
        when: 'pregunto si puedoAgregarCita'
        def puedoAgregarCita = unaOrden.puedoAgregarCita()
        then: "A la orden no se le puede agregar una cita"
        puedoAgregarCita == false
    }
    
    void "test24 una orden en estado espera informe, puedoAgregarCita debe dar false" (){
        given:'una orden de estudio en estado espera informe'
        unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaInforme()
        when: 'pregunto si puedoAgregarCita'
        def puedoAgregarCita = unaOrden.puedoAgregarCita()
        then: "A la orden no se le puede agregar una cita"
        puedoAgregarCita == false
    }
    
    void "test25 una orden en estado cancelada, puedoAgregarCita debe dar false" (){
        given:'una orden de estudio en estado cancelada'
        unaOrden.estadoDeLaOrden= new EstadoOrdenCancelada()
        when: 'pregunto si puedoAgregarCita'
        def puedoAgregarCita = unaOrden.puedoAgregarCita()
        then: "A la orden no se le puede agregar una cita"
        puedoAgregarCita == false
    }
    
    void "test26 una orden en estado espera estudio, puedoAgregarCita debe dar false" (){
        given:'una orden de estudio en estado espera estudio'
        unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaEstudio()
        when: 'pregunto si puedoAgregarCita'
        def puedoAgregarCita = unaOrden.puedoAgregarCita()
        then: "A la orden no se le puede agregar una cita"
        puedoAgregarCita == false
    }
    
    // estos casos son para agregar robustes al sistema, en caso de que el JOB que actualiza los estados
    // no corriera por alguna caida del sistema
    
    void "test27 una orden en estado registrada, puedoAgregarCita debe dar true cuando pasan 29 dias desde que se creo" (){
        given:'una orden registrada el 1-8-2020'
            LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,23,59)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        when: 'pasan 29 dias desde que fue creada la orden (30-8-2020) al preguntar si puedo agregar cita'
            LocalDateTime fecha29DiasDespues = LocalDateTime.of(2020,8,30,0,00)
            def puedoAgregarCita = unaOrden.puedoAgregarCita(fecha29DiasDespues)
        then: "A la orden se le puede agregar una cita"
            puedoAgregarCita == true
    }
    
    void "test28 una orden en estado registrada, puedoAgregarCita debe dar false cuando pasan 30 dias desde que se creo" (){
        given:'una orden registrada el 1-8-2020'
        
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,23,59)
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        when: 'pasan 30 dias desde que fue creada la orden (31-8-2020 a las 00:00 hs) al preguntar si puedo agregar cita'
        LocalDateTime fecha30DiasDespues = LocalDateTime.of(2020,8,31,0,0)
        def puedoAgregarCita = unaOrden.puedoAgregarCita(fecha30DiasDespues)
        then: "A la orden se le puede agregar una cita"
        puedoAgregarCita == false
    }
    
    void "test29 una orden en estado registrada, puedoAgregarCita debe dar false cuando pasan 31 dias desde que se creo" (){
        given:'una orden registrada el 1-8-2020'
            LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,0,0)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico, paciente, prioridad, fechaDeCreacion, nota, procedimiento)
        when: 'pasan 31 dias desde que fue creada la orden (1-p-2020) al preguntar si puedo agregar cita'
            LocalDateTime fecha31DiasDespues = LocalDateTime.of(2020,9,1,0,0)
            def puedoAgregarCita = unaOrden.puedoAgregarCita(fecha31DiasDespues)
        then: "A la orden se le puede agregar una cita"
            puedoAgregarCita == false
    }
    
   
    
    
    
    void "test30 una orden en estado espera repro, puedoAgregarCita debe dar true si no pasaron 2 dias" (){
        given:'una orden de estudio en estado espera reprogramacion el dia 20-8-2020'
            LocalDateTime fechaDeEntradaEnReprogramacion = LocalDateTime.of(2020,8,20,0,1)
            unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
        when: 'pasan 1 dia y 23 hs y 59 minutos, al preguntar si puedoAgregarCita'
            LocalDateTime fecha1Dia23HsY59MinPosteriores = LocalDateTime.of(2020,8,21,23,59)
            def puedoAgregarCita = unaOrden.puedoAgregarCita(fecha1Dia23HsY59MinPosteriores)
        then: "A la orden  se le puede agregar una cita"
            puedoAgregarCita == true
    }
    
    void "test31 una orden en estado espera repro, puedoAgregarCita debe dar false  si pasaron 2 dias" (){
        given:'una orden de estudio en estado espera reprogramacion el dia 20-8-2020'
            LocalDateTime fechaDeEntradaEnReprogramacion = LocalDateTime.of(2020,8,20,0,0)
            unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
        when: 'pasan 2 dias, al preguntar si puedoAgregarCita'
            LocalDateTime fecha2DiasPosteriores = LocalDateTime.of(2020,8,22,0,0)
            def puedoAgregarCita = unaOrden.puedoAgregarCita(fecha2DiasPosteriores)
        then: "A la orden  no se le puede agregar una cita"
            puedoAgregarCita == false
    }
    
    void "test32 una orden en estado espera repro, puedoAgregarCita debe dar false  si pasaron 3 dias" (){
        given:'una orden de estudio en estado espera reprogramacion el dia 20-8-2020'
            LocalDateTime fechaDeEntradaEnReprogramacion = LocalDateTime.of(2020,8,20,0,0)
            unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeEntradaEnReprogramacion)
        when: 'pasan 3 dias, al preguntar si puedoAgregarCita'
            LocalDateTime fecha3DiasPosteriores = LocalDateTime.of(2020,8,23,0,1)
            def puedoAgregarCita = unaOrden.puedoAgregarCita(fecha3DiasPosteriores)
        then: "A la orden  no se le puede agregar una cita"
        puedoAgregarCita == false
    }
    
    
   /*
     METODO  AGREGAR CITA
     */
    
    void "test33 una orden en estado registrada al agregar cita, queda en estado asignada" (){
        given:'una orden de estudio en estado registrada'
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
            unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        then: "La orden queda en estado asignada"
            unaOrden.estadoDeLaOrden == new EstadoOrdenAsignada()
            unaOrden.estadoDeLaOrden != new EstadoOrdenRegistrada()
    }
    
    void "test34 una orden en estado registrada al agregar cita, queda almacenada en la orden" (){
        given:'una orden de estudio en estado registrada'
        def unaCita
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
        unaCita=unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        then: "La cita queda almacenada en la orden"
        unaCita == unaOrden.citas.first()
    }
    
    void "test35 una orden en estado registrada el dia 1-8-2020  al intentar agregar cita el dia 31-8-2020, debe lanzar excepcion" (){
        given:'una orden de estudio en estado registrada el 1-8-2020'
            LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,2,54)
            LocalDateTime fecha30diasDespues= LocalDateTime.of(2020,8,31,0,0)
            List<Cita> listaDeCitas = new ArrayList<Cita>()
            SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas )
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(
                                                        medico,
                                                        paciente,
                                                        prioridad,
                                                        fechaDeCreacion,
                                                        nota,
                                                        procedimiento)
        when: 'intento agregar una cita el dia 31-8-2020'
            unaOrden.agregarCita (fecha30diasDespues, salaDeExamen,fechaDeCreacion)
        then: "La orden queda en estado cancelada y no se crea ninguna cita"
            Exception exception = thrown()
            exception.message == 'Error: No se puede agregar una cita: Orden registrada fuera de plazo'
    }
    
    void "test36 una orden en estado registrada el dia 1-8-2020  al intentar agregar cita el dia 1-9-2020, debe lanzar excepcion" (){
        given:'una orden de estudio en estado registrada el 1-8-2020'
            LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,1,2,54)
            LocalDateTime fecha31diasDespues= LocalDateTime.of(2020,9,1,0,0)
            List<Cita> listaDeCitas = new ArrayList<Cita>()
            SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas )
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(
                                                        medico,
                                                        paciente,
                                                        prioridad,
                                                        fechaDeCreacion,
                                                        nota,
                                                        procedimiento)
        when: 'intento agregar una cita el dia 1-9-2020 (31 dias despues)'
            unaOrden.agregarCita (fecha31diasDespues, salaDeExamen,fechaDeCreacion)
        then: "La orden queda en estado cancelada y no se crea ninguna cita"
            Exception exception = thrown()
            exception.message == 'Error: No se puede agregar una cita: Orden registrada fuera de plazo'
    }
    
    /*
    Escenario B: Creación de cita médica en una orden Asignada.

        Dado que el usuario utilizando la aplicación tiene el perfil de recepcionista,
        y la orden seleccionada está asignada,
        Cuando el recepcionista intente asignar una cita para el estudio médico indicando la fecha, horario y sala en la cual se realizará el estudio médico
        Entonces no se podrá crear la nueva cita hasta que cancele la anterior
     */
    
    void "test37 una orden en estado asignada al intentar agregar cita, debe lanzar excepcion" (){
        given:'una orden de estudio en estado asignada'
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
            unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        when: 'intento agregar otra cita'
            unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        then: "no se puede agregar y lanza excepcion"
        unaOrden.estadoDeLaOrden == new EstadoOrdenAsignada()
        Exception exception = thrown()
        exception.message == 'Error: No se puede agregar una cita en una orden asignada'
    }
    
    void "test38 una orden en estado finalizada al intentar agregar cita, debe lanzar excepcion" (){
        given:'una orden de estudio en estado finalizada'
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
        unaOrden.estadoDeLaOrden= new EstadoOrdenFinalizado()
        when: 'intento agregar una cita'
        unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        then: "no se puede agregar y lanza excepcion"
        unaOrden.estadoDeLaOrden == new EstadoOrdenFinalizado()
        Exception exception = thrown()
        exception.message == 'Error: No se puede agregar una cita en una orden finalizada'
    }
    
    void "test39 una orden en estado esperando informe al intentar agregar cita, debe lanzar excepcion" (){
        given:'una orden de estudio en estado esperando informe'
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
        unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaInforme()
        when: 'intento agregar una cita'
        unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        then: "no se puede agregar y lanza excepcion"
        unaOrden.estadoDeLaOrden == new EstadoOrdenEsperaInforme()
        Exception exception = thrown()
        exception.message == 'Error: No se puede agregar una cita en una orden que esta esperando informe'
    }
    
    void "test40 una orden en estado esperando estudio al intentar agregar cita, debe lanzar excepcion" (){
        given:'una orden de estudio en estado esperando estudio'
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
        unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaEstudio()
        when: 'intento agregar una cita'
        unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        then: "no se puede agregar y lanza excepcion"
        unaOrden.estadoDeLaOrden == new EstadoOrdenEsperaEstudio()
        Exception exception = thrown()
        exception.message == 'Error: No se puede agregar una cita en una orden que esta esperando estudio'
    }
    
    void "test41 una orden en estado cancelada al intentar agregar cita, debe lanzar excepcion" (){
        given:'una orden de estudio en estado cancelada'
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
        unaOrden.estadoDeLaOrden= new EstadoOrdenCancelada()
        when: 'intento agregar una cita'
        unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        then: "no se puede agregar y lanza excepcion"
        unaOrden.estadoDeLaOrden == new EstadoOrdenCancelada()
        Exception exception = thrown()
        exception.message == 'Error: No se puede agregar una cita en una orden cancelada'
    }
    
    /* Reprogramacion de citas
    
        Escenario A: Reprogramación de cita médica en una orden esperando reprogramación
        Dado que un paciente desea reprogramar una cita médica
        y la orden médica se encuentra esperando reprogramación
        y no transcurrieron más de 2 dias  desde que se reprogramo
        Cuando  le asigne una nueva cita disponible, indicando la fecha, horario y sala en la cual se realizará el estudio médico
        Entonces la orden quedará en estado asignado, con la nueva cita médica creada, manteniendo las antiguas citas.
     
     */
    
    void "test42 una orden en estado esperando reprogramacion al intentar agregar cita, la orden queda en estado asignada" (){
        given:'una orden de estudio en estado esperando reprogramacion'
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
        unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeCreacion)
        when: 'intento agregar una cita'
        unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        then: "la orden queda en estado asignada"
        unaOrden.estadoDeLaOrden == new EstadoOrdenAsignada()
    }
    
    void "test43 una orden en estado esperando reprogramacion al intentar agregar cita, la nueva cita queda almacenada" (){
        given:'una orden de estudio en estado esperando reprogramacion'
        def miCita
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
        unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeCreacion)
        when: 'intento agregar una cita'
        miCita = unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        then: "la nueva cita queda almacenada"
        miCita ==  unaOrden.citas.last()
        unaOrden.estadoDeLaOrden == new EstadoOrdenAsignada()
    }
    
    void "test44 una orden en estado esperando reprogramacion al intentar agregar cita, mantiene almacenadas las antiguas citas" (){
        given:'una orden de estudio en estado esperando reprogramacion'
        def miCitaUno, miCitaDos
        LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,9,2,54)
        LocalDateTime fechaDeCreacion2 = LocalDateTime.of(2020,8,10,2,54)
        List<Cita> listaDeCitas = new ArrayList<Cita>()
        SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas )
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(
                medico,
                paciente,
                prioridad,
                fechaDeCreacion,
                nota,
                procedimiento)
        miCitaUno = unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion)
        unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeCreacion)
        when: 'intento agregar otra cita'
        miCitaDos = unaOrden.agregarCita (fechaDeCreacion, salaDeExamen,fechaDeCreacion2)
        then: "la nueva cita queda almacenada"
        miCitaUno ==  unaOrden.citas.first()
        miCitaDos ==  unaOrden.citas.last()
        unaOrden.estadoDeLaOrden == new EstadoOrdenAsignada()
    }
    
    void "test45 una orden en estado esperando reprogramacion, 2 dias despues al intentar agregar cita debe lanzar excepcion" (){
        given:'una orden de estudio en estado esperando reprogramacion'
            LocalDateTime fechaDeCreacion = LocalDateTime.of(2020,8,9,2,54)
            LocalDateTime fechaDeReprogramacion = LocalDateTime.of(2020,8,10,2,54)
            List<Cita> listaDeCitas = new ArrayList<Cita>()
            SalaDeExamen salaDeExamen = new SalaDeExamen(listaDeCitas )
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(
                                                        medico,
                                                        paciente,
                                                        prioridad,
                                                        fechaDeCreacion,
                                                        nota,
                                                        procedimiento)
            unaOrden.estadoDeLaOrden= new EstadoOrdenEsperaRepro(fechaDeReprogramacion)
        when: 'pasan 2 dias e intento agregar otra cita'
            LocalDateTime fecha2DiasDespues = LocalDateTime.of(2020,8,12,2,54)
            LocalDateTime fechaCita = LocalDateTime.of(2020,8,15,2,54)
            unaOrden.agregarCita(fecha2DiasDespues,salaDeExamen,fechaCita)
        then: "la orden debe lanzar excepcion"
            Exception exception = thrown()
            exception.message == 'Error: No se puede agregar una cita: Orden fuera de plazo para reprogramar'
        }
    
  /*
            Metodo notificarCitaCancelada
   */
    
    void "test46 notificarCitaCancelada en una orden en estado registrada, debe lanzar excepcion" (){
        given:'una orden de estudio en estado registrada'
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaDeCreacion,nota,procedimiento)
        when: 'se llama a notificarCitaCancelada'
            unaOrden.notificarCitaCancelada(fechaDeCreacion)
        then: "la orden debe lanzar excepcion"
            Exception exception = thrown()
            exception.message == 'Error: Orden Registrada no tiene citas'
     }
    
    void "test47 notificarCitaCancelada en una orden en estado esperando Reprogramacion, debe lanzar excepcion" (){
        given:'una orden de estudio en estado esperando reprogramacion'
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaDeCreacion,nota,procedimiento)
        unaOrden.estadoDeLaOrden = new EstadoOrdenEsperaRepro(fechaDeCreacion)
        when: 'se llama a notificarCitaCancelada'
        unaOrden.notificarCitaCancelada(fechaDeCreacion)
        then: "la orden debe lanzar excepcion"
        Exception exception = thrown()
        exception.message == 'Error: Orden esperando reprogramacion'}
 
    void "test48 notificarCitaCancelada en una orden en estado esperando informe, debe lanzar excepcion" (){
        given:'una orden de estudio en estado esperando informe'
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaDeCreacion,nota,procedimiento)
        unaOrden.estadoDeLaOrden= new  EstadoOrdenEsperaInforme()
        when: 'se llama a notificarCitaCancelada'
        unaOrden.notificarCitaCancelada(fechaDeCreacion)
        then: "la orden debe lanzar excepcion"
        Exception exception = thrown()
        exception.message == 'Error: Orden en espera de informe'}
    
    void "test49 notificarCitaCancelada en una orden en estado esperando estudio, debe lanzar excepcion" (){
        given:'una orden de estudio en estado esperando estudio'
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaDeCreacion,nota,procedimiento)
        unaOrden.estadoDeLaOrden = new  EstadoOrdenEsperaEstudio()
        when: 'se llama a notificarCitaCancelada'
        unaOrden.notificarCitaCancelada(fechaDeCreacion)
        then: "la orden debe lanzar excepcion"
        Exception exception = thrown()
        exception.message == 'Error: Orden en espera de estudio'}
    
    void "test50 notificarCitaCancelada en una orden en estado cancelada, debe lanzar excepcion" (){
        given:'una orden de estudio en estado cancelada'
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaDeCreacion,nota,procedimiento)
        unaOrden.estadoDeLaOrden = new  EstadoOrdenCancelada()
        when: 'se llama a notificarCitaCancelada'
        unaOrden.notificarCitaCancelada(fechaDeCreacion)
        then: "la orden debe lanzar excepcion"
        Exception exception = thrown()
        exception.message == 'Error: Orden en estado cancelada'}
    
    void "test51 notificarCitaCancelada en una orden en estado finalizada, debe lanzar excepcion" (){
        given:'una orden de estudio en estado finalizada'
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaDeCreacion,nota,procedimiento)
        unaOrden.estadoDeLaOrden = new  EstadoOrdenFinalizado()
        when: 'se llama a notificarCitaCancelada'
        unaOrden.notificarCitaCancelada(fechaDeCreacion)
        then: "la orden debe lanzar excepcion"
        Exception exception = thrown()
        exception.message == 'Error: Orden en estado Finalizada'}
    
    
    
    void "test52 notificarCitaCancelada en una orden en estado asignada, deja la orden en estado esperando reprogramacion" (){
        given:'una orden de estudio en estado asignada'
        OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaDeCreacion,nota,procedimiento)
        unaOrden.estadoDeLaOrden = new  EstadoOrdenAsignada()
        when: 'se llama a notificarCitaCancelada'
        unaOrden.notificarCitaCancelada(fechaDeCreacion)
        then: "la orden queda en estado esperando reprogramacion"
        unaOrden.estadoDeLaOrden == new EstadoOrdenEsperaRepro(fechaDeCreacion)
    }
    
    void "test53 notificarCitaCancelada con fecha 1-8-2020, en una orden en estado asignada  deja la orden en estado esperando reprogramacion 1-8-2020" (){
        given:'una orden de estudio en estado asignada'
            LocalDateTime fechaCreacion = LocalDateTime.of(2020,8,1,0,0)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaCreacion,nota,procedimiento)
            unaOrden.estadoDeLaOrden = new  EstadoOrdenAsignada()
        when: 'se llama a notificarCitaCancelada'
            LocalDateTime fechaCancelacion = LocalDateTime.of(2020,8,1,0,0)
            unaOrden.notificarCitaCancelada(fechaCancelacion)
        then: "la orden queda en estado esperando reprogramacion con fecha 1-8-2020"
            unaOrden.estadoDeLaOrden == new EstadoOrdenEsperaRepro(fechaCancelacion)
    }
    
    void "test54 notificarCitaCancelada con fecha 1-8-2020 en una orden en estado asignada creada el 30-7-2020, deja la orden en estado esperando reprogramacion 1-8-2020" (){
        given:'una orden de estudio creada el 30-7-2020 en estado asignada '
            LocalDateTime fechaCreacion = LocalDateTime.of(2020,7,30,0,0)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaCreacion,nota,procedimiento)
            unaOrden.estadoDeLaOrden = new  EstadoOrdenAsignada()
        when: 'se llama a notificarCitaCancelada el dia 1-8-2020'
            LocalDateTime fechaCancelacion = LocalDateTime.of(2020,8,1,0,0)
            unaOrden.notificarCitaCancelada(fechaCancelacion)
        then: "la orden queda en estado esperando reprogramacion con fecha 1-8-2020"
            unaOrden.estadoDeLaOrden == new EstadoOrdenEsperaRepro(fechaCancelacion)
    }
    
    void "test55 notificarCitaCancelada con fecha 1-8-2020 en una orden en estado asignada creada el 2-8-2020, debe lanzar excepcion" (){
        given:'una orden de estudio creada el 2-8-2020 en estado asignada '
            LocalDateTime fechaCreacion = LocalDateTime.of(2020,8,2,0,0)
            OrdenDeEstudio unaOrden= new OrdenDeEstudio(medico,paciente,prioridad,fechaCreacion,nota,procedimiento)
            unaOrden.estadoDeLaOrden = new  EstadoOrdenAsignada()
        when: 'se llama a notificarCitaCancelada el dia 1-8-2020'
            LocalDateTime fechaCancelacion = LocalDateTime.of(2020,8,1,0,0)
            unaOrden.notificarCitaCancelada(fechaCancelacion)
        then: "la orden debe lanzar excepcion"
            Exception exception = thrown()
            exception.message == 'Error: La fecha de notificacion no puede ser anterior a la de creacion de la orden'
    }
    
    
    
    /* ************************************************************************************
            Estos son test de integracion que hay que poner en otro lado
     *******************************************************************************/
    
    /*
       Escenario B: Reprogramación de cita en una orden asignada con cita médica no vencida

        Dado que un paciente desea reprogramar una cita médica antes del horario de realización de la misma
        y la orden médica se encuentra asignada
        Cuando  el recepcionista cancele la cita
        y en ese momento o hasta 2 días después le asigne una nueva cita disponible,
        indicando la fecha, horario y sala en la cual se realizará el estudio médico
        Entonces la orden seguirá en estado asignado, con la nueva cita médica en estado registrada y la antigua cancelada
        
        
     
    
    
   
    void "t*****est54 una orden en estado asignada, al cancelar el 3-8-2020 con cita no vencida el 10-8-2020 queda con fecha de espera repro 3-8-2020" (){
        expect:"fix me"
        true == false
    }
    void "test55 una orden en estado esprando repro con cita cancelada el 3-8-2020 10am y que vence el 4-8-2020 10 am  queda asignada el 5-8-2020 9 am" (){
        expect:"fix me"
        true == false
    }
    void "test56 una orden en estado esprando repro con cita cancelada el 3-8-2020 10am y que vence el 4-8-2020 10 am el 5-8-2020 10 am debe lanzar exepcion si se intenta agregar cita" (){
        expect:"fix me"
        true == false
        //Exception exception = thrown()
        //exception.message == 'Error: No se puede agregar una cita: Orden registrada fuera de plazo'
    }
   
  */
    
    
    /*
        Escenario C: Reprogramación de cita en una orden asignada con cita médica vencida
        Dado que un paciente desea reprogramar una cita médica después del horario de realización de la misma
        y la orden médica se encuentra asignada
        y no transcurrieron mas de 2 dias desde que venció la cita
        Cuando se asigne una nueva cita disponible, indicando la fecha, horario y sala en la cual se realizará el estudio médico
        Entonces la orden pasará a estar asignada nuevamente, con la nueva cita médica creada.
     */
    /*
    void "test57 una orden en estado asignada, al cancelar el 3-8-2020 con cita vencida el 2-8-2020 queda con fecha de espera repro 2-8-2020" (){
        expect:"fix me"
        true == false
    }
    
    void "test58 una orden en estado esprando repro con cita cancelada el 3-8-2020 y vencida el 2-8-2020 10 am  queda asignada el 4-8-2020 9 am" (){
        expect:"fix me"
        true == false
    }
    void "test59 una orden en estado esprando repro con cita cancelada el 3-8-2020 y vencida el 2-8-2020 10 am  el 4-8-2020 11 am debe lanzar exepcion si se intenta agregar cita" (){
     //   Exception exception = thrown()
      //  exception.message == 'Error: No se puede agregar una cita: Orden registrada fuera de plazo'
    }
    
  
   */
    
}
