import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {
    private static BookManager bookManager = new BookManager();
    private static Admin admin = new Admin(bookManager);

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sistema de Gerenciamento de Livros");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 350);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        // Campos de entrada
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField yearField = new JTextField();

        panel.add(new JLabel("Título:"));
        panel.add(titleField);
        panel.add(new JLabel("Autor:"));
        panel.add(authorField);
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        panel.add(new JLabel("Ano:"));
        panel.add(yearField);

        // Área de exibição de mensagens
        JTextArea outputArea = new JTextArea(5, 30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Botões
        JButton addButton = new JButton("Cadastrar");
        JButton updateButton = new JButton("Atualizar");
        JButton deleteButton = new JButton("Excluir");
        JButton displayButton = new JButton("Exibir");

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(displayButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Ações dos botões
        addButton.addActionListener(e -> {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                String isbn = isbnField.getText();
                int year = Integer.parseInt(yearField.getText());
                admin.addBook(new Book(title, author, isbn, year));
                outputArea.setText("Livro cadastrado com sucesso!");
            } catch (Exception ex) {
                outputArea.setText("Erro ao cadastrar livro. Verifique os campos.");
            }
        });

        updateButton.addActionListener(e -> {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                String isbn = isbnField.getText();
                int year = Integer.parseInt(yearField.getText());
                admin.updateBook(isbn, new Book(title, author, isbn, year));
                outputArea.setText("Livro atualizado (se encontrado).");
            } catch (Exception ex) {
                outputArea.setText("Erro ao atualizar livro. Verifique os campos.");
            }
        });

        deleteButton.addActionListener(e -> {
            String isbn = isbnField.getText();
            admin.deleteBook(isbn);
            outputArea.setText("Livro excluído (se encontrado).");
        });

        displayButton.addActionListener(e -> {
            String isbn = isbnField.getText();
            Book book = bookManager.getBook(isbn);
            if (book != null) {
                outputArea.setText("Título: " + book.getTitle() +
                                   "\nAutor: " + book.getAuthor() +
                                   "\nISBN: " + book.getIsbn() +
                                   "\nAno: " + book.getYear());
            } else {
                outputArea.setText("Livro não encontrado!");
            }
        });

        frame.setVisible(true);
    }
}