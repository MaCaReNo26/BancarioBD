Login.java
Este formulario permite al usuario iniciar sesión utilizando credenciales almacenadas en la base de datos.
Es el punto de entrada al sistema y controla el acceso a las demás funcionalidades.

Funcionamiento:
El usuario ingresa su nombre de usuario y contraseña.
El sistema se conecta a la base de datos usando la clase ConexionDB.

Se verifica:
Si el usuario existe y si está activo
El número de intentos fallidos

Si la contraseña es incorrecta:
Se incrementa el contador de intentos
El usuario se bloquea al tercer intento

Si las credenciales son correctas:
Se reinician los intentos
Se abre el formulario principal BancoForm

Código Login.java:

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

            //Consultar usuario
            String sql = """
                SELECT id, password, saldo, activo, intentos
                FROM usuarios
                WHERE username = ?
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Usuario no registrado");
                return;
            }

            int idUsuario = rs.getInt("id");
            boolean activo = rs.getBoolean("activo");
            int intentos = rs.getInt("intentos");
            String passBD = rs.getString("password");
            double saldo = rs.getDouble("saldo");

            if (!activo) {
                JOptionPane.showMessageDialog(this, "Usuario bloqueado");
                return;
            }

            //Contraseña incorrecta
            if (!passBD.equals(contra)) {
                intentos++;

                String update = """
                    UPDATE usuarios
                    SET intentos = ?, activo = ?
                    WHERE id = ?
                """;

                PreparedStatement ups = con.prepareStatement(update);
                ups.setInt(1, intentos);
                ups.setBoolean(2, intentos < 3);
                ups.setInt(3, idUsuario);
                ups.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Contraseña incorrecta. Intento " + intentos + " de 3"
                );
                return;
            }

            //Cargar sesión
            Sesion.idUsuario = idUsuario;
            Sesion.username = usuario;
            Sesion.saldo = saldo;

            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso");
            dispose();
            new BancoForm();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión con la base de datos");
            e.printStackTrace();
        }
    }
}

<img width="653" height="408" alt="image" src="https://github.com/user-attachments/assets/72505a52-f1e8-4710-bd7b-5b2e25306a04" />
<img width="836" height="305" alt="image" src="https://github.com/user-attachments/assets/159b279e-fdd1-4c54-9de2-fdb15ca3b685" />
<img width="803" height="301" alt="image" src="https://github.com/user-attachments/assets/dfe0c487-919b-4d42-9ecb-207e7b8eef86" />

BancoForm.java
Es el formulario principal del sistema, donde el usuario puede ver su saldo y acceder a las operaciones bancarias.

Funcionamiento:
Muestra el saldo actual almacenado en la variable estática saldo.

Contiene botones para:
Depositar
Retirar
Transferir
Salir del sistema
Cada botón abre un formulario diferente.
El saldo se maneja mediante una variable estática, lo que permite que todos los formularios compartan y actualicen el mismo valor.

Código BancoForm.java:
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

<img width="604" height="412" alt="image" src="https://github.com/user-attachments/assets/19e31e67-d233-40db-8166-414bcb0235a8" />

DepositarForm.java
Permite al usuario depositar dinero en su cuenta.

Funcionamiento
El usuario ingresa un monto.
Se valida que el monto sea mayor a cero.
El monto se suma al saldo actual.
Se muestra un mensaje de confirmación.
Se regresa al formulario principal.

Modifica directamente la variable BancoForm.saldo.

Código DepositarForm.java:
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

<img width="630" height="217" alt="image" src="https://github.com/user-attachments/assets/9919f083-583c-4c1f-99de-235cb28d1508" />
<img width="558" height="347" alt="image" src="https://github.com/user-attachments/assets/b402baf5-0d2f-482a-b7bd-c1b606bbf9be" />

RetirarForm.java
Permite al usuario retirar dinero de su cuenta.

Funcionamiento
Se valida que el monto sea mayor a cero.
Se verifica que el saldo sea suficiente.
Se descuenta el monto del saldo.
Se muestra un mensaje de retiro exitoso.
Retorna al menú principal.
Utiliza y modifica el saldo compartido del sistema.

Código RetirarForm.java:
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

<img width="636" height="230" alt="image" src="https://github.com/user-attachments/assets/ea430c37-b597-4918-a4b6-02a511fcd9b5" />
<img width="639" height="398" alt="image" src="https://github.com/user-attachments/assets/9e8ab2bf-a3fc-48cd-b17d-97a78cf2f336" />

TransferenciaForm.java
Permite realizar una transferencia a otro destinatario.

Funcionamiento
Se valida que el destinatario no esté vacío.
Se valida el monto ingresado.
Se verifica el saldo disponible.
Se descuenta el monto del saldo.
Se muestra un mensaje confirmando la transferencia.
Comparte la lógica del saldo con los demás formularios.

Código TransferenciaForm.java:
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



<img width="767" height="327" alt="image" src="https://github.com/user-attachments/assets/7fe4e74d-c1c3-4d8a-a06c-be8e8ac032df" />
<img width="644" height="417" alt="image" src="https://github.com/user-attachments/assets/74631a34-0de3-48e2-9be8-e0e3cc77be17" />

Main.java
Es el punto de inicio del programa.

Funcionamiento
Verifica la conexión con la base de datos.
Muestra mensajes en consola según el estado de la conexión.
Abre el formulario de inicio de sesión.

Código Main.java:
public class Main {

    public static void main(String[] args) {
        try {
            ConexionDB.getConexion();
            System.out.println("Conexion exitosa");
            new Login();
        } catch (Exception e) {
            System.out.println("Error de conexion");
            e.printStackTrace();
        }
    }
}

ConexionDB.java
Establece la conexión con la base de datos PostgreSQL.

Funcionamiento
Usa DriverManager para conectarse a la base de datos.
Implementa un patrón de conexión reutilizable.
Garantiza una sola conexión activa.
Es utilizada principalmente por el formulario Login.

Código ConexionDB.java:
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL =
            "jdbc:mysql://bbkfvleazphx4yonrovc-mysql.services.clever-cloud.com:3306/bbkfvleazphx4yonrovc?useSSL=true&serverTimezone=UTC";
    private static final String USER = "u14xnegxgaclg9ln";
    private static final String PASSWORD = "84wU6rejMj4trLG6QckI";

    private static Connection conexion;

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conexion;
    }
}
