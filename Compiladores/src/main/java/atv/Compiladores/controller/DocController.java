package atv.Compiladores.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController // Alterado para RestController para suportar JSON
@RequestMapping("/api")
public class DocController {

    @GetMapping("/")
    public String home() {
        return "index"; 
    }

    @PostMapping("/validar-documento")
    public Map<String, Boolean> validarDocumento(@RequestBody Map<String, String> request) {
        String tipo = request.get("tipo");
        String documento = request.get("documento");

        boolean valido = validar(tipo, documento);

        Map<String, Boolean> response = new HashMap<>();
        response.put("valido", valido);
        return response;
    }

    private boolean validar(String tipo, String documento) {
        if ("cpf".equalsIgnoreCase(tipo)) {
            return documento != null && documento.matches("\\d{11}"); 
        } else if ("cnpj".equalsIgnoreCase(tipo)) {
            return documento != null && documento.matches("\\d{14}");
        }
        return false;
    }
}
