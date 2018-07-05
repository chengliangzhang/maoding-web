package com.maoding.user.dao.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.user.dao.UserAttachDao;
import com.maoding.user.entity.UserAttachEntity;


@Service("userAttachDao")
public class UserAttachDaoImpl extends GenericDao<UserAttachEntity> implements UserAttachDao{

	@Override
	public List<UserAttachEntity> getUserAttachByUserId(String userId) {
		return this.sqlSession.selectList("UserAttachEntityMapper.selectByUserId", userId);
	}

	@Override
	public int delUserAttachByUserId(String userId) {
		return this.sqlSession.delete("UserAttachEntityMapper.deleteByUserId", userId);
	}
	
	public  List<UserAttachEntity>  getAttachByType(Map<String,Object>paraMap){
		return this.sqlSession.selectList("UserAttachEntityMapper.selectByType", paraMap);
	}

	@Override
	public String getHeadImg(String userId) {
		return this.sqlSession.selectOne("UserAttachEntityMapper.getHeadImgUrl", userId);
	}

}
