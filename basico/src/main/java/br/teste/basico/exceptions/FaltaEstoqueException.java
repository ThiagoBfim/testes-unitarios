package br.teste.basico.exceptions;

public class FaltaEstoqueException extends RuntimeException {

    public FaltaEstoqueException(String message) {
        super(message);
    }
}
