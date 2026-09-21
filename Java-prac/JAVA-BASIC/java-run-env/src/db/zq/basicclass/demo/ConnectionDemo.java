package db.zq.basicclass.demo;


import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ConnectionDemo {
    public static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
    //mysql 数据ylkd连接格式
    // jdbc:mysql://ip:port/dbname
    public static final String DB_URL = "jdbc:mysql://localhost:3306/test";

    public static void main(String[] args){
        Connection conn = null;
        Statement stmt = null;
        String sql = "INSERT INTO zq_user(name,passwd,age,sex,birthday) VALUES(\"wangwu7\",\"4567\",89,\"2\",\"1908-01-01\");";
        String quetySql = "SELECT name,passwd,age FROM zq_user WHERE NAME like ?";

        PreparedStatement pstmt = null;
        String pSql = "INSERT INTO zq_user(name,passwd,age,sex,birthday) VALUES(?,?,?,?,?);";
        try {
            Class.forName(DBDRIVER);
            conn = DriverManager.getConnection(DB_URL, "root", "123456");
//            pstmt = conn.prepareStatement(pSql);

            pstmt = conn.prepareStatement(quetySql);
            /**
             * 配置不同参数，可滚动的结果 JDBC2.0
             */
            pstmt = conn.prepareStatement(quetySql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String keyWord = "yuan";
            pstmt.setString(1,"%" + keyWord + "%");

            ResultSet rs =  pstmt.executeQuery();
            while (rs.next()){
                System.out.print("name:" + rs.getString(1) + "\t");
                System.out.print("passwd:" + rs.getString(2) + "\t");
                System.out.println("age:" + rs.getInt(3));
            }

            // 设置参数
//            pstmt.setString(1, "huoyuanjia");
//            pstmt.setString(2, "3333");
//            pstmt.setInt(3, 36);
//            pstmt.setString(4, "2");
//
//            java.util.Date tmp = new SimpleDateFormat("yyyy-MM-dd").parse("2001-12-28");
//            java.sql.Date sqlDate =new java.sql.Date(tmp.getTime());
//            pstmt.setDate(5, sqlDate);
//
//            pstmt.executeUpdate();
            pstmt.close();

//            stmt = conn.createStatement();
//            stmt.executeUpdate(sql);
//            stmt.execute(sql);
//            stmt.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
