package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNotSupportedException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CuentaServiceTest {

    @InjectMocks
    private CuentaService cuentaService;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCuentaExistente() throws CuentaAlreadyExistsException, TipoCuentaNotSupportedException, TipoCuentaAlreadyExistsException {
        Cuenta cuentaExistente = new Cuenta();
        cuentaExistente.setNumeroCuenta(223);
        cuentaExistente.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuentaExistente.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(223);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.find(223)).thenReturn(cuentaExistente);

        assertThrows(CuentaAlreadyExistsException.class, () -> {
            cuentaService.darDeAltaCuenta(cuenta, 123456L);
        });

    }

    @Test
    public void testCuentaNoSoportada() throws TipoCuentaNotSupportedException{
        Cuenta cuentaNoSoportada = new Cuenta();
        cuentaNoSoportada.setNumeroCuenta(223);
        cuentaNoSoportada.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuentaNoSoportada.setMoneda(TipoMoneda.DOLARES);

        when(cuentaDao.find(223)).thenReturn(null);

        assertThrows(TipoCuentaNotSupportedException.class, () -> {
            cuentaService.darDeAltaCuenta(cuentaNoSoportada, 123456L);
        });

    }

    @Test
    public void testClienteYaTieneCuentaDeEsteTipo() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException {
        Cliente cliente = new Cliente();
        cliente.setDni(123456L);
        cliente.setNombre("Diego");
        cliente.setApellido("Bruno");

        Cuenta cuentaExistente = new Cuenta();
        cuentaExistente.setNumeroCuenta(223);
        cuentaExistente.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuentaExistente.setMoneda(TipoMoneda.PESOS);

        cuentaService.darDeAltaCuenta(cuentaExistente, 123456L);

        when(cuentaDao.find(cuentaExistente.getNumeroCuenta())).thenReturn(cuentaExistente);
        //Inficamos que cuando llamemos al metodo getCuentas el mock devuelve una lista con el objeto cuentaExistent
        when(cuentaDao.getCuentasByCliente(cliente.getDni())).thenReturn(Arrays.asList(cuentaExistente));

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setNumeroCuenta(224);
        cuenta2.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta2.setMoneda(TipoMoneda.PESOS);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> {
            cuentaService.darDeAltaCuenta(cuenta2, cliente.getDni());
        });

    }

    @Test
    public void testCuentaCreadaExitosamente() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNotSupportedException {
        Cuenta cuenta  = new Cuenta();
        cuenta.setNumeroCuenta(223);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        cuentaService.darDeAltaCuenta(cuenta, 123456L);

        when(cuentaDao.find(223)).thenReturn(cuenta);

        Cuenta cuentaResult = cuentaService.find(223);

        verify(cuentaDao, times(1)).save(cuenta);
        assertNotNull(cuentaResult);
        assertEquals(cuenta.getNumeroCuenta(), cuentaResult.getNumeroCuenta());
        assertEquals(cuenta.getTipoCuenta(), cuentaResult.getTipoCuenta());
        assertEquals(cuenta.getMoneda(), cuentaResult.getMoneda());
    }


}
