package argenris.repositorios


import argenris.Procedimiento

class ProcedimientoRepositorio {
	
	
	Optional<Procedimiento> getById(Long id) {
		Optional.ofNullable(Procedimiento.get(id))
	}
	
}
