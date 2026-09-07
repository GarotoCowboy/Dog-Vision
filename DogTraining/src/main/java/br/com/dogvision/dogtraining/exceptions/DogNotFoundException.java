package br.com.dogvision.dogtraining.exceptions;


public class DogNotFoundException extends RuntimeException {
    public DogNotFoundException() {
        super("Dog not found");
    }
}
