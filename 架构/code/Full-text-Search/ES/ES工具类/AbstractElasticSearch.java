 
import com.alibaba.fastjson.JSON;
 
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

/**
 * ES搜索服务的抽象类
 * 
 
 *
 * @param <T>
 */
public abstract class AbstractElasticSearch<T> {
	/** 日志记录 */
	public static final Logger logger = LoggerFactory.getLogger(AbstractElasticSearch.class);

	// 创建访问ES服务器的客户端

	protected Client client = ElasticSearchClientHelper.getInstance().getClient();

	/**
	 * 索引名称 相当于数据库的数据名称
	 * 
	 * @return
	 */
	protected abstract String getIndex();

	/**
	 * 类型名称 相当于数据库的表名
	 * 
	 * @return
	 */
	protected abstract String getType();

	/**
	 * 获取集成类的类对象
	 * 
	 * @return
	 */
	protected abstract Class<T> getBeanClass();

//	/**
//	 * 自定义的查询条件 HashMap<查询的字段, 查询的内容>
//	 *
//	 * @param map
//	 * @return
//	 */
//	public abstract List<T> query(HashMap<String, Object> map);

//	/**
//	 * 将数据重新加载到ES到服务器中
//	 */
//	public abstract void reLoad();

	/**
	 * 将list集合转换为key为id和value为对象的map
	 * 
	 * @param list
	 * @return
	 */
	protected  HashMap<String, T> listToMap(List<T> list){
		if (list == null || list.size() <= 0) {
			return null;
		}

		HashMap<String, T> map = new HashMap<>();
		for (T obj : list) {
			if (obj != null) {
				try {
					map.put(obj.getClass().getDeclaredField("id").get(obj).toString(), obj);
				} catch (IllegalAccessException e) {
					logger.error("list to map error",e);
				} catch (NoSuchFieldException e) {
					logger.error("list to map error no such field Exception",e);
				}
			}
		}

		return map;
	}

//	/**
//	 * 将map转换为对象，主要用于返回数据转换为正常对象
//	 *
//	 * @param map
//	 * @return
//	 */
//	protected abstract T mapToBean(Map<String, Object> map);

	/**
	 * 指定id批量添加
	 * 
	 * map中的 String是id T是对象
	 * 
	 * @param map
	 * @return
	 */
	public BulkResponse addDocByBulk(Map<String, T> map) {
		BulkRequestBuilder builder = client.prepareBulk();
		// 批量添加
		int count = 0;
		for (Entry<String, T> entry : map.entrySet()) {
			if (entry.getKey() == null || entry.getKey().length() <= 0 || entry.getValue() == null) {
				continue;
			}
			try {
				String json = JSON.toJSONString(entry.getValue());
				builder.add(
						client.prepareIndex(getIndex(), getType(), entry.getKey()).setSource(json, XContentType.JSON));
				// 每1000条刷新提交一次
				if (count % 1000 == 0) {
					builder.execute().actionGet();
					logger.debug("addDocByBulk index[{}] type[{}] num[{}]", getIndex(), getType(), count);
				}
				count++;
			} catch (Exception e) {
				logger.error("{}", e);
				continue;
			}

		}
		BulkResponse response = builder.execute().actionGet();
		logger.debug("addDocByBulk index[{}] type[{}] num[{}] status[{}]", getIndex(), getType(), count,
				response.status());
		return response;
	}

	/**
	 * 批量添加，id由es自动生成
	 * 
	 * @param list
	 * @return
	 */
	public BulkResponse addDocByBulk(List<T> list) {
		// List<T> list
		BulkRequestBuilder builder = client.prepareBulk();
		// 批量添加
		int count = 0;
		for (T t : list) {
			if (t == null) {
				continue;
			}
			try {
				builder.add(client.prepareIndex(getIndex(), getType()).setSource(JSON.toJSONString(t),
						XContentType.JSON));
				// 每1000条刷新提交一次
				if (count % 1000 == 0) {
					builder.execute().actionGet();
					logger.info("提交了：" + count);
				}
				count++;
			} catch (Exception e) {
				logger.error("{}", e);
				continue;
			}
		}
		BulkResponse response = builder.execute().actionGet();
		logger.info("addDocByBulk response status {}",response.status());
		// 查看是否有失败情况
		if (response.hasFailures()) {
			logger.info("addDocByBulk 存在错误");
			logger.error("addDocByBulk ByList index[{}] type[{}] num[{}] status[{}] hasFailures[{}]", getIndex(),
					getType(), count, response.status(), response.hasFailures());
		}
		logger.debug("addDocByBulk ByList index[{}] type[{}] num[{}] status[{}] hasFailures[{}]", getIndex(), getType(),
				count, response.status(), response.hasFailures());
		return response;
	}

	/**
	 * 添加 单个
	 * 
	 * @param id
	 * @param bean
	 * @throws Exception
	 */
	public void addDoc(Object id, T bean) throws Exception {
		if (id == null && bean == null) {
			return;
		}
		logger.debug("addDoc index[{}] type[{}] id[{}]", getIndex(), getType(), id);

		client.prepareIndex(getIndex(), getType(), id.toString())
				.setSource(JSON.toJSONString(bean), XContentType.JSON).get();
	}

	/**
	 * 更新文档 单个 保证ES搜索库中没有否则用upsert方式
	 * 
	 * @param id
	 * @param bean
	 * @throws Exception
	 */
	public void updateDoc(Object id, T bean) throws Exception {
		if (id == null && bean == null) {
			return;
		}
		logger.debug("updateDoc upsert index[{}] type[{}] id[{}]", getIndex(), getType(), id);
		client.prepareUpdate(getIndex(), getType(), id.toString()).setDoc(JSON.toJSONString(bean), XContentType.JSON)
				.get();
	}

	/**
	 * 通过upsert进行更新，如果没有就插入
	 * 
	 * @param id
	 * @param bean
	 * @throws Exception
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void updateDocByUpsert(Object id, T bean) throws Exception, InterruptedException, ExecutionException {
		if (id == null && bean == null) {
			return;
		}
		String json = JSON.toJSONString(bean);
		// 1. 先定义好要添加的数据 (如果没更新成功就添加的数据)
		IndexRequest indexRequest = new IndexRequest(getIndex(), getType(), id.toString()).source(json,
				XContentType.JSON);
		// 2. 定义好要修改的的数据，如果修改不成功，则进行upsert操作 进行插入
		UpdateRequest updateRequest = new UpdateRequest(getIndex(), getType(), id.toString())
				.doc(JSON.toJSONString(bean), XContentType.JSON).upsert(indexRequest);
		// 3. 提交update请求 操作成功返回OK，否则返回NOT_FOUND
		UpdateResponse response = client.update(updateRequest).get();

		logger.debug("updateDoc index[{}] type[{}] id[{}] status[{}] ", getIndex(), getType(), id, response.status());
		// 4. 查看添加文档是否成功
		// logger.info(response.status());
	}

	/**
	 * 通过Bulk 批量更新
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public BulkResponse updateByBulk(Map<String, T> map) throws Exception {
		if (map == null || map.isEmpty()) {
			return null;
		}
		BulkRequestBuilder builder = client.prepareBulk();
		// 批量添加
		int count = 0;
		for (Entry<String, T> entry : map.entrySet()) {
			if (entry.getKey() == null || entry.getKey().length() <= 0 || entry.getValue() == null) {
				continue;
			}
			try {
				String json = JSON.toJSONString(entry.getValue());
				builder.add(
						client.prepareUpdate(getIndex(), getType(), entry.getKey()).setDoc(json, XContentType.JSON));
				// 每1000条刷新提交一次
				if (count % 1000 == 0) {
					builder.execute().actionGet();
					logger.debug("addDocByBulk index[{}] type[{}] num[{}]", getIndex(), getType(), count);
				}
				count++;
			} catch (Exception e) {
				logger.error("{}", e);
				continue;
			}

		}
		BulkResponse response = builder.execute().actionGet();
		logger.debug("updateByBulk index[{}] type[{}] num[{}] status[{}]", getIndex(), getType(), count,
				response.status());
		logger.info("updateByBulk response status {}",response.status());
		return response;

	}

	// 按id删除
	public void delDoc(Object id) {
		if (id == null) {
			return;
		}
		logger.debug("delDoc  index[{}] type[{}] id[{}]", getIndex(), getType(), id);
		// 3. 指定要删除的文档
		client.prepareDelete(getIndex(), getType(), id.toString()).get();
	}

	/**
	 * 按id批量删除 调用bulk
	 * 
	 * @param ids
	 */
	public void delDocByBulk(Object[] ids) {

		if (ids == null || ids.length <= 0) {
			return;
		}
		BulkRequestBuilder builder = client.prepareBulk();
		// 批量添加
		int count = 0;
		for (Object id : ids) {
			if (id == null || "".equals(id.toString())) {
				continue;
			}
			builder.add(client.prepareDelete(getIndex(), getType(), id.toString()));
			// 每1000条刷新提交一次
			if (count % 1000 == 0) {
				builder.execute().actionGet();
				logger.info("delDocByBulk ids index[{}] type[{}] num[{}]", getIndex(), getType(), count);
			}
			count++;
		}
		BulkResponse response = builder.execute().actionGet();
		logger.debug("delDocByBulk ByList index[{}] type[{}] num[{}] status[{}] hasFailures[{}]", getIndex(), getType(),
				count, response.status(), response.hasFailures());
		// 查看是否有失败情况
		if (response.hasFailures()) {
			logger.info("delDocByBulk 存在错误");
			logger.error("delDocByBulk ByList index[{}] type[{}] num[{}] status[{}] hasFailures[{}]", getIndex(),
					getType(), count, response.status(), response.hasFailures());
		}
	}

	// 查询 按id查询

	/**
	 * 获取单个对象
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public T get(Object id) throws Exception {
		if (id == null || id.toString().trim().length() <= 0) {
			return null;
		}
		// 3. 数据查询展示
		GetResponse response = client.prepareGet(getIndex(), getType(), id.toString()).execute().actionGet();
		String sourceAsString = response.getSourceAsString();
		return JSON.parseObject(sourceAsString,getBeanClass());

	}

	public List<T> multiGet(Object[] ids) throws Exception {
		// 如果id为空 就返回null
		if (ids == null || ids.length <= 0) {
			return null;
		}

		// 数据查询展示
		MultiGetRequestBuilder prepareMultiGet = client.prepareMultiGet();
		for (Object id : ids) {
			if (StringUtils.isNotBlank(id.toString())) {
				prepareMultiGet.add(getIndex(), getType(), id.toString());
			}
		}
		MultiGetResponse multiGetResponse = prepareMultiGet.get();
		// 遍历输出一下
		List<T> list = new ArrayList<>();
		for (MultiGetItemResponse response : multiGetResponse) {
			GetResponse gr = response.getResponse();
			if (gr != null && gr.isExists() && StringUtils.isNotBlank(gr.getSourceAsString())) {
				list.add(JSON.parseObject(gr.getSourceAsString(), getBeanClass()));
				logger.info("multiGet responseAsString {}",gr.getSourceAsString());
			}
		}

		return list;
	}

	/**
	 * 按条件查询
	 * 
	 * @param queryBuilder
	 *            查询器
	 * @param from
	 *            分页 从那个开始 默认为0
	 * @param size
	 *            分页 每页几条 默认为10
	 * @return
	 * @throws Exception
	 */
	public List<T> query(QueryBuilder queryBuilder, int from, int size, List<SortBuilder> sortBuilders, Page<T> page) {
		// 查询器没有的话就返回null
		if (queryBuilder == null) {
			return null;
		}
		// 输入条件重置
		if (from < 0) {
			from = 0;
		}
		if (size <= 1) {
			size = 20;
		}
		// 按照size大小创建list集合，节省内存
		List<T> list = new ArrayList<>(size);

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(getIndex()).setTypes(getType()).setQuery(queryBuilder).setFrom(from).setSize(size);
		//排序
		if (CollectionUtils.isNotEmpty(sortBuilders)){
			for (SortBuilder sortBuilder :sortBuilders){
				searchRequestBuilder.addSort(sortBuilder);
			}
		}
		//设置返回排除字段
		if (CollectionUtils.isNotEmpty(page.getExclude())){
			List<String> stringList = page.getExclude();
			String[] excludeArray =stringList.toArray(new String[stringList.size()]);
			searchRequestBuilder.setFetchSource(null,excludeArray);
		}
		//设置返回指定字段
		if (CollectionUtils.isNotEmpty(page.getInclude())){
			List<String> stringList = page.getInclude();
			String[] includeArray =stringList.toArray(new String[stringList.size()]);
			searchRequestBuilder.setFetchSource(includeArray,null);
		}
		//搜索
		long startTime = System.currentTimeMillis();
 		SearchResponse response = searchRequestBuilder.get();
 		long endTime = System.currentTimeMillis();
		logger.info(searchRequestBuilder.toString());
		logger.info("es query hit total {} ,hits cost time [{} ms] ",response.getHits().getTotalHits(),endTime-startTime);
		page.setCount( (int)response.getHits().getTotalHits()) ;
		for (SearchHit hit : response.getHits()) {
//			logger.info("es query hit getSourceAsString {}",hit.getSourceAsString());
			try {
				list.add(JSON.parseObject(hit.getSourceAsString(), getBeanClass()));
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("{},{}", hit.getSourceAsString(), e);
			}
		}
		return list;
	}

//	public List<T> query(String fieldName, Object value, int from, int size) {
//		if (StringUtils.isBlank(fieldName)) {
//			return null;
//		}
//		if (value == null) {
//			return null;
//		}
//		return query(QueryBuilders.matchQuery(fieldName, value), from, size);
//	}

	/**
	 * 联合查询
	 * 
	 * @param must
	 *            必须成立
	 * @param mustNot
	 *            必须不
	 * @param should
	 *            或者
	 * @param filter
	 *            过滤
	 * @param from
	 * @param size
	 * @return
	 */
//	public List<T> boolQuery(QueryBuilder must, QueryBuilder mustNot, QueryBuilder should, QueryBuilder filter,
//			int from, int size) {
//		if (must == null && mustNot == null && should == null) {
//			return null;
//		}
//		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//		// 必须 且
//		if (must != null) {
//			boolQuery.must(must);
//		}
//		// 必须不 否
//		if (mustNot != null) {
//			boolQuery.mustNot(mustNot);
//		}
//		// 或者
//		if (should != null) {
//			boolQuery.should(should);
//		}
//		// 过滤条件
//		if (filter != null) {
//			boolQuery.filter(filter);
//		}
//		return query(boolQuery, from, size);
//	}

	/**
	 * 按照关键字查询，查询内容不分词
	 * 
	 * @param fieldName
	 *            查询范围
	 * @param value
	 *            查询值
	 * @param from
	 *            分页 开始行数
	 * @param size
	 *            每页多少行
	 * @return
	 */
//	public List<T> termQuery(String fieldName, Object value, int from, int size) {
//		return query(QueryBuilders.termQuery(fieldName, value), from, size);
//	}

	/**
	 * 按关键字查询，查询内容不分词，一个
	 * 
	 * @param fieldName
	 * @param value
	 * @param from
	 * @param size
	 * @return
	 */
//	public List<T> termsQuery(String fieldName, Object[] value, int from, int size) {
//		return query(QueryBuilders.termsQuery(fieldName, value), from, size);
//	}

	/**
	 * 多个字段查询一个查询词
	 * 
	 * @param fieldName
	 * @param value
	 * @param from
	 * @param size
	 * @return
	 */
//	public List<T> multiMatchQuery(String[] fieldName, Object value, int from, int size) {
//		return query(QueryBuilders.multiMatchQuery(value, fieldName), from, size);
//	}

	/**
	 * 获取索引mapping的Json字符串 如果自定义的mapping最好重写
	 * 
	 * @return
	 */
	public String getMapping() {
		ImmutableOpenMap<String, MappingMetaData> mappings = client.admin().cluster().prepareState().execute()
				.actionGet().getState().getMetaData().getIndices().get(getIndex()).getMappings();
		if (mappings == null || mappings.isEmpty()) {
			return null;
		}
		return mappings.get(getType()).source().toString();
	}

	/**
	 * 
	 * @Description: 重构索引(更新词库之后)
	 */
	public void reindex() {
		SearchResponse scrollResp = client.prepareSearch(getIndex())//
				.setScroll(new TimeValue(60000))//
				.setQuery(QueryBuilders.matchAllQuery())//
				.setSize(100).get(); // max of 100 hits will be returned for
		// Scroll until no hits are returned
		do {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				client.prepareIndex(getIndex(), hit.getType(), hit.getId())
						.setSource(hit.getSourceAsString(), XContentType.JSON).execute().actionGet();
			}
			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute()
					.actionGet();
		} while (scrollResp.getHits().getHits().length != 0);
	}





}
