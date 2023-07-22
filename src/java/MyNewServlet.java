
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/MyNewServlet"})
public class MyNewServlet extends HttpServlet {

    Connection c1;
    Statement st;

    @Override
    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbemp?zeroDateTimeBehavior="
                    + "CONVERT_TO_NULL", "root", "root");
            st = c1.createStatement();
        } catch (Exception e) {
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter prt = res.getWriter();

        String i_name, i_Quantity, myf, del_item;
        int i_Price = 0, Total = 0;
        
        myf = req.getParameter("show");
        i_name = req.getParameter("menu");
        i_Quantity = req.getParameter("Quantity");

        if (myf.equals("s1") == true) {
            try {

                ResultSet rs = st.executeQuery("Select * from item_menu ");
                prt.print("<style>th{border:2px solid red;background-color:yellow;}"
                        + "td{border:2px solid green;background-color:#FF95FFB7;}</style>"
                        + "<table><th>Item_Name</th>"
                        + "<th>Item_Quantity</th>"
                        + "<th>Item_Price</th>"
                        + "<th>Total Amount</th>");
                while (rs.next()) {
                    prt.print("<tr><td>" + rs.getString(1) + "</td>"
                            + "<td>" + rs.getString(2) + "</td>"
                            + "<td>" + rs.getString(3) + "</td>"
                            + "<td>" + rs.getString(4) + "</td></tr>");
                }
                
                ResultSet rs1=st.executeQuery("select SUM(Total_Price) from item_menu");
                String t_amt="";
                while (rs1.next()) {
//                     prt.print("<tr><td colspan=4>"+"Total Amount Of Your Bill Is  :      "+rs1.getString(1)+"</td></tr>");
                 t_amt= rs1.getString(1);
                }
                     
                if (t_amt != null) {
      prt.print("<tr><td colspan=4>"+"Total Amount Of Your Bill Is  :      "+t_amt+"</td></tr>");
                } else {

                    prt.print("<tr><td colspan=4>" + "Total Amount Is  : 0 Confirm Your Order Once.....? </td></tr>");
                }
                         
                
                prt.print("</table>");

            } catch (SQLException e) {

            }//show data if

        } else if (myf.equals("sub") == true) {

            String a[] = i_name.split("/");
            int p1 = Integer.parseInt(a[1]);
            int q1 = Integer.parseInt(i_Quantity);
            Total = p1 * q1;

            try {
                boolean b1 = st.execute("insert into item_menu values('" + a[0] + "'," + i_Quantity + "," + a[1] + "," + Total + ")  ");
                if (b1 == false) {
                    res.sendRedirect("index.html");

                }
            } catch (SQLException e) {
            }

        }//submit else if
        else if (myf.equals("remove") == true) {
            del_item = req.getParameter("rem");
            try {
                boolean b2 = st.execute("delete from item_menu where item_name='"+del_item+"' ");
                if (b2 == false) {
                    res.sendRedirect("myshow.html");
                }
              
            } catch (Exception e) {
                prt.print(e);
            }
        } else if (myf.equals("removeall") == true) {
      
            try {
                boolean b2 = st.execute("delete from item_menu where 1=1 ");
                if (b2 == false) {
                    res.sendRedirect("myshow.html");
                }
              
            } catch (Exception e) {
                prt.print(e);
            }
        }//All clean else if

    }

    @Override
    public void destroy() {
        try {              
            st.close();
            c1.close();
        } catch (Exception e) {
        }

    }
}
