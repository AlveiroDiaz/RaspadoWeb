/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raspadoprueba;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Al
 */
public class RaspadoPrueba {
    
    
    private static Connection con;
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String user="root";
    private  static final String pass="123456";
    private static final String url="jdbc:mysql://localhost:3306/raspado";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
       String url = "https://www.alkomprar.com/tv-video/televisores/c/BI_120";
       int i = 3;
      conectar();
       
       try {  
            Document doc = Jsoup.connect(url).get();
       
            String pag =  doc.select("div.sort-refine-bar__orderBy__paginator > nav.pagination--buttons > ul").attr("data-numbers");            

            
            System.out.println("hola " + doc.select("div.sort-refine-bar__orderBy__paginator > nav > div > a[href]").get(1).attr("href"));
            
           for (int j=0; j<Integer.parseInt(pag);j++){
                
                if(j>0 && j<Integer.parseInt(pag)){
                    String newlink = doc.select("div.sort-refine-bar__orderBy__paginator > nav > div > a[href]").get(1).attr("href");
                    doc = Jsoup.connect(newlink).get();
                }
                
                
                  for (Element e : doc.select("div.product__information")) {
               
               String link = "https://www.alkomprar.com/"+ (e.select("a[href]").attr("href")); 
               Document newDoc = Jsoup.connect(link).get();
               String nombre = newDoc.select("h1").text();
               Element input = newDoc.select("input[class=price-hidden]").first();
               String valor = input.attr("value");
               System.out.println(valor);
               
               
              
           String sql = "insert into datosAlkomprar (pagina,url,nombre,precio) values (?,?,?,?)";
           PreparedStatement ps;
           
           ps = con.prepareStatement(sql);
           ps.setInt(1, j+1);
           ps.setString(2, link);
           ps.setString(3, nombre);
           ps.setDouble(4, Double.parseDouble(valor));
           ps.executeUpdate();
           
            }
                
            } 
                   
            
            
            
            
            

            
        } catch (IOException ex) {
            Logger.getLogger(RaspadoPrueba.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    
    }
    
    public static void conectar(){
        
            con=null;
        try{
            Class.forName(driver);
            // Nos conectamos a la bd
            con= (Connection) DriverManager.getConnection(url, user, pass);
            // Si la conexion fue exitosa mostramos un mensaje de conexion exitosa
            if (con!=null){
                JOptionPane.showMessageDialog(null, "Conexion establecida");
            }
        }
        // Si la conexion NO fue exitosa mostramos un mensaje de error
        catch (ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null, "Error" + e);
        }
    }
    
}
