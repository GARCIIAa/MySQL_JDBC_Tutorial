package mysqltutorial;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;

public class Main {

	public static void main(String[] args) {

		// queryData();

		// updateData();

		int id = insertCandidate("Bush", "Lily", Date.valueOf("1980-01-04"),
				"bush.l@yahoo.com", "(408) 898-6666");
		System.out.println(String.format(
				"A new candidate with id %d has been inserted.", id));
	}

	public static void queryData() {

		String sql = "SELECT first_name, last_name, email " + "FROM candidates";
		try (Connection conn = MySQLJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getString("first_name") + "\t"
						+ rs.getString("last_name") + "\t"
						+ rs.getString("email"));

			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Update candidate demo
	 */
	public static void updateData() {

		String sqlUpdate = "UPDATE candidates " + "SET last_name = ? "
				+ "WHERE id = ?";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

			// prepare data for update
			String lastName = "William";
			int id = 100;
			pstmt.setString(1, lastName);
			pstmt.setInt(2, id);

			int rowAffected = pstmt.executeUpdate();
			System.out.println(String.format("Row affected %d", rowAffected));

			// reuse the prepared statement
			lastName = "Grohe";
			id = 101;
			pstmt.setString(1, lastName);
			pstmt.setInt(2, id);

			rowAffected = pstmt.executeUpdate();
			System.out.println(String.format("Row affected %d", rowAffected));

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Insert a new candidate
	 * 
	 * @param firstName
	 * @param lastName
	 * @param dob
	 * @param email
	 * @param phone
	 * @return
	 */
	public static int insertCandidate(String firstName, String lastName,
			Date dob, String email, String phone) {
		// for insert a new candidate
		ResultSet rs = null;
		int candidateId = 0;

		String sql = "INSERT INTO candidates(first_name,last_name,dob,phone,email) "
				+ "VALUES(?,?,?,?,?)";

		try (Connection conn = MySQLJDBCUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);) {

			// set parameters for statement
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setDate(3, dob);
			pstmt.setString(4, phone);
			pstmt.setString(5, email);

			int rowAffected = pstmt.executeUpdate();
			if (rowAffected == 1) {
				// get candidate id
				rs = pstmt.getGeneratedKeys();
				if (rs.next())
					candidateId = rs.getInt(1);

			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return candidateId;
	}
}