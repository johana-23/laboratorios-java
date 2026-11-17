package com.example.petmatch_lab.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("Ya existe una cuenta con este correo electrónico.");
    }
}
