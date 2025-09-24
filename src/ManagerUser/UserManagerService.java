package ManagerUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

/**
 * Classe de serviço para operações de CRUD na tabela de usuários
 * Implementa todas as funcionalidades de gerenciamento de usuários
 */
public class UserManagerService {

    /**
     * Cadastra um novo usuário no sistema
     * Verifica se CPF ou Email já existem antes de cadastrar
     */
    public boolean cadastrarUsuario(Usuario usuario, JTextArea outputArea) {
        if (!usuario.isValid()) {
            outputArea.append("❌ Todos os campos devem ser preenchidos!\n");
            return false;
        }

        // Verificar se CPF ou Email já existem
        if (verificarCpfExistente(usuario.getCpf(), outputArea)) {
            outputArea.append("❌ CPF já cadastrado no sistema!\n");
            return false;
        }

        if (verificarEmailExistente(usuario.getEmail(), outputArea)) {
            outputArea.append("❌ Email já cadastrado no sistema!\n");
            return false;
        }

        String sql = "INSERT INTO users (nome, cargo, cpf, email, senha) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCargo());
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getSenha());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Obter o ID gerado
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    usuario = new Usuario(generatedKeys.getInt(1), 
                                       usuario.getNome(), usuario.getCargo(), 
                                       usuario.getCpf(), usuario.getEmail(), 
                                       usuario.getSenha());
                }
                outputArea.append("✅ Usuário cadastrado com sucesso! ID: " + usuario.getId() + "\n");
                return true;
            }

        } catch (SQLException e) {
            outputArea.append("❌ Erro ao cadastrar usuário: " + e.getMessage() + "\n");
        }
        return false;
    }

    /**
     * Altera os dados de um usuário existente
     * Verifica conflitos de CPF e Email com outros usuários
     */
    public boolean alterarUsuario(Usuario usuario, JTextArea outputArea) {
        if (!usuario.isValid() || usuario.getId() <= 0) {
            outputArea.append("❌ Dados do usuário inválidos!\n");
            return false;
        }

        // Verificar se o usuário existe
        if (!verificarUsuarioExistente(usuario.getId(), outputArea)) {
            outputArea.append("❌ Usuário não encontrado!\n");
            return false;
        }

        // Verificar se CPF já existe em outro usuário
        if (verificarCpfExistenteOutroUsuario(usuario.getCpf(), usuario.getId(), outputArea)) {
            outputArea.append("❌ CPF já cadastrado para outro usuário!\n");
            return false;
        }

        // Verificar se Email já existe em outro usuário
        if (verificarEmailExistenteOutroUsuario(usuario.getEmail(), usuario.getId(), outputArea)) {
            outputArea.append("❌ Email já cadastrado para outro usuário!\n");
            return false;
        }

        String sql = "UPDATE users SET nome = ?, cargo = ?, cpf = ?, email = ?, senha = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCargo());
            stmt.setString(3, usuario.getCpf());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getSenha());
            stmt.setInt(6, usuario.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                outputArea.append("✅ Usuário alterado com sucesso!\n");
                return true;
            }

        } catch (SQLException e) {
            outputArea.append("❌ Erro ao alterar usuário: " + e.getMessage() + "\n");
        }
        return false;
    }

    /**
     * Exclui um usuário do sistema
     */
    public boolean excluirUsuario(int id, JTextArea outputArea) {
        // Verificar se o usuário existe
        if (!verificarUsuarioExistente(id, outputArea)) {
            outputArea.append("❌ Usuário não encontrado!\n");
            return false;
        }

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                outputArea.append("✅ Usuário excluído com sucesso!\n");
                return true;
            }

        } catch (SQLException e) {
            outputArea.append("❌ Erro ao excluir usuário: " + e.getMessage() + "\n");
        }
        return false;
    }

    /**
     * Pesquisa usuários por nome, email ou CPF
     */
    public List<Usuario> pesquisarUsuarios(String tipo, String termo, JTextArea outputArea) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "";

        switch (tipo.toUpperCase()) {
            case "NOME":
                sql = "SELECT * FROM users WHERE nome LIKE ? ORDER BY nome";
                break;
            case "EMAIL":
                sql = "SELECT * FROM users WHERE email LIKE ? ORDER BY nome";
                break;
            case "CPF":
                sql = "SELECT * FROM users WHERE cpf LIKE ? ORDER BY nome";
                break;
            default:
                outputArea.append("❌ Tipo de pesquisa inválido!\n");
                return usuarios;
        }

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + termo + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cargo"),
                    rs.getString("cpf"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            outputArea.append("❌ Erro na pesquisa: " + e.getMessage() + "\n");
        }

        return usuarios;
    }

    /**
     * Lista todos os usuários cadastrados
     */
    public List<Usuario> listarTodosUsuarios(JTextArea outputArea) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY nome";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cargo"),
                    rs.getString("cpf"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            outputArea.append("❌ Erro ao listar usuários: " + e.getMessage() + "\n");
        }

        return usuarios;
    }

    /**
     * Busca um usuário específico pelo ID
     */
    public Usuario buscarUsuarioPorId(int id, JTextArea outputArea) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cargo"),
                    rs.getString("cpf"),
                    rs.getString("email"),
                    rs.getString("senha")
                );
            }

        } catch (SQLException e) {
            outputArea.append("❌ Erro ao buscar usuário: " + e.getMessage() + "\n");
        }

        return null;
    }

    // Métodos auxiliares para verificação de existência
    private boolean verificarCpfExistente(String cpf, JTextArea outputArea) {
        return verificarExistencia("cpf", cpf, outputArea);
    }

    private boolean verificarEmailExistente(String email, JTextArea outputArea) {
        return verificarExistencia("email", email, outputArea);
    }

    private boolean verificarCpfExistenteOutroUsuario(String cpf, int idUsuario, JTextArea outputArea) {
        return verificarExistenciaOutroUsuario("cpf", cpf, idUsuario, outputArea);
    }

    private boolean verificarEmailExistenteOutroUsuario(String email, int idUsuario, JTextArea outputArea) {
        return verificarExistenciaOutroUsuario("email", email, idUsuario, outputArea);
    }

    private boolean verificarExistencia(String campo, String valor, JTextArea outputArea) {
        String sql = "SELECT id FROM users WHERE " + campo + " = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            outputArea.append("❌ Erro ao verificar " + campo + ": " + e.getMessage() + "\n");
        }
        return false;
    }

    private boolean verificarExistenciaOutroUsuario(String campo, String valor, int idUsuario, JTextArea outputArea) {
        String sql = "SELECT id FROM users WHERE " + campo + " = ? AND id != ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, valor);
            stmt.setInt(2, idUsuario);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            outputArea.append("❌ Erro ao verificar " + campo + ": " + e.getMessage() + "\n");
        }
        return false;
    }

    private boolean verificarUsuarioExistente(int id, JTextArea outputArea) {
        String sql = "SELECT id FROM users WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            outputArea.append("❌ Erro ao verificar usuário: " + e.getMessage() + "\n");
        }
        return false;
    }
}
