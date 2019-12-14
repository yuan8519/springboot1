package com.great.springboot.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminHandler
{
	@RequestMapping("/handlerRequest.action")
	public ModelAndView handleRequest(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;
	}

	@RequestMapping("/login.action")
	public String login(HttpServletRequest request, HttpServletResponse response){
		System.out.println(request.getParameter("username")+request.getParameter("password"));
	return "success";
	}

}
