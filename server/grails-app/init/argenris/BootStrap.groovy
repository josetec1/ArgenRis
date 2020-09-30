package argenris

import argenris.Cita.Cita
import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDate
import java.time.LocalDateTime

class BootStrap {

    def init = { servletContext ->
    
        
      
        
        
        // medicos
        Medico riviera = new Medico (nombre: "DocRiviera").save(failOnError : true)
        Medico hibbert = new Medico (nombre: "DocHibbert").save(failOnError : true)
    
        //Pacientes
        def paciente1 = new Paciente("Diego","Maradona","Segurola y Habana", 48426598, LocalDate.of(1960,10,30), "maradona@gmail.com", "42504587" ).save(failOnError : true)
        def paciente2 = new Paciente("Bombita","Rodriguez","Segurola y Habana", 38423598, LocalDate.of(1960,10,30), "bombitarod@gmail.com", "42504587" ).save(failOnError : true)
        def paciente3 =new Paciente("Arnaldo","Andree","Segurola y Habana", 18112311, LocalDate.of(1960,10,30), "Arnaldo@gmail.com", "42504587" ).save(failOnError : true)
        def paciente4 = new Paciente("Darsena","Mortin","Segurola y Habana", 18426598, LocalDate.of(1960,10,30), "cha@gmail.com", "42504587" ).save(failOnError : true)
        def paciente5 = new Paciente("dIango","Martel","Segurola y Habana", 48426198, LocalDate.of(1960,10,30), "che@gmail.com", "42504587" ).save(failOnError : true)
        def paciente6 = new Paciente("Dionisio","Teatroiski","Segurola y Habana", 48421198, LocalDate.of(1960,10,30), "che1@gmail.com", "42504587" ).save(failOnError : true)
    
        //Procedimientos
        new Procedimiento().save(failOnError : true)
        new Procedimiento().save(failOnError : true)
        new Procedimiento().save(failOnError : true)
    
        
        //Salas
        AreaDeExamen sala1 = new SalaDeExamen(new ArrayList<Cita>()).save(failOnError : true)
        AreaDeExamen sala2 = new SalaDeExamen(new ArrayList<Cita>()).save(failOnError : true)
    
        
        
        //OrdenDeEstudio
        OrdenDeEstudio miOrden = new OrdenDeEstudio(riviera,paciente4,
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap",
                new Procedimiento()).save(failOnError : true)
    
        OrdenDeEstudio miOrden2 = new OrdenDeEstudio(riviera,
                    paciente3,
                    Prioridad.NORMAL, LocalDateTime.now(),
                    "orden de estudio powered by BootStrap 2",
                    new Procedimiento()).save(failOnError : true)
    
        OrdenDeEstudio miOrden3 = new OrdenDeEstudio(riviera,
                paciente2,
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap 3",
                new Procedimiento()).save(failOnError : true)
    
        OrdenDeEstudio miOrden4 = new OrdenDeEstudio(hibbert,
                paciente1,
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap 4",
                new Procedimiento()).save(failOnError : true)
    
        // ************************************************
        // estas cosas asi no se usan, ir retirando
          Cita cita1=   sala1.crearCita(LocalDateTime.now(),Prioridad.NORMAL.toString(),miOrden).save(failOnError : true)
        
        
        
    
        // Fin de las cosas que no deberian usarse asi
        //**********************************************
        
    }
    def destroy = {
    }
}
