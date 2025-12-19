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

