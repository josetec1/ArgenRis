package argenris

import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDateTime

class BootStrap {

    def init = { servletContext ->
     
        Medico Riviera = new Medico (nombre: "DocRiviera").save(failOnError : true)
        new Medico (nombre: "DocHibbert").save(failOnError : true)
       
        new Paciente(nombre: "Diego", apellido : "Maradona", email: "maradona@gmail.com" ).save(failOnError : true)
        new Paciente(nombre: "Bombita", apellido : "Rodriguez", email: "bombitarod@gmail.com" ).save(failOnError : true)
        new Paciente(nombre: "Arnaldo", apellido : "Andree", email: "Arnaldo@gmail.com" ).save(failOnError : true)
    
        new Procedimiento().save(failOnError : true)
        new Procedimiento().save(failOnError : true)
        new Procedimiento().save(failOnError : true)
        
        
	   new OrdenDeEstudio(Riviera,
			   new Paciente(nombre: "Nino", apellido : "Bravo", email: "nino@gmail.com" ),
			   Prioridad.NORMAL, LocalDateTime.now(),
			   "orden de estudio powered by BootStrap",
			   new Procedimiento()).save(failOnError : true)
        
        
        
    }
    def destroy = {
    }
}
