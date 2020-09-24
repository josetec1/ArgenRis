package argenris

import argenris.Cita.Cita
import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDate
import java.time.LocalDateTime

class BootStrap {

    def init = { servletContext ->
    
        Medico riviera = new Medico (nombre: "DocRiviera").save(failOnError : true)
    
        Medico hibbert = new Medico (nombre: "DocHibbert").save(failOnError : true)
    
     
        
       def paciente1 = new Paciente("Diego","Maradona","Segurola y Habana", 48426598, LocalDate.of(1960,10,30), "maradona@gmail.com", "42504587" ).save(failOnError : true)
        def paciente2 = new Paciente("Bombita","Rodriguez","Segurola y Habana", 38423598, LocalDate.of(1960,10,30), "bombitarod@gmail.com", "42504587" ).save(failOnError : true)
        def paciente3 =new Paciente("Arnaldo","Andree","Segurola y Habana", 18112311, LocalDate.of(1960,10,30), "Arnaldo@gmail.com", "42504587" ).save(failOnError : true)
        
       
    
    
        new Procedimiento().save(failOnError : true)
        new Procedimiento().save(failOnError : true)
        new Procedimiento().save(failOnError : true)
    
    
       OrdenDeEstudio miOrden = new OrdenDeEstudio(riviera,paciente1,
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap",
                new Procedimiento()).save(failOnError : true)
    
        new SalaDeExamen(new ArrayList<Cita>()).save(failOnError : true)
        new SalaDeExamen(new ArrayList<Cita>()).save(failOnError : true)
        new Cita(LocalDateTime.now(),Prioridad.NORMAL,miOrden).save(failOnError : true)
    
      OrdenDeEstudio miOrden2 = new OrdenDeEstudio(riviera,
                paciente2,
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap 2",
                new Procedimiento()).save(failOnError : true)
    
       OrdenDeEstudio miOrden3 = new OrdenDeEstudio(riviera,
                paciente3,
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap 3",
                new Procedimiento()).save(failOnError : true)
    
        OrdenDeEstudio miOrden4 = new OrdenDeEstudio(hibbert,
                paciente2,
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap 4",
                new Procedimiento()).save(failOnError : true)
    
    }
    def destroy = {
    }
}
