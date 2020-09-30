package argenris


import grails.rest.*
import grails.converters.*

class SalaDeExamenController {
	static responseFormats = ['json', 'xml']
	
	def index(Integer max) {
		params.max = Math.min(max ?: 2, 100)
		//  respond pacienteService.list(params), model:[pacienteCount: pacienteService.count()]
		respond SalaDeExamen.list(params), model:[pacienteCount: SalaDeExamen.count()]
	}
	
	def show(Long id) {
		
		if ( SalaDeExamen.get(id).citas.empty){render "vaciolista"}else {render "contienelista"}
	}
	
	
	def obtenerCitasDelDia(Long id) {
		
		if ( SalaDeExamen.get(id).obtenerCitasDelDia().empty){render "vacio"}else {render "contiene"}
		
		//respond SalaDeExamen.get(id).obtenerCitasDelDia()
	}
	
}
