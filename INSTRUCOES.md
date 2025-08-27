# 🧪 PROSEFA - Instruções de Execução

## Programa de Selos Fiscais de Alta Segurança

Este documento contém todas as instruções necessárias para executar e testar a aplicação PROSEFA localmente.

---

## 📋 Pré-requisitos

- **Java 21+** instalado
- **Maven 3.9+**
- **IDE** (IntelliJ IDEA, Eclipse, VS Code)

---

## 🚀 Como Rodar a Aplicação Localmente

### 1. **Clone o Repositório**

```bash
git clone https://github.com/seu-usuario/prosefa-challenge.git
cd prosefa-challenge
```

### 2. **Executar a Aplicação**

#### Via Maven:

```bash
mvn spring-boot:run
```

#### Via IDE:

1. Faça o build do projecto.
2. Execute a classe `BackEndTestApplication.java`

### 4. **Verificar se Está Funcionando**

A aplicação estará disponível em: **http://localhost:8080**

#### Endpoints de Verificação:

- **Health Check**: `GET http://localhost:8080/actuator/health`
- **H2 Console** (se usando H2): `http://localhost:8080/h2-console`
- **Swagger UI** (se configurado): `http://localhost:8080/swagger-ui.html`

---

## 📱 Exemplos de Chamadas da API

### **Base URL**: `http://localhost:8080/api`

---

## 1. 🏢 Gestão de Empresas

### **1.1 Registrar Empresa**

```bash
# cURL
curl -X POST http://localhost:8080/api/empresas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Empresa ABC Lda",
    "nif": "123456789",
    "tipo": "FABRICANTE"
  }'
```

**Resposta (201 Created):**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "nome": "Empresa ABC Lda",
  "nif": "123456789",
  "tipo": "FABRICANTE",
  "status": "ATIVA",
  "dataRegistro": "2025-08-27T14:30:00"
}
```

### **1.2 Listar Empresas**

```bash
# cURL
curl -X GET http://localhost:8080/api/empresas
```

### **1.3 Buscar Empresa por ID**

```bash
# cURL
curl -X GET http://localhost:8080/api/empresas/550e8400-e29b-41d4-a716-446655440000
```

### **1.4 Verificar Status de Bloqueio**

```bash
# cURL
curl -X GET http://localhost:8080/api/empresas/550e8400-e29b-41d4-a716-446655440000/status-bloqueio
```

**Resposta:**

```json
{
  "empresaId": "550e8400-e29b-41d4-a716-446655440000",
  "bloqueada": false,
  "selosVencidos": 0,
  "totalSelosNaoValidados": 2,
  "dataSeloMaisAntigo": null,
  "diasLimite": 30,
  "mensagem": "Empresa liberada para solicitar selos"
}
```

---

## 2. 📋 Solicitação de Selos

### **2.1 Solicitar Selos**

```bash
# cURL
curl -X POST http://localhost:8080/api/selos/solicitar \
  -H "Content-Type: application/json" \
  -d '{
    "empresaId": "550e8400-e29b-41d4-a716-446655440000",
    "produto": "Whisky Premium 750ml",
    "quantidade": 100
  }'
```

**Resposta (201 Created):**

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "empresaId": "550e8400-e29b-41d4-a716-446655440000",
  "nomeEmpresa": "Empresa ABC Lda",
  "produto": "Whisky Premium 750ml",
  "quantidade": 100,
  "status": "PENDENTE",
  "dataSolicitacao": "2025-08-27T14:35:00",
  "dataProcessamento": null
}
```

### **2.2 Listar Solicitações**

```bash
# cURL
curl -X GET http://localhost:8080/api/selos/solicitacoes
```

---

## 3. 🏷️ Emissão e Validação de Selos

### **3.1 Emitir Selos (Aprovar Solicitação)**

```bash
# cURL
curl -X POST http://localhost:8080/api/selos/solicitacoes/123e4567-e89b-12d3-a456-426614174000/emitir
```

**Resposta (200 OK):**

```json
[
  {
    "id": "111e1111-e11b-11d3-a111-111111111111",
    "codigo": "PROSEFA-2025-000001",
    "empresaId": "550e8400-e29b-41d4-a716-446655440000",
    "nomeEmpresa": "Empresa ABC Lda",
    "produto": "Whisky Premium 750ml",
    "solicitacaoId": "123e4567-e89b-12d3-a456-426614174000",
    "dataEmissao": "2025-08-27T14:40:00",
    "estado": "EMITIDO"
  },
  {
    "id": "222e2222-e22b-22d3-a222-222222222222",
    "codigo": "PROSEFA-2025-000002",
    "empresaId": "550e8400-e29b-41d4-a716-446655440000",
    "nomeEmpresa": "Empresa ABC Lda",
    "produto": "Whisky Premium 750ml",
    "solicitacaoId": "123e4567-e89b-12d3-a456-426614174000",
    "dataEmissao": "2025-08-27T14:40:00",
    "estado": "EMITIDO"
  }
]
```

### **3.2 Validar Selo Individual**

```bash
# cURL
curl -X PUT http://localhost:8080/api/selos/111e1111-e11b-11d3-a111-111111111111/validar
```

**Resposta (200 OK):**

```json
{
  "id": "111e1111-e11b-11d3-a111-111111111111",
  "codigo": "PROSEFA-2025-000001",
  "empresaId": "550e8400-e29b-41d4-a716-446655440000",
  "nomeEmpresa": "Empresa ABC Lda",
  "produto": "Whisky Premium 750ml",
  "solicitacaoId": "123e4567-e89b-12d3-a456-426614174000",
  "dataEmissao": "2025-08-27T14:40:00",
  "estado": "VALIDADO"
}
```

### **3.3 Buscar Selo por ID**

```bash
# cURL
curl -X GET http://localhost:8080/api/selos/111e1111-e11b-11d3-a111-111111111111
```

### **3.4 Listar Selos por Empresa**

```bash
# cURL
curl -X GET http://localhost:8080/api/selos/empresa/550e8400-e29b-41d4-a716-446655440000
```

---

## 4. 📊 Logs de Auditoria

### **4.1 Listar Todos os Logs**

```bash
# cURL
curl -X GET http://localhost:8080/api/auditoria
```

### **4.2 Filtrar Logs por Entidade**

```bash
# cURL
curl -X GET "http://localhost:8080/api/auditoria?entidade=SeloFiscal"
```

### **4.3 Filtrar Logs por Ação**

```bash
# cURL
curl -X GET "http://localhost:8080/api/auditoria?acao=SELO_VALIDADO"
```

### **4.4 Filtrar Logs por Período**

```bash
# cURL
curl -X GET "http://localhost:8080/api/auditoria?inicio=2025-08-27T00:00:00&fim=2025-08-27T23:59:59"
```

**Resposta:**

```json
[
  {
    "id": "333e3333-e33b-33d3-a333-333333333333",
    "entidade": "SeloFiscal",
    "acao": "SELO_VALIDADO",
    "usuario": "admin",
    "dataHora": "2025-08-27T14:45:00",
    "detalhes": "{\"seloId\":\"111e1111-e11b-11d3-a111-111111111111\",\"codigo\":\"PROSEFA-2025-000001\",\"produto\":\"Whisky Premium 750ml\"}"
  }
]
```

---

## 🧪 Cenários de Teste Completos

### **Cenário 1: Fluxo Completo de Sucesso**

```bash
# 1. Registrar empresa
curl -X POST http://localhost:8080/api/empresas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Destilaria Premium Lda",
    "nif": "987654321",
    "tipo": "FABRICANTE"
  }'

# 2. Solicitar selos (use o ID retornado acima)
curl -X POST http://localhost:8080/api/selos/solicitar \
  -H "Content-Type: application/json" \
  -d '{
    "empresaId": "SEU_EMPRESA_ID_AQUI",
    "produto": "Vodka Premium 1L",
    "quantidade": 50
  }'

# 3. Emitir selos (use o ID da solicitação)
curl -X POST http://localhost:8080/api/selos/solicitacoes/SEU_SOLICITACAO_ID_AQUI/emitir

# 4. Validar um selo (use o ID do selo)
curl -X PUT http://localhost:8080/api/selos/SEU_SELO_ID_AQUI/validar

# 5. Verificar logs de auditoria
curl -X GET http://localhost:8080/api/auditoria
```

### **Cenário 2: Teste de Bloqueio por Selos Vencidos**

```bash
# 1. Criar empresa
curl -X POST http://localhost:8080/api/empresas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Empresa Teste Bloqueio",
    "nif": "111222333",
    "tipo": "IMPORTADOR"
  }'

# 2. Verificar status inicial (não bloqueada)
curl -X GET http://localhost:8080/api/empresas/SEU_EMPRESA_ID/status-bloqueio

# 3. Para testar o bloqueio, você precisa:
#    - Ter selos emitidos há mais de 30 dias não validados
#    - Ou alterar a configuração: prosefa.bloqueio.dias-limite=1
```

### **Cenário 3: Teste de Validações**

```bash
# 1. Tentar registrar empresa com NIF duplicado
curl -X POST http://localhost:8080/api/empresas \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Empresa Duplicada",
    "nif": "123456789",
    "tipo": "FABRICANTE"
  }'
# Esperado: 400 Bad Request

# 2. Tentar validar selo já validado
curl -X PUT http://localhost:8080/api/selos/SELO_JA_VALIDADO_ID/validar
# Esperado: 400 Bad Request

# 3. Tentar emitir selos para solicitação já processada
curl -X POST http://localhost:8080/api/selos/solicitacoes/SOLICITACAO_JA_PROCESSADA_ID/emitir
# Esperado: 400 Bad Request
```

---

## 🔧 Configurações Importantes

### **application.properties**

```properties
# Configuração do servidor
server.port=8080

# Configuração do banco
spring.datasource.url=jdbc:h2:mem:prosefa
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Configuração específica do PROSEFA
prosefa.bloqueio.dias-limite=30

# Logs
logging.level.com.prosefa=DEBUG
logging.level.org.springframework.web=DEBUG
```

### **Para Testes**

```properties
# application-test.properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
prosefa.bloqueio.dias-limite=7
logging.level.com.prosefa=INFO
```

---

## 🐛 Troubleshooting

### **Problema: Aplicação não inicia**

- Verifique se o Java 17+ está instalado: `java -version`
- Verifique se a porta 8080 está livre: `netstat -an | grep 8080`

### **Problema: Erro de conexão com banco**

- Se usando PostgreSQL: verifique se está rodando e acessível
- Se usando H2: verifique o console em `http://localhost:8080/h2-console`

### **Problema: Endpoints retornam 404**

- Verifique se a aplicação iniciou completamente
- Confirme se está usando a URL correta: `http://localhost:8080/api/`

### **Problema: Erro de validação**

- Verifique se os dados JSON estão no formato correto
- Confirme se os UUIDs existem no banco de dados

---

## 🧪 Executar Testes

```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=EmpresaServiceTest

# Executar com perfil de teste
mvn test -Dspring.profiles.active=test

# Com relatório de cobertura
mvn test jacoco:report
```

---

## 📚 Recursos Adicionais

- **Documentação Spring Boot**: https://spring.io/projects/spring-boot
- **Postman Collections**: Importe as chamadas acima como collection
- **H2 Database Console**: http://localhost:8080/h2-console (user: `sa`, password: vazio)

---

## 🎯 Próximos Passos

1. **Autenticação**: Implementar JWT para endpoints protegidos
2. **Swagger**: Adicionar documentação automática da API
3. **Docker**: Containerizar a aplicação
4. **CI/CD**: Configurar pipeline de deploy
5. **Monitoring**: Adicionar métricas e health checks

---

**Boa sorte com o teste! 🚀**

Em caso de dúvidas, consulte os logs da aplicação ou entre em contato com a equipe técnica.