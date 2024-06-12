package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CuentaService {
    CuentaDao cuentaDao = new CuentaDao();

    @Autowired
    ClienteService clienteService;


    public boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
        return ((cuenta.getTipoCuenta().equals(TipoCuenta.CAJA_AHORRO) && cuenta.getMoneda().equals(TipoMoneda.PESOS)) ||
                (cuenta.getTipoCuenta().equals(TipoCuenta.CAJA_AHORRO) && cuenta.getMoneda().equals(TipoMoneda.DOLARES)) ||
                (cuenta.getTipoCuenta().equals(TipoCuenta.CUENTA_CORRIENTE) && cuenta.getMoneda().equals(TipoMoneda.PESOS))
        );
    }

    public boolean clienteTieneCuentaDeEsteTipo(long dni, TipoCuenta tipoCuenta, TipoMoneda moneda) {
        List<Cuenta> cuentasDelCliente = cuentaDao.getCuentasByCliente(dni);
        for (Cuenta cuenta : cuentasDelCliente) {
            if (cuenta.getTipoCuenta().equals(tipoCuenta) &&
                    cuenta.getMoneda().equals(moneda)) {
                return true;
            }
        }
        return false;
    }


    //Generar casos de test para darDeAltaCuenta
    //    1 - cuenta existente
    //    2 - cuenta no soportada
    //    3 - cliente ya tiene cuenta de ese tipo
    //    4 - cuenta creada exitosamente
    public void darDeAltaCuenta(Cuenta cuenta, long dniTitular) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException {
        if(cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        //Chequear cuentas soportadas por el banco CA$ CC$ CAU$S
        if (!tipoCuentaEstaSoportada(cuenta)) {
            throw new TipoCuentaNotSupportedException("El tipo de cuenta " + cuenta.getTipoCuenta() + " con moneda " + cuenta.getMoneda() + " no está soportado.");
        }

        if(cuentaDao.getCuentasByCliente(dniTitular) != null && cuentaDao.find(dniTitular) != null) {
            throw new TipoCuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        if (clienteTieneCuentaDeEsteTipo(dniTitular, cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya tiene una cuenta de este tipo y moneda.");
        }

        clienteService.agregarCuenta(cuenta, dniTitular);
        cuentaDao.save(cuenta);
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }
}
