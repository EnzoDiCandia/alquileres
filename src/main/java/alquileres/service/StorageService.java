package alquileres.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class StorageService {

    private static final String SUPABASE_URL = "https://rnivaeidqdbgjyqxzqcs.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJuaXZhZWlkcWRiZ2p5cXh6cWNzIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc3OTgyMDYwNSwiZXhwIjoyMDk1Mzk2NjA1fQ.mZkQvSWwiP3ZCvHomSJ-ZPUY23f2F7WfjZY3wGTrEXg";
    private static final String BUCKET = "contratos";

    public String subirArchivo(MultipartFile archivo, int idContrato) throws Exception {
        String extension = "";
        String original = archivo.getOriginalFilename();
        if (original != null && original.contains(".")) {
            extension = original.substring(original.lastIndexOf("."));
        }
        String nombreArchivo = "contrato_" + idContrato + "_" + System.currentTimeMillis() + extension;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SUPABASE_URL + "/storage/v1/object/" + BUCKET + "/" + nombreArchivo))
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .header("Content-Type", archivo.getContentType() != null ? archivo.getContentType() : "application/octet-stream")
                .POST(HttpRequest.BodyPublishers.ofByteArray(archivo.getBytes()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return SUPABASE_URL + "/storage/v1/object/public/" + BUCKET + "/" + nombreArchivo;
        } else {
            throw new RuntimeException("Error al subir archivo: " + response.statusCode() + " - " + response.body());
        }
    }

    public boolean eliminarArchivo(String urlArchivo) throws Exception {
        String nombreArchivo = urlArchivo.substring(urlArchivo.lastIndexOf("/") + 1);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SUPABASE_URL + "/storage/v1/object/" + BUCKET + "/" + nombreArchivo))
                .header("Authorization", "Bearer " + SUPABASE_KEY)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }
}
