import javax.swing.*;

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

            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "Monto invalido");
                return;
            }

            if (monto > BancoForm.saldo) {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente");
                return;
            }

            BancoForm.saldo -= monto;
            JOptionPane.showMessageDialog(this, "Retiro exitoso");

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
