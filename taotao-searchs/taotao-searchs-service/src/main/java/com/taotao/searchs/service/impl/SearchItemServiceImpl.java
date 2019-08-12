package com.taotao.searchs.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.SearchItemMapper;
import com.taotao.searchs.dao.SearchDao;
import com.taotao.searchs.service.SearchItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchItemServiceImpl implements SearchItemService {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;
    @Autowired
    private SearchDao searchDao;
    @Override
    public TaotaoResult importAllItems() {
        List<SearchItem> searchItems = searchItemMapper.getItemList();
        for (SearchItem searchItem:searchItems) {
            // 5、向索引库中添加文档。
            try {
                SolrInputDocument document = new SolrInputDocument();
                // 4、为文档添加域
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategory_name());
                document.addField("item_desc", searchItem.getItem_desc());
                solrServer.add(document);
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                solrServer.commit();
            } catch (SolrServerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TaotaoResult.ok();
    }

    @Override
    public SearchResult search(String queryString, int page, int rows) throws Exception {
        SolrQuery query = new SolrQuery();
        if(queryString!=null&&!"".equals(queryString)){
            query.setQuery(queryString);
        }else {
            query.setQuery("*:*");
        }
        query.setStart((page-1)*rows);
        query.setRows(rows);
        // 4、需要指定默认搜索域。
        query.set("df", "item_keywords");

        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<span style='color:red'/");
        query.setHighlightSimplePost("</span>");
        // 6、执行查询，调用SearchDao。得到SearchResult
        /*QueryResponse response = solrServer.query(query);
        //取商品列表
        SolrDocumentList solrDocumentList = response.getResults();
        //商品列表
        List<SearchItem> itemList = new ArrayList<>();
        for (SolrDocument solrDocument : solrDocumentList) {
            SearchItem item = new SearchItem();
            item.setId((String) solrDocument.get("id"));
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            item.setImage((String) solrDocument.get("item_image"));
            item.setPrice((long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = "";
            //有高亮显示的内容时。
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            item.setTitle(itemTitle);
            //添加到商品列表
            itemList.add(item);
        }
        SearchResult result = new SearchResult();
        //商品列表
        result.setItemList(itemList);
        //总记录数
        long totalNum = solrDocumentList.getNumFound();
        result.setRecordCount(totalNum);
         */
        SearchResult result = searchDao.search(query);
        long totalPage = result.getRecordCount()%rows==0?result.getRecordCount()/rows:result.getRecordCount()/rows+1;
        result.setPageCount(totalPage);
        return result;
    }

    @Override
    public void addDocument(SearchItem searchItem) {
        try {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_desc", searchItem.getItem_desc());
            solrServer.add(document);
            solrServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
