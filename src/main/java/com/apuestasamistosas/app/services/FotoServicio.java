package com.apuestasamistosas.app.services;

import com.apuestasamistosas.app.entities.Foto;
import com.apuestasamistosas.app.repositories.FotoRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicio {

	@Autowired
	private FotoRepositorio fotoRepositorio;

	public Foto guardar(MultipartFile archivo) throws Exception {
		if (archivo != null && archivo.getSize() != 0) {
			try {
				Foto foto = new Foto();
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());
				return fotoRepositorio.save(foto);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return null;
			}

		} else {
			return null;
		}

	}

	public Foto actualizar(String idfoto, MultipartFile archivo) throws Exception {
		if (archivo != null && archivo.getSize() != 0) {
			System.out.println("INGRESO EN IF");
			try {

				if (idfoto != null) {
					Optional<Foto> respuesta = fotoRepositorio.findById(idfoto);
					if (respuesta.isPresent()) {
						Foto foto = respuesta.get();
						foto.setMime(archivo.getContentType());
						foto.setNombre(archivo.getName());
						foto.setContenido(archivo.getBytes());
						return fotoRepositorio.save(foto);
					}
				} else {
					Foto foto = new Foto();
					foto.setMime(archivo.getContentType());
					foto.setNombre(archivo.getName());
					foto.setContenido(archivo.getBytes());
					return fotoRepositorio.save(foto);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}

		}
		return null;
	}

}