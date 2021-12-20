package nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	private final static String DB_URL = "jdbc:mysql://localhost:3306/db_nations";
	private final static String DB_USER = "root";
	private final static String DB_PASSWORD = "1234gatto1";

	// Milestone 2
	private final static String DB_QUERY = "select c.name, c.country_id, r.name as region, c2.name as continent \r\n"
			+ "from countries c\r\n" + "join regions r ON r.region_id = c.region_id\r\n"
			+ "join continents c2 on c2.continent_id = r.continent_id\r\n" + "group by c.name\r\n" + "order by c.name;";

	// Milestone 3
	private final static String DB_QUERY2 = "select c.name, c.country_id, r.name as region, c2.name as continent \r\n"
			+ "from countries c\r\n" + "join regions r ON r.region_id = c.region_id\r\n"
			+ "join continents c2 on c2.continent_id = r.continent_id\r\n" + "where c.name like " + "?\r\n"
			+ "group by c.name\r\n" + "order by c.name;";

	// BONUS
	private final static String DB_QUERY3 = "select c.name from countries c \r\n" + "where c.country_id = ?;";
	
	private final static String DB_QUERY4 = "select l.`language` from country_languages cl\r\n"
			+ "join languages l on cl.language_id = l.language_id\r\n"
			+ "where country_id = ?;";
	
	private final static String DB_QUERY5 = "select max(cs.`year`), cs.population, cs.gdp from country_stats cs\r\n"
			+ "where country_id = ?;";

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(DB_QUERY);

			while (result.next()) {
				System.out.println("Country name: " + result.getString("c.name"));
				System.out.println("ID n. " + result.getInt("c.country_id"));
				System.out.println("Region name: " + result.getString("region") + ". ");
				System.out.println("Continent name: " + result.getString("continent") + ". ");
			}

			System.out.println("Search: ");
			String UserInput = scanner.nextLine();
			String UserInputFinal = "%" + UserInput + "%";

			try (PreparedStatement preparedStatement = connection.prepareStatement(DB_QUERY2)) {

				preparedStatement.setString(1, UserInputFinal);
				ResultSet result2 = preparedStatement.executeQuery();

				while (result2.next()) {
					System.out.println("Country name: " + result2.getString("c.name"));
					System.out.println("ID n. " + result2.getInt("c.country_id"));
					System.out.println("Region name: " + result2.getString("region") + ". ");
					System.out.println("Continent name: " + result2.getString("continent") + ". ");
				}

			}
			
			System.out.println("Insert country \"ID\": ");
			String UserInput2 = scanner.nextLine();
			
			try(PreparedStatement preparedStatement2 = connection.prepareStatement(DB_QUERY3)){
				preparedStatement2.setString(1, UserInput2);
				ResultSet result3 = preparedStatement2.executeQuery();
				
				while(result3.next()) {
					System.out.println("Details for country: "+ result3.getString("c.name"));

				}
				
			}
			
			try(PreparedStatement preparedStatement3 = connection.prepareStatement(DB_QUERY4)){
				preparedStatement3.setString(1, UserInput2);
				ResultSet result4 = preparedStatement3.executeQuery();
				
				while(result4.next()) {
					System.out.print(result4.getString("l.language") + ": ");
				}
				
				
			}
			
			try(PreparedStatement preparedStatement4 = connection.prepareStatement(DB_QUERY5)){
				preparedStatement4.setString(1, UserInput2);
				ResultSet result5 = preparedStatement4.executeQuery();
				
				while(result5.next()) {
					System.out.println("\n" + "Year: " + result5.getInt("max(cs.`year`)"));
					System.out.println("Population: " + result5.getInt("cs.population"));
					System.out.println("GDP: " + result5.getBigDecimal("cs.gdp"));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		scanner.close();

	}

}
