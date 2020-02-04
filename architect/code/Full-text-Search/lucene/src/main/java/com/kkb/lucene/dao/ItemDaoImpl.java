package com.kkb.lucene.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.kkb.lucene.po.Item;

public class ItemDaoImpl implements ItemDao {

	@Override
	public List<Item> queryItemList() {
		// 数据库链接
				Connection connection = null;
				// 预编译statement
				PreparedStatement preparedStatement = null;
				// 结果集
				ResultSet resultSet = null;
				// 图书列表
				List<Item> list = new ArrayList<Item>();

				try {
					// 加载数据库驱动
					Class.forName("com.mysql.jdbc.Driver");
					// 连接数据库
					connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456");

					// SQL语句
					String sql = "SELECT * FROM item";
					// 创建preparedStatement
					preparedStatement = connection.prepareStatement(sql);
					// 获取结果集
					resultSet = preparedStatement.executeQuery();
					// 结果集解析
					while (resultSet.next()) {
						Item item = new Item();
						item.setId(resultSet.getInt("id"));
						item.setName(resultSet.getString("name"));
						item.setPrice(resultSet.getDouble("price"));
						item.setPic(resultSet.getString("pic"));
						item.setDescription(resultSet.getString("description"));
						list.add(item);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return list;

	}

}
