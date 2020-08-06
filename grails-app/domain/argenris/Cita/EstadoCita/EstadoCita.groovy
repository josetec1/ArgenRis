package argenris.Cita.EstadoCita

import java.time.LocalDateTime


abstract class EstadoCita {


     boolean estaVencida(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual) {
            fechaYHoraActual.isAfter(fechaYHoraDeCita.plusMinutes(30))
     }

   abstract EstadoCita pacienteArribando(LocalDateTime fechaYHoraDeCita, LocalDateTime fechaYHoraActual)
   abstract EstadoCita cancelar()
}
