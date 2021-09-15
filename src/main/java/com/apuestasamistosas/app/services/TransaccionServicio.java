
package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Apuesta;
import com.apuestasamistosas.app.entities.Transaccion;
import com.apuestasamistosas.app.errors.ErrorTransaccion;
import com.apuestasamistosas.app.repositories.TransaccionRepositorio;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TransaccionServicio {

	@Autowired
	private TransaccionRepositorio transaccionRepositorio;
	
	private final String adminWalletId = "e04a3271-56ab-4c73-af3b-c6e09b9e20e8";

	@Transactional
	public void crearTransaccion(Apuesta apuesta, boolean tipoPago) throws ErrorTransaccion {
		
		ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
		LocalDateTime hoy = LocalDateTime.now(argentina);
		
		Double precioPremio = apuesta.getPremio().getPrecio();
		
		String idUA = apuesta.getUsuarioA().getId();
		
		switch (apuesta.getEstado()) {
		
		case PENDIENTE: // aca solo paga usuarioA a adminWalletId
			Transaccion user = new Transaccion();
			idUA = apuesta.getUsuarioA().getId();
			user.setApuesta(apuesta);
			user.setFechaTransaccion(hoy);
			user.setPrecio(0.0);
			user.setIdObject(idUA);
			user.setSaldo(0.0);
			if (tipoPago) { // pago externo
				if (existenciaTransaccion(adminWalletId)) {// chequea si ya tiene transaccion adminWalletId
					Double saldoActualizado = transaccionRepositorio.saldoActual(adminWalletId) + precioPremio;
					Transaccion pago = new Transaccion();
					pago.setFechaTransaccion(hoy);
					pago.setApuesta(apuesta);
					pago.setPrecio(precioPremio);
					pago.setIdObject(adminWalletId);
					pago.setSaldo(saldoActualizado);
					transaccionRepositorio.save(pago);
				} else { // si es el primer movimiento en la cuenta
					Transaccion pago = new Transaccion();
					pago.setFechaTransaccion(hoy);
					pago.setApuesta(apuesta);
					pago.setPrecio(precioPremio);
					pago.setIdObject(adminWalletId);
					pago.setSaldo(precioPremio);
					transaccionRepositorio.save(pago);
				}
			} else { // billetera betbuy
				idUA = apuesta.getUsuarioA().getId();
				validarBilletera(precioPremio, idUA, apuesta);
				if (existenciaTransaccion(adminWalletId)) {// chequea si ya tiene transaccion adminWalletId
					Double saldoActualizado = transaccionRepositorio.saldoActual(adminWalletId) + precioPremio;
					Transaccion pago = new Transaccion();
					pago.setFechaTransaccion(hoy);
					pago.setApuesta(apuesta);
					pago.setPrecio(precioPremio);
					pago.setIdObject(adminWalletId);
					pago.setSaldo(saldoActualizado);
					transaccionRepositorio.save(pago);
				} else {// si es el primer movimiento en la cuenta
					Transaccion pago = new Transaccion();
					pago.setFechaTransaccion(hoy);
					pago.setApuesta(apuesta);
					pago.setPrecio(precioPremio);
					pago.setIdObject(adminWalletId);
					pago.setSaldo(precioPremio);
					transaccionRepositorio.save(pago);
				}
			}
			
			break;

		case CONFIRMADA:// Aca paga usuarioB y adminWalletId Paga a proveedor

			if (tipoPago) {// pago externo
				if (existenciaTransaccion(adminWalletId)) {// chequea si ya tiene transaccion adminWalletId
					Double saldoActualizado = transaccionRepositorio.saldoActual(adminWalletId) + precioPremio;
					Transaccion credito = new Transaccion();
					credito.setFechaTransaccion(hoy);
					credito.setApuesta(apuesta);
					credito.setPrecio(precioPremio);
					credito.setIdObject(adminWalletId);
					credito.setSaldo(saldoActualizado);
					transaccionRepositorio.save(credito);
				} else {// si es el primer movimiento en la cuenta
					Transaccion pago = new Transaccion();
					pago.setFechaTransaccion(hoy);
					pago.setApuesta(apuesta);
					pago.setPrecio(precioPremio);
					pago.setIdObject(adminWalletId);
					pago.setSaldo(precioPremio);
					transaccionRepositorio.save(pago);
				}
			} else {// uso de billetera betbuy
				String idUB = apuesta.getUsuarioB().getId();
				validarBilletera(precioPremio, idUB, apuesta);// le resta dinero a la billetera del usuario o mensaje de
																// que
																// no tiene saldo
				if (existenciaTransaccion(adminWalletId)) {// chequea si ya tiene transaccion adminWalletId
					Double saldoActualizado = transaccionRepositorio.saldoActual(adminWalletId) + precioPremio;
					Transaccion pago = new Transaccion();
					pago.setFechaTransaccion(hoy);
					pago.setApuesta(apuesta);
					pago.setPrecio(precioPremio);
					pago.setIdObject(adminWalletId);
					pago.setSaldo(saldoActualizado);
					transaccionRepositorio.save(pago);
				} else {// si es el primer movimiento en la cuenta
					Transaccion pago = new Transaccion();
					pago.setFechaTransaccion(hoy);
					pago.setApuesta(apuesta);
					pago.setPrecio(precioPremio);
					pago.setIdObject(adminWalletId);
					pago.setSaldo(precioPremio);
					transaccionRepositorio.save(pago);
				}
			}
			break;
		case RECHAZADA:
			debitoadminWalletId(precioPremio, apuesta);
			reversion(precioPremio, idUA, apuesta);
			break;
		case EXPIRADA:
			debitoadminWalletId(precioPremio, apuesta);
			reversion(precioPremio, idUA, apuesta);
			break;
		case CONCLUIDA:
			apuestaFinalizada(apuesta);
			break;
		}
	}

	@Transactional
	public void apuestaFinalizada(Apuesta apuesta) throws ErrorTransaccion { // MOVIMIENTOS EN CUENTA UNA VEZ FINALIZADA
																				// LA APUESTA
		String idUA = apuesta.getUsuarioA().getId();
		String idUB = apuesta.getUsuarioB().getId();
		Double precioPremio = apuesta.getPremio().getPrecio();

		switch (apuesta.getResultadoApuesta()) {

		case EMPATE: // SE LE DEVUELVE EL SALDO A LOS 2 USUARIOS Y QUEDA EN SU BILLETERA
			reversion(precioPremio, idUA, apuesta);
			debitoadminWalletId(precioPremio, apuesta);
			reversion(precioPremio, idUB, apuesta);
			debitoadminWalletId(precioPremio, apuesta);
			break;

		case USUARIO_A:// SE DEVUELVE EL SALDO AL USUARIO A
			reversion(precioPremio, idUA, apuesta);
			break;

		case USUARIO_B:// SE DEVUELVE EL SALDO AL USUARIO B
			reversion(precioPremio, idUA, apuesta);
			break;

		case INDEFINIDO:
			reversion(precioPremio, idUA, apuesta);
			break;

		}
	}

	@Transactional
	public void validarBilletera(Double precio, String id, Apuesta apuesta) throws ErrorTransaccion {
		ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
		LocalDateTime hoy = LocalDateTime.now(argentina);
		Double saldoActual = transaccionRepositorio.saldoActual(id);
		if(saldoActual == null) {
			throw new ErrorTransaccion(ErrorTransaccion.SALDO_INSUFICIENTE);
		}
		if (precio <= saldoActual) {
			Double saldo = saldoActual - precio;
			Transaccion transaccion = new Transaccion();
			transaccion.setFechaTransaccion(hoy);
			transaccion.setApuesta(apuesta);
			transaccion.setIdObject(id);
			transaccion.setSaldo(saldo);
			transaccionRepositorio.save(transaccion);
		} else {
			throw new ErrorTransaccion(ErrorTransaccion.SALDO_INSUFICIENTE);
		}

	}

	@Transactional
	public void pagoProveedor(Double precio, String id, Apuesta apuesta) throws ErrorTransaccion {// pago al proveedor
															// apuesta

		ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
		LocalDateTime hoy = LocalDateTime.now(argentina);

		String idProveedor = apuesta.getPremio().getProveedor().getId();

		Double comision = precio * 0.15;

		Double pago = precio - comision;

		if (existenciaTransaccion(idProveedor)) {
			Double saldoActualizado = transaccionRepositorio.saldoActual(idProveedor) + pago;
			Transaccion proveedor = new Transaccion();
			proveedor.setFechaTransaccion(hoy);
			proveedor.setApuesta(apuesta);
			proveedor.setIdObject(idProveedor);
			proveedor.setPrecio(precio);
			proveedor.setSaldo(saldoActualizado);
			transaccionRepositorio.save(proveedor);
		} else {
			Transaccion proveedor = new Transaccion();
			proveedor.setFechaTransaccion(hoy);
			proveedor.setApuesta(apuesta);
			proveedor.setIdObject(idProveedor);
			proveedor.setPrecio(precio);
			proveedor.setSaldo(precio);
			transaccionRepositorio.save(proveedor);
		}
		if (existenciaTransaccion(adminWalletId)) {
			Double saldoActualizado = transaccionRepositorio.saldoActual(adminWalletId) + comision;
			Transaccion comisionTransaccion = new Transaccion();
			comisionTransaccion.setFechaTransaccion(hoy);
			comisionTransaccion.setApuesta(apuesta);
			comisionTransaccion.setPrecio(comision);
			comisionTransaccion.setIdObject(adminWalletId);
			comisionTransaccion.setSaldo(saldoActualizado);
			transaccionRepositorio.save(comisionTransaccion);
		} else {
			Transaccion comisionTransaccion = new Transaccion();
			comisionTransaccion.setFechaTransaccion(hoy);
			comisionTransaccion.setApuesta(apuesta);
			comisionTransaccion.setPrecio(comision);
			comisionTransaccion.setIdObject(adminWalletId);
			comisionTransaccion.setSaldo(comision);
			transaccionRepositorio.save(comisionTransaccion);
		}

	}

	public Boolean existenciaTransaccion(String id) {// chequeo de existencia de movimiento den cuenta
		boolean existencia = true;
		Optional<Transaccion> transaccion = transaccionRepositorio.findId(id);
		if (transaccion.isPresent()) {
			return existencia;
		} else {
			existencia = false;
			return existencia;
		}
	}

	@Transactional
	public void reversion(Double precio, String id, Apuesta apuesta) throws ErrorTransaccion {// se devuelve credito al usuario
		ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
		LocalDateTime hoy = LocalDateTime.now(argentina);
		Double saldoActual = transaccionRepositorio.saldoActual(id);
		if(saldoActual == 0.0) {

			Transaccion reversion = new Transaccion();
			reversion.setFechaTransaccion(hoy);
			reversion.setApuesta(apuesta);
			reversion.setPrecio(precio);
			reversion.setIdObject(id);
			reversion.setSaldo(precio);
			transaccionRepositorio.save(reversion);
		}else {
		   saldoActual = transaccionRepositorio.saldoActual(id) + precio;
			Transaccion reversion = new Transaccion();
			reversion.setFechaTransaccion(hoy);
			reversion.setApuesta(apuesta);
			reversion.setPrecio(precio);
			reversion.setIdObject(id);
			reversion.setSaldo(saldoActual);
			transaccionRepositorio.save(reversion);
		}
	}

	@Transactional
	public void debitoadminWalletId(Double precio, Apuesta apuesta) {// se debita las devoluciones a los usuarios
		ZoneId argentina = ZoneId.of("America/Argentina/Buenos_Aires");
		LocalDateTime hoy = LocalDateTime.now(argentina);
		Double saldoActualizado = transaccionRepositorio.saldoActual(adminWalletId) - precio;
		Transaccion debito = new Transaccion();
		debito.setFechaTransaccion(hoy);
		debito.setApuesta(apuesta);
		debito.setPrecio(precio);
		debito.setIdObject(adminWalletId);
		debito.setSaldo(saldoActualizado);
		transaccionRepositorio.save(debito);
	}

}
