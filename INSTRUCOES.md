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
git clone  https://github.com/blackbomb404/teste-backend-statement-labs/pull/new/teste-leonel-rocha-nascimento
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

A aplicação estará disponível em: **http://localhost:8080
#### Endpoints de Verificação:

- **H2 Console**: `http://localhost:8080/h2-console`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`

---

## 📱 Exemplos de Chamadas da API

---

## 1. 🏢 Gestão de Empresas

### **1.1 Registrar Empresa**

```bash
# cURL
curl -X POST http://localhost:8080/api/v1/empresas \
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
	"id": "f4ffb79a-af07-490e-8fa3-8eb177bd326d",
	"nome": "Empresa ABC Lda",
	"nif": "123456789",
	"tipo": "FABRICANTE",
	"status": "ATIVA",
	"dataRegistro": "2025-08-27T19:18:23.679250426"
}
```

---

## 2. 📋 Solicitação de Selos

### **2.1 Solicitar Selos**

```bash
# cURL
curl -X POST http://localhost:8080/api/v1/solicitacoes \
  -H "Content-Type: application/json" \
  -d '{
    "idEmpresa": "f4ffb79a-af07-490e-8fa3-8eb177bd326d",
    "produto": "Whisky Premium 750ml",
  }'
```

**Resposta (201 Created):**

```json
{
	"idSolicitacao": "04880841-6ed0-41ed-9d3d-b10165bf37ce",
	"idEmpresa": "f4ffb79a-af07-490e-8fa3-8eb177bd326d",
	"produto": "Whisky Premium 750ml",
	"status": "PENDENTE",
	"dataSolicitacao": "2025-08-27T19:23:52.348453491"
}
```

---

## 3. 🏷️ Emissão e Validação de Selos

### **3.1 Emitir Selo (Aprovar Solicitação)**

```bash
# cURL
curl -X 'POST' \
	'http://localhost:8080/api/v1/selos' \
	-H 'accept: */*' \
	-H 'Content-Type: application/json' \
	-d '{
		"idSolicitacao": "04880841-6ed0-41ed-9d3d-b10165bf37ce"
}'
```

**Resposta (201 Created):**

```json
{
	"id": "d94b0f73-bc2f-4fb0-8635-93bdfee1a4e1",
	"codigo": "PROSEFA-2025-000001",
	"empresaId": "f4ffb79a-af07-490e-8fa3-8eb177bd326d",
	"nomeEmpresa": "Leonel Lda",
	"produto": "Whisky Premium 750ml",
	"dataEmissao": "2025-08-27T19:25:51.45987368",
	"estado": "EMITIDO"
}
```

### **3.2 Validar Selo Individual**

```bash
# cURL
curl -X 'PUT' \
	'http://localhost:8080/api/v1/selos/{id}?id=d94b0f73-bc2f-4fb0-8635-93bdfee1a4e1' \
	-H 'accept: */*'
```

**Resposta (200 OK):**

```json
{
	"id": "d94b0f73-bc2f-4fb0-8635-93bdfee1a4e1",
	"codigo": "PROSEFA-2025-000001",
	"empresaId": "f4ffb79a-af07-490e-8fa3-8eb177bd326d",
	"nomeEmpresa": "Leonel Lda",
	"produto": "string",
	"dataEmissao": "2025-08-27T19:25:51.459874",
	"estado": "VALIDADO"
}
```

### **3.3 Listar Selos por Empresa** (com paginação)

```bash
# cURL
curl -X 'GET' \
	'http://localhost:8080/api/v1/selos/{idEmpresa}?idEmpresa=f4ffb79a-af07-490e-8fa3-8eb177bd326d&pageNumber=0&pageCapacity=5' \
	-H 'accept: */*'{
	"id": "d94b0f73-bc2f-4fb0-8635-93bdfee1a4e1",
	"codigo": "PROSEFA-2025-000001",
	"empresaId": "f4ffb79a-af07-490e-8fa3-8eb177bd326d",
	"nomeEmpresa": "Leonel Lda",
	"produto": "string",
	"dataEmissao": "2025-08-27T19:25:51.459874",
	"estado": "VALIDADO"
}
```

**Resposta (200 OK):**

```json
{
	"content": [ 
		{
			 "id": "d94b0f73-bc2f-4fb0-8635-93bdfee1a4e1",
			 "codigo": "PROSEFA-2025-000001",
			 "empresaId": "f4ffb79a-af07-490e-8fa3-8eb177bd326d",
			 "nomeEmpresa": "Leonel Lda",
			 "produto": "string",
			 "dataEmissao": "2025-08-27T19:25:51.459874",
			 "estado": "VALIDADO"
		}
	],
	"pageable": {
		"pageNumber": 0,
		"pageSize": 5,
		"sort": {
			"sorted": false,
			"empty": true,
			"unsorted": true
		},
		"offset": 0,
		"paged": true,
		"unpaged": false
		},
		"first": true,
		"last": true,
		"size": 5,
		"number": 0,
		"sort": {
			"sorted": false,
			"empty": true,
			"unsorted": true
		},
		"numberOfElements": 1,
		"empty": false
	}
```

---

## 4. 📊 Logs de Auditoria

### **4.1 Listar Todos os Logs** (com paginação)

```bash
# cURL
curl -X 'GET' \
	'http://localhost:8080/api/v1/logs?pageNumber=0&pageCapacity=5' \
	-H 'accept: */*'
```

**Resposta (200 OK):**

```json
{
	"content": [
		{
			"id": "809c40de-a7ad-499a-b3af-bee0e5fcf05f",
			"entidade": "Empresa",
			"accao": "EMPRESA_CRIADA",
			"usuario": "sistema",
			"dataHora": "2025-08-27T19:15:15.646217",
			"detalhes": {
				"nomeEmpresa": "string",
				"idEmpresa": "fbe1e318-638c-4ed3-94c9-a18443d07685",
				"tipo": "FABRICANTE",
				"nif": "string"
			}
		}, {
			"id": "d7e24742-dce4-46f9-a5e6-9fcadbee1aad",
			"entidade": "Empresa",
			"accao": "EMPRESA_CRIADA",
			"usuario": "sistema",
			"dataHora": "2025-08-27T19:18:23.680077",
			"detalhes": {
				"nomeEmpresa": "Leonel Lda",
				"idEmpresa": "f4ffb79a-af07-490e-8fa3-8eb177bd326d",
				"tipo": "FABRICANTE",
				"nif": "stringa"
			}
		}, {
			"id": "e0b23e48-5d8c-41e6-820a-015e7702730d",
			"entidade": "SolicitacaoSeloFiscal",
			"accao": "SOLICITACAO_CRIADA",
			"usuario": "sistema",
			"dataHora": "2025-08-27T19:23:52.351229",
			"detalhes": {
				"nomeEmpresa": "Leonel Lda",
				"idEmpresa": "f4ffb79a-af07-490e-8fa3-8eb177bd326d",
				"dataSolicitacao": "2025-08-27T19:23:52.348453491",
				"produto": "string"
			}
		}, {
			"id": "e115f187-ed80-4f22-bd45-3f6d0f9cc3e0",
			"entidade": "SeloFiscal",
			"accao": "SELO_EMITIDO",
			"usuario": "sistema",
			"dataHora": "2025-08-27T19:25:51.466289",
			"detalhes": {
				"produto": "string",
				"nomeEmpresa": "Leonel Lda",
				"idSolicitacao": "04880841-6ed0-41ed-9d3d-b10165bf37ce",
				"codigoSelo": "PROSEFA-2025-000001",
				"idSelo": "d94b0f73-bc2f-4fb0-8635-93bdfee1a4e1"
			}
		}, {
			"id": "161f46a7-6c99-4319-9eb3-5424091410dd",
			"entidade": "SeloFiscal",
			"accao": "SELO_VALIDADO",
			"usuario": "sistema",
			"dataHora": "2025-08-27T19:30:21.823758",
			"detalhes": {
				"idSelo": "d94b0f73-bc2f-4fb0-8635-93bdfee1a4e1",
				"nomeEmpresa": "Leonel Lda",
				"produto": "string"
			}
		}
	],
	"pageable": {
		"pageNumber": 0,
		"pageSize": 5,
		"sort": {
			"sorted": false,
			"empty": true,
			"unsorted": true
		},
		"offset": 0,
		"paged": true,
		"unpaged": false
	},
	"last": true,
	"totalElements": 5,
	"totalPages": 1,
	"first": true,
	"size": 5,
	"number": 0,
	"sort": {
		"sorted": false,
		"empty": true,
		"unsorted": true
	},
	"numberOfElements": 5,
	"empty": false
}
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