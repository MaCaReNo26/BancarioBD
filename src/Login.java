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

            String sql = """
                SELECT password, activo, intentos
                FROM usuarios_cliente
                WHERE username = ?
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Usuario no registrado");
                return;
            }

            boolean activo = rs.getBoolean("activo");
            int intentos = rs.getInt("intentos");
            String passBD = rs.getString("password");

            if (!activo) {
                JOptionPane.showMessageDialog(this, "Usuario bloqueado");
                return;
            }

            if (!passBD.equals(contra)) {
                intentos++;

                String update = """
                    UPDATE usuarios_cliente
                    SET intentos = ?, activo = ?
                    WHERE username = ?
                """;

                PreparedStatement ups = con.prepareStatement(update);
                ups.setInt(1, intentos);
                ups.setBoolean(2, intentos < 3);
                ups.setString(3, usuario);
                ups.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Contraseña incorrecta. Intento " + intentos + " de 3"
                );
                return;
            }
            
            String reset = """
                UPDATE usuarios_cliente
                SET intentos = 0
                WHERE username = ?
            """;

            PreparedStatement resetPs = con.prepareStatement(reset);
            resetPs.setString(1, usuario);
            resetPs.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registro exitoso.");
            dispose();
            new BancoForm(); // saldo = 1000

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión con la base de datos");
            e.printStackTrace();
        }
    }
}
