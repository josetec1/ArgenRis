package argenris

import grails.gorm.transactions.Transactional


import java.time.LocalDateTime

@Transactional

class OrdenService {
    
  //  def pacienteRepositorio
    
 //   def medicoActualRepositorio
     
    
    
    OrdenDeEstudio crear(Long pacienteID,Prioridad prioridad, LocalDateTime fechaCreacion,String nota, Long procedimientoID) {
        
        // 1 obtener paciente del repositorio
        Paciente paciente = Paciente.get (pacienteID) // hay que ver por que no inyecta la dependencia
        //2 obtener los demas datos
       
        Procedimiento procedimiento = Procedimiento.get (procedimientoID)
        
        
        
        //3 obtener el medico actual
        //   def medico = medicoActualRepositorio.buscar()
        Medico medico = new Medico("juan")
        
       
        //4 crear la orden de estudio
        
            /*
           OrdenDeEstudio orden =  medico.crearOrdenDeEstudio(paciente,
                    prioridad,
                    fechaCreacion,
                    nota,
                    procedimiento)
        */
         OrdenDeEstudio orden =  new OrdenDeEstudio(medico,paciente,prioridad,fechaCreacion,nota,procedimiento)
    
       
        
        
        
            //5 persistirla
            orden.save()
            
        
        
    }
}
