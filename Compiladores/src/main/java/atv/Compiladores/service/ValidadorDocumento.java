package atv.Compiladores.service;
import org.springframework.stereotype.Service;

@Service // Necessário pro Spring
public class ValidadorDocumento {

    private int somaPonderadaRg = 0; // Acumula soma de números enquanto a validação do RG não finaliza
    private int multiplCalcDigRg = 2; // Controle o multiplicador de cada dígito do RG

    public boolean run(final String documento, final String type) {
        try {
            return isValidDocument(documento, type); // Se o documento for válido, true, senão, false
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isValidDocument(String documento, String type) {
        if (type.equalsIgnoreCase("rg")) {
            return rgValido(documento);
        } else if (type.equalsIgnoreCase("cpf")) {
            return cpfValido(documento);
        } else {
            return false;
        }
    }

    private enum Estado {
        Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, ERROR
    }


    // Validação RG
    public boolean rgValido(String documento) { //Método principal para validar RG
        somaPonderadaRg = 0;
        multiplCalcDigRg = 2;
        Estado estadoCorrente = Estado.Q0; // estadoCorrente controla em qual estado da validação o código está - estados definidos no enum
        boolean primeiroPonto = false; // Variável de controle pra verificar se o RG tem "."
        boolean segundoPonto = false; // Variável de controle pra verificar se o RG tem "."
        boolean caractereFinal = false; // Variável de controle pra verificar se o RG tem "-"

        for (char c : documento.toCharArray()) { //Transforma a string em um array pra analisar cada caractere individualmente
            switch (estadoCorrente) { //switch usado para decidir o que fazer em cada estado
                case Q0:
                estadoCorrente = (Character.isDigit(c)) ? Estado.Q1 : Estado.ERROR; // Verifica se é um número, se for vai pro próximo estado
                    icrementaRgSoma(c); // acumula o valor do carcatere na soma
                    break;
                case Q1:
                estadoCorrente = (Character.isDigit(c)) ? Estado.Q2 : Estado.ERROR;
                    icrementaRgSoma(c);
                    break;
            
                    //Validação de Pontuação - início

                case Q2:
                    if (Character.isDigit(c)) {
                        estadoCorrente = Estado.Q3;
                        icrementaRgSoma(c);
                    } else if (c == '.' && !primeiroPonto) { // aqui o autômato identifica que o primeiro ponto não havia sido encontrado ainda
                        estadoCorrente = Estado.Q2; // Nesse caso, ele mantem o usuário em Q2 pra que ele coloque o número.
                        primeiroPonto = true; // Mudamos a variável primeiroPonto pra true, pra ter a marcaçãod e que já apareceu
                    } else {
                        estadoCorrente = Estado.ERROR;
                    }
                    break;

                    //Validação de Pontuação - fim

                case Q3:
                estadoCorrente = (Character.isDigit(c)) ? Estado.Q4 : Estado.ERROR;
                    icrementaRgSoma(c);
                    break;
                case Q4:
                estadoCorrente = (Character.isDigit(c)) ? Estado.Q5 : Estado.ERROR;
                    icrementaRgSoma(c);
                    break;

                    //Validação de ponto - início

                case Q5:
                    if (Character.isDigit(c)) {
                        estadoCorrente = Estado.Q6;
                        icrementaRgSoma(c);
                    } else if (c == '.' && !segundoPonto && primeiroPonto) { // Aqui a validação de só acietar o segundo ponto se o primeiro já for TRUE
                        estadoCorrente = Estado.Q5; // Mantém o uusário no estado q5
                        segundoPonto = true; // Passa a variável pra TRUE pra ter a marcação do segundo ponto
                    } else {
                        estadoCorrente = Estado.ERROR;
                    }
                    break;

                    // Validação de ponto - fim

                case Q6:
                estadoCorrente = (Character.isDigit(c)) ? Estado.Q7 : Estado.ERROR;
                    icrementaRgSoma(c);
                    break;
                case Q7:
                estadoCorrente = (Character.isDigit(c)) ? Estado.Q8 : Estado.ERROR;
                    icrementaRgSoma(c);
                    break;

                    // Validação de hífem - início
                case Q8:
                    if (Character.isDigit(c)) {
                        estadoCorrente = Estado.Q9;
                    } else if (c == '-' && !caractereFinal && segundoPonto) {
                        estadoCorrente = Estado.Q8;
                        caractereFinal = true;
                    } else {
                        estadoCorrente = Estado.ERROR;
                    }
                    break;

                    // Validação de hífem - fim


                default: // acionado quando nenhum dos casos anteriores é atendido
                estadoCorrente = Estado.ERROR;
            }

            if (estadoCorrente == Estado.ERROR) {
                break;
            }
        }

        if (primeiroPonto && segundoPonto && !caractereFinal) { // Se tiver o primeiro e segundo ponto, e não tiver hífen, documento inválido
            return false;
        }

        if (primeiroPonto && !segundoPonto) {
            return false;
        }

        if (estadoCorrente == Estado.Q9) { // Se o autômato está no último estado
            int digitoVerificador = (somaPonderadaRg % 11 == 0) ? 0 : 11 - (somaPonderadaRg % 11); // condicao ? valorSeVerdadeiro : valorSeFalso;

            if (digitoVerificador == 10 && String.valueOf(documento.charAt(8)).equalsIgnoreCase("x")) {
                return true;
            } else if (digitoVerificador == Integer.parseInt(String.valueOf(documento.charAt(documento.length() - 1)))) {
                return true;
            }
        }
        return false;
    }

    public void icrementaRgSoma(char c) {
        somaPonderadaRg += Integer.parseInt(String.valueOf(c)) * multiplCalcDigRg;
        multiplCalcDigRg++;
    }



    //Validação CPF
    public boolean cpfValido(String documento) {
        if (!cpfFormatoValido(documento)) {
            return false;
        }
   
        String cpfSemFormatacao = documento.replaceAll("[^\\d]", "");
        return cpfLogicaValida(cpfSemFormatacao);
    }

    private boolean cpfFormatoValido(String documento) {
        enum EstadoCPF { Q0, Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, ERROR }
        EstadoCPF estado = EstadoCPF.Q0;
        
        for (char c : documento.toCharArray()) {
            switch (estado) {
                case Q0: case Q1: case Q2: case Q3: case Q4:
                case Q5: case Q6: case Q7: case Q8: case Q9:
                case Q10:
                    if (Character.isDigit(c)) {
                        estado = EstadoCPF.values()[estado.ordinal() + 1];
                    } else if (c == '.') {
                        if (estado.ordinal() == 3 || estado.ordinal() == 6) {
                        } else {
                            estado = EstadoCPF.ERROR;
                        }
                    } else if (c == '-') {
                        if (estado.ordinal() == 9) {
                        } else {
                            estado = EstadoCPF.ERROR;
                        }
                    } else {
                        estado = EstadoCPF.ERROR;
                    }
                    break;
                default:
                    estado = EstadoCPF.ERROR;
            }
            
            if (estado == EstadoCPF.ERROR) {
                return false;
            }
        }
        
        return (estado == EstadoCPF.Q11);
    }
        
    private boolean cpfLogicaValida(String cpf) {
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;
        
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;
        
        return (digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                digito2 == Character.getNumericValue(cpf.charAt(10)));
    }
}
