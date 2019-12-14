package com.great.springboot.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.great.springboot.javabeen.*;
import com.great.springboot.service.MyService;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class AdminController
{

	private int userid;

	@Resource
	private MyService myService;

	@RequestMapping("/webapp/{url}")
	public String machUrl(@PathVariable(value = "url") String path){
		System.out.println(path);
		return path;
	}


    @RequestMapping("/login")
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");

		return modelAndView ;
	}

	@RequestMapping("/menuTree")
	public ModelAndView menuTree()
	{

		return new ModelAndView("menuTree");
	}


	@RequestMapping("/login.action")
//	@ResponseBody
	public String test1(HttpServletRequest request, HttpServletResponse response, String username, String password)
	{

		Usertable usertable = myService.findUserById(username, password);
		 userid = usertable.getUserid();
		System.out.println("userid..."+userid);

		HashMap<String,ArrayList<Menutable>> map = new HashMap<>();
		List<Menutable> list = myService.menuID(userid);
	    request.getSession().setAttribute("userid",userid);//存userid

		for (int i = 0; i < list.size(); i++)
		{
			Menutable menutable = list.get(i);

			if (map.containsKey(menutable.getMname())){

				ArrayList<Menutable> list1 = map.get(menutable.getMname());
				list1.add(menutable);

			}else {
				ArrayList<Menutable> list1 = new ArrayList<>();
				list1.add(menutable);
				map.put(menutable.getMname(),list1);
			}

		}

		for (Map.Entry<String,ArrayList<Menutable>>entry : map.entrySet()){
			System.out.println("key.."+entry.getKey()+ ":" +"value"+entry.getValue());
		}


		/*1.判断usertable是否为空
		2.为空则返回登录界面，不为空则登录成功，进入菜单界面*/
		if (usertable != null)
		{
			//传值给拦截器，并将用户名（username）set进session域
			request.setAttribute("menu",map);
			request.setAttribute("username",username);
			request.getSession().setAttribute("username",username);
			return "menu";
		} else
		{
			System.out.println("登录失败");
			return "login";
		}

	}


	//增加
	//@RequestMapping("/save")
//	public ModelAndView save(HttpServletRequest request, HttpServletResponse response,Usertable usertable){
//		//ModelAndView modelAndView = new ModelAndView();
//		//String name = request.getParameter("USERNAME");
//
//	}

	@RequestMapping("/usertable.action")
	public ModelAndView usertable(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("userlist");
	}

	private String add(Usertable usertable){
		System.out.println(usertable.getUsername()+","+usertable.getUserpwd()+","+usertable.getUsersex());
		return "增加成功";
	}

	//查询
	//@RequestMapping("/find")



	@RequestMapping(value = "/userlist.action" )
	@ResponseBody
	public TableBean getTable(int page, String username, String startDate, String endDate)
	{

		System.out.println("user.." + username);
		System.out.println("时间" + startDate + "时间2" + endDate);

		TableBean tableBean = myService.userfindId(page, username, startDate, endDate);

        return tableBean;
	}

       @RequestMapping(value = "/delete.action" )
       @ResponseBody
       public Msg delUser(int userid){
	       System.out.println(" 进入删除方法 ");
     	int uid = myService.deUser(userid);
        Msg msg = new Msg();
        if (uid>0){

        	msg.setSendMsg("1");
	        System.out.println("刪除成功");
        }else {

        	msg.setSendMsg("2");
	        System.out.println("刪除失敗");
        }
        return msg;
       }


       @RequestMapping(value = "/add.action")
	   @ResponseBody
	public TableBean adduser(Usertable usertable){
	       System.out.println("增加用户");
           //获取当前时间
	       Date date = new Date();
	       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	       String time = simpleDateFormat.format(date);
	       usertable.setUserregistime(time);
	       int user = myService.adduser(usertable);
	       System.out.println("user。。"+user);
	       TableBean tableBean = new TableBean();
       System.out.println("usertables"+tableBean);

           if (user > 0){
           	tableBean.setMsg("1");
	           System.out.println("增加成功");
           }else {
	           System.out.println("增加失败");
           }

          return tableBean;

       }

       @RequestMapping(value = "/update.action")
       @ResponseBody
	public TableBean updateUser(int userid, String username){
	       System.out.println("修改用户");
	       Usertable usertable = new Usertable();
	       usertable.setUserid(userid);
	       usertable.setUsername(username);
	       int updateuser = myService.updateuser(usertable);
	       System.out.println("updateuser"+updateuser);
	       TableBean tableBean = new TableBean();
	       if (updateuser > 0){
	       	tableBean.setMsg("1");
		       System.out.println("修改成功");
	       }else {
		       System.out.println("修改失败");

	       }
	       return tableBean;

       }

       //文件上传
	@RequestMapping(value = "/file.action")
	@ResponseBody
	public JSONObject uploadFile(String filename, String fileintro, String downloadpoints, MultipartFile  file){
		String filepath = "D:\\mmm\\";//上传路径
		String fileName = filepath + System.currentTimeMillis() + "_" + file.getOriginalFilename();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg","ok");
		System.out.println("控制層："+fileName);
		try{
		FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
			IOUtils.copy(file.getInputStream(),fileOutputStream);
			//file.transferTo(new File("D:\\AAAAA"));
			fileOutputStream.close();
		}catch (Exception e){
			e.printStackTrace();
		}

	return jsonObject;
	}

   //动态菜单
	@RequestMapping(value = "/menu.action")
	@ResponseBody
	public String addMenu(HttpServletRequest request, HttpServletResponse response){
		return "menu";

	}

	@RequestMapping(value = "/menuTree.action")
	@ResponseBody
	public List<Treetable> treeMenu(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("menuTree进入");
	     userid = (int)request.getSession().getAttribute("userid");//获取userid
		List<Treetable> list = myService.quanTree(userid);
        return list;
	}

//	@RequestMapping(value = "/role.action")
//	@ResponseBody
//	public List<Roletable> getRole(){
//		System.out.println("获取角色---controller");
//		List<Roletable> role = myService.findByRid();
//        role.add(Roletable);
//
//		return role;
//	}

	@RequestMapping(value = "/role.action")
	public ModelAndView getRole(){

		ModelAndView modelAndView = new ModelAndView();
		List<Roletable> role = myService.findByRid();

		modelAndView.addObject("role",role);//添加角色
		modelAndView.setViewName("menuTree");

		return modelAndView;

	}

	@RequestMapping(value = "/updateMenuTree.action")
	@ResponseBody
	public String updateTree(String date, String roleid){
		System.out.println("updateMenuTree");
		System.out.println("获取菜单数据"+date);
		System.out.println("获取角色"+roleid);
		Gson gson = new Gson();
		List<Integer> menuTb = new ArrayList<>();
		//将gson的list数据装在list里面
		List<Treetable> list = gson.fromJson(date, new TypeToken<List<Treetable>>() {}.getType());
		for (Treetable treetable : list){
			menuTb.add(treetable.getId());
			for (Treetable child : treetable.getChildren()){
				menuTb.add(child.getId());
			}
		}
			int x = Integer.parseInt(roleid);
		int i = myService.roleQuan(x,menuTb);//添加权限
		if (i > 0){
			return "修改成功";
		}

		int j = myService.delRidTree(Integer.parseInt(roleid));//删除权限


		return null;
	}







}

