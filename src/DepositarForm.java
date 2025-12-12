import javax.swing.*;

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
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "Monto invalido");
                return;
            }
            BancoForm.saldo += monto;
            JOptionPane.showMessageDialog(this, "Deposito exitoso");
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
