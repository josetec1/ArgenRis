package argenris

enum Prioridad {
    URGENTE(15), NORMAL(30)

    private final int rango

    Prioridad(int rango) {
        this.rango = rango
    }

    int obtenerRango() {
        rango
    }

}