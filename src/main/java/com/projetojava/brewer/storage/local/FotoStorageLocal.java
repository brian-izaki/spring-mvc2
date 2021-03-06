package com.projetojava.brewer.storage.local;

import static java.nio.file.FileSystems.getDefault;

import com.projetojava.brewer.storage.FotoStorage;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class FotoStorageLocal implements FotoStorage {
    private static final Logger logger = LoggerFactory.getLogger(FotoStorageLocal.class);
    private static final String THUMBNAIL_PREFIX = "thumbnail.";

    private Path local;
    private Path localTemporario;

    public FotoStorageLocal() {
        this(getDefault().getPath(System.getenv("HOME"), ".brewerfotos"));
    }

    public FotoStorageLocal(Path path) {
        this.local = path;
        criarPastas();
    }

    @Override
    public byte[] recuperarFotoTemporaria(String nome) {
        try {
            return Files.readAllBytes(this.localTemporario.resolve(nome));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler a foto temporária", e);
        }
    }

    @Override
    public void salvar(String foto) {
        try {
            Files.move(this.localTemporario.resolve(foto), this.local.resolve(foto));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao mover a foto para o destino final", e);
        }

        try {
            Thumbnails
                    .of(this.local.resolve(foto).toString())
                    .size(40, 68)
                    .toFiles(Rename.PREFIX_DOT_THUMBNAIL);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public byte[] recuperar(String nome) {
        try {
            return Files.readAllBytes(this.local.resolve(nome));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler a foto", e);
        }
    }

    @Override
    public void excluir(String foto) {
        try {
            Files.deleteIfExists(this.local.resolve(foto));
            Files.deleteIfExists(this.local.resolve(THUMBNAIL_PREFIX + foto));
        } catch (IOException e) {
            logger.warn(String.format("Erro ao apagar foto '%s'. Mensagem: %s", foto, e.getMessage()));
        }
    }

    @Override
    public String salvarTemporariamente(MultipartFile[] files) {
        String novoNome = null;
        if (files != null && files.length > 0) {
            MultipartFile arquivo = files[0];
            novoNome = renomearArquivo(arquivo.getOriginalFilename());
            try {
                arquivo.transferTo(
                        new File(this.localTemporario.toAbsolutePath().toString()
                                + getDefault().getSeparator()
                                + novoNome));
            } catch (IOException e) {
                throw new RuntimeException("Erro salvando a foto na pasta temporária");
            }
        }
        return novoNome;
    }

    private void criarPastas() {
        try {
            Files.createDirectories(this.local);
            this.localTemporario = getDefault().getPath(this.local.toString(), "temp");
            Files.createDirectories(this.localTemporario);

            if (logger.isDebugEnabled()) {
                logger.debug("Pastas criadas para salvar fotos");
                logger.debug("pasta default: " + this.local.toAbsolutePath());
                logger.debug("pasta temp: " + this.localTemporario.toAbsolutePath());
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro criando pasta para salvar foto", e);
        }
    }

    private String renomearArquivo(String nomeOriginal) {
        String novoNome = UUID.randomUUID().toString() + "_" + nomeOriginal;
        return novoNome;
    }
}
