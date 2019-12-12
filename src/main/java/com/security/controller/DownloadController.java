package com.security.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DownloadController
{
	@RequestMapping(value = "/download.html", method = RequestMethod.GET)
	public void downloadFile(@RequestParam("fileid") int fileid, HttpServletResponse response) {
		//ModelAndView mc = new ModelAndView();
		System.out.println("downloadFile called.");
//		Connection c=DbConnection.getConnection();
//		try {
//			PreparedStatement ps=c.prepareStatement("select filename,file from files where fileid=?");
//			ps.setInt(1,fileid);
//			ResultSet rs=ps.executeQuery();
//			if(rs.next())
//			{
//				String filename = rs.getString(1);
//				Blob o=rs.getBlob(2);
//				byte[] file = o.getBytes(1, (int)o.length());
//				ByteArrayInputStream bais = new ByteArrayInputStream(file);
//				response.setContentType("application/octet-stream");
//	            response.addHeader("Content-Disposition", "attachment; filename="+filename);
//	            try
//	            {
//	                IOUtils.copy(bais, response.getOutputStream());
//	                response.getOutputStream().flush();
//	            }
//	            catch (IOException ex) {
//	                ex.printStackTrace();
//	            }
//			}
//		
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			File file = new File("E:\\Users\\Documents\\SpringSecurity\\Upload\\"+fileid).listFiles()[0];
			String filename = file.getName();
			byte[] bytes = FileUtils.readFileToByteArray(file);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename="+filename);
            try
            {
                IOUtils.copy(bais, response.getOutputStream());
                response.getOutputStream().flush();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
