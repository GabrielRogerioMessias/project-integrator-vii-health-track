# HealthTracker - Gestão Integrada de Doenças Crônicas - Em desenvolvimento

 ![Logo do HealthTracker](assets/logo.jpeg)
 
## 📋 Introdução  
O **HealthTracker** é uma plataforma digital focada no gerenciamento de doenças crônicas (como diabetes, hipertensão e obesidade). Nosso objetivo é conectar pacientes e profissionais de saúde em um ecossistema seguro, oferecendo:  
- **Monitoramento contínuo** de indicadores de saúde (glicemia, pressão arterial, etc.).  
- **Comunicação facilitada** entre médicos e pacientes.  
- **Ferramentas clínicas** para análise de dados e personalização de tratamentos.  

Ideal para clínicas, hospitais e usuários individuais que buscam praticidade e precisão no cuidado diário.  

---

## ✨ Funcionalidades Principais  

### 👤 **Para Pacientes**  
- Cadastro de perfil com dados do paciente.  
- Registro de medições e sintomas em tempo real.  
- Busca por médicos por especialidade ou localização. 

### 🩺 **Para Médicos**  
- Cadastro próprio e gestão de pacientes vinculados.  
- Visualização dos pacientes e dados.  
- Busca avançada por pacientes (filtros por casos e gêneros)

---

## 🖥 Telas do Sistema  

### 1. **Login/Cadastro**  
- **Login**: Autenticação por e-mail e senha.  
- **Cadastro**:  
  - Paciente: Nome, e-mail, tipo de doença crônica, data de diagnóstico.  
  - Médico: Nome, CRM validado, especialidade, e-mail.  
- **Design**: Layout intuitivo com validação em tempo real.  

### 2. **Dashboard Médico**  
- **Lista de Pacientes**: Tabela com colunas personalizáveis (nome, última medição, status).  
- **Alertas**: Destaque para pacientes com valores fora da meta.  
- **Ações Rápidas**: Cadastrar paciente, exportar dados, filtrar por urgência.  

### 3. **Listagem de Médicos**  
- Tabela interativa com:  
  - Nome, CRM, especialidade, status (Ativo/Inativo).  
  - Filtros por nome, CRM ou data de cadastro.
  - Descrição.  
- **Status Visual**: Ícones coloridos (🔴 Inativo / 🟢 Ativo).  

### 4. **Confirmação de Exclusão**  
- Modal de segurança com:  
  - Confirmação por senha.  
  - Mensagem de registro em log: *"Esta ação será auditada."*  
  - Opções: "Cancelar" ou "Confirmar Exclusão".  

---

## 🛠 Tecnologias Utilizadas  
- **Frontend**: Angular, Figma (UI/UX).  
- **Backend**: Java, MySQL.  
- **Documentação da API**: Swagger



# Configure as variáveis de ambiente  
cp .env.example .env  

# Inicie o servidor  
npm run dev  
