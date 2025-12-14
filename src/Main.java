
import java.sql.Connection;

void main() {
    try {
        Connection con = ConexionDB.getConexion();
        System.out.println("✅ Conexión exitosa a PostgreSQL");
    } catch (Exception e) {
        System.out.println("❌ Error de conexión");
        e.printStackTrace();
    }
    new Login();
}
