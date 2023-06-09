package com.exampm.spbbs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.exampm.spbbs.dto.SpDto;

public class SpDao {

	// db 접근 클래스

	DataSource dataSource;

	public SpDao() {
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/spbbs");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 글쓰기
	public void write(String uname, String upass, String title, String content) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			String sql = "insert into spboard (uname,uapss,title,content) value (?,?,?,?)";
			pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, uname);
			pstmt.setString(2, upass);
			pstmt.setString(3, title);
			pstmt.setString(4, content);

			pstmt.executeUpdate();

			rs = pstmt.getGeneratedKeys(); // 쿼리 실행후 생성된 키 값 반환
			pstmt.clearParameters(); // pstmt 다시 사용을 위해 비움

			if (rs.next()) {
				int num = rs.getInt(1);
				try {
					String updateQuery = "update spboard set s_group = ? where num = ?";
					pstmt = conn.prepareStatement(updateQuery);
					pstmt.setInt(1, num);
					pstmt.setInt(2, num);
					pstmt.executeUpdate();
				} catch (Exception ee) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception eee) {
			}
		}
	}

	// 글 수정하기
	public SpDto update(String cNum) {
		int iNum = Integer.parseInt(cNum);
		SpDto dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			String sql = "select * from spboard where num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, iNum);
			rs = pstmt.executeQuery();

			// dto에 담기
			if (rs.next()) {
				dto = new SpDto();
				int num = rs.getInt("num");
				String uname = rs.getString("uname");
				String upass = rs.getString("upass");
				String title = rs.getString("title");
				String content = rs.getString("content");

				dto.setNum(num);
				dto.setUname(uname);
				dto.setUpass(upass);
				dto.setTitle(title);
				dto.setContent(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ee) {

			}
		}

		return dto;
	}

	public void updateok(String num, String uname, String upass, String title, String content) {
		int inum = Integer.parseInt(num);
		String sql = "update spboard set uname=?, upass=?, title=?, content=?, where num=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uname);
			pstmt.setString(2, upass);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			pstmt.setInt(5, inum);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// 본문보기
	public SpDto detail(String cNum) {

		int iNum = Integer.parseInt(cNum);
		hitAdd(iNum); // 조회수 증가
		SpDto dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			String sql = "select * from spboard where num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, iNum);
			rs = pstmt.executeQuery();
			// dto에 담는 작업
			if (rs.next()) {
				int num = rs.getInt("num");
				int s_group = rs.getInt("s_group");
				int s_step = rs.getInt("s_step");
				int s_indent = rs.getInt("s_indent");
				String uname = rs.getString("uname");
				String upass = rs.getString("upass");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int ct = rs.getInt("ct");
				int hit = rs.getInt("hit");
				Timestamp wdate = rs.getTimestamp("wdate");

				dto = new SpDto(num, s_group, s_step, s_indent, uname, upass, title, content, ct, hit, wdate);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();

			} catch (Exception e2) {

			}
		}

		return dto;

	}

	// 데이터를 받아서 SpDto에 담음
	public ArrayList<SpDto> list() {
		ArrayList<SpDto> dtos = new ArrayList<SpDto>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			String sql = "select * from spboard order by s_group desc, s_step asc";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int num = rs.getInt("num");
				int s_group = rs.getInt("s_group");
				int s_step = rs.getInt("s_step");
				int s_indent = rs.getInt("s_indent");
				String uname = rs.getString("uname");
				String upass = rs.getString("upass");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int ct = rs.getInt("ct");
				int hit = rs.getInt("hit");
				Timestamp wdate = rs.getTimestamp("wdate");

				SpDto dto = new SpDto(num, s_group, s_step, s_indent, uname, upass, title, content, ct, hit, wdate);
				dtos.add(dto);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close the database resources (ResultSet, PreparedStatement, Connection)
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					// Handle the exception
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					// Handle the exception
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					// Handle the exception
				}
			}
		}
		return dtos;
	} // list



	private void hitAdd(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = dataSource.getConnection();
			String sql = "update spboard set hit = hit +1 where num =?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			int r = pstmt.executeUpdate();
			System.out.println("hit update : " + r);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ee) {
			}
		}
	}

}
