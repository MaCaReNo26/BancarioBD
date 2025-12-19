import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RetirarForm extends JFrame {
    private JTextField MontoTextField;
    private JButton confirmarButton;
    private JButton cancelarButton;
    private JLabel JLabelPMonto;
    private JPanel RetirarPanel;

    public RetirarForm() {
        setTitle("Retirar");
        setContentPane(RetirarPanel);
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        confirmarButton.addActionListener(e -> retirar());
        cancelarButton.addActionListener(e -> salir());
    }

    private void retirar() {
        try {
            double monto = Double.parseDouble(MontoTextField.getText());
            //Validar monto
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "Ingrese un monto válido");
                return;
            }
            // Validar saldo suficiente
            if (monto > Sesion.saldo) {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente");
                return;
            }

            // Actualizar en BD
            Connection con = ConexionDB.getConexion();
            String sql = """
                UPDATE usuarios
                SET saldo = saldo - ?
                WHERE id = ?
            """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, monto);
            ps.setInt(2, Sesion.idUsuario);
            ps.executeUpdate();

            // Actualizar sesión
            Sesion.saldo -= monto;
            JOptionPane.showMessageDialog(
                    this,
                    "Retiro realizado con exito\nNuevo saldo: $" + Sesion.saldo
            );
            dispose();
            new BancoForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al realizar el retiro");
            e.printStackTrace();
        }
    }

    private void salir() {
        dispose();
        new BancoForm();
    }
}
