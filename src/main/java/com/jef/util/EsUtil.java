package com.jef.util;

import com.jef.constant.BasicConstant;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.InetAddresses;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xcontent.XContentFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Jef
 * @date: 2021/5/10 15:55
 */
public class EsUtil {
    private static final Logger logger = LogManager.getLogger(EsUtil.class);

    // 集群地址（多个机器ip的话用,隔开）
    private static String esServerIps = BasicConstant.ES_SERVER_IPS;
    // 集群名称
    private static String clusterName = BasicConstant.ES_CLUSTER_NAME;
//	private static int port = BasicConstant.ES_PORT; // 端口号

    private static TransportClient client = null;
    private static BulkProcessor bulkProcessor = null;

    private static Object lockObj = new Object();
    private static Object lockObj2 = new Object();

    //指定该方法在对象被创建后马上调用 相当于配置文件中的init-method属性
    private static void init() {
        logger.info("EsearchTool bean init()...");
        if (client != null) {
            return;
        }
        try {
            // 设置集群名称
            Settings settings = Settings.builder().put("cluster.name", clusterName).build();
            // 创建client
            client = new PreBuiltTransportClient(settings);
            String esIps[] = esServerIps.split(",");
            // 添加集群IP列表
            for (String esIp : esIps) {
                String[] ipPort = esIp.split(":");
                TransportAddress transportAddress = new TransportAddress(InetAddresses.forString(ipPort[0]), Integer.valueOf(ipPort[1]));
                client.addTransportAddress(transportAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("EsearchTool -> getClient() occur exception:" + e.toString());
        }
    }

    /**
     * 获取TransportClient实例
     */
    public static TransportClient getClient() {
        if (client == null) {
            synchronized (lockObj) {
                init();
            }
        }
        return client;
    }

    /**
     * 插入数据，指定id的值
     * <br/> 注：如果id已存在，数据被更新
     */
    public static IndexResponse insertData(TransportClient client, String index, String type, String id, Map<String, Object> map) {
        IndexResponse response = client.prepareIndex(index, type, id)
                .setSource(map).execute().actionGet();
        return response;
    }

    /**
     * 根据id查询
     */
    public static Map<String, Object> getDataById(TransportClient client, String index, String type, String id) {
        GetResponse actionGet = client.prepareGet(index, type, id).execute().actionGet();
        Map<String, Object> resultMap = actionGet.getSourceAsMap();
        return resultMap;
    }

    /**
     * 批量查询某些id对应的数据
     */
    public List<Map<String, Object>> getDataByIds(TransportClient client, String index, String type, String... ids) {
        MultiGetResponse multiGetResponse = client.prepareMultiGet().add(index, type, ids).get();
        List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
        for (MultiGetItemResponse itemResponse : multiGetResponse) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                Map<String, Object> sourceAsMap = response.getSourceAsMap();
                resultMapList.add(sourceAsMap);
            }
        }
        return resultMapList;
    }


    /**
     * 获取通用的查询部分
     */
    public BoolQueryBuilder getGeneralBoolQueryBuilder(Map<String, Object> map) {
        // 查询建立
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (String in : map.keySet()) {
            if (!StringUtils.equals("startTime", in) && !StringUtils.equals("endTime", in)) {
                Object value = map.get(in);
                boolQueryBuilder.must(QueryBuilders.matchQuery(in, value)); //must表示and
            }
        }

        // 时间范围查询
        if (map.get("startTime") != null || map.get("endTime") != null) {
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createdTime");
            if (map.get("startTime") != null) {
                rangeQueryBuilder.from(map.get("startTime"));
            }
            if (map.get("endTime") != null) {
                rangeQueryBuilder.to(map.get("endTime"));
            }

            boolQueryBuilder.must(rangeQueryBuilder);
        }
        return boolQueryBuilder;
    }

    /**
     * 多条件 模糊查询
     *
     * @param sortField 排序字段，如果不排序传空字符串
     * @param sortType  desc 降序，asc 升序
     */
    public SearchResponse getDataByMultilConditionQuery(TransportClient client, String index, String type, BoolQueryBuilder boolQueryBuilder, int from, int pageSize, String sortField, String sortType) {
        long sTime = System.currentTimeMillis();
        //生成DSL查询语句
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        SearchResponse response;

        sourceBuilder.query(boolQueryBuilder);
        logger.info("EsearchTool - getDataByMultilConditionQuery() query sql -> " + sourceBuilder.toString());

        SearchRequestBuilder requestBuilder = client.prepareSearch(index).setTypes(type);
        if (StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(sortType)) {//如果需要排序
            org.elasticsearch.search.sort.SortOrder sortOrder = sortType.equals("desc") ? org.elasticsearch.search.sort.SortOrder.DESC : org.elasticsearch.search.sort.SortOrder.ASC;
            requestBuilder = requestBuilder.addSort(sortField, sortOrder);
        }

        response = requestBuilder
                .setQuery(boolQueryBuilder)
                .setFrom(from).setSize(pageSize)
                .setRequestCache(false) // 设置是否使用缓存
                .setTimeout(TimeValue.timeValueSeconds(5)) // 设置超时时间 -> 5s
                .setExplain(true).execute().actionGet();
        long eTime = System.currentTimeMillis();
        long time = eTime - sTime;
        logger.info("getDataByMultilConditionQuery() spend time : {} ms", time); // 查询耗时？毫秒

        return response;
    }

    /**
     * 将查询结果转成list
     */
    public List<Map<String, Object>> responseToList(SearchResponse response) {
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits().value;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (total > 0) {
//			for (SearchHit hit : hits) {
//				Map<String, Object> map = hit.getSourceAsMap();
//				list.add(map);
//			}
            for (int i = 0; i < hits.getHits().length; i++) {
                Map<String, Object> map = hits.getHits()[i].getSourceAsMap();
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 获取页面查询结果的总记录数
     */
    public int getPageTotalCount(SearchResponse response) {
        SearchHits hits = response.getHits();
        Long total = hits.getTotalHits().value;
        return total.intValue();
    }

    //指定该方法在对象销毁之前调用 相当于配置文件中的destory-method属性
    public void close() {
        logger.info("EsearchTool bean close()...");
        if (client != null) {
            client.close();
        }
    }

    /**
     * 获取BulkProcessor实例
     */
    public BulkProcessor getBulkProcessor(TransportClient client) {
        if (bulkProcessor == null) {
            synchronized (lockObj2) {
                bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {

                    /**
                     * beforeBulk会在批量提交之前执行，可以从BulkRequest中获取请求信息request.requests()或者请求数量request.numberOfActions()
                     */
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {

                    }

                    /**
                     * 该方法会在批量成功后执行，可以跟beforeBulk配合计算批量所需时间
                     */
                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {

                    }

                    /**
                     * 该方法会在批量失败后执行
                     */
                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable throwable) {
                        logger.error("{} data bulk failed,resason : {}", request.numberOfActions(), throwable);
                    }
                })
                        .setBulkActions(5000) //设置提交批处理操作的请求阀值数
                        .setBulkSize(new ByteSizeValue(50, ByteSizeUnit.MB)) //设置提交批处理操作的请求大小阀值
                        .setConcurrentRequests(2) //设置并发处理线程个数
                        .setFlushInterval(TimeValue.timeValueSeconds(5)) //设置刷新索引时间间隔
                        .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(500), 5))  //设置回滚策略，等待时间500ms,retry次数为5次
                        .build();
                // 当请求超过5000个（default=1000）或者总大小超过50M（default=5M）时，触发批量提交动作，注：后面的参数根据实际情况调整
            }
        }
        return bulkProcessor;
    }

    /**
     * 删除数据，指定id的值
     */
    public static DeleteResponse deleteData(TransportClient client, String index, String type, String id) {
        DeleteResponse response = client.prepareDelete(index, type, id).get();
        return response;
    }

    /**
     * 更新数据，指定id的值
     */
    public static IndexResponse updateData(TransportClient client, String index, String type, String id, Map<String, Object> map) {
        return insertData(client, index, type, id, map);
    }

    /**
     * 更新数据，指定id的值
     */
    public static UpdateResponse updateDataV2(TransportClient client, String index, String type, String id) throws ExecutionException, InterruptedException, IOException {
        IndexRequest request = new IndexRequest(index, type, id).source(
                XContentFactory.jsonBuilder().startObject()
                        .field("name", "XuUpdate").endObject()
        );
        UpdateRequest updateRequest = new UpdateRequest(index, type, id).doc(
                XContentFactory.jsonBuilder().startObject()
                        .field("name", "XuUpdate2").endObject()
        ).upsert(request);
        UpdateResponse updateResponse = client.update(updateRequest).get();
        return updateResponse;
    }
}