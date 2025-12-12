import javax.swing.*;

public class BancoForm extends JFrame {
    private JPanel BancoPanel;
    private JButton depositarButton;
    private JButton retirarButton;
    private JButton salirButton;
    private JButton transferenciaButton;
    private JTextField textUsuario;
    private JTextField TextFieldSaldoI;
    private JLabel JLabelBienvenido;
    private JLabel JLabelSaldo;

    public static double saldo = 1000;

    public BancoForm() {
        setTitle("Banco");
        setContentPane(BancoPanel);
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        textUsuario.setEditable(false);
        TextFieldSaldoI.setEditable(false);

        textUsuario.setText("      CLIENTE 1");
        TextFieldSaldoI.setText(String.valueOf(saldo));

        depositarButton.addActionListener(e -> abrirDeposito());
        retirarButton.addActionListener(e -> abrirRetiro());
        transferenciaButton.addActionListener(e -> abrirTransferencia());
        salirButton.addActionListener(e -> System.exit(0));
    }

    private void abrirDeposito() {
        dispose();
        new DepositarForm();
    }
    private void abrirRetiro() {
        dispose();
        new RetirarForm();
    }
    private void abrirTransferencia() {
        dispose();
        new TransferenciaForm();
    }
    public static void actualizarSaldo(JTextField field) {
        field.setText(String.valueOf(saldo));
    }
}

