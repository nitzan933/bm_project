package server.db_logic;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import server.exceptions.BMServerException;
import utility.enums.ErrorType;
import utility.enums.OrderType;
//TODO: func get month as arg, delete files maybe? generate only on dates 
public class ReportsDBController {

	private final String ordersTableInDB = "orders";
	private final String restaurantTableInDB = "restaurant";
	private final String dishInOrderTableInDB = "dish_in_order";
	private final String dishesTableInDB = "dishes";
	private final String IncomeReportTableNameInDB = "income_report";
	private final String orderReportTableNameInDB = "order_report";
	private final String performanceReportTableNameInDB = "performance_report";
	private final String ceoReportTableNameInDB = "ceo_report";
	HashMap <Integer,Integer> restaurantTotalIncome; 
	HashMap <Integer,Integer> restaurantOrder = new HashMap<Integer,Integer>();
	HashMap <Integer,String> restaurantNameAndId = new HashMap<Integer,String>();
	HashMap <Integer,ArrayList<String>> orderRowPdf = new HashMap<Integer,ArrayList<String>>();
	HashMap <Integer,ArrayList<Integer>> resCountDelays = new HashMap<Integer,ArrayList<Integer>>();
	HashMap <String,ArrayList<String>> PerformanceRowPdf = new HashMap<String,ArrayList<String>>();
	ArrayList <String> rowInPdf ;
	
	Connection dbConnection;
	String dbName;
	
	public ReportsDBController(DBController dbController) {
		this.dbConnection = dbController.getDBConnection();
		this.dbName = dbController.getDBName();
		
	}
	
//	public String checkDate() {
//		PreparedStatement ps;
//		String query = "SELECT CURRENT_DATE()";
//		try {
//			ps = dbConnection.prepareStatement(query);
//			ResultSet rs= ps.executeQuery();
//			if(rs.next()){
//				return rs.getString(1);
//				
//			} 
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//		
//	}
	
	public void createIncomeReport(String month,String year,String branch)  throws BMServerException{
		rowInPdf = new ArrayList<String>();
		restaurantTotalIncome = new HashMap<Integer,Integer>();
		int currTotalPrice;
		PreparedStatement ps,ps2;
		try {
			System.out.println(String.valueOf(year)+"-"+String.valueOf(month)+"-01");
			String query = "SELECT resId,totalPrice FROM  `"+ dbName + "`." + ordersTableInDB + " WHERE status = ? and (timeOfOrder BETWEEN ? and ?) ";
			ps = dbConnection.prepareStatement(query);
			ps.setString(1, "done");
			ps.setString(2, year+"-"+month+"-01");
			ps.setString(3, year+"-"+month+"-31");
			ResultSet rs= ps.executeQuery();
			while(rs.next()) {
				// Add price of next order to existing restaurant
				if (restaurantTotalIncome.containsKey(rs.getInt(1))){
					currTotalPrice = restaurantTotalIncome.get(rs.getInt(1));
					currTotalPrice += rs.getInt(2);
					restaurantTotalIncome.replace(rs.getInt(1),currTotalPrice);
				}
				else
				{
					// Add new restaurant
					restaurantTotalIncome.put(rs.getInt(1), rs.getInt(2));
				}
			
			}
			query = "SELECT resId,resName  FROM  `"+ dbName + "`." + restaurantTableInDB +  " WHERE branch = ?";
			ps2=dbConnection.prepareStatement(query);
			ps2.setString(1, branch);
			rs= ps2.executeQuery();
			while(rs.next()) {
				if(restaurantTotalIncome.containsKey(rs.getInt(1))) {
					rowInPdf.add(rs.getString(2)+": "+restaurantTotalIncome.get(rs.getInt(1)));
					
				}
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		}
		
		generatePDF(IncomeReportTableNameInDB,"Income Report",month,year,branch,rowInPdf,null);
		
	}
	
	public void createIncomeReportForEachSupplier(String month,String year)  throws BMServerException{
		restaurantTotalIncome = new HashMap<Integer,Integer>();
		int currTotalPrice;
		PreparedStatement ps,ps2;
		System.out.println("enter inot the fuction ");
		try {
			System.out.println(String.valueOf(year)+"-"+String.valueOf(month)+"-01");
			String query = "SELECT resId,totalPrice FROM  `"+ dbName + "`." + ordersTableInDB + " WHERE status = ? and (timeOfOrder BETWEEN ? and ?) ";
			ps = dbConnection.prepareStatement(query);
			ps.setString(1, "done");
			ps.setString(2, year+"-"+month+"-01");
			ps.setString(3, year+"-"+month+"-31");
			ResultSet rs= ps.executeQuery();
			while(rs.next()) {
				// Add price of next order to existing restaurant
				if (restaurantTotalIncome.containsKey(rs.getInt(1))){
					currTotalPrice = restaurantTotalIncome.get(rs.getInt(1));
					currTotalPrice += rs.getInt(2);
					restaurantTotalIncome.replace(rs.getInt(1),currTotalPrice);
				}
				else
				{
					// Add new restaurant
					restaurantTotalIncome.put(rs.getInt(1), rs.getInt(2));
				}
			
			}
			query = "SELECT resId,resName  FROM  `"+ dbName + "`." + restaurantTableInDB ;
			ps2=dbConnection.prepareStatement(query);
			rs= ps2.executeQuery();
			while(rs.next()) {
				if(restaurantTotalIncome.containsKey(rs.getInt(1))) {
					ArrayList<String> commisionIncome = new ArrayList<String>();
					commisionIncome.add(rs.getString(2)+": ");
					commisionIncome.add("Before comission the income- " + restaurantTotalIncome.get(rs.getInt(1)));
					commisionIncome.add("After commission the income is- "+ ((double)restaurantTotalIncome.get(rs.getInt(1)))*0.93);
					System.out.println(commisionIncome);
					generatePdfForComissionFile(restaurantTableInDB,"IncomeReport:" + rs.getString(2),month,year,commisionIncome,rs.getInt(1));
				}
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		}
		
		
		
	}
	
	public void generatePdfForComissionFile(String table,String title,String month,String year, ArrayList<String> txt,int resId) throws BMServerException {
		Document doc =  new Document(); 
		String location;
		try {			
			location = "C:\\reports\\"+title +"-("+year+"-"+month+").pdf";
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(location));
			System.out.println("PDF created."); 
			
			doc.open();
			doc.add(new Paragraph(title));
			for(String row : txt) {
				doc.add(new Paragraph(row));
			}
			doc.close();
			writer.close();
			savebillReportForSupplier(location,resId);
			System.out.println("pdf saved to db");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		}

	}
	

	public void createOrderReport(String month,String year,String branch)  throws BMServerException{
		rowInPdf = new ArrayList<String>();
		restaurantOrder = new HashMap<Integer,Integer>();
		restaurantNameAndId = new HashMap<Integer,String>();
		orderRowPdf = new HashMap<Integer,ArrayList<String>>(); 
		int counter;
		PreparedStatement ps,ps2;
		try {
			String query = "SELECT dishId FROM `"+ dbName + "`." + dishInOrderTableInDB +" as A, "+"`"+ dbName + "`." + ordersTableInDB+" as B WHERE B.status = ? AND(( A.orderId = B.orderId) AND (B.timeOfOrder LIKE ?))";
			ps = dbConnection.prepareStatement(query);
			ps.setString(1, "done");
			ps.setString(2, year+"-"+month+"%");
			ResultSet rs= ps.executeQuery();
			while(rs.next()) {
				// Add amount of dishes to existing dish
				if (restaurantOrder.containsKey(rs.getInt(1))){
					counter = restaurantOrder.get(rs.getInt(1));
					counter ++;
					restaurantOrder.replace(rs.getInt(1),counter);
				}
				else
				{
					// Add new dish
					restaurantOrder.put(rs.getInt(1), 1);
				}
			}
			query = "SELECT resId,resName  FROM  `"+ dbName + "`." + restaurantTableInDB + " WHERE branch = ?";
			ps2=dbConnection.prepareStatement(query);
			ps2.setString(1, branch);
			rs= ps2.executeQuery();
			while(rs.next()) {
				restaurantNameAndId.put(rs.getInt(1), rs.getString(2));
			}
			query = "SELECT dishId,resId,name  FROM  `"+ dbName + "`." + dishesTableInDB;
			ps2=dbConnection.prepareStatement(query);
			rs= ps2.executeQuery();
			while(rs.next()) {
				if(restaurantNameAndId.containsKey(rs.getInt(2)) && restaurantOrder.containsKey(rs.getInt(1))) {
					if(orderRowPdf.containsKey(rs.getInt(2))) {
						orderRowPdf.get(rs.getInt(2)).add(rs.getString(3)+": "+restaurantOrder.get(rs.getInt(1)));
						
					}
					else {
						orderRowPdf.put(rs.getInt(2), new ArrayList<String>());
						orderRowPdf.get(rs.getInt(2)).add(rs.getString(3)+": "+restaurantOrder.get(rs.getInt(1)));
						
					}
				}
			}
			
			for(int key : orderRowPdf.keySet()) {
				rowInPdf.add(restaurantNameAndId.get(key));
				rowInPdf.addAll(orderRowPdf.get(key));
				
			}
			generatePDF(orderReportTableNameInDB, "order report",month,year, branch, rowInPdf,null);
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		}	
	}
	
	
	
	public void createPerformanceReport(String month,String year,String branch)  throws BMServerException{
		rowInPdf = new ArrayList<String>();
		int countonTime=0,countDelay=0;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		resCountDelays = new HashMap<Integer,ArrayList<Integer>>();
		PreparedStatement ps,ps2;
		try {
			// Get resID all delayed orders  
			String query = "SELECT resId FROM  `"+ dbName + "`." + ordersTableInDB 
					+ " WHERE status = ? AND ((timeOfOrder LIKE ? AND ((typeOfOrder=?) OR (typeOfOrder=?)) AND (time_to_sec(TIMEDIFF(timeOfArrival,timeOfOrder))/60>60)) "
					+ " OR (timeOfOrder LIKE ? AND typeOfOrder=? AND (time_to_sec(TIMEDIFF(timeOfArrival,timeOfOrder))/60>20)))" ;
			ps = dbConnection.prepareStatement(query);
			ps.setString(1,"done");
			ps.setString(2, year+"-"+month+"%");
			ps.setString(3, OrderType.DELIVERY_REGULAR.toString());
			ps.setString(4, OrderType.DELIVERY_ROBOT.toString());
			ps.setString(5, year+"-"+month+"%");
			ps.setString(6, OrderType.DELIVERY_EARLY.toString());
			ResultSet rs= ps.executeQuery();
			while(rs.next()) {
				// Add amount of delays to existing res
				if (resCountDelays.containsKey(rs.getInt(1))){
					temp = resCountDelays.get(rs.getInt(1));
					countDelay=temp.remove(0);
					temp.add(0, ++countDelay);
					resCountDelays.replace(rs.getInt(1),temp); 
				}
				else
				{
					// Add new res
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.add(1);
					res.add(0);
					resCountDelays.put(rs.getInt(1), res);
				}

			}
			
			// Get resId all orders on time 
			query = "SELECT resId FROM  `"+ dbName + "`." + ordersTableInDB 
					+ " WHERE status = ? AND ((timeOfOrder LIKE ? AND ((typeOfOrder=?) OR (typeOfOrder=?)) AND (time_to_sec(TIMEDIFF(timeOfArrival,timeOfOrder))/60<=60)) "
					+ " OR (timeOfOrder LIKE ? AND typeOfOrder=? AND (time_to_sec(TIMEDIFF(timeOfArrival,timeOfOrder))/60<=20)))" ;
			ps = dbConnection.prepareStatement(query);
			ps.setString(1,"done");
			ps.setString(2, year+"-"+month+"%");
			ps.setString(3, OrderType.DELIVERY_REGULAR.toString());
			ps.setString(4, OrderType.DELIVERY_ROBOT.toString());
			ps.setString(5, year+"-"+month+"%");
			ps.setString(6, OrderType.DELIVERY_EARLY.toString());
			rs= ps.executeQuery();
			while(rs.next()) {
				// Add amount of on time to existing res
				if (resCountDelays.containsKey(rs.getInt(1))){
					temp = resCountDelays.get(rs.getInt(1));
					countonTime=temp.remove(1);
					temp.add(1, ++countonTime);
					resCountDelays.replace(rs.getInt(1),temp); 
				}
				else
				{
					// Add new res and no delays
					ArrayList<Integer> res = new ArrayList<Integer>();
					res.add(0);
					res.add(1);
					resCountDelays.put(rs.getInt(1), res);
				}

			}
			// Get names and ID of restaurant
			query = "SELECT resId,resName  FROM  `"+ dbName + "`." + restaurantTableInDB + " WHERE branch = ?";
			ps2=dbConnection.prepareStatement(query);
			ps2.setString(1, branch);
			rs= ps2.executeQuery();
			while(rs.next()) {
				ArrayList<Integer> delayeAndOnTime = resCountDelays.get(rs.getInt(1));
				if(delayeAndOnTime != null){
				rowInPdf.add(rs.getString(2)+": Delayed- "+String.valueOf(delayeAndOnTime.get(0))+" On time- "+String.valueOf(delayeAndOnTime.get(1)));
				}else {
					rowInPdf.add(rs.getString(2)+": Delayed- 0 On time- 0");
				}
			}
			generatePDF(performanceReportTableNameInDB,"performance report",month,year,branch,rowInPdf,null);
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		}
		
		
	}
	
	public void ceoReport(String month,String year) throws BMServerException {
		rowInPdf = new ArrayList<String>();
		restaurantTotalIncome = new HashMap<Integer,Integer>();
		PreparedStatement ps;
		HashMap<Integer,Integer> countOrder = new HashMap<Integer,Integer>(); 
		int currTotalPrice,count,totalIncomeInQuarter=0;
		try {
			//(timeOfOrder >= '2021-07-01%' and timeOfOrder <'2021-09-01%') and status = "done";
			String query = "SELECT resId,totalPrice FROM  `"+ dbName + "`." + ordersTableInDB + " WHERE (timeOfOrder >= ? and timeOfOrder < ?) and status = ?";
			ps = dbConnection.prepareStatement(query);
			int range = Integer.parseInt(month) - 2;
			if(range < 10) {
				ps.setString(1, year+"-0"+String.valueOf((range))+"-01");
				System.out.println(year+"-0"+String.valueOf((range))+"-01");
			}else {
				ps.setString(1, year+"-"+String.valueOf((range))+"-01");
			}
			int monthInt = Integer.parseInt(month) + 1;
			String newMonth;
			if(monthInt < 10) {
				newMonth = "0"+String.valueOf(monthInt); 
			}else {
				newMonth = String.valueOf(monthInt); 
			}
			
			ps.setString(2, year+"-"+ newMonth +"-01");
			ps.setString(3, "done");
			ResultSet rs= ps.executeQuery();
			System.out.println("before while after query");
			while(rs.next()) {
				System.out.println("in the while");
				// Add price of next order to existing restaurant
				if (restaurantTotalIncome.containsKey(rs.getInt(1))){
					currTotalPrice = restaurantTotalIncome.get(rs.getInt(1));
					currTotalPrice += rs.getInt(2);
					restaurantTotalIncome.replace(rs.getInt(1),currTotalPrice);
					//Count how many orders for each restaurant
					count = countOrder.get(rs.getInt(1));
					countOrder.replace(rs.getInt(1),++count);
					System.out.println("count: " + count);
					System.out.println("resId: "+ rs.getInt(1));
				}
				else
				{
					// Add new restaurant
					restaurantTotalIncome.put(rs.getInt(1), rs.getInt(2));
					countOrder.put(rs.getInt(1), 1);
				}

	
			}
			
			JFreeChart barChart = ChartFactory.createBarChart("Order quarterly report","Restaurant","Amount"
	    		  ,createDataset(countOrder),PlotOrientation.VERTICAL,true, true, false);
		      
			for (Integer key : restaurantTotalIncome.keySet()) {
				totalIncomeInQuarter+=restaurantTotalIncome.get(key);
		      }
	

			rowInPdf.add("Total income in quarter: "+totalIncomeInQuarter);
			
			query = "SELECT resId,resName  FROM  `"+ dbName + "`." + restaurantTableInDB;
			ps=dbConnection.prepareStatement(query);
			rs= ps.executeQuery();
			while(rs.next()) {
				if(restaurantTotalIncome.containsKey(rs.getInt(1))) {
					rowInPdf.add(rs.getString(2)+": "+restaurantTotalIncome.get(rs.getInt(1)));
				}
			}
			generatePDF(ceoReportTableNameInDB,"ceo quarterly report for",month,year,"",rowInPdf,barChart);
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		}
		
	}
	
	   public CategoryDataset createDataset(HashMap<Integer,Integer> data ) {      
		     
		      final DefaultCategoryDataset dataset = 
		      new DefaultCategoryDataset( );  
		      
		      for (Integer key : data.keySet()) {
				dataset.addValue((double)data.get(key), key, key);
		      }

		      return dataset; 
		   }
	
	public void generatePDF(String table,String title,String month,String year,String branch, ArrayList<String> txt,JFreeChart barChart) throws BMServerException {
		Document doc =  new Document(); 
		String location;
		try {
			if(table == "ceo_report") {
				int range = Integer.parseInt(month) - 3;
				location="C:\\reports\\"+title+year+"-"+month+"-"+String.valueOf(range)+".pdf";
			}
			else {
				location = "C:\\reports\\"+title +"-" +  branch + " "+year+"-"+month+".pdf";
			}
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(location));
			System.out.println("PDF created."); 
			
			doc.open();
			doc.add(new Paragraph(title));
			if(barChart != null) {
				PdfContentByte cb = writer.getDirectContent();
				PdfTemplate bar = cb.createTemplate(600, 400);
				Graphics2D g2d2 = new PdfGraphics2D(bar, 600, 400);
				Rectangle2D r2d2 = new Rectangle2D.Double(0, 0, 600, 400);
				barChart.draw(g2d2, r2d2);
				g2d2.dispose();
				cb.addTemplate(bar, 0, 0);
			}
			for(String row : txt) {
				doc.add(new Paragraph(row));
			}
			doc.close();
			writer.close();
			saveReport(table,location,month,year,branch);
			System.out.println("pdf saved to db");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		}

	}
	


	private byte[] getByteArrayFromFile(String location) throws BMServerException {
	    try {
	    	
		    final byte[] buffer = new byte[500];
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			File newFile = new File (location);
			FileInputStream fis = new FileInputStream(newFile); 
		    BufferedInputStream bis = new BufferedInputStream(fis);
	
		    int read = -1;
		    while ((read = bis.read(buffer)) > 0) {
		        baos.write(buffer, 0, read);
		    }
		    bis.close();
		    return baos.toByteArray();
	    }

	    catch(IOException e) {
			e.printStackTrace();
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
	    }

	}
	
	private void saveReport(String tableName,String location,String month,String year,String branch) {
		PreparedStatement ps;
		String query;

		try {
		query = "INSERT INTO `"+ dbName + "`." + tableName+"(date,file,branch) values(?,?,?)";
		ps = dbConnection.prepareStatement(query);
		ps.setString(1, year+"-"+month);
		ByteArrayInputStream bais = new ByteArrayInputStream(getByteArrayFromFile(location));
		ps.setBlob(2, bais);
		ps.setString(3, branch);
		ps.execute();
		}
		catch (Exception e){
			e.printStackTrace();
		}

	}
	private void savebillReportForSupplier(String location,int resId) {
		PreparedStatement ps;
		String query;
		try {
			query = "UPDATE `"+ dbName + "`." + restaurantTableInDB + " SET incomeFile = ? WHERE resId = ?";
			ps = dbConnection.prepareStatement(query);
			ByteArrayInputStream bais = new ByteArrayInputStream(getByteArrayFromFile(location));
			ps.setBlob(1, bais);
			ps.setInt(2, resId);
			ps.executeUpdate();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}

//	public static void main(String[] args) {
//		DBController dbc;
//        dbc = DBController.getDBControllerInstanceFor("bm-db", "root", "Tzachi1234!");
//        dbc.connectToDBServer();
//        ReportsDBController report = new ReportsDBController(dbc);
//		try {
//			report.createIncomeReportForEachSupplier("12","2021");
//		} catch (BMServerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}
