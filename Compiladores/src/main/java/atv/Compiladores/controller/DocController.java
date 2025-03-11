package atv.Compiladores.controller;

import atv.Compiladores.service.ValidadorDocumento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
public class DocController {

    @Autowired
    private ValidadorDocumento validadorDocumento; 
    @GetMapping("/")
    public String home() {
        return "index"; 
    }

    @PostMapping("/documento")
    public ResponseEntity<Boolean> validarDocumento(@RequestBody Map<String, String> request) {
        String tipo = request.get("tipo");
        String documento = request.get("documento");

        // Validando o documento com o tipo (RG ou CPF)
        boolean valido = validadorDocumento.run(documento, tipo);

        // Retornando o boolean diretamente no ResponseEntity com o status adequado
        return ResponseEntity.ok().body(valido);
    }
}
