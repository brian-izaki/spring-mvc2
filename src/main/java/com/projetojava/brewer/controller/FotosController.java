package com.projetojava.brewer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.projetojava.brewer.storage.FotoStorageRunnabel;

@RestController
@RequestMapping("/fotos")
public class FotosController {
	
	@PostMapping
	public DeferredResult<String> upload(@RequestParam("files[]") MultipartFile[] files) {
		DeferredResult<String> resultado = new DeferredResult<>();
		
		// cria uma outra thread, java assíncrono.
		Thread thread = new Thread(new FotoStorageRunnabel(files, resultado));
		thread.start();
		
		
		return resultado;
	}
	
}
