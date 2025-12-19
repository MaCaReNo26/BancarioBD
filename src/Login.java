import javax.swing.*;
import java.sql.*;

public class Login extends JFrame {
    private JPanel Login_pa;
    private JTextField txtUsuario;
    private JTextField txtContraseña;
    private JButton btnIngresar;

    public Login() {
        setTitle("Inicio de Sesion");
        setContentPane(Login_pa);
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        btnIngresar.addActionListener(e -> init());
    }

    public void init() {
        String usuario = txtUsuario.getText();
        String contra = txtContraseña.getText();

        try {
            Connection con = ConexionDB.getConexion();

            //Consultar usuario
            String sql = """
                SELECT id, password, saldo, activo, intentos
                FROM usuarios
                WHERE username = ?
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Usuario no registrado");
                return;
            }

            int idUsuario = rs.getInt("id");
            boolean activo = rs.getBoolean("activo");
            int intentos = rs.getInt("intentos");
            String passBD = rs.getString("password");
            double saldo = rs.getDouble("saldo");

            if (!activo) {
                JOptionPane.showMessageDialog(this, "Usuario bloqueado");
                return;
            }

            //Contraseña incorrecta
            if (!passBD.equals(contra)) {
                intentos++;

                String update = """
                    UPDATE usuarios
                    SET intentos = ?, activo = ?
                    WHERE id = ?
                """;

                PreparedStatement ups = con.prepareStatement(update);
                ups.setInt(1, intentos);
                ups.setBoolean(2, intentos < 3);
                ups.setInt(3, idUsuario);
                ups.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Contraseña incorrecta. Intento " + intentos + " de 3"
                );
                return;
            }

            //Cargar sesión
            Sesion.idUsuario = idUsuario;
            Sesion.username = usuario;
            Sesion.saldo = saldo;

            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
            dispose();
            new BancoForm();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión con la base de datos");
            e.printStackTrace();
        }
    }
}

