package argenris.OrdenDeEstudio.EstadoOrden

import argenris.Cita.Cita

import java.time.LocalDateTime

//todo esto hay que moverlo a src/main/groovy/....
	abstract class EstadoDeLaOrden {
	
		abstract EstadoDeLaOrden cancelar(Set<Cita> citas)
		abstract EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual)
	}


	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenAsignada extends EstadoDeLaOrden {
		
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas) {
			//todo alternativa al try es preguntar si se le puede cancelar
			try {
				citas.each {it.cancelar()}
			} catch (Exception ex) {
			
			}
	
			return new EstadoOrdenCancelada()
			
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return this
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenRegistrada extends EstadoDeLaOrden{
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas) {new EstadoOrdenCancelada()}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			if ( !(fechaYHoraActual.toLocalDate()).isBefore((fechaOrden.toLocalDate()).plusDays(30))) { return new EstadoOrdenCancelada()}
			 new EstadoOrdenRegistrada()
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenCancelada extends EstadoDeLaOrden{
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas) {
			throw new Exception("Error: No se puede cancelar una Orden en estado Cancelada")
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return this
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenEsperaRepro extends EstadoDeLaOrden{
		
		LocalDateTime fechaEntradaEnReprogramacion
		
		EstadoOrdenEsperaRepro (LocalDateTime fechaEsperaRepro){
			this.fechaEntradaEnReprogramacion = fechaEsperaRepro
		}
		
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas) {new EstadoOrdenCancelada()}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			if   (!fechaYHoraActual.isBefore(this.fechaEntradaEnReprogramacion.plusHours(48))) { return new EstadoOrdenCancelada()}
			return this
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenFinalizado extends EstadoDeLaOrden{
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas) {
			throw new Exception("Error: No se puede cancelar una Orden en estado Finalizado")
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return this
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenEsperaInforme extends EstadoDeLaOrden{
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas) {
			throw new Exception("Error: No se puede cancelar una Orden en estado Espera Informe")
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return this
		}
	}

	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenEsperaEstudio extends EstadoDeLaOrden{
		@Override
		EstadoDeLaOrden cancelar(Set<Cita> citas) {
			throw new Exception("Error: No se puede cancelar una Orden en estado Espera Estudio")
		}
		
		@Override
		EstadoDeLaOrden notificarPasoDelTiempo(LocalDateTime fechaOrden, LocalDateTime fechaYHoraActual) {
			return this
		}
	}