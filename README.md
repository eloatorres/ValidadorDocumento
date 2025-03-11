# Validador de CPF e RG - Autômato

Este projeto implementa um **autômato** para validar documentos de **CPF** e **RG**. Ele verifica tanto o formato dos documentos quanto a lógica dos dígitos verificadores para garantir que o documento seja válido.

## Funcionalidade

O projeto permite a validação de **CPF** e **RG** através de um serviço em **Spring**. Ele utiliza autômatos finitos para validar:

- O **formato** dos documentos.
- A **lógica** dos dígitos verificadores.

### Tipos de Documento Suportados

- **CPF**: Validação do formato (com ou sem pontuação) e dos dois dígitos verificadores, conforme o padrão nacional.
- **RG**: Validação do formato (com ou sem pontuação) e do dígito verificador, conforme o padrão nacional.

## Como Usar

1. **Configuração**: Este projeto utiliza o framework **Spring**. Para rodá-lo, basta garantir que o Spring esteja configurado corretamente no seu ambiente.
   
2. **Método de Validação**:
    - O serviço expõe um método chamado `run` que recebe o **documento** e o **tipo** (`cpf` ou `rg`) e retorna `true` se o documento for válido e `false` caso contrário.

    - Exemplo de uso:

    ```java
    ValidadorDocumento validador = new ValidadorDocumento();
    boolean isValidCpf = validador.run("123.456.789-09", "cpf");
    boolean isValidRg = validador.run("12.345.678-9", "rg");
    ```

## Estrutura do Código

### Validação de CPF

A validação do **CPF** envolve duas etapas principais:
1. Verificação do formato correto (XXX.XXX.XXX-XX), com pontos e hífen.
2. Cálculo dos dois dígitos verificadores com base em um algoritmo específico.

### Validação de RG

A validação do **RG** também envolve duas etapas:
1. Verificação do formato correto (XX.XXX.XXX-X), com pontos e hífen.
2. Cálculo do dígito verificador utilizando uma soma ponderada.

## Tecnologias Utilizadas

- **Java** (Linguagem de Programação)
- **Spring Framework** (Framework)


