package argenris

import spock.lang.Specification

class PrioridadSpec extends Specification{

    def setup() {
    }

    def cleanup() {
    }

    void "Prioridad obtener rango devuelve 15 cuando prioridad es URGENTE"() {
       given:
            def prioridad = Prioridad.URGENTE
       when:
            def rango = prioridad.obtenerRango()
       then:
            rango == 15
    }
    void "Prioridad obtener rango devuelve 15 cuando prioridad es NORMAL"() {
        given:
        def prioridad = Prioridad.NORMAL
        when:
        def rango = prioridad.obtenerRango()
        then:
        rango == 30
    }
}
