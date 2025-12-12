import javax.swing.*;

public class TransferenciaForm extends JFrame {
    private JTextField textIDatos;
    private JTextField textMDatos;
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

        validarButton.addActionListener(e -> validar());
        confirmarButton.addActionListener(e -> transferir());
        cancelarButton.addActionListener(e -> salir());
    }

    private void validar() {
        if (textIDatos.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese destinatario");
        } else {
            JOptionPane.showMessageDialog(this, "Destinatario valido");
        }
    }

    private void transferir() {
        try {
            String destinatario = textIDatos.getText();
            double monto = Double.parseDouble(MontoTextField.getText());
            if (destinatario.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese destinatario");
                return;
            }
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "Monto invalido");
                return;
            }
            if (monto > BancoForm.saldo) {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente");
                return;
            }
            BancoForm.saldo -= monto;
            JOptionPane.showMessageDialog(this,
                    "Transferencia realizada a " + destinatario + " por $" + monto);
            dispose();
            new BancoForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ERROR >:|");
        }
    }
    private void salir() {
        dispose();
        new BancoForm();
    }
}
