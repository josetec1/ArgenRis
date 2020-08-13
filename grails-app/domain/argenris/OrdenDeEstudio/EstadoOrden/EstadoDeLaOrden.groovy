package argenris.OrdenDeEstudio.EstadoOrden

//todo esto hay que moverlo a src/main/groovy/....
	abstract class EstadoDeLaOrden {
	
	}


	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenAsignada extends EstadoDeLaOrden {
	}
	@groovy.transform.EqualsAndHashCode
	class EstadoOrdenRegistrada extends EstadoDeLaOrden{
	
	}