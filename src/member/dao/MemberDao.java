package member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.JdbcUtil;
import member.model.Member;

public class MemberDao {
	
	public void update(Connection conn, Member member) throws SQLException{
		String sql = "UPDATE member SET name=?, password=? where memberid=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getId());
			pstmt.executeUpdate();
		}
	}
	
	public Member selectById(Connection con, String id)
			throws SQLException{
		
		Member member= null;
		
		String sql = "SELECT memberid, name, password, regdate FROM member where memberid=?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				member= new Member();
				member.setId(rs.getString(1));
				member.setName(rs.getString(2));
				member.setPassword(rs.getString(3));
				member.setRegDate(rs.getTimestamp(4));
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.close(rs, pstmt);
		}
		
		return member;
	}

	public void insert(Connection con, Member member) throws SQLException{
		String sql = "INSERT INTO member (memberid, name, password, regdate) "+
					  "VALUES(?, ?, ?, SYSDATE)";
		
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getPassword());
			
			pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			JdbcUtil.close(pstmt);
		}
		
	}
	
	public void delete(Connection con, String id) throws SQLException{
		String sql ="DELETE FROM member WHERE memberid=?";
		try(PreparedStatement pstmt = con.prepareStatement(sql)){
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		}
	}

}
