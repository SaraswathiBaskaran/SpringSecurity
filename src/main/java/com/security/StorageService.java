package com.security;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.web.multipart.MultipartFile;

import com.connection.DbConnection;

public class StorageService {


	    public int store(MultipartFile file,String email)
	    {
	    	Connection c=DbConnection.getConnection();
	    	int fileid=1;
	    	try {
	    		Statement s = c.createStatement();
	    		ResultSet rs = s.executeQuery("select max(fileid) from files");
	    		if(rs.next()) {
	    			fileid=rs.getInt(1)+1;
	    		}
	    		else {
	    			fileid=1;
	    		}
				PreparedStatement st=c.prepareStatement("insert into files(username,fileid,file,filename,clientemail) values (?,?,?,?,?)");
				st.setString(1,"client");
				Blob b=c.createBlob();
				b.setBytes(1,file.getBytes());
				st.setInt(2, fileid);
				st.setBlob(3,b);
				st.setString(4,file.getOriginalFilename());
				st.setString(5,email);
				st.executeUpdate();
				
				new File("E:\\Users\\Documents\\SpringSecurity\\Upload\\"+fileid).mkdir();
				File file2 = new File("E:\\Users\\Documents\\SpringSecurity\\Upload\\"+fileid+"\\"+file.getOriginalFilename());
		        FileOutputStream fos = null;
		        try {
		            fos = new FileOutputStream(file2);
		            // Writes bytes from the specified byte array to this file output stream 
		            fos.write(file.getBytes());
		        }
		        catch (FileNotFoundException e) {
		            System.out.println("File not found" + e);
		        }
		        catch (IOException ioe) {
		            System.out.println("Exception while writing file " + ioe);
		        }
		        finally {
		            // close the streams using close method
		            try {
		                if (fos != null) {
		                    fos.close();
		                }
		            }
		            catch (IOException ioe) {
		                System.out.println("Error while closing stream: " + ioe);
		            }
		        }
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	    	return fileid;
	    }

	    
}
