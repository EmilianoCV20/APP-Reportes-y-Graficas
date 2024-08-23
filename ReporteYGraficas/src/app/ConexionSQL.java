
package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQL {
    
    public static Connection getConexion(){
        
        String url= "jdbc:sqlserver://localhost:1433;"
                + "database=REPORTE;"
                + "user=sa;"
                + "password=Emi7337175.;"
                + "loginTimeout=30";
        try{
            Connection con = DriverManager.getConnection(url);
            return con;
        }catch(SQLException e){
            System.out.println(e.toString());
            return null;
        }
    }
}
