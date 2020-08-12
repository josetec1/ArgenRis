package argenris



import java.time.LocalDateTime

class BootStrap {

    def init = { servletContext ->
       
        new Medico (nombre: "DocRiviera").save(failOnError : true)
        new Medico (nombre: "DocHibbert").save(failOnError : true)
        
        new Paciente(nombre: "Diego", apellido : "Maradona", email: "dios@gmail.com" ).save()
        new Paciente(nombre: "Bombita", apellido : "Rodriguez", email: "bombitarod@gmail.com" ).save()
        new Paciente(nombre: "James", apellido : "Boo", email: "AgenteJamesBoo@gmail.com" ).save()
        
        new OrdenDeEstudio(new Medico (nombre: "DocJose"),
                new Paciente(nombre: "Camino", apellido : "Alcielo", email: "nofunciona@gmail.com" ),
                Prioridad.NORMAL, LocalDateTime.now(),
                "orden de estudio powered by BootStrap",
                new Procedimiento()).save(failOnError : true)
        
        new Procedimiento().save(failOnError : true)
        
        
        
        
    }
    def destroy = {
    }
}
