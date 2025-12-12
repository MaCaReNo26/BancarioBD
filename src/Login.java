import javax.swing.*;

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

    public String init() {
        String usuario = txtUsuario.getText();
        String contra = txtContraseña.getText();
        if (usuario.equals("cliente123") && contra.equals("clave456")) {
            JOptionPane.showMessageDialog(this, "Registro exitoso.");
            dispose();
            new BancoForm();
        }else{
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            txtUsuario.setText("");
            txtContraseña.setText("");
        }
        return usuario;
    }
}