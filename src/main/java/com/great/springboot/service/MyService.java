package com.great.springboot.service;


import com.great.springboot.javabeen.*;
import com.great.springboot.dao.MyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyService
{
	@Resource
	private MyMapper myMapper;
//登录

	public Usertable findUserById(String username, String password){
		Map<String,String> map = new HashMap<>();
		map.put("username",username.trim());
		map.put("password",password.trim());
		return myMapper.findUserById(map);
}

//表格数据回显和查询
     public TableBean userfindId(int page, String username, String startDate, String endDate){
	     System.out.println(page);
        int i = (page-1)*3;
//	     int m = page * 3;
	     Pageinf pageinf = new Pageinf();
	     pageinf.setPage(i);
//	     pageinf.setLimit(m);
	     pageinf.setUsername(username);
	     pageinf.setStartDate(startDate);
         pageinf.setEndDate(endDate);
         List<Usertable> list = myMapper.userfindId(pageinf);
	     System.out.println("hhh"+list.size());

         for (Usertable usertable : list)
	{
		usertable.setTypename(usertable.getTypetable().getTypename());
		System.out.println(usertable.toString());
	}
	     TableBean tableBean = new TableBean();
	     tableBean.setCode(0);
	     tableBean.setCount(myMapper.selectPageCount(pageinf));
	     System.out.println("h1"+tableBean.getCount());
	     tableBean.setData(list);
	     return tableBean;

     }

     //删
     public int deUser(int userid){

         int delsql = myMapper.deluser(userid);

    return delsql;

}

    //增加用户
	public int adduser(Usertable usertable){

		int user = myMapper.adduser(usertable);

		System.out.println("usertable>."+usertable);

		 return user ;
	}

	//修改用户
	public int updateuser(Usertable usertable){

	return myMapper.upUser(usertable);
	}
    //获取动态菜单
	public List<Menutable> menuID(int userid){
		return myMapper.menuID(userid);
	}

    //获取一二级菜单
	public List<Treetable> quanTree(int userid){
		return myMapper.quanTree(userid);
	}
    //获取角色
	public List<Roletable> findByRid(){
		return myMapper.findByRid();
	}

	public int roleQuan(int roleid,List<Integer> list){
		return myMapper.uploadRole(roleid,list);
	}

	//删除角色
	public int delRidTree(int roleid){
		return myMapper.delRidTree(roleid);
	}

	public int menuRoleId(int roleid,int menuid){
		return myMapper.menuRoleId(roleid,menuid);
	}







}
