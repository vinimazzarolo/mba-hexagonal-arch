package br.com.fullcycle.hexagonal.application.usecases;

public abstract class UnitUseCase<INPUT> {
    public abstract INPUT execute();
}
