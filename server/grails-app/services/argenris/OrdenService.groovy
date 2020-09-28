package argenris


import argenris.OrdenDeEstudio.OrdenDeEstudio
import grails.gorm.transactions.Transactional
import java.time.LocalDateTime

@Transactional // es para indicar que sea una transaccion (se hace completo o no se hace nada)
class OrdenService {
    
    def pacienteRepositorio
    def procedimientoRepositorio
    def medicoActualRepositorio
    
    OrdenDeEstudio crear(Long pacienteID, Prioridad prioridad, LocalDateTime fechaCreacion, String nota, Long procedimientoID) {
        
        //el controlador tuvo que hacer validacion del estilo " los campos estan todos, vino un id, etc
        // al margen de  eso, el dominio tiene que hacer sus propias validaciones de reglas de negocio
        // no agregar logica en el servicio.
        
        // 1 obtener paciente del repositorio
          Paciente paciente = pacienteRepositorio.getById(pacienteID).orElseThrow(
                  { ->new IllegalStateException ("no hay paciente con id=${pacienteID}") })
        
        Procedimiento procedimiento = procedimientoRepositorio.getById(procedimientoID).orElseThrow(
                { ->new IllegalStateException ("no hay procedimiento con id=${procedimientoID}") })
        
        //2 obtener el medico actual
        Medico medico = medicoActualRepositorio.buscar()
        
        //3 crear la orden de estudio
        def orden = medico.crearOrdenDeEstudio(paciente,prioridad,fechaCreacion,nota,procedimiento)
        
        //4 persistirla
       orden.save(failOnError : true)
        orden
    }
    
    //todo refactor
    OrdenDeEstudio cancelar(Long ordenId) {
        
        //el controlador tuvo que hacer validacion del estilo " los campos estan todos, vino un id, etc
        // al margen de  eso, el dominio tiene que hacer sus propias validaciones de reglas de negocio
        // no agregar logica en el servicio.
        
        // 1 obtener paciente del repositorio
        def orden = OrdenDeEstudio.findById(ordenId)
        
       
        orden.cancelar(LocalDateTime.now())
        
        //4 persistirla
        orden.save(failOnError : true)
        orden
    }
}
