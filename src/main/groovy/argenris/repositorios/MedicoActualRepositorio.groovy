package argenris.repositorios

import argenris.Medico
import org.grails.web.util.WebUtils
import org.springframework.stereotype.Component

//@Component  //quiero crear un bean de esta clase y que spring maneje el siclo de vida
class MedicoActualRepositorio {

	//todo implementar
	Medico buscar(){
		
		// esto busca el
			WebUtils.retrieveGrailsWebRequest().getCurrentRequest().getSession() //.id usuario logueado
	
	}
	
	// Spring security
}
