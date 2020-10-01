package argenris

import argenris.Cita.Cita
import grails.rest.*
import grails.converters.*
import org.springframework.web.client.HttpClientErrorException

import java.time.LocalDateTime
import java.time.ZoneId

import static org.springframework.http.HttpStatus.BAD_REQUEST

class SalaCommand{
	
	Date fechaYHoraActual
	
	static constraints = {
		fechaYHoraActual nullable: false, blank: false, date: true
	}
}



class SalaDeExamenController {
	static responseFormats = ['json', 'xml']
	
	def index(Integer max) {
		params.max = Math.min(max ?: 2, 100)
		//  respond pacienteService.list(params), model:[pacienteCount: pacienteService.count()]
		respond SalaDeExamen.list(params), model:[pacienteCount: SalaDeExamen.count()]
	}
	
	//todo hacer bien
	def show(Long id) {
		
		SalaDeExamen sala = SalaDeExamen.get(id)
		if (!sala){
			render status: BAD_REQUEST
			return
		}
		respond sala
	}
	
	
	def obtenerCitasDelDia(SalaCommand cmd) {
		def salaID
		if ( params.id.isNumber()  && params.id.toLong() >0){
			salaID = params.id.toLong()
		}else {
			render status: BAD_REQUEST
			return
		}
		
		if (!cmd.hasErrors()){
			
			LocalDateTime fechaYHoraActualConvertida = LocalDateTime.ofInstant(cmd.fechaYHoraActual.toInstant(), ZoneId.systemDefault());
			try {
				 SalaDeExamen sala = SalaDeExamen.get(salaID)
				
				if (!sala) throw new Exception("Error: no existe la sala")
			
			List<Cita> citasDelDia = sala.obtenerCitasDelDia(fechaYHoraActualConvertida)
			
				respond citasDelDia
				
			} catch (e) {
				respond (text: e.message, status: BAD_REQUEST)
			}
		
		}
		else {
			respond cmd.errors, view:'create'
		}
		
	}
	
}
