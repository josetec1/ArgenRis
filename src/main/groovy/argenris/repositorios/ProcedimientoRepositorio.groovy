package argenris.repositorios


import argenris.Procedimiento

class ProcedimientoRepositorio {
	
	
	Procedimiento getById (Long id){
		
		Procedimiento procedimiento = Procedimiento.get(id)
		if (!procedimiento) throw new IllegalStateException ("no hay procedimiento con id=${id}")
		procedimiento
		
	}
	
}
