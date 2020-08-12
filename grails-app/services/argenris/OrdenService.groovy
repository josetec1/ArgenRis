package argenris

import argenris.Medico
import argenris.OrdenDeEstudio
import argenris.Paciente
import argenris.Prioridad
import argenris.Procedimiento
import grails.gorm.transactions.Transactional
import java.time.LocalDateTime

@Transactional
class OrdenService {
    
    def pacienteRepositorio
    def procedimientoRepositorio
    def medicoActualRepositorio
    
    OrdenDeEstudio crear(Long pacienteID, Prioridad prioridad, LocalDateTime fechaCreacion, String nota, Long procedimientoID) {
        
        // 1 obtener paciente del repositorio
        Paciente paciente = pacienteRepositorio.getById(pacienteID)
        Procedimiento procedimiento = procedimientoRepositorio.getById(procedimientoID)
        
        //2 obtener el medico actual
        Medico medico = medicoActualRepositorio.buscar()
        
        //3 crear la orden de estudio
        def orden = medico.crearOrdenDeEstudio(paciente,prioridad,fechaCreacion,nota,procedimiento)
        
        //4 persistirla
        orden.save()
    }
}
