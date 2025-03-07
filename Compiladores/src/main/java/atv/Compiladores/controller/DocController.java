package atv.Compiladores.controller;

import atv.Compiladores.service.ValidadorDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DocController {

    @Autowired
    private ValidadorDocumento validadorDocumento; 
    @GetMapping("/")
    public String home() {
        return "index"; 
    }

    @PostMapping("/validar-documento")
    public Map<String, Boolean> validarDocumento(@RequestBody Map<String, String> request) {
        String tipo = request.get("tipo");
        String documento = request.get("documento");

        // Validando o documento com o tipo (RG ou CPF)
        boolean valido = validadorDocumento.run(documento, tipo);

        // Criando um mapa para retornar a resposta
        Map<String, Boolean> response = new HashMap<>();
        response.put("valido", valido); // Retornando se o documento é válido ou não
        return response;
    }
}
