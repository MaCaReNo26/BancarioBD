
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

