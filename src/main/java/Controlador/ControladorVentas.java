/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author nati2
 */
public class ControladorVentas {
  
    public void BuscarProducto(JTextField nombreProducto,JTable tablaproductos){
      Configuracion.CConexion objetoConexion=new Configuracion.CConexion();
      Modelos.ModeloProducto objetoProducto=new Modelos.ModeloProducto();
      
      DefaultTableModel modelo=new DefaultTableModel();
      modelo.addColumn("id");
      modelo.addColumn("NombreP");
      modelo.addColumn("precioProducto");
      modelo.addColumn("stock");
      
      tablaproductos.setModel(modelo);
      
      try{
          String consulta="select * from producto where producto.nombre like concat('%',?,'%')";
          PreparedStatement ps=objetoConexion.estableceConexion().prepareStatement(consulta);
          
          ps.setString(1,nombreProducto.getText());
          ResultSet rs=ps.executeQuery();
          while(rs.next()){
              objetoProducto.setIdProducto(rs.getInt("idproducto"));
              objetoProducto.setNombreProducto(rs.getString("nombre"));
              objetoProducto.setPrecioProducto(rs.getDouble("precioProducto"));
              objetoProducto.setStockProducto(rs.getInt("stock"));
              
              modelo.addRow(new Object[]{objetoProducto.getIdProducto(),objetoProducto.getNombreProducto(),objetoProducto.getPrecioProducto(),objetoProducto.getStockProducto()});
              
          }
          tablaproductos.setModel(modelo);
      }catch (Exception e){
          JOptionPane.showMessageDialog(null, "Error al mostrar"+e.toString());
          
      }
      finally{
          objetoConexion.cerrarConexion();
      }
      for(int column=0;column<tablaproductos.getColumnCount();column++){
         Class<?> columClass=tablaproductos.getColumnClass(column);
         tablaproductos.setDefaultEditor(columClass,null);
      }
      
    }
    
    public void SeleccionarProductoVenta(JTable tablaproducto,JTextField id,JTextField nombre,JTextField precioproducto,JTextField stock,JTextField preciofinal){
        int fila=tablaproducto.getSelectedRow();
        
        try{
            if(fila>=0){
                id.setText(tablaproducto.getValueAt(fila,0).toString());
                nombre.setText(tablaproducto.getValueAt(fila,1).toString());
                precioproducto.setText(tablaproducto.getValueAt(fila,2).toString());
                stock.setText(tablaproducto.getValueAt(fila,3).toString());
                preciofinal.setText(tablaproducto.getValueAt(fila,2).toString());
                
            }
        }catch (Exception e){
                    JOptionPane.showMessageDialog(null, "Error de seleccion"+e.toString());
                    }
    }
    
    
    public void BuscarCliente(JTextField nombreCliente,JTable tablaclientes){
      Configuracion.CConexion objetoConexion=new Configuracion.CConexion();
      Modelos.ModeloCliente objetoCliente=new Modelos.ModeloCliente();
      
      DefaultTableModel modelo=new DefaultTableModel();
      modelo.addColumn("id");
      modelo.addColumn("Nombres");
      modelo.addColumn("appaterno");
      modelo.addColumn("apmaterno");
      
      tablaclientes.setModel(modelo);
      
      try{
          String consulta="select * from cliente where cliente.nombre like concat('%',?,'%')";
          PreparedStatement ps=objetoConexion.estableceConexion().prepareStatement(consulta);
          
          ps.setString(1,nombreCliente.getText());
          ResultSet rs=ps.executeQuery();
          while(rs.next()){
              objetoCliente.setIdCliente(rs.getInt("idcliente"));
              objetoCliente.setNombre(rs.getString("nombre"));
              objetoCliente.setApPaterno(rs.getString("appaterno"));
              objetoCliente.setApMaterno(rs.getString("apmaterno"));
              
              modelo.addRow(new Object[]{objetoCliente.getIdCliente(),objetoCliente.getNombre(),objetoCliente.getApPaterno(),objetoCliente.getApMaterno()});
              
          }
          tablaclientes.setModel(modelo);
      }catch (Exception e){
          JOptionPane.showMessageDialog(null, "Error al mostrar"+e.toString());
          
      }
      finally{
          objetoConexion.cerrarConexion();
      }
      for(int column=0;column<tablaclientes.getColumnCount();column++){
         Class<?> columClass=tablaclientes.getColumnClass(column);
         tablaclientes.setDefaultEditor(columClass,null);
      }
      
    }
    
     public void SeleccionarClienteVenta(JTable tablacliente,JTextField id,JTextField nombres,JTextField appaterno,JTextField apmaterno){
        int fila=tablacliente.getSelectedRow();
        
        try{
            if(fila>=0){
                id.setText(tablacliente.getValueAt(fila,0).toString());
                nombres.setText(tablacliente.getValueAt(fila,1).toString());
                appaterno.setText(tablacliente.getValueAt(fila,2).toString());
                apmaterno.setText(tablacliente.getValueAt(fila,3).toString());
                
                
            }
        }catch (Exception e){
                    JOptionPane.showMessageDialog(null, "Error de seleccion"+e.toString());
                    }
    }
     
    public void pasarProductosVenta(JTable tablaresumen,JTextField idproducto,JTextField nombreproducto,JTextField precioproducto,JTextField cantidadVenta,JTextField stock ) {
        DefaultTableModel modelo=(DefaultTableModel) tablaresumen.getModel();
        int stockDisponible=Integer.parseInt(stock.getText());
        String idProducto=idproducto.getText();
        
        for(int i=0;i<modelo.getRowCount();i++){
            String idExistente=(String) modelo.getValueAt(i, 0);
            if (idExistente.equals(idProducto)){
                JOptionPane.showMessageDialog(null, "El producto ya esta registrado");
                return;
            }
        }
        
        String nombProducto=nombreproducto.getText();
        Double precioUnitario=Double.parseDouble(precioproducto.getText());
        int cantidad=Integer.parseInt(cantidadVenta.getText());
        
        if(cantidad>stockDisponible){
         JOptionPane.showMessageDialog(null, "La cantidad de venta no puede ser mayor al stok disponible");
                return;  
        }
        double subtotal=precioUnitario*cantidad;
        
        modelo.addRow(new Object[]{idProducto,nombProducto,precioUnitario,cantidad,subtotal});
    }
    
    public void eliminarProductoSeleccionadoResumenVenta(JTable tablaresumen){
   try{
       DefaultTableModel modelo=(DefaultTableModel) tablaresumen.getModel();
       int indiceSeleccionado=tablaresumen.getSelectedRow();
    
    if(indiceSeleccionado !=-1){
        modelo.removeRow(indiceSeleccionado);
    }
    else{
        JOptionPane.showMessageDialog(null, "Selecione una fila a eliminar");
    }
   }catch(Exception e){
       JOptionPane.showMessageDialog(null, "Error al seleccionar: "+e.toString());
   }
    
    }
    
    public void CalcularTotalPagar(JTable tablaresumen,JLabel IGV,JLabel totalPagar){
        DefaultTableModel modelo=(DefaultTableModel) tablaresumen.getModel();
        
        double totalSubtotal=0;
        double igv=0.18;
        double totaligv=0;
        
        DecimalFormat formato=new DecimalFormat("#.##");
        for(int i=0;i<modelo.getRowCount();i++){
            totalSubtotal=Double.parseDouble(formato.format(totalSubtotal+(double)modelo.getValueAt(i, 4)));
            totaligv=Double.parseDouble(formato.format(igv*totalSubtotal));
        }
        totalPagar.setText(String.valueOf(totalSubtotal));
        IGV.setText(String.valueOf(totaligv));
                
    }
    
    public void crearFactura(JTextField codCliente){
      Configuracion.CConexion objetoConexion=new Configuracion.CConexion();
      Modelos.ModeloCliente objetocliente=new Modelos.ModeloCliente();
      String consulta="insert into factura (fechaFactura,fkcliente) values (curdate(),?)";
      
      try{
          objetocliente.setIdCliente(Integer.parseInt(codCliente.getText()));
          
          CallableStatement cs=objetoConexion.estableceConexion().prepareCall(consulta);
          cs.setInt(1,objetocliente.getIdCliente());
          
          cs.execute();
          JOptionPane.showMessageDialog(null, "Factura creada");
      }catch (Exception e){
          JOptionPane.showMessageDialog(null, "Error al crear factura: "+e.toString());
      }finally{
        objetoConexion.cerrarConexion();
      }
        
    }
    
    public void realizarVenta(JTable tablaresumenventa){
        Configuracion.CConexion objetoConexion=new Configuracion.CConexion();
        
        String consultaDetalle="insert into detalle (fkfactura,fkproducto,cantidad,precioVenta) values ((select max(idfactura) from factura),?,?,?)";
        String consultaStock="update producto set producto.stock=stock - ? where idproducto= ?";
        
        try{
           PreparedStatement psDetalle=objetoConexion.estableceConexion().prepareStatement(consultaDetalle);
           PreparedStatement psStock=objetoConexion.estableceConexion().prepareStatement(consultaStock);
           
           int filas=tablaresumenventa.getRowCount();
           
           for(int i=0;i<filas;i++){
               int idproducto=Integer.parseInt(tablaresumenventa.getValueAt(i, 0).toString());
               int cantidad=Integer.parseInt(tablaresumenventa.getValueAt(i, 3).toString());
               double precioVenta=Double.parseDouble(tablaresumenventa.getValueAt(i, 2).toString());
               
               psDetalle.setInt(1,idproducto);
               psDetalle.setInt(2,cantidad);
               psDetalle.setDouble(3,precioVenta);
               
               psDetalle.executeUpdate();
               
               psStock.setInt(1, cantidad);
               psStock.setInt(2, idproducto);
               
               psStock.executeUpdate();
               
               JOptionPane.showMessageDialog(null, "Venta realizada");
           }
           
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error de venta:"+ e.toString());
        }finally{
           objetoConexion.cerrarConexion();
        }
        
        
    }
    
   
    public void limpiarCamposVenta(JTextField buscarCliente,JTable tablaCliente,JTextField buscarProducto,JTable tablaProducto,
                                    JTextField selectIdCliente,JTextField selectNombreCliente,JTextField selectApPaterno,
                                    JTextField selectApMaterno, JTextField selectIdProducto, JTextField selectnombreProducto, 
                                    JTextField selectprecioProducto,JTextField selectstockProducto,JTextField precioVenta,
                                    JTextField cantidadVenta,JTable tablaresumen, JLabel IGV,JLabel total){
        buscarCliente.setText("");
        buscarCliente.requestFocus();
        DefaultTableModel modeloCliente=(DefaultTableModel) tablaCliente.getModel();
        modeloCliente.setRowCount(0);
        
        buscarProducto.setText("");
        DefaultTableModel modeloProducto=(DefaultTableModel) tablaProducto.getModel();
        modeloProducto.setRowCount(0);
        
        selectIdCliente.setText("");
        selectNombreCliente.setText("");
        selectApPaterno.setText("");
        selectApMaterno.setText("");
        
        selectIdProducto.setText("");
        selectnombreProducto.setText("");
        selectprecioProducto.setText("");
        selectstockProducto.setText("");
        
        precioVenta.setText("");
        precioVenta.setEnabled(false);
        
        cantidadVenta.setText("");
        DefaultTableModel modeloResumenVenta=(DefaultTableModel) tablaresumen.getModel();
        modeloResumenVenta.setRowCount(0);
        
        IGV.setText("----");
        total.setText("----");
    }
    
    public void MostrarUltimaFactura(JLabel ultimaFactura){
        Configuracion.CConexion objetoConexion=new Configuracion.CConexion();
        
        try{
            String consulta="select max(idfactura) as UltimaFactura from factura;";
            
            PreparedStatement ps=objetoConexion.estableceConexion().prepareStatement(consulta);
            
            ResultSet rs=ps.executeQuery();
            
            if(rs.next()){
               ultimaFactura.setText(String.valueOf(rs.getInt("UltimaFactura")));
            }
            
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error al mostrar la ultima factura:"+ e.toString());
        }finally{
           objetoConexion.cerrarConexion(); 
        }
    }
}
