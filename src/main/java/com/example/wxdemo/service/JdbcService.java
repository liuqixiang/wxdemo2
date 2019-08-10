package com.example.wxdemo.service;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JdbcService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public void save() {
		jdbcTemplate.update("insert into t_test(name) values(?)", "test1");
		System.out.println(1 / 0);
		jdbcTemplate.update("insert into t_test(name) values(?)", "test2");
	}

	public List<Map<String, Object>> selectContentExt(Integer start,Integer pageSize,String keyword) {
		//参数列表
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from jc_content_ext where 1=1 ");
		if(StringUtils.isNotBlank(keyword)){
			sql.append("and title like ? ");
			params.add("%"+keyword+"%");
		}
		sql.append("order by release_date desc limit ?,? ");
		params.add(start);
		params.add(pageSize);
		Object[] para = params.toArray(new Object[params.size()]);
		List<Map<String, Object>> listMaps = jdbcTemplate.queryForList(sql.toString(),para);
		return listMaps;
	}

	public Map<String, Object> getContentByContentId(Integer contentId){
		String sql="select ce.*,ct.txt from jc_content_ext ce left join jc_content_txt ct on ce.content_id=ct.content_id where ce.content_id=?";
		List<Map<String, Object>> listMaps  = jdbcTemplate.queryForList(sql, new Object[]{contentId});
		return listMaps.get(0);
	}


	public Integer selectCount(String keyword) {
		//参数列表
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(1) from jc_content_ext where 1=1 ");
		if(StringUtils.isNotBlank(keyword)){
			sql.append("and title like ? ");
			params.add("%"+keyword+"%");
		}
		Object[] para = params.toArray(new Object[params.size()]);
		Integer count=jdbcTemplate.queryForObject(sql.toString(),para,Integer.class);
		return count;
	}
}
