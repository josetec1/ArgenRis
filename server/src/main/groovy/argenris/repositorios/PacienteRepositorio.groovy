package argenris.repositorios

import argenris.Paciente
import org.springframework.stereotype.Component


//@Component  //quiero crear un bean de esta clase y que spring maneje el siclo de vida

class PacienteRepositorio {
	
	Optional<Paciente> getById(Long id) {
		Optional.ofNullable(Paciente.get(id)) //este opcional puede inicializarse con null si no hay paciente, tiene maps filter...
	}
	
	
	
}



