package argenris.repositorios

import argenris.Paciente
import org.springframework.stereotype.Component


//@Component  //quiero crear un bean de esta clase y que spring maneje el siclo de vida
class PacienteRepositorio {
	
	Paciente getById (Long id) {
		Paciente paciente = Paciente.get(id)
		if (!paciente) throw new IllegalStateException ("no hay paciente con id=${id}")
		paciente
	}
	
	
	//todo hay que ver como usar este
	Optional<Paciente> getByIdOptional (long id) {
		Optional.ofNullable(Paciente.get(id))
	}
	
	
}



