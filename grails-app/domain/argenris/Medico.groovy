package argenris


import java.time.LocalDateTime

//todo falta implementar. constructor etc.

class Medico {

    String nombre
    Set<OrdenDeEstudio> ordenesDeEstudio =[]
  
    static hasMany = [
            ordenesDeEstudio: OrdenDeEstudio
    ]
    
    static constraints = {
        nombre nullable: false
    }
    
    Medico (String nombre) {
        this.nombre = nombre
    }

    
    
    OrdenDeEstudio crearOrdenDeEstudio (Paciente paciente,
                                        Prioridad prioridad,
                                        LocalDateTime fechaDeCreacion,
                                        String nota,
                                        Procedimiento procedimiento){
        
        OrdenDeEstudio orden = new OrdenDeEstudio(this,paciente,prioridad,fechaDeCreacion,nota,procedimiento)
        this.ordenesDeEstudio << orden
        orden
    }
}


