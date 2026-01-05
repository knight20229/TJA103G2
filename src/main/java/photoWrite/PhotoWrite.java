package photoWrite;

import java.sql.*;
import java.util.Arrays;
import java.io.*;

class PhotoWrite {

	public static void main(String argv[]) {
		Connection con = null;
		PreparedStatement pstmt = null;
		InputStream fin = null;
		String url = "jdbc:mysql://localhost:3306/dreamhouse?serverTimezone=Asia/Taipei";
		String userid = "root";
		String passwd = "123456";
		String photos = "src/main/resources/static/images"; //測試用圖片已置於【專案錄徑】底下的【resources/images】目錄內
		String update = "update product set image_data =? where product_id=?";

		int maxProducts = 8; 
		int count = 1;
		
		try {
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(update);
			File[] photoFiles = new File(photos).listFiles();
			Arrays.sort(photoFiles);
			
			
			for (File f : photoFiles) {
				
				if (count > maxProducts) {
	                System.out.println("圖片數量多於資料庫列數，已停止於第 " + (count-1) + " 筆。");
	                break;
	            }
				
				if (f.isFile()) {
				
				fin = new FileInputStream(f);
				pstmt.setBinaryStream(1, fin);
				pstmt.setInt(2, count);
								
				int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("更新成功: " + f.getName() + " -> product_id: " + count);
                } else {
                    System.out.println("更新失敗: 資料庫找不到 product_id " + count);
                }
                
				count++;
				System.out.print(" update the database...");
				System.out.println(f.toString());
				fin.close();

				}
			}

			pstmt.close();
			System.out.println("加入圖片-更新成功.........");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
