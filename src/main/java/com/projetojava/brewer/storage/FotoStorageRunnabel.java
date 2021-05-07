package com.projetojava.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

public class FotoStorageRunnabel implements Runnable {

	private MultipartFile[] files;
	private DeferredResult<String> resultado;
	
	public FotoStorageRunnabel(MultipartFile[] files, DeferredResult<String> resultado) {
		this.files = files;
		this.resultado = resultado;
	}
	
	@Override
	public void run() {
		resultado.setResult("OK foto recebida");

	}

}
