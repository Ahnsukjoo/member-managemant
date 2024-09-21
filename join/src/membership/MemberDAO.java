package membership;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {

    Connection conn;
    PreparedStatement pstmt;
    DataSource factory;

    public MemberDAO() {
        try {
            Context ctx = new InitialContext();
            Context envContext = (Context) ctx.lookup("java:/comp/env");
            factory = (DataSource) envContext.lookup("jdbc/oracle");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<MemberVO> readDB() {
        List<MemberVO> list = new ArrayList<>();
        try {
            conn = factory.getConnection();
            String query = "SELECT * FROM t_member ORDER BY joinDate DESC";
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String pwd = rs.getString("pwd");
                String nanme = rs.getString("name");
                String email = rs.getString("email");
                Date joinDate = rs.getDate("joinDate");

                list.add(new MemberVO(id, pwd, nanme, email, joinDate));
            }

            conn.close();
            pstmt.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
