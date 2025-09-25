# SistemaBiblioTech

📚 Sistema BiblioTech

O Sistema BiblioTech é um conjunto de aplicações em Java 21 para gerenciar uma biblioteca escolar.
O projeto foi desenvolvido como parte de um trabalho de faculdade, utilizando banco de dados hospedado na Aiven (MySQL gerenciado em nuvem).

O sistema está dividido em três módulos principais:

Gerenciamento de Usuários (ManagerUser/) → Cadastro, edição e exclusão de usuários.

Sistema de Reservas (User/) → Reserva de livros pelos usuários cadastrados.

Gerenciamento de Livros (book-management-system/) → Cadastro, consulta, atualização e remoção de livros.

📂 Estrutura do Projeto
Sistema BiblioTech/

│── lib/

│   └── mysql-connector-j-9.4.0.jar   # Driver JDBC do MySQL

│── src/

│   │── bin/                          # Arquivos compilados

│   │── ManagerUser/                  # Sistema de Gerenciamento de Usuários - GUI

│   │   ├── ...

│   │── User/                         # Sistema de Reservas - GUI

│   │   ├── ...

│   │── book-management-system/       # Sistema de Gerenciamento de Livros - GUI

│   │   ├── ...

⚙️ Tecnologias Utilizadas

Java 21 (JDK 21)

MySQL (Aiven - banco de dados em nuvem)

JDBC (MySQL Connector mysql-connector-j-9.4.0.jar)

Swing (Interface gráfica)

📌 Pré-requisitos

Java JDK 21

Conta no Aiven
 com instância MySQL configurada

Conector JDBC (mysql-connector-j-9.4.0.jar) dentro da pasta lib/

🔧 Configuração da Conexão no Database.java

No arquivo Database.java, configure os dados de conexão fornecidos pela Aiven:

public class Database {
    private static final String URL = "jdbc:mysql://bibliotech-project.aivencloud.com:12345/bibliotech?sslmode=require";
    private static final String USER = "avnadmin";
    private static final String PASSWORD = "senhaSegura123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}


➡️ Explicação dos campos (exemplo fictício):

Host: bibliotech-project.aivencloud.com

Porta: 12345

Database: bibliotech

Usuário: avnadmin

Senha: senhaSegura123

SSL: ?sslmode=require (obrigatório para conexões Aiven)

Os dados reais devem ser copiados diretamente do painel da Aiven.

📊 Estrutura Básica do Banco de Dados
CREATE DATABASE bibliotech;

USE bibliotech;

-- Usuários
CREATE TABLE user (
    "id INT AUTO_INCREMENT PRIMARY KEY, " +
    "nome VARCHAR(100) NOT NULL, " +
    "cargo VARCHAR(50) NOT NULL, " +
    "cpf VARCHAR(14) UNIQUE NOT NULL, " +
    "email VARCHAR(100) UNIQUE NOT NULL, " +
    "senha VARCHAR(100) NOT NULL, " +
    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
);

-- Livros
CREATE TABLE books (
    "id INT AUTO_INCREMENT PRIMARY KEY, " +
    "title VARCHAR(255) NOT NULL, " +
    "author VARCHAR(255) NOT NULL, " +
    "isbn VARCHAR(20) NOT NULL UNIQUE, " +
    "year VARCHAR(10) NOT NULL, " +
    "quantidade_total INT NOT NULL DEFAULT 1, " +
    "quantidade_disponivel INT NOT NULL DEFAULT 1, " +
    "reservados INT NOT NULL DEFAULT 0, " +
    "todos_reservados BOOLEAN NOT NULL DEFAULT FALSE, " +
    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
);

-- Histórico de reservas/empréstimos
CREATE TABLE books_history (
   "id INT AUTO_INCREMENT PRIMARY KEY, " +
   "user_id INT NOT NULL, " +
   "user_nome VARCHAR(100) NOT NULL, " +
   "user_email VARCHAR(100) NOT NULL, " +
   "book_id INT NOT NULL, " +
   "book_titulo VARCHAR(255) NOT NULL, " +
   "book_isbn VARCHAR(20) NOT NULL, " +
   "acao VARCHAR(20) NOT NULL, " + // RESERVA ou DEVOLUCAO
   "data_acao TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
   "FOREIGN KEY (user_id) REFERENCES users(id))";
);

▶️ Como Compilar e Executar
1. Compilar (exemplo ManagerUser)
javac -cp "lib/mysql-connector-j-9.4.0.jar" -d bin src/ManagerUser/*.java

2. Executar diretamente
java -cp "bin;lib/mysql-connector-j-9.4.0.jar" ManagerUser.App


(No Linux/Mac, troque ; por : no classpath.)


📦 Executáveis .jar

Basta dar duplo clique em cada .jar para abrir o sistema, sem precisar do terminal.


📖 Funcionalidades

👤 Gerenciamento de Usuários

Cadastro de novos usuários

Edição e exclusão de usuários

Autenticação de login

📚 Gerenciamento de Livros

Cadastro e remoção de livros

Atualização de informações

Pesquisa por título, autor ou ISBN

📑 Sistema de Reservas / Histórico

Reserva de livros por usuários cadastrados

Registro em books_history de todas as ações realizadas

Consulta de reservas e histórico de empréstimos

🚀 Próximos Passos

Implementar relatórios de livros emprestados e reservas pendentes.

Adicionar controle de permissões (usuário comum x administrador).

Melhorar a interface gráfica.
javac -cp ".;lib/mysql-connector-j-9.4.0.jar" src/ManagerUser/*.java

2. Executar a classe principal
java -cp ".;lib/mysql-connector-j-9.4.0.jar;src" ManagerUser.App


(No Linux/Mac, troque ; por : no classpath.)
