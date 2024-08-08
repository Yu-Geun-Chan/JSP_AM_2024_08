package com.KoreaIT.java.jsp_AM.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.KoreaIT.java.jsp_AM.dao.ArticleDao;
import com.KoreaIT.java.jsp_AM.util.DBUtil;
import com.KoreaIT.java.jsp_AM.util.SecSql;

public class ArticleService {

	private Connection conn;
	private ArticleDao articleDao;
	
	public ArticleService(Connection conn) {
		this.conn = conn;
		this.articleDao = new ArticleDao(conn);		
	}

	public int getTotalCnt() {
		return  articleDao.getTotalCnt();
	}

	public List<Map<String, Object>> getForPrintArticles(int limitFrom, int itemsInAPage) {
		return articleDao.getForPrintArticles(limitFrom, itemsInAPage);
	}

}
