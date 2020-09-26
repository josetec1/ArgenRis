package argenris.OrdenDeEstudio

import argenris.AreaDeExamen
import argenris.Cita.Cita
import argenris.OrdenDeEstudio.EstadoOrden.EstadoDeLaOrden
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenAsignada
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenCancelada
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenEsperaEstudio
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenEsperaInforme
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenEsperaRepro
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenFinalizado
import argenris.OrdenDeEstudio.EstadoOrden.EstadoOrdenRegistrada
import argenris.Medico
import argenris.Paciente
import argenris.Prioridad
import argenris.Procedimiento

import java.time.LocalDateTime

class OrdenDeEstudio {
    
   
    
    Medico medico
    Paciente paciente
    Prioridad prioridad
    LocalDateTime dateCreated  //fecha real en que se ingresa en el sistema la orden
    LocalDateTime fechaCreacionDeOrden       // fecha de creacion que ingresa el medico
    String notaAdicional
    Set<Cita> citas =[]
    EstadoDeLaOrden estadoDeLaOrden
    Procedimiento procedimiento
    
    
    
    static hasMany = [
            citas: Cita
    ]
    
    static constraints = {
        medico nullable: false
        paciente nullable: false
        prioridad nullable: false
        fechaCreacionDeOrden nullable: false
        notaAdicional nullable: true, blank: true
        procedimiento nullable: false
        
    }
    
    
    
    OrdenDeEstudio (Medico medico,
                    Paciente paciente,
                    Prioridad prioridad,
                    LocalDateTime fechaDeCreacion,
                    String nota,
                    Procedimiento procedimiento){
        
        this.medico = medico
        this.paciente = paciente
        this.prioridad = prioridad
        this.fechaCreacionDeOrden = fechaDeCreacion
        this.notaAdicional = nota
        this.procedimiento = procedimiento
        
    
        this.estadoDeLaOrden =  new EstadoOrdenRegistrada()
        this.paciente.agregarOrden(this)
        
        
    }
    
    boolean puedoAgregarCita (LocalDateTime fechayHoraActual){
        this.estadoDeLaOrden.puedoAgregarcita(this.fechaCreacionDeOrden, fechayHoraActual)
    }
    
  
    Cita agregarCita (LocalDateTime fechayHoraActual ,AreaDeExamen salaDeExamen, LocalDateTime fechaDeCita){
        
        if (fechaDeCita <this.fechaCreacionDeOrden  ) {throw new Exception ('Error: No se puede planificar una Cita con fecha de cita anterior a la orden')}
        
        //todo 3 problemas
        // 1 esto puede ser inconsistente si el cron job no corrio y la orden por ende en un estado desactualizado.
        // no te dejo avanzar, pero sigue quedando sin actualizar el estado de la orden
        // 2 estoy pasando 6 parametros para poder lanzar la exepcion especifica de por que falla
        // si ponie el if aca ahorraba pasar parametros lanzando una exepcion generica
        //todo 3 ademas de enviar varios parametros de la orden , tambien estoy pasando la orden
     
        
        this.estadoDeLaOrden = this.estadoDeLaOrden.agregarCita(salaDeExamen,fechaDeCita,fechayHoraActual,this.fechaCreacionDeOrden,this.citas,this.prioridad,this)
  
        return this.citas.last()
        
    }
	
        //despues de 30 dias queda cancelada
	 void notificarPasoDelTiempo(LocalDateTime fechayHoraActual) {
       this.estadoDeLaOrden= this.estadoDeLaOrden.notificarPasoDelTiempo(this.fechaCreacionDeOrden, fechayHoraActual)
       
    }
    
     void cancelar (LocalDateTime fechaActualDeCancelacion){
       
       this.estadoDeLaOrden =  this.estadoDeLaOrden.cancelar(this.citas, fechaActualDeCancelacion)
     }
    //todo refactor  mover a estados
     void notificarCitaCancelada (LocalDateTime fechaNotificacion) {
         
         if (this.estadoDeLaOrden == new EstadoOrdenAsignada()){
            if (  fechaNotificacion.toLocalDate()< this.fechaCreacionDeOrden.toLocalDate() ) {throw new Exception("Error: La fecha de notificacion no puede ser anterior a la de creacion de la orden")}
            this.estadoDeLaOrden = new EstadoOrdenEsperaRepro(fechaNotificacion)
             return
         }
         
         if (this.estadoDeLaOrden == new EstadoOrdenRegistrada()){throw new Exception("Error: Orden Registrada no tiene citas")}
         if (this.estadoDeLaOrden == new EstadoOrdenEsperaRepro(fechaNotificacion)){throw new Exception("Error: Orden esperando reprogramacion")}
         if (this.estadoDeLaOrden == new EstadoOrdenEsperaEstudio()){throw new Exception("Error: Orden en espera de estudio")}
         if (this.estadoDeLaOrden == new EstadoOrdenEsperaInforme()){throw new Exception("Error: Orden en espera de informe")}
         if (this.estadoDeLaOrden == new EstadoOrdenCancelada()){throw new Exception("Error: Orden en estado cancelada")}
         if (this.estadoDeLaOrden == new EstadoOrdenFinalizado()){throw new Exception("Error: Orden en estado Finalizada")}
     
     }
    
    
    //todo refactor  mover a estados
    void notificarCitaConcretada (LocalDateTime fechaNotificacion) {
       
        if (this.estadoDeLaOrden == new EstadoOrdenAsignada()){
            if (  fechaNotificacion.toLocalDate()< this.fechaCreacionDeOrden.toLocalDate() ) {throw new Exception("Error: La fecha de notificacion no puede ser anterior a la de creacion de la orden")}
          this.estadoDeLaOrden = new EstadoOrdenEsperaEstudio()
          return
        }
        
        if (this.estadoDeLaOrden == new EstadoOrdenRegistrada()){throw new Exception("Error: Orden Registrada no tiene citas")}
        if (this.estadoDeLaOrden == new EstadoOrdenEsperaRepro(fechaNotificacion)){throw new Exception("Error: Orden esperando reprogramacion")}
        if (this.estadoDeLaOrden == new EstadoOrdenEsperaEstudio()){throw new Exception("Error: Orden en espera de estudio")}
        if (this.estadoDeLaOrden == new EstadoOrdenEsperaInforme()){throw new Exception("Error: Orden en espera de informe")}
        if (this.estadoDeLaOrden == new EstadoOrdenCancelada()){throw new Exception("Error: Orden en estado cancelada")}
        if (this.estadoDeLaOrden == new EstadoOrdenFinalizado()){throw new Exception("Error: Orden en estado Finalizada")}
      
    }
}
