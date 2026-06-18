package br.com.dogvision.dogmanagement.exceptions;


public class DogNotFoundException extends RuntimeException {
    public DogNotFoundException() {
        super("Dog not found");
    }
}
