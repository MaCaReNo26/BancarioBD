import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DepositarForm extends JFrame {
    private JTextField MontoTextField;
    private JButton confirmarButton;
    private JButton cancelarButton;
    private JLabel JLabelIMonto;
    private JPanel DepositarPanel;

    public DepositarForm() {
        setTitle("Depositar");
        setContentPane(DepositarPanel);
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        confirmarButton.addActionListener(e -> depositar());
        cancelarButton.addActionListener(e -> salir());
    }

    private void depositar() {
        try {
            double monto = Double.parseDouble(MontoTextField.getText());

            // Validaciones
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "Ingrese un monto válido");
                return;
            }

            // Actualizar en BD
            Connection con = ConexionDB.getConexion();
            String sql = """
                UPDATE usuarios
                SET saldo = saldo + ?
                WHERE id = ?
            """;
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, monto);
            ps.setInt(2, Sesion.idUsuario);
            ps.executeUpdate();

            //Actualizar sesion
            Sesion.saldo += monto;
            JOptionPane.showMessageDialog(
                    this,
                    "Deposito realizado con exito\nNuevo saldo: $" + Sesion.saldo
            );
            dispose();
            new BancoForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un numero válido");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al realizar el deposito");
            e.printStackTrace();
        }
    }

    private void salir() {
        dispose();
        new BancoForm();
    }
}

