package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
import com.taotao.service.jedis.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Value("${ITEM_INFO_PRE}")
    private String ITEM_INFO_PRE;
    @Value("${ITEM_INFO_EXPIRE}")
    private Integer ITEM_INFO_EXPIRE;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    @Override
    public TbItem getItemById(Long itemId) {
        try {
            //查询缓存
            String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                //把json转换为java对象
                TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
                jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":BASE",ITEM_INFO_EXPIRE);
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TbItem tbItem = tbItemMapper.findItemById(itemId);
        jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":BASE",JsonUtils.objectToJson(tbItem));
        jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":BASE",ITEM_INFO_EXPIRE);
        return tbItem;
    }

    @Override
    public EasyUIResult getItemList(int page, int rows) {
        //初始化分页设置 他的底层原理是 自动拼接limit语句
        PageHelper.startPage(page, rows);
        //查询所有商品信息
        List<TbItem> items = tbItemMapper.findItemAll();
        //取分页信息
        PageInfo<TbItem> pageInfo = new PageInfo<>(items);
        //ctrl+alt+L  整理代码
        EasyUIResult result = new EasyUIResult();
        //使用插件得到总记录条数
        result.setTotal(pageInfo.getTotal());
        //被分页插件处理以后得到的结果集了集合对象
        result.setRows(items);
        return result;
    }

    @Override
    public TaotaoResult deleteItems(Integer[] ids) {
        int i = tbItemMapper.deleteItems(ids);
        if (i != 0) {
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public TaotaoResult addItems(TbItem tbItem, String desc, String itemParams) {
        final Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 1);
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        //所有数据准备完毕才能添加商品信息
        int itemCount = tbItemMapper.addItems(tbItem);
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(itemId);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        //准备了描述信息的所有数据才能添加描述信息
        int itemDescCount = tbItemDescMapper.addItemDesc(itemDesc);

        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParams);
        tbItemParamItem.setCreated(date);
        tbItemParamItem.setUpdated(date);
        //存入规格参数
        int itemParamConut = tbItemParamItemMapper.addTbitemParamItem(tbItemParamItem);
        if(itemCount!=0&&itemDescCount!=0&&itemParamConut!=0){
            //商品对象的json
            jmsTemplate.send(topicDestination, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(itemId+"");
                    return message;
                }
            });
            return TaotaoResult.ok();
        }

        return TaotaoResult.build(500,"添加商品有误，请重新输入");
    }

    /*@Override
    public TaotaoResult addItems(TbItem tbItem, String desc) {
        final Long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 1);
        Date date = new Date();
        tbItem.setCreated(date);
        tbItem.setUpdated(date);
        //所有数据准备完毕才能添加商品信息
        int itemCount = tbItemMapper.addItems(tbItem);
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(itemId);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);
        //准备了描述信息的所有数据才能添加描述信息
        int itemDescCount = tbItemDescMapper.addItemDesc(itemDesc);



        if(itemCount!=0&&itemDescCount!=0){
           jmsTemplate.send(topicDestination, new MessageCreator() {
               @Override
               public Message createMessage(Session session) throws JMSException {
                   TextMessage message = session.createTextMessage(itemId+"");
                   return message;
               }
           });
            return TaotaoResult.ok();
        }
        //.
        return TaotaoResult.build(500,"添加商品有误，请重新输入");
    }*/

    @Override
    public TaotaoResult downItem(Integer[] ids) {
        int i = tbItemMapper.downItem(ids);
        if (i != 0) {
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public TaotaoResult upItem(Integer[] ids) {
        int i = tbItemMapper.upItem(ids);
        if (i != 0) {
            return TaotaoResult.ok();
        }
        return null;
    }

    @Override
    public TaotaoResult getItemDesc(Long id) {
        TbItemDesc itemDesc = tbItemDescMapper.findDescById(id);
        return TaotaoResult.ok(itemDesc);
    }

    @Override
    public TaotaoResult updateItem(TbItem item, String desc) {
        int i = tbItemMapper.updateItem(item);
        Long ID = item.getId();
        int j = tbItemDescMapper.updateItemDesc(desc,ID);
        TaotaoResult result = new TaotaoResult();
        result.setStatus(200);
        return result;
    }

    @Override
    public TbItemDesc getItemDescByItemId(Long itemId) {
        try {
            //查询缓存
            String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                //把json转换为java对象
                TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":DESC",ITEM_INFO_EXPIRE);
                return itemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.getItemDescByItemId(itemId);
        jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":DESC",JsonUtils.objectToJson(tbItemDesc));
        jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":DESC",ITEM_INFO_EXPIRE);
        return tbItemDesc;
    }
}
