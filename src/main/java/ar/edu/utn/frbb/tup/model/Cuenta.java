package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;

import java.time.LocalDateTime;
import java.util.Random;

public class Cuenta {
    private long numeroCuenta;
    LocalDateTime fechaCreacion;
    double balance;
    TipoCuenta tipoCuenta;
    Cliente titular;
    TipoMoneda moneda;

    public Cuenta() {
        this.numeroCuenta = new Random().nextLong();
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Cliente getTitular() {
        return titular;
    }

    public void setTitular(Cliente titular) {
        this.titular = titular;
    }


    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public Cuenta setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public void debitarDeCuenta(int cantidadADebitar) throws TipoCuentaAlreadyExistsException.NoAlcanzaException, TipoCuentaAlreadyExistsException.CantidadNegativaException {
        if (cantidadADebitar < 0) {
            throw new TipoCuentaAlreadyExistsException.CantidadNegativaException();
        }

        if (balance < cantidadADebitar) {
            throw new TipoCuentaAlreadyExistsException.NoAlcanzaException();
        }
        this.balance = this.balance - cantidadADebitar;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void forzaDebitoDeCuenta(int i) {
        this.balance = this.balance - i;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    @Override
    public String toString() {
        return "Número de cuenta: " + numeroCuenta;
    }


}
