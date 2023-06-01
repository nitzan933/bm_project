package server.db_logic;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;

import server.exceptions.BMServerException;
import utility.enums.ErrorType;

public class OpenReoprtDBController {
	Connection dbConnection;
	String dbName;
	private final String tableNameBranchToCEO = "branch_to_ceo";
	private final String ceoReportTable = "ceo_report";
	
	public OpenReoprtDBController(DBController dbController) {
		this.dbConnection = dbController.getDBConnection();
		this.dbName = dbController.getDBName();
		
	}
	
	
	public String openReoprt(String type,String year,String month,String path,String branch) throws BMServerException {
		PreparedStatement ps;
		FileOutputStream output = null;
		String date = year + "-" + month;
		String result;
		int b=0;
        try {
        	String tableName = getTableName(type);
        	if(tableName.equals("error")) {
        		throw new BMServerException(ErrorType.REPORT_NOT_EXIST, "ERROR_OPEN_REPORT");
        	}
        	String query = "SELECT file  FROM  `"+ dbName + "`." + tableName + " WHERE date = ? AND branch = ?";
            ps = dbConnection.prepareStatement(query);
            ps.setString(1, date);
            ps.setString(2, branch);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
            	throw new BMServerException(ErrorType.REPORT_NOT_EXIST, "REPORT_NOT_EXIST");
            }
            String fileName = "\\" + type + "(" + date + ").pdf";
    		File report = new File(path + fileName);
            InputStream ins =rs.getBinaryStream(1);
            BufferedInputStream bis = new BufferedInputStream(ins);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data =new byte[ins.available()];
            try {
            output = new FileOutputStream(report);
            }catch(IOException e2) {
            	//e2.printStackTrace();
    			throw new BMServerException(ErrorType.FILE_ALREADY_OPEN, "FILE_ALREADY_OPEN");
            }
            while ((b = ins.read(data)) != -1) {
                 output.write(data, 0, b);
            }
            output.close();
            result = Base64.getEncoder().encodeToString(data);
            System.out.println(result);
            return result;
        } catch (SQLException e) {
        	e.printStackTrace();
			throw new BMServerException(ErrorType.REPORT_NOT_EXIST, "REPORT_NOT_EXIST");
		}
		 catch (IOException e1) {
        	e1.printStackTrace();
			throw new BMServerException(ErrorType.REPORT_NOT_EXIST, "REPORT_NOT_EXIST");
		}
	}
	

	public String openQuarterlyReport(String quarter, String year, String path) throws BMServerException {
	PreparedStatement ps;
	FileOutputStream output = null;
	String month = getMonthOfQuarter(quarter);
	String date = year + "-" + month;
	String result;
	int b=0;
    try {
    	String query = "SELECT file  FROM  `"+ dbName + "`." + ceoReportTable + " WHERE date = ? ";
        ps = dbConnection.prepareStatement(query);
        ps.setString(1, date);
        ResultSet rs = ps.executeQuery();
        if(!rs.next()) {
        	throw new BMServerException(ErrorType.REPORT_NOT_EXIST, "REPORT_NOT_EXIST");
        }
        String repTitle = year + "-" + "quarter " + quarter;
		 String fileName = "\\QuarterlyReport-(" + repTitle + ").pdf";
		File report = new File(path + fileName);
        InputStream ins =rs.getBinaryStream(1);
        BufferedInputStream bis = new BufferedInputStream(ins);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data =new byte[4096];
        try {
        output = new FileOutputStream(report);
        }catch(IOException e2) {
        	//e2.printStackTrace();
			throw new BMServerException(ErrorType.FILE_ALREADY_OPEN, "FILE_ALREADY_OPEN");
        }
        while ((b = ins.read(data)) != -1) {
             output.write(data, 0, b);
        }
        output.close();
        result = Base64.getEncoder().encodeToString(data);
        return result;
    } catch (SQLException e) {
    	e.printStackTrace();
		throw new BMServerException(ErrorType.REPORT_NOT_EXIST, "REPORT_NOT_EXIST");
	}
	 catch (IOException e1) {
    	e1.printStackTrace();
		throw new BMServerException(ErrorType.REPORT_NOT_EXIST, "REPORT_NOT_EXIST");
	}
}
	public String openIncomReoprtSupplier(int resId, String path) throws BMServerException {
		PreparedStatement ps;
		FileOutputStream output = null;
		String result;
		int b=0;
	    try {
	    	String query = "SELECT incomeFile  FROM  `"+ dbName + "`.restaurant WHERE resId = ? ";
	        ps = dbConnection.prepareStatement(query);
	        ps.setInt(1, resId);
	        ResultSet rs = ps.executeQuery();
	        if(!rs.next()) {
	        	throw new BMServerException(ErrorType.REPORT_NOT_EXIST, "REPORT_NOT_EXIST");	
	        }
	        String fileName = "\\IncomeReport.pdf";
	        File report = new File(path + fileName);
	        InputStream ins =rs.getBinaryStream(1);
	        byte[] data =new byte[4096];
	        try {
	            output = new FileOutputStream(report);
	            }catch(IOException e2) {
	            	//e2.printStackTrace();
	    			throw new BMServerException(ErrorType.FILE_ALREADY_OPEN, "FILE_ALREADY_OPEN");
	            }
	            while ((b = ins.read(data)) != -1) {
	                 output.write(data, 0, b);
	            }
	            output.close();
	            result = Base64.getEncoder().encodeToString(data);
	            return result;
	    	}catch (SQLException e) {
	        	e.printStackTrace();
	    		throw new BMServerException(ErrorType.REPORT_NOT_EXIST, "REPORT_NOT_EXIST");
	    	}
	    	 catch (IOException e1) {
	        	e1.printStackTrace();
	    		throw new BMServerException(ErrorType.REPORT_NOT_EXIST, "REPORT_NOT_EXIST");
	    	}
	}
	

	
	private String getMonthOfQuarter(String quarter) {
		String month;
		switch(quarter) {
			case "1":
				month = "03";
				break;
			case "2":
				month = "06";
				break;
			case "3":
				month = "09";
				break;
			case "4":
				month = "12";
				break;
			default:
				month = "error";
				break;	
		}
		return month;
	}


	public void saveFileForCEOInPdf(byte[] arrByteForFile,String branch) throws BMServerException {
		ByteArrayInputStream bais = new ByteArrayInputStream(arrByteForFile);
		PreparedStatement ps;
		Calendar cal = Calendar.getInstance();
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int month = (((cal.get(Calendar.MONTH)+11))%12)+1;
		int year = (cal.get(Calendar.YEAR));
		try {
			String query = "INSERT INTO `"+ dbName + "`." + tableNameBranchToCEO + " (date,branch,file) values(?,?,?)";
			ps = dbConnection.prepareStatement(query);
			ps.setString(1,String.valueOf(year)+"-"+String.valueOf(month));
			ps.setString(2,branch);
			ps.setBlob(3, bais);
			ps.execute();
		} catch (SQLException e) {
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		}
	}


	//genarte name for the right table according to the requset
	private String getTableName(String type) {
		String tableName="";
		switch(type) {
			case "Income":
				tableName = "income_report";
				break;
			case "Order":
				tableName = "order_report";
				break;
			case "Performance":
				tableName = "performance_report";
				break;
			case "reportFromBranch":
				tableName="branch_to_ceo";
				break;
			default:
				tableName = "error";
				break;
		}
		return tableName;
				
	
	}
	//check if the file is open 
	 private boolean isFileClosed(File file) {  
         boolean closed;
         Channel channel = null;
         try {
             channel = new RandomAccessFile(file, "rw").getChannel();
             closed = true;
         } catch(Exception ex) {
             closed = false;
         } finally {
             if(channel!=null) {
                 try {
                     channel.close();
                 } catch (IOException ex) {
                     // exception handling
                 }
             }
         }
         return closed;
 }


	public ArrayList<String> getBranchReportsFroCEO() throws BMServerException {
		String query;
		String row;
		ArrayList<String> result = new ArrayList<String>();
		query = "SELECT date,branch FROM `"+ dbName + "`." + tableNameBranchToCEO;
		 PreparedStatement ps;
		try {
			ps = dbConnection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				row = rs.getString(1) + " " + rs.getString(2);
				result.add(row);
			}
			return result;
		} catch (SQLException e) {
			throw new BMServerException(ErrorType.ERROR_CREATING_REPORT, "ERROR_CREATING_REPORT");
		}
	    
	}


	


	
	
	
}
