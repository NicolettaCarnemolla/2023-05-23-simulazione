package it.polito.tdp.baseball.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.baseball.model.Appearances;
import it.polito.tdp.baseball.model.Arco;
import it.polito.tdp.baseball.model.People;
import it.polito.tdp.baseball.model.Team;


public class BaseballDAO {
	
	/**
	 * Metodo che legge una lista di interi come anni
	 * @return
	 */
	public List<Integer> readAllYears(){
		String sql = "SELECT distinct YEAR "
				+ "FROM teams";
		List<Integer> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(rs.getInt("YEAR"));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	/**
	 * Metodo che crea un grafo;
	 * Partiamo dai vertici:
	 * @return
	 */
	public List<People> readAllPlayersWithCondition(int anno, double salario){
		String sql = "SELECT DISTINCT p.* "
				+ "FROM salaries s, people p "
				+ "WHERE s.playerID=p.playerID AND s.salary>? and s.year=?";
		List<People> result = new ArrayList<People>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, salario);
			st.setInt(2, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new People(rs.getString("playerID"), 
						rs.getString("birthCountry"), 
						rs.getString("birthCity"), 
						rs.getString("deathCountry"), 
						rs.getString("deathCity"),
						rs.getString("nameFirst"), 
						rs.getString("nameLast"), 
						rs.getInt("weight"), 
						rs.getInt("height"), 
						rs.getString("bats"), 
						rs.getString("throws"),
						getBirthDate(rs), 
						getDebutDate(rs), 
						getFinalGameDate(rs), 
						getDeathDate(rs)) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	/**
	 * Terzo punto: Analisi Archi
	 * @return
	 */
	public List<Arco> readAllArchi(int anno, double salario, Map<String,People> idMap){
		String sql = "SELECT distinct a1.playerID, a2.playerID "
				+ "	FROM appearances a1, appearances a2 "
				+ "	WHERE a1.teamID=a2.teamID AND a1.playerID < a2.playerID AND a1.year=a2.year AND a1.year=? AND "
				+ "	a1.playerID IN (SELECT p.playerID "
				+ "						 FROM salaries s, people p  "
				+ "						 WHERE s.playerID=p.playerID AND s.salary>? and s.year=?) "
				+ "	AND a2.playerID IN (SELECT p.playerID "
				+ "						 FROM salaries s, people p "
				+ "						 WHERE s.playerID=p.playerID AND s.salary>? and s.year=?)";
		List<Arco> result = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setDouble(2, salario);
			st.setInt(3, anno);
			st.setDouble(4, salario);
			st.setInt(5, anno);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Arco(idMap.get(rs.getString("a1.playerID")),idMap.get(rs.getString("a2.playerID"))));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	public List<People> readAllPlayers(){
		String sql = "SELECT * "
				+ "FROM people";
		List<People> result = new ArrayList<People>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new People(rs.getString("playerID"), 
						rs.getString("birthCountry"), 
						rs.getString("birthCity"), 
						rs.getString("deathCountry"), 
						rs.getString("deathCity"),
						rs.getString("nameFirst"), 
						rs.getString("nameLast"), 
						rs.getInt("weight"), 
						rs.getInt("height"), 
						rs.getString("bats"), 
						rs.getString("throws"),
						getBirthDate(rs), 
						getDebutDate(rs), 
						getFinalGameDate(rs), 
						getDeathDate(rs)) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	
	public List<Team> readAllTeams(){
		String sql = "SELECT * "
				+ "FROM  teams";
		List<Team> result = new ArrayList<Team>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Team( rs.getInt("iD"),
						rs.getInt("year"), 
						rs.getString("teamCode"), 
						rs.getString("divID"), 
						rs.getInt("div_ID"), 
						rs.getInt("teamRank"),
						rs.getInt("games"), 
						rs.getInt("gamesHome"), 
						rs.getInt("wins"), 
						rs.getInt("losses"), 
						rs.getString("divisionWinnner"), 
						rs.getString("leagueWinner"),
						rs.getString("worldSeriesWinnner"), 
						rs.getInt("runs"), 
						rs.getInt("hits"), 
						rs.getInt("homeruns"), 
						rs.getInt("stolenBases"),
						rs.getInt("hitsAllowed"), 
						rs.getInt("homerunsAllowed"), 
						rs.getString("name"), 
						rs.getString("park")  ) );
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	
	
	//=================================================================
	//==================== HELPER FUNCTIONS   =========================
	//=================================================================
	
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getBirthDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("birth_date") != null) {
				return rs.getTimestamp("birth_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getDebutDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("debut_date") != null) {
				return rs.getTimestamp("debut_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getFinalGameDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("finalgame_date") != null) {
				return rs.getTimestamp("finalgame_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Helper function per leggere le date e gestire quando sono NULL
	 * @param rs
	 * @return
	 */
	private LocalDateTime getDeathDate(ResultSet rs) {
		try {
			if (rs.getTimestamp("death_date") != null) {
				return rs.getTimestamp("death_date").toLocalDateTime();
			} else {
				return null;
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
