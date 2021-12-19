import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
public class Main {
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		int number = 0;
		Scanner sc = new Scanner(System.in);
		while(number != 3) {
			System.out.println("1. ���డ���� ȣ���� ã��, \n2. ���� �̿��� ȣ�ڿ��� �̿��� ���� ����ī��ü(��ȣ���� �̿��ѻ������ ���� ����) \n3. ����");
			number = sc.nextInt();
			sc.nextLine();
			if(number == 1) {
				System.out.println("���� ���۳�¥�� �Է��ϼ��� ex)20211224");
				String start = sc.nextLine();
				System.out.println("���� ��������¥�� �Է��ϼ��� ex)20211225");
				String end = sc.nextLine();
				System.out.println("���� ������ �Է��ϼ��� ex)�����, ��⵵, ��û���� ��");
				String state = sc.nextLine();
				System.out.println("���� ������ �Է��ϼ��� ex)���۱�, ������, õ�Ƚ� ��");
				String city = sc.nextLine();
				System.out.println("���� ������ �Է��ϼ��� ex)�󵵵�, ��ź��, ������ ��");
				String village = sc.nextLine();
				System.out.println("���� �ο� ���� �Է��ϼ���");
				int peopleNum = sc.nextInt();
				sc.nextLine();
				try{
					Connection conn = 
							DriverManager.getConnection("jdbc:mysql://localhost/hotel_db?useUnicode=true&useJDBCCompliantTimezoneShift=true"
									+ "&useLegacyDatetimeCode=false&serverTimezone=UTC","root","lys1823");
					Statement stmt = conn.createStatement();
					String sql = "select distinct hotelinfo.hotelId, hotelName "
							+ "from hotelinfo, roominfo "
							+ "where state = ? and city = ? and village = ? "
							+ "	and hotelinfo.hotelId = roominfo.hotelId and peopleAvailable>=? and "
							+ "	(roominfo.hotelId, roomInfo.roomId) not in ("
							+ "		select hotelId, roomId "
							+ "		from hotelreservation "
							+ "		where startTime<=STR_TO_DATE(?, '%Y%m%d') and endTime>=STR_TO_DATE(?, '%Y%m%d'));";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1,state);
					pstmt.setString(2,city);
					pstmt.setString(3,village);
					pstmt.setInt(4, peopleNum);
					pstmt.setString(5,start);
					pstmt.setString(6,end);
					
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
					    System.out.println("Hotel ID = " + rs.getString("hotelId") + ", Hotel name = " + rs.getString("hotelName"));
					}
					stmt.close();
					conn.close();
				}
				catch(SQLException sqle){
					System.out.println("SQLException : "+sqle);
				}
			}
			else if(number == 2) {
				System.out.println("�����ϴ�ȣ��ID�� �Է��ϼ��� ex)aaaa1");
				String hotelId = sc.nextLine();
				System.out.println("���ϴ� �ּ���������� �Է��ϼ��� ex)4");
				int score = sc.nextInt();
				sc.nextLine();
				try{
					Connection conn = 
							DriverManager.getConnection("jdbc:mysql://localhost/hotel_db?useUnicode=true&useJDBCCompliantTimezoneShift=true"
									+ "&useLegacyDatetimeCode=false&serverTimezone=UTC","root","lys1823");
					Statement stmt = conn.createStatement();
					String sql = "select  rentstore.storeId, rentstore.storeName,  avg(score)\r\n"
							+ "from rentStore, rentCarReservation, rentCarReview\r\n"
							+ "where rentcarreservation.reservationId = rentcarreview.reservationId and \r\n"
							+ "	rentcarreservation.storeID = rentstore.storeID\r\n"
							+ "	and userId in (select userId\r\n"
							+ "	from hotelreservation\r\n"
							+ "	where HotelId = ?)\r\n"
							+ "group by rentstore.storeId, rentstore.storeName\r\n"
							+ "having avg(score)>=?;";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1,hotelId);
					pstmt.setInt(2,score);

					
					ResultSet rs = pstmt.executeQuery();
					while (rs.next())
					{
					    System.out.println("store ID = " + rs.getString("storeId") + ", store name = " + rs.getString("storeName")+ ", ���� " + rs.getFloat("avg(score)"));
					}
					stmt.close();
					conn.close();
				}
				catch(SQLException sqle){
					System.out.println("SQLException : "+sqle);
				}
			}
			else if(number == 3) {
				System.out.println("�����մϴ�.");
				break;
			}
			else{
				System.out.println("�߸��� ���� �ԷµǾ����ϴ�.");
			}
		}
	}
}