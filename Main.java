package mysqltutorial;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Main {

	public static void main(String[] args) {
		// queryData();
		//updateData();
		
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
}