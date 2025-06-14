/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import com.toedter.calendar.JDateChooser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.sql.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nati2
 */
public class ControladorReportes {
  public void BuscarFacturaMostrarDatosCliente(JTextField numeroFactura,JLabel numeroFacturaEncontrado,JLabel fechaFacturaEncontrado,JLabel nombrecliente,JLabel appaterno,JLabel apmaterno){
      Configuracion.CConexion objetoCConexion=new Configuracion.CConexion();
      
      try{
          String consulta="select factura.idfactura,factura.fechafactura,cliente.nombre,cliente.appaterno,cliente.apmaterno from factura inner join cliente on cliente.idcliente=factura.fkcliente where factura.idfactura=?;";
          
          PreparedStatement ps=objetoCConexion.estableceConexion().prepareStatement(consulta);
          ps.setInt(1,Integer.parseInt(numeroFactura.getText()));
          
          ResultSet rs=ps.executeQuery();
          
          if (rs.next()){
              numeroFacturaEncontrado.setText(String.valueOf(rs.getInt("idfactura")));
              fechaFacturaEncontrado.setText(String.valueOf(rs.getDate("fechafactura").toString()));
              nombrecliente.setText(rs.getString("nombre"));
              appaterno.setText(rs.getString("appaterno"));
              apmaterno.setText(rs.getString("apmaterno"));
          }
          else{
              numeroFacturaEncontrado.setText("");
              fechaFacturaEncontrado.setText("");
              nombrecliente.setText("");
              appaterno.setText("");
              apmaterno.setText("");
              
              JOptionPane.showMessageDialog(null,"No se encontró la factura");
          }
              
      }catch (Exception e){
          
          JOptionPane.showMessageDialog(null,"Error al buscar a factura"+ e.toString());
          
      }finally{
          
      }
      
      
  }
  
  public void BuscarFacturaMostrarDatosProductos(JTextField numeroFactura,JTable tablaProductos,JLabel IGV, JLabel total){
     
      Configuracion.CConexion objetoConexion=new Configuracion.CConexion();
      DefaultTableModel modelo=new DefaultTableModel();
      
      modelo.addColumn("N.Producto");
      modelo.addColumn("Cantidad");
      modelo.addColumn("Precioventa");
      modelo.addColumn("Subtotal");
      
      tablaProductos.setModel(modelo);
      //"";
      try{
          String consulta="select producto.nombre,detalle.cantidad,detalle.precioVenta from detalle " + 
                  " inner join factura on factura.idfactura=detalle.fkfactura " + 
                  " inner join producto on producto.idproducto=detalle.fkproducto " + 
                  " where factura.idfactura=?";
          
          PreparedStatement ps=objetoConexion.estableceConexion().prepareStatement(consulta);
          ps.setInt(1, Integer.parseInt(numeroFactura.getText()));
          
          ResultSet rs=ps.executeQuery();
          
          double totalFactura=0.0;
          double valorIGV=0.18;
          
          DecimalFormat formato=new DecimalFormat ("#.##");
          
          while(rs.next()){
              
              String nombreProducto=rs.getString("nombre");
              int cantidad=rs.getInt("cantidad");
              double precioVenta=rs.getDouble("precioVenta");
              double subtotal=cantidad*precioVenta;
          
          totalFactura=Double.parseDouble(formato.format(totalFactura+subtotal));
          
          modelo.addRow(new Object[]{nombreProducto,cantidad,precioVenta,subtotal});
          }
          double totalIGV=Double.parseDouble(formato.format(totalFactura*valorIGV));
          
          IGV.setText(String.valueOf(totalIGV));
          total.setText(String.valueOf(totalFactura));
          
          
      }catch (Exception e){
          JOptionPane.showMessageDialog(null,"Error al mostrar los productos de la factura"+ e.toString());
      }finally{
          objetoConexion.cerrarConexion();
      }
  }
  
  
  
  public void mostrarTotalVentaPorFecha(JDateChooser desde, JDateChooser hasta, JTable tablaVentas, JLabel totalGeneral){
     Configuracion.CConexion objetoConexion=new Configuracion.CConexion();
     DefaultTableModel modelo=new DefaultTableModel(); 
     
     modelo.addColumn ("idFactura"); 
     modelo.addColumn ("FechaFactura");
     modelo.addColumn ("NProducto");
     modelo.addColumn ("Cantidad");
     modelo.addColumn ("PrecioVenta");
     modelo.addColumn ("Subtotal");
     
     tablaVentas.setModel (modelo);
     
     try{
         String consulta="select factura.idfactura, factura.fechafactura, producto.nombre, detalle.cantidad, detalle.precioVenta from detalle " + 
                 "inner join factura on factura.idfactura=detalle.fkfactura " + 
                  " inner join producto on producto.idproducto=detalle.fkproducto " + 
                  " where factura.fechafactura between ? and ?;";
         
         PreparedStatement ps=objetoConexion.estableceConexion().prepareStatement(consulta);
         
         java.util.Date fechaDesde=desde.getDate();
         java.util.Date fechaHasta=hasta.getDate();
         java.sql.Date fechaDesdeSQL=new java.sql.Date(fechaDesde.getTime());
         java.sql.Date fechaHastaSQL=new java.sql.Date(fechaHasta.getTime());
         
         ps.setDate(1, fechaDesdeSQL);
         ps.setDate(2, fechaHastaSQL);
         
         ResultSet rs=ps.executeQuery();
         
         double totalFactura=0.0;
         DecimalFormat formato=new DecimalFormat ("#.##");
         
         while (rs.next()){
             int idFactura=rs.getInt("idfactura");
             Date fechaFactura=rs.getDate("fechafactura");
             String nombreProducto=rs.getString("nombre");
             int cantidad =rs.getInt("cantidad");
             double precioVenta=rs.getDouble("precioVenta");
             double subtotal=cantidad*precioVenta;
             
             totalFactura=Double.parseDouble(formato.format(totalFactura+subtotal));
             modelo.addRow(new Object[] {idFactura,fechaFactura,nombreProducto, cantidad,precioVenta,subtotal});
             
         }
         
         totalGeneral.setText(String.valueOf(totalFactura));
         
     }catch(Exception e){
         
         JOptionPane.showMessageDialog(null,"Error al buscar los ingresos por fechas: "+ e.toString());
         
     }finally{
             objetoConexion.cerrarConexion();
             }
     
     for(int column=0; column<tablaVentas.getColumnCount();column++){
         Class<?> columClass=tablaVentas.getColumnClass(column);
         tablaVentas.setDefaultEditor(columClass, null);
     }
  }
  
  
  
}
