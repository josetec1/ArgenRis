package argenris.OrdenDeEstudio.EstadoOrden

import argenris.AreaDeExamen
import argenris.Cita.Cita
import argenris.OrdenDeEstudio.OrdenDeEstudio
import argenris.Prioridad
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

//todo esto hay que moverlo a src/main/groovy/....
	
	abstract class EstadoDeLaOrden {
	
		abstract EstadoDeLaOrden cancelar(Set<Cita> citas, LocalDateTime fechaActualDeCancelacion)
		abstract EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual)
		
		abstract  boolean puedoAgregarcita(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual)
		
		
		abstract EstadoDeLaOrden agregarCita(AreaDeExamen salaDeExamen, LocalDateTime fechaDeCita,LocalDateTime fechayHoraActual,LocalDateTime fechaOrden, Set<Cita> citas, Prioridad prioridad,OrdenDeEstudio unaOrden)
		
		abstract  EstadoDeLaOrden notificarCitaCancelada(LocalDateTime fechaOrden, LocalDateTime fechaNotificacion)
	}

	
	@EqualsAndHashCode
	class EstadoOrdenAsignada extends EstadoDeLaOrden {
		
		//Todo probar que la ejecucion continue si corta.
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas, LocalDateTime fechaActualDeCancelacion) {
			//todo alternativa al try es preguntar si se le puede cancelar
			citas.each {
				try {it.cancelar(fechaActualDeCancelacion)}
				catch (ex){}
				}
			return new EstadoOrdenCancelada()
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return this
		}
		
		@Override
		boolean puedoAgregarcita(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return false
		}
		
		@Override
		EstadoDeLaOrden agregarCita(AreaDeExamen salaDeExamen, LocalDateTime fechaDeCita,LocalDateTime fechayHoraActual,LocalDateTime fechaOrden, Set<Cita> citas, Prioridad prioridad, OrdenDeEstudio unaOrden) {
			throw new Exception("Error: No se puede agregar una cita en una orden asignada")
		}
		
		@Override
		EstadoDeLaOrden notificarCitaCancelada(LocalDateTime fechaOrden, LocalDateTime fechaNotificacion) {
				if (  fechaNotificacion.toLocalDate()< fechaOrden.toLocalDate() ) {throw new Exception("Error: La fecha de notificacion no puede ser anterior a la de creacion de la orden")}
				return new EstadoOrdenEsperaRepro(fechaNotificacion)
				
			}
		}
	
	
	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenRegistrada extends EstadoDeLaOrden{
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas, LocalDateTime fechaActualDeCancelacion) {
			new EstadoOrdenCancelada()
			
		}
		
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			if (!puedoAgregarcita(fechaOrden,fechaYHoraActual) ) { return new EstadoOrdenCancelada()}
			 return this
		}
		
		@Override
		boolean puedoAgregarcita(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			(fechaYHoraActual.toLocalDate()).isBefore((fechaOrden.toLocalDate()).plusDays(30))
		}
		
		@Override
		EstadoDeLaOrden agregarCita(AreaDeExamen salaDeExamen, LocalDateTime fechaDeCita,LocalDateTime fechayHoraActual,LocalDateTime fechaOrden, Set<Cita> citas, Prioridad prioridad, OrdenDeEstudio unaOrden) {
			//este es el caso de una orden que no actualizo el cron job
			if (!puedoAgregarcita(fechaOrden,fechayHoraActual) ) { throw new Exception("Error: No se puede agregar una cita: Orden registrada fuera de plazo")}
			
			Cita cita = salaDeExamen.crearCita(fechaDeCita, prioridad.toString(),unaOrden)
			citas.add(cita)
			new EstadoOrdenAsignada()
			
		}
		
		@Override
		EstadoDeLaOrden notificarCitaCancelada(LocalDateTime fechaOrden, LocalDateTime fechaNotificacion) {
			throw new Exception("Error: Orden Registrada no tiene citas")
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenCancelada extends EstadoDeLaOrden{
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas, LocalDateTime fechaActualDeCancelacion) {
			throw new Exception("Error: No se puede cancelar una Orden en estado Cancelada")
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return this
		}
		
		@Override
		boolean puedoAgregarcita(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return false
		}
		
		@Override
		EstadoDeLaOrden agregarCita(AreaDeExamen salaDeExamen, LocalDateTime fechaDeCita,LocalDateTime fechayHoraActual,LocalDateTime fechaOrden, Set<Cita> citas, Prioridad prioridad, OrdenDeEstudio unaOrden) {
			throw new Exception("Error: No se puede agregar una cita en una orden cancelada")
		}
		
		@Override
		EstadoDeLaOrden notificarCitaCancelada(LocalDateTime fechaOrden, LocalDateTime fechaNotificacion) {
			throw new Exception("Error: Orden en estado cancelada")
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenEsperaRepro extends EstadoDeLaOrden{
		
		LocalDateTime fechaEntradaEnReprogramacion
		
		EstadoOrdenEsperaRepro (LocalDateTime fechaEsperaRepro){
			this.fechaEntradaEnReprogramacion = fechaEsperaRepro
		}
		
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas, LocalDateTime fechaActualDeCancelacion) {new EstadoOrdenCancelada()}
		
		
		boolean sePuedeReprogramar (LocalDateTime fechaYHoraActual){
			(fechaYHoraActual.toLocalDate()).isBefore((this.fechaEntradaEnReprogramacion.toLocalDate()).plusDays(2))
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			if   (!sePuedeReprogramar(fechaYHoraActual)) { return new EstadoOrdenCancelada()}
			return this
		}
		
		@Override
		boolean puedoAgregarcita(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return sePuedeReprogramar(fechaYHoraActual)
		}
		
		//todo refactorizar por que queda codigo repetido... habria que moverlo a la orden
		//y usar un case para saber que estado tenia y poder informar la exepcion correspondiente
		
		@Override
		EstadoDeLaOrden agregarCita(AreaDeExamen salaDeExamen, LocalDateTime fechaDeCita,LocalDateTime fechayHoraActual,LocalDateTime fechaOrden, Set<Cita> citas, Prioridad prioridad, OrdenDeEstudio unaOrden) {
			//este es el caso de una orden que no actualizo el cron job
			if (!puedoAgregarcita(fechaOrden,fechayHoraActual) ) { throw new Exception("Error: No se puede agregar una cita: Orden fuera de plazo para reprogramar")}
			
			Cita cita = salaDeExamen.crearCita(fechaDeCita, prioridad.toString(),unaOrden)
			citas.add(cita)
			new EstadoOrdenAsignada()
		}
		
		@Override
		EstadoDeLaOrden notificarCitaCancelada(LocalDateTime fechaOrden, LocalDateTime fechaNotificacion) {
			throw new Exception("Error: Orden esperando reprogramacion")
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenFinalizado extends EstadoDeLaOrden{
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas, LocalDateTime fechaActualDeCancelacion) {
			throw new Exception("Error: No se puede cancelar una Orden en estado Finalizado")
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return this
		}
		
		@Override
		boolean puedoAgregarcita(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return false
		}
		
		@Override
		EstadoDeLaOrden agregarCita(AreaDeExamen salaDeExamen, LocalDateTime fechaDeCita,LocalDateTime fechayHoraActual,LocalDateTime fechaOrden, Set<Cita> citas, Prioridad prioridad, OrdenDeEstudio unaOrden) {
			throw new Exception("Error: No se puede agregar una cita en una orden finalizada")
			
		}
		
		@Override
		EstadoDeLaOrden notificarCitaCancelada(LocalDateTime fechaOrden, LocalDateTime fechaNotificacion) {
			throw new Exception("Error: Orden en estado Finalizada")
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenEsperaInforme extends EstadoDeLaOrden{
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas, LocalDateTime fechaActualDeCancelacion) {
			throw new Exception("Error: No se puede cancelar una Orden en estado Espera Informe")
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return this
		}
		
		@Override
		boolean puedoAgregarcita(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return false
		}
		
		@Override
		EstadoDeLaOrden agregarCita(AreaDeExamen salaDeExamen, LocalDateTime fechaDeCita,LocalDateTime fechayHoraActual,LocalDateTime fechaOrden, Set<Cita> citas, Prioridad prioridad, OrdenDeEstudio unaOrden) {
			throw new Exception("Error: No se puede agregar una cita en una orden que esta esperando informe")
		}
		
		@Override
		EstadoDeLaOrden notificarCitaCancelada(LocalDateTime fechaOrden, LocalDateTime fechaNotificacion) {
			throw new Exception("Error: Orden en espera de informe")
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenEsperaEstudio extends EstadoDeLaOrden{
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas, LocalDateTime fechaActualDeCancelacion) {
			throw new Exception("Error: No se puede cancelar una Orden en estado Espera Estudio")
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return this
		}
		
		@Override
		boolean puedoAgregarcita(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return false
		}
		
		@Override
		EstadoDeLaOrden agregarCita(AreaDeExamen salaDeExamen, LocalDateTime fechaDeCita,LocalDateTime fechayHoraActual,LocalDateTime fechaOrden, Set<Cita> citas, Prioridad prioridad, OrdenDeEstudio unaOrden) {
			throw new Exception("Error: No se puede agregar una cita en una orden que esta esperando estudio")
		}
		
		@Override
		EstadoDeLaOrden notificarCitaCancelada(LocalDateTime fechaOrden, LocalDateTime fechaNotificacion) {
			throw new Exception("Error: Orden en espera de estudio")
		}
	}
