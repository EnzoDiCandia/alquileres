package alquileres.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

@Service
public class StorageService {

    private static final Set<String> EXTENSIONES_PERMITIDAS = Set.of(".pdf", ".doc", ".docx");

    private final Path raiz;

    // La carpeta sale de la propiedad storage.path (variable de entorno STORAGE_PATH).
    // En Railway apunta al volume, por ejemplo /data/contratos.
    public StorageService(@Value("${storage.path:uploads/contratos}") String ruta) throws IOException {
        this.raiz = Paths.get(ruta).toAbsolutePath().normalize();
        Files.createDirectories(this.raiz);
        System.out.println("StorageService: guardando archivos en " + this.raiz);
    }

    /** Guarda el archivo y devuelve el nombre con el que quedó guardado. */
    public String subirArchivo(MultipartFile archivo, int idContrato) throws IOException {
        String extension = "";
        String original = archivo.getOriginalFilename();
        if (original != null && original.contains(".")) {
            extension = original.substring(original.lastIndexOf(".")).toLowerCase();
        }
        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new IllegalArgumentException("Formato no permitido. Subí un PDF, DOC o DOCX");
        }

        String nombreArchivo = "contrato_" + idContrato + "_" + System.currentTimeMillis() + extension;
        Path destino = rutaDe(nombreArchivo);
        try (InputStream in = archivo.getInputStream()) {
            Files.copy(in, destino, StandardCopyOption.REPLACE_EXISTING);
        }
        return nombreArchivo;
    }

    /** Borra el archivo. Devuelve false si no existía en el disco. */
    public boolean eliminarArchivo(String referencia) throws IOException {
        return Files.deleteIfExists(rutaDe(referencia));
    }

    /**
     * Devuelve la ruta en disco a partir de lo que hay guardado en la base.
     * Acepta tanto el nombre solo como una URL vieja de Supabase: en ambos casos
     * se queda con lo que está después de la última "/".
     */
    public Path rutaDe(String referencia) {
        String nombre = referencia.substring(referencia.lastIndexOf('/') + 1);
        Path ruta = raiz.resolve(nombre).normalize();
        if (!ruta.startsWith(raiz)) {
            throw new IllegalArgumentException("Nombre de archivo inválido");
        }
        return ruta;
    }

    public String tipoContenido(String nombreArchivo) {
        String n = nombreArchivo.toLowerCase();
        if (n.endsWith(".pdf")) return "application/pdf";
        if (n.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (n.endsWith(".doc")) return "application/msword";
        return "application/octet-stream";
    }
}
