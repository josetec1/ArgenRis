import argenris.repositorios.MedicoActualRepositorio
import argenris.repositorios.PacienteRepositorio
import argenris.repositorios.ProcedimientoRepositorio

// Place your Spring DSL code here


beans = {
	
	pacienteRepositorio (PacienteRepositorio)
	medicoActualRepositorio (MedicoActualRepositorio)
	procedimientoRepositorio (ProcedimientoRepositorio)

}
