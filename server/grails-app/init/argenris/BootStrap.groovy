package argenris

import argenris.Cita.Cita
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
    
    
        OrdenDeEstudio miOrden = new OrdenDeEstudio(Riviera,
                new Paciente(nombre: "Nino", apellido : "Bravo", email: "nino@gmail.com" ),
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap",
                new Procedimiento()).save(failOnError : true)
    
        new SalaDeExamen(new ArrayList<Cita>()).save(failOnError : true)
        new SalaDeExamen(new ArrayList<Cita>()).save(failOnError : true)
        new Cita(LocalDateTime.now(),Prioridad.NORMAL,miOrden).save(failOnError : true)
    
        OrdenDeEstudio miOrden2 = new OrdenDeEstudio(Riviera,
                new Paciente(nombre: "Franco", apellido : "Devita", email: "Franco@gmail.com" ),
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap 2",
                new Procedimiento()).save(failOnError : true)
    
        OrdenDeEstudio miOrden3 = new OrdenDeEstudio(Riviera,
                new Paciente(nombre: "Daniel", apellido : "Laruzzo", email: "Laruzo@gmail.com" ),
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap 3",
                new Procedimiento()).save(failOnError : true)
    
    }
    def destroy = {
    }
}
