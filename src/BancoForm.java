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

    public BancoForm() {
        setTitle("Banco");
        setContentPane(BancoPanel);
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        textUsuario.setEditable(false);
        TextFieldSaldoI.setEditable(false);
        //Mostrar datos reales de la sesión
        textUsuario.setText(Sesion.username);
        TextFieldSaldoI.setText(String.valueOf(Sesion.saldo));

        depositarButton.addActionListener(e -> abrirDeposito());
        retirarButton.addActionListener(e -> abrirRetiro());
        transferenciaButton.addActionListener(e -> abrirTransferencia());
        salirButton.addActionListener(e -> cerrarSesion());
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

    private void cerrarSesion() {
        Sesion.idUsuario = 0;
        Sesion.username = null;
        Sesion.saldo = 0;
        dispose();
        new Login();
    }
}

