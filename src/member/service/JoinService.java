package member.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.ConnectionProvider;
import jdbc.JdbcUtil;
import member.dao.MemberDao;
import member.model.Member;

public class JoinService {
	private MemberDao memberDao = new MemberDao();
	
	public void join(JoinRequest joinReq) {
		// .......
		Connection con = null;
		try {
			con = ConnectionProvider.getConnection();
			con.setAutoCommit(false);
			
			Member m = memberDao.selectById(con, joinReq.getId());
			
			if (m !=  null) {
				JdbcUtil.rollback(con);
				throw new DuplicateIdException();
			}
			
			Member member = new Member();
			member.setId(joinReq.getId());
			member.setName(joinReq.getName());
			member.setPassword(joinReq.getPassword());
			
			memberDao.insert(con, member);
			
			con.commit();
		} catch (SQLException e) {
			JdbcUtil.rollback(con);
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(con);
		}
	}
}
