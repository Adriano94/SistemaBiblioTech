# Sistema de Gerenciamento de Livros - BibliotecaJava

Este projeto é um sistema simples de gerenciamento de livros desenvolvido em Java, com interface de console. Ele permite que um administrador cadastre, altere, exclua e visualize informações de livros.

## Estrutura dos Arquivos

- **Book.java**  
  Define a classe `Book`, que representa um livro com os atributos: título, autor, ISBN e ano. Possui métodos para acessar e modificar esses dados, além de exibir as informações do livro.

- **BookManager.java**  
  Gerencia uma lista de livros. Permite adicionar, atualizar, remover e buscar livros pelo ISBN. Todos os métodos de manipulação de livros estão centralizados nesta classe.

- **Admin.java**  
  Representa o administrador do sistema. Utiliza os métodos do `BookManager` para realizar as operações de cadastro, atualização, exclusão e exibição de livros.

- **Main.java**  
  Contém o método principal (`main`) e uma interface de console para interação com o usuário. Permite escolher as opções de gerenciamento de livros e executa as ações conforme a escolha.

## Como Executar

1. **Compile os arquivos Java**  
   No terminal, navegue até a pasta `src` e execute:
   ```
   javac *.java
   ```

2. **Execute o programa principal**  
   ```
   java Main
   ```

3. **Utilize o menu exibido no console**  
   Escolha as opções para cadastrar, atualizar, excluir ou exibir livros conforme desejar.

## Observações

- O sistema utiliza o ISBN para identificar cada livro.
- Todas as operações são feitas em memória (não há persistência em arquivos ou banco de dados).
- O código é didático e pode ser expandido para novas funcionalidades conforme necessário.