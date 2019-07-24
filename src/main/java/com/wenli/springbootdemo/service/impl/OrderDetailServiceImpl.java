package com.wenli.springbootdemo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wenli.springbootdemo.common.HttpCode;
import com.wenli.springbootdemo.common.MyException;
import com.wenli.springbootdemo.common.PageParam;
import com.wenli.springbootdemo.dao.OrderDetailDao;
import com.wenli.springbootdemo.dao.ProductDao;
import com.wenli.springbootdemo.model.OrderDetail;
import com.wenli.springbootdemo.model.Product;
import com.wenli.springbootdemo.service.OrderDetailService;
import com.wenli.springbootdemo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @program: springbootdemo
 * @description:
 * @author: Koty
 * @create: 2019-07-11 19:19
 **/
@Slf4j
@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    OrderDetailDao orderDetailDao;

    @Cacheable(key = "#p0", value = "OrderDetails")
    @Override
    public Object getAllOrderDetail(PageParam<OrderDetail> pageParam) {

        log.info("走的是数据库");

        /**
         * 1.设置分页的页面属性
         * pagehelper插件中的开始页设置，这里传了一个（第几页）pageNum，和一个（页尺寸）pageSize
         */
        PageHelper.startPage(pageParam.getPageNum(),pageParam.getPageSize());

        /**
         * 2.实现（多）排序
         * 根据排序参数实现排序功能，用遍历实现多条件排序（需要排序的参数可能不止一个：id、username...）
         * 按照排序参数数组中的索引顺序依次实现排序需求
         */
        for (int i = 0; i < pageParam.getOrderParams().length; i++) {
            // 调用pagehelper的排序方法对排序参数数组依次排序
            PageHelper.orderBy(pageParam.getOrderParams()[i]);
        }

        List<OrderDetail> orderDetailList = orderDetailDao.getAllOrderDetail(pageParam.getModel());
        // 将OrderDetaildao中查出来的OrderDetaillist赋给OrderDetailpageinfo并返回
        PageInfo<OrderDetail> orderDetailPageInfo = new PageInfo<>(orderDetailList);

        return orderDetailPageInfo;
    }



    /**p
     * @Cacheable(key = "#p0", value = "OrderDetails")
     * 当缓存中没有想要的值（key）时，才调用下面的方法,
     * #p（取参数）0（下面方法中的第1个）及id  赋值给key
     * value 的值为下面方法返回值   todo(是不是对的)
     */
    @Cacheable(key = "#p0", value = "OrderDetails")
    @Override
    public OrderDetail getOrderDetailById(int id) {
        log.info("走的是数据库");
        return orderDetailDao.getOrderDetailById(id);
    }


    @CachePut(key = "#p0.id", value = "OrderDetails")
    @Override
    public Object addOrderDetail(OrderDetail orderDetail) {

        log.info("走的是数据库");

        orderDetailDao.addOrderDetail(orderDetail); // mapper文件中添加完后会执行selectKey并返回id的值

        return orderDetailDao.getOrderDetailById(orderDetail.getId()); // 根据id的值得到整个对象并返回
    }

    @CacheEvict(key = "#p0", value = "OrderDetails")
    @Override
    public boolean deleteOrderDetailById(int id) {

        log.info("走的是数据库");

        return orderDetailDao.deleteOrderDetailById(id) == 1;
    }

    @Override
    public boolean updateOrderDetail(OrderDetail orderDetail) {

        if(StringUtils.isEmpty(orderDetail.getId())){
            throw new MyException(HttpCode.ERROR).msg("通过id修改订单表体时，id不能为空");
        }

        return orderDetailDao.updateOrderDetail(orderDetail) == 1;
    }


}
