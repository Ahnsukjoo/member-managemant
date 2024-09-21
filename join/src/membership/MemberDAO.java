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

    public void addDB(String id, String pwd, String name, String email) {
        try {
            conn = factory.getConnection();
            String query = "INSERT INTO t_member(id, pwd, name, email) values (?,?,?,?)";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            pstmt.setString(2, pwd);
            pstmt.setString(3, name);
            pstmt.setString(4, email);
            pstmt.executeUpdate();

            conn.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MemberVO readModDB(String id) {
        MemberVO info = new MemberVO();
        try {
            conn = factory.getConnection();
            String query = "SELECT * FROM t_member WHERE id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            rs.next();
            info.setId(rs.getString("id"));
            info.setPwd(rs.getString("pwd"));
            info.setName(rs.getString("name"));
            info.setEmail(rs.getString("email"));
            info.setJoinDate(rs.getDate("joinDate"));

            conn.close();
            pstmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    public void updateDB(MemberVO vo) {
        try {
            String pwd = vo.getPwd();
            String name = vo.getName();
            String email = vo.getEmail();
            String id = vo.getId();
            conn = factory.getConnection();
            String query = "UPDATE t_member SET pwd = ?, name = ?, email = ? WHERE id = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, pwd);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, id);
            pstmt.executeUpdate();
            conn.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
