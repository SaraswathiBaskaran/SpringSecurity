package com.security.controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.connection.DbConnection;
import com.security.OtpGenerator;
import com.security.SendMail;
import com.security.StorageService;

@Controller
public class UploadController
{
	/*@RequestMapping(value = "/upload.html", method = RequestMethod.POST)
	public ModelAndView upload() {
		ModelAndView mc = new ModelAndView();
	//	mc.setViewName();
		return mc;
	} */
	 public static boolean isValid(String email) 
	    { 
	        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
	                            "[a-zA-Z0-9_+&*-]+)*@" + 
	                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
	                            "A-Z]{2,7}$"; 
	                              
	        Pattern pat = Pattern.compile(emailRegex); 
	        if (email == null) 
	            return false; 
	        return pat.matcher(email).matches(); 
	    } 
	  
	private final StorageService storageService= new StorageService();
	@RequestMapping(value = "validating.html", method = RequestMethod.POST)
//	@GetMapping("validating.html")
	@ResponseBody
	public String validate(@RequestParam("clientname") String clientname,@RequestParam("email") String email) throws SQLException  
		//ModelAndView mc = new ModelAndView();
		{
		Connection con=DbConnection.getConnection();
//		PreparedStatement st=con.createStatement();
		PreparedStatement ps=null;
		ps=con.prepareStatement("select * from clientdetails where clientname=? AND email=?");
	ps.setString(1, clientname);
	ps.setString(2,email);
			ResultSet rs = ps.executeQuery();	
			
			if(rs.next()/* && rs.getString(1)!=null && rs.getString(1).equals(clientname)&& rs.next() && rs.getString(2)!=null && rs.getString(2).equals(email)*/)
			{
				return /*"redirect:/upload.html"*/"valid";
			}else
			{
				return /*"redirect:/Invalidotp.jsp"*/"invalid";
			}
	//
//return "redirect:/upload.html";
	//return "redirect:/mailcontent.jsp";

		//return "success";
	
	}

  /*  @Autowired
    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }
*/
   /* @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(UploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
*/
    @PostMapping("/upload.html")
    public String handleFileUpload(@RequestParam("f1") MultipartFile file,
            RedirectAttributes redirectAttributes,@RequestParam("clientname") String clientname,@RequestParam("email") String email) throws SQLException {
//String otp=OtpGenerator.generateOtp(10);
        int fileid = storageService.store(file,email);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
System.out.println(file.getOriginalFilename());
Resource r=new ClassPathResource("applicationContext.xml");  //email
BeanFactory b=new XmlBeanFactory(r);    
SendMail m=(SendMail)b.getBean("mailMail"); 

//PreparedStatement ps1;
//String receiver;



String sender="loosyhoppo581@gmail.com";//write here sender gmail id  


Connection c=DbConnection.getConnection();
int linkid=1;
try {
	LocalDateTime time = LocalDateTime.now();
	String timeString = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	Statement s = c.createStatement();
	ResultSet rs = s.executeQuery("select max(linkid) from link");
	if(rs.next()) {
		linkid=rs.getInt(1)+1;
	}
	else {
		linkid=1;
	}
	PreparedStatement st=c.prepareStatement("insert into link(linkid,linktimeofcreation) values (?,?)");
	st.setInt(1,linkid);
	st.setString(2, timeString);
	st.executeUpdate();
}
catch(SQLException e)
{
	e.printStackTrace();
}
try
{
	Connection con=DbConnection.getConnection();
	PreparedStatement ps1=con.prepareStatement("select email from clientdetails where clientname=?");
	ps1.setString(1,clientname);
	
	ResultSet result = ps1.executeQuery();
	
	if(result.next())
	{
		//st.executeUpdate("insert into fileaccesslogs(id,username,fileid,Time) values ('"+id+"','"+username+"','"+fileid+"','"+Time+"')");	
		String receiver = result.getString("email");

	//ps1 = con.prepareStatement("select email from clientdetails where clientname=? and email=?"); 
//	ps1.setString(1, clientname);
//	ps1.setString(1, email);
	//ps.setString(2, otp);
	//ResultSet ry=ps1.executeQuery();
//	receiver=ry.getString(email);//"sharonrithika11@gmail.com";//write here receiver id // localhost= 192.168.1.4
		m.sendMail(sender,receiver,"link for validation and downloading the file.","http://192.168.1.4:8080/SpringSecurity/sendingmail.html?fileid="+fileid+"&linkid="+linkid);
		m.sendMail(sender,"loosyhoppo581@gmail.com","link for validation and downloading the file.","http://localhost:8080/SpringSecurity/sendingmail.html?fileid="+fileid+"&linkid="+linkid);// url: http://localhost:8080/SpringSecurity/otp.jsp?fileid="+fileid);	
		System.out.println("success"); 
	System.out.println("email rx:"+receiver);
	 // String email = "contribute@geeksforgeeks.org"; 
      if (isValid(receiver)) 
          System.out.print("Yes,email checked"); 
      else
          System.out.print("No");
	}
	else 
	{
		System.out.println("wrong");
		return "redirect:/Invalidotp.jsp";
	}
	
}
	catch(Exception e) {
	                                                                                                                                                                                                                                                                                                                                                                                 
		e.printStackTrace();
	}
        return "redirect:/";
    }
 
   
  /*  @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
*/

@RequestMapping("/sendingmail.html")
public String sendingMail(@RequestParam("fileid") int fileid,@RequestParam("linkid") int linkid)
{
	
		ModelAndView m2 = new ModelAndView();
		String otp=OtpGenerator.generateOtp(10);
        //int fileid = storageService.store(file);
        //redirectAttributes.addFlashAttribute("message",
                //"You successfully uploaded " + file.getOriginalFilename() + "!");
//System.out.println(file.getOriginalFilename());
		try
		{
			Connection con=DbConnection.getConnection();
			PreparedStatement ps=con.prepareStatement("select linktimeofcreation, linkcount from link where linkid=?");
			ps.setInt(1, linkid);
			//ps.setString(2, otp);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				//st.executeUpdate("insert into fileaccesslogs(id,username,fileid,Time) values ('"+id+"','"+username+"','"+fileid+"','"+Time+"')");	
				String generatedTimeString = rs.getString("linktimeofcreation");
				LocalDateTime generatedTime = LocalDateTime.parse(generatedTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				LocalDateTime now = LocalDateTime.now();
				if(ChronoUnit.MINUTES.between(generatedTime, now)>30)
				{
					return "redirect:/Linkexpired.jsp";
				}
				else if(rs.getInt("linkcount")>=3)
				{
					return "redirect:/LinkCountExpired.jsp";
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		Connection connection = DbConnection.getConnection();
		try {
			LocalDateTime time = LocalDateTime.now();
			String timeString = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			PreparedStatement ps = connection.prepareStatement("INSERT INTO otp (otp, fileid,time) VALUES (?,?,?)");
			ps.setString(1, otp);
			ps.setInt(2, fileid);
		
			ps.setString(3, timeString);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			PreparedStatement ps = connection.prepareStatement("UPDATE link SET linkcount=linkcount+1 WHERE linkid=?");
			ps.setInt(1, linkid);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String receiver="";
		try
		{
			Connection con=DbConnection.getConnection();
			PreparedStatement ps=con.prepareStatement("select clientemail from files where fileid=?");
			ps.setInt(1, fileid);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				receiver = rs.getString("clientemail");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
Resource r=new ClassPathResource("applicationContext.xml");  
BeanFactory b=new XmlBeanFactory(r);    
SendMail m=(SendMail)b.getBean("mailMail");  
String sender="loosyhoppo581@gmail.com";//write here sender gmail id  admin
//String receiver="sharonrithika11@gmail.com";//write here receiver id  
	//	mc.setViewName();

	
m.sendMail(sender,receiver,"Otp for accesing the  files."," File is being accesed. Your otp is "+otp);
m.sendMail(sender,"loosyhoppo581@gmail.com","Otp for accesing the  files."," File is being accesed. Your otp is "+otp);
//return m2;
return "redirect:/otp.jsp?fileid="+fileid;
}
}
