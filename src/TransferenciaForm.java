import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransferenciaForm extends JFrame {
    private JTextField textIDatos;   // destinatario
    private JTextField textMDatos;   // se mantiene
    private JTextField MontoTextField;
    private JButton confirmarButton;
    private JButton cancelarButton;
    private JButton validarButton;
    private JLabel JLabelMonto;
    private JLabel JLabelMDatos;
    private JLabel JLabelIDatos;
    private JPanel TransferenciaPanel;

    public TransferenciaForm() {
        setTitle("Transferencia");
        setContentPane(TransferenciaPanel);
        setSize(350, 260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        confirmarButton.addActionListener(e -> transferir());
        cancelarButton.addActionListener(e -> salir());
        validarButton.setVisible(false); // ya no se usa
    }

    private void transferir() {
        try {
            String destinatario = textIDatos.getText();
            double monto = Double.parseDouble(MontoTextField.getText());

            // Validaciones simples
            if (destinatario.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese destinatario");
                return;
            }
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "Monto inválido");
                return;
            }
            if (monto > Sesion.saldo) {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente");
                return;
            }

            Connection con = ConexionDB.getConexion();
            //Restar al usuario actual
            PreparedStatement restar = con.prepareStatement(
                    "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?"
            );
            restar.setDouble(1, monto);
            restar.setInt(2, Sesion.idUsuario);
            restar.executeUpdate();

            //Sumar al destinatario
            PreparedStatement sumar = con.prepareStatement(
                    "UPDATE usuarios SET saldo = saldo + ? WHERE username = ?"
            );
            sumar.setDouble(1, monto);
            sumar.setString(2, destinatario);
            sumar.executeUpdate();

            //Actualizar sesion
            Sesion.saldo -= monto;
            JOptionPane.showMessageDialog(
                    this,
                    "Transferencia realizada\nMonto: $" + monto +
                            "\nSaldo actual: $" + Sesion.saldo
            );
            dispose();
            new BancoForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al realizar la transferencia");
            e.printStackTrace();
        }
    }
    private void salir() {
        dispose();
        new BancoForm();
    }
}


