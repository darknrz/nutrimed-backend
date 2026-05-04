package com.tuapp.nutrimed_backend.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    // Constructor que inyecta las propiedades del application.yml
    public CloudinaryService(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    /**
     * Genera una firma (signature) para subidas seguras desde Flutter.
     * @param folder Carpeta donde se guardará la imagen (ej: "nutrimed/avatars")
     * @return Mapa con la firma, el timestamp y las credenciales públicas.
     */
    public Map<String, Object> getSignature(String folder) {
        long timestamp = System.currentTimeMillis() / 1000L;

        // 1. Definir los parámetros exactos que se van a firmar
        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("folder", folder);
        paramsToSign.put("timestamp", timestamp);

        // 2. Generar la firma usando el API SECRET (esto solo ocurre en el servidor)
        String signature = cloudinary.apiSignRequest(paramsToSign, apiSecret);

        // 3. Preparar la respuesta para el frontend (Flutter)
        Map<String, Object> response = new HashMap<>();
        response.put("signature", signature);
        response.put("timestamp", timestamp);
        response.put("cloud_name", cloudinary.config.cloudName);
        response.put("api_key", cloudinary.config.apiKey);
        response.put("folder", folder);

        return response;
    }
}