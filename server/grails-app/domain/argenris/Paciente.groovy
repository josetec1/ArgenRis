package argenris

import argenris.OrdenDeEstudio.OrdenDeEstudio

import java.time.LocalDate


class Paciente {
    
    String nombre
    String apellido
    String direccion
    Long dni
    LocalDate fechaDeNacimiento
    String email
    String telefono  //todo falta validar esto
    Set<OrdenDeEstudio> ordenesDeEstudio =[]
    
    static hasMany = [
            ordenesDeEstudio: OrdenDeEstudio
    ]
    
    static constraints = {
        nombre blank: false, nullable: false
        apellido blank: false, nullable: false
        direccion blank: false, nullable: false
        email blank: false, nullable: false, email:true, unique: true
        dni blank: false, nullable:false, number: true, unique: true
        fechaDeNacimiento blank: false, nullable:false, Date: true
    }
    
    Paciente(String nombre,String apellido,String direccion,Long dni,LocalDate fechaDeNacimiento,String email,String telefono){
        
        this.nombre = nombre
        this.apellido = apellido
        this.direccion = direccion
        this.dni = dni
        this.fechaDeNacimiento = fechaDeNacimiento
        this.email = email
        this.telefono = telefono
        
    }
    
    void agregarOrden (OrdenDeEstudio unaOrden){
        //todo validar que esta orden contenga al mismo paciente
        this.ordenesDeEstudio << unaOrden
    }
}
