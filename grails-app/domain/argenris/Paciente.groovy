package argenris

class Paciente {
    
    
    //TODO implementar
    
    String nombre
    String apellido
    String email
    
    static constraints = {
        nombre blank: false, nullable: false
        apellido blank: false, nullable: false
        email blank: false, nullable: false, email:true, unique: true
    }
    
}
