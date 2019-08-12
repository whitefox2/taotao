package com.taotao.searchs.service.impl;



import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MyTest {
    @Test
    public void demo1() throws IOException, SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.109.102:8080/solr");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","test02");
        document.addField("item_title","测试商品");
        document.addField("item_price","200");
        solrServer.add(document);
        solrServer.commit();
    }
    @Test
    public void demo2() throws IOException, SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.109.102:8080/solr");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","test02");
        document.addField("item_title","测试商品");
        document.addField("item_price","300");
        solrServer.add(document);
        solrServer.commit();
    }
    @Test
    public void demo3() throws IOException, SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.109.102:8080/solr");
        solrServer.deleteById("test02");
        solrServer.commit();
    }
    @Test
    public void demo4() throws IOException, SolrServerException {
        SolrServer solrServer = new HttpSolrServer("http://192.168.109.102:8080/solr");
        SolrQuery query = new SolrQuery();
        //controller肯定是接受数据的  但是接受那些数据 servie不知道
        query.setQuery("测试");
        //df default field 默认域为item_keywords （复制域）
        query.set("df","item_keywords");
        //开启高亮显示
        query.setHighlight(true);
        //高亮显示的域
        query.addHighlightField("item_title");
        //拼接到 item_title域之前
        query.setHighlightSimplePre("<font style='color:red'>");
        //拼接到 item_title域之后
        query.setHighlightSimplePost("</font>");
        //使用solrServer的query方法来执行查询  查询结果集
        QueryResponse queryResponse = solrServer.query(query);
        //通过查询结果集对象的getResults方法得到 文档集合对象
        SolrDocumentList results = queryResponse.getResults();
        for (SolrDocument solrDocument : results) {
            //从文档对象里面根据域名 取域值
            System.out.println(solrDocument.get("id"));
            //我们要从 高亮里面 获取title了
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = null;
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            System.out.println(itemTitle);


            System.out.println(solrDocument.get("item_price"));
        }
    }
}
