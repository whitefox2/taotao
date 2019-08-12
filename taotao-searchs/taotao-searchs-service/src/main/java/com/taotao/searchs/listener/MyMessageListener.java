package com.taotao.searchs.listener;

import com.taotao.common.pojo.SearchItem;
import com.taotao.mapper.SearchItemMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.searchs.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyMessageListener implements MessageListener {
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SearchItemService searchItemService;
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String itemId = textMessage.getText();
            SearchItem searchItem = searchItemMapper.getItemById(Long.valueOf(itemId));
            searchItemService.addDocument(searchItem);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
