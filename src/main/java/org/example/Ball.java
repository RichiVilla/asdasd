package org.example;

class Ball {
    constructor(position, color) {
        this.position = position; // Posición de la pelota en el tablero
        this.color = color; // Color de la pelota
    }

    // Método para cambiar la posición de la pelota
    move(newPosition) {
        this.position = newPosition; // Actualiza la posición de la pelota
    }

    // Método para obtener el color de la pelota
    getColor() {
        return this.color; // Retorna el color de la pelota
    }

    // Método para obtener la posición de la pelota
    getPosition() {
        return this.position; // Retorna la posición de la pelota
    }
}
