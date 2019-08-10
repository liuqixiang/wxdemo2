package com.example.wxdemo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.wxdemo.service.JdbcService;
import com.example.wxdemo.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
	@Autowired
	private JdbcService jdbcService;
	@GetMapping
	public String hello() {
		return "hello world";
	}

	@GetMapping("/map")
	public Map<String, Object> map() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "lqx");
		map.put("age", 18);

		return map;
	}
	@GetMapping("/test01")
	public Map<String,Object> test01() {
		List<Map<String, Object>> maps = jdbcService.selectContentExt(0, 10,"");
		Map<String,Object> resultMap=new HashMap<>();
		resultMap.put("data",maps);
		Integer count = jdbcService.selectCount("");
		PageUtil pageUtil=new PageUtil(0,10,count);
		resultMap.put("page",pageUtil);
		return resultMap;
	}
	@GetMapping("/test02")
	public Map<String,Object> test02() {
		Map<String, Object> maps = jdbcService.getContentByContentId(43);

		return maps;
	}
}
