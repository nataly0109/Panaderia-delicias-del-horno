/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Configuracion;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author nati2
 */

public class CConexion {
    Connection conectar=null;
    String servidor="localhost";//127.0.0.1
    String puerto="3306";
    String baseDatos="dbpanaderia";
    String usuario="root";
    String clave="123456";
    String cadena="jdbc:mysql://"+servidor+":"+puerto+"/"+baseDatos;
    
    
    public Connection estableceConexion(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar=DriverManager.getConnection(cadena,usuario,clave);
            
            JOptionPane.showMessageDialog(null,"Conexion correcta a Base de Datos");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"NO SE ENCONTRÓ LA BASE DE DATOS"+e.toString());
        }
        return conectar;
       
    }

    public void cerrarConexion(){
        try{
            if(conectar!=null){
                conectar.close();
                //JOptionPane.showMessageDialog(null,"Conexion cerrada");
            }
        }
            catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error al cerrar"+e.toString());
        }
        
    }
    

}


