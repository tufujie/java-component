package com.jef.util;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.util.List;
import java.util.Map;

/**
 * solr工具类
 * @author Jef
 * @date 2019/11/5
 */
public class SolrUtil {
    private static final String SOLR_URL = "http://localhost:8983/solr/";
    public static final String USER_CORE_NAME = "user";
    public static final String TESTALL_CORE_NAME = "testAll";

    /**
     * 创建solr客户端
     * @return
     */
    public static SolrClient getSolrClient(String coreName) {
        // 构建Solr客户端
        HttpSolrClient.Builder builder = new HttpSolrClient.Builder(SOLR_URL + coreName);
        return builder.build();
    }

    /**
     * // 查询
     * SolrJ提供的查询功能比较强大，可以进行结果中查询、范围查询、排序等。
     * 补充一下范围查询的格式：[star t TO end]，start与end是相应数据格式的值的字符串形式，“TO” 一定要保持大写！
     * field 查询的字段名称数组 key 查询的字段名称对应的值 start 查询的起始位置 count 一次查询出来的数量 sortfield
     * 需要排序的字段数组 flag 需要排序的字段的排序方式如果为true 升序 如果为false 降序 hightlight 是否需要高亮显示
     * @param queryParams 请求条件
     * @param start 设置起始位置
     * @param count 返回结果数
     * @param sortFields 排序字段
     * @param flags 对应的排序方式，true升序，false降序
     * @param hightLight 是否高亮
     * @return
     */
    public static QueryResponse search(Map<String, Object> queryParams, int start,
                                       int count, List<String> sortFields, List<Boolean> flags, Boolean hightLight, String coreName, String... fields) {
        SolrClient solrClient = getSolrClient(coreName);
        // 检测输入是否合法
        if (queryParams == null || queryParams.size() == 0) {
            return null;
        }
        if (null == sortFields || null == flags
                || sortFields.size() != flags.size()) {
            return null;
        }
        SolrQuery query = null;
        try {
            // 初始化查询对象
            query = new SolrQuery("*:*");
            query.setFields(fields);
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                query.addFilterQuery(entry.getKey() + ":" + entry.getValue());
            }
            query.setStart(start);
            query.setRows(count);
            // 设置排序
            for (int i = 0; i < sortFields.size(); i++) {
                if (flags.get(i)) {
                    query.addSort(sortFields.get(i), SolrQuery.ORDER.asc);
                } else {
                    query.addSort(sortFields.get(i), SolrQuery.ORDER.desc);
                }
            }
            // 设置高亮
            if (null != hightLight) {
                query.setHighlight(hightLight); // 开启高亮组件
                query.addHighlightField("name");// 高亮字段
                query.setHighlightSimplePre("<font color=\"red\">");// 标记
                query.setHighlightSimplePost("</font>");
                query.setHighlightSnippets(1);// 结果分片数，默认为1
                query.setHighlightFragsize(1000);// 每个分片的最大长度，默认为100

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        QueryResponse rsp = null;
        try {
            rsp = solrClient.query(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // 返回查询结果
        return rsp;
    }
}