package com.xunxiang.xunxiangwikibase.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xunxiang.xunxiangwikibase.domain.Wikibook;
import com.xunxiang.xunxiangwikibase.domain.WikibookExample;
import com.xunxiang.xunxiangwikibase.mapper.WikibookMapper;
import com.xunxiang.xunxiangwikibase.req.WikibookQueryReq;
import com.xunxiang.xunxiangwikibase.resp.PageResp;
import com.xunxiang.xunxiangwikibase.resp.WikibookResp;
import com.xunxiang.xunxiangwikibase.service.WikibookService;
import com.xunxiang.xunxiangwikibase.util.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WikibookServiceImpl implements WikibookService {

    @Resource
    WikibookMapper wikibookMapper;

    @Override
    public PageResp<WikibookResp> list(WikibookQueryReq wikiBookQueryReq) {

        // Create example to search for
        WikibookExample wikibookExample = new WikibookExample();
        WikibookExample.Criteria criteria = wikibookExample.createCriteria();
        if(!ObjectUtils.isEmpty(wikiBookQueryReq.getTitle())) {
            criteria.andTitleLike("%"+wikiBookQueryReq.getTitle()+"%");
        }
        if(!ObjectUtils.isEmpty(wikiBookQueryReq.getDescription())) {
            criteria.andDescriptionLike("%"+wikiBookQueryReq.getDescription()+"%");
        }
        if(!ObjectUtils.isEmpty(wikiBookQueryReq.getCategory2Id())) {
            criteria.andCategory2IdEqualTo(wikiBookQueryReq.getCategory2Id());
        }

        // Get Pagination params
        PageHelper.startPage(wikiBookQueryReq.getPage(),wikiBookQueryReq.getSize());
        List<Wikibook> wikibookList = wikibookMapper.selectByExample(wikibookExample);

        List<WikibookResp> wikibookResps = CopyUtil.copyList(wikibookList,WikibookResp.class);

        // Get Pagination info
        PageInfo<Wikibook> pageInfo = new PageInfo<>(wikibookList);
        PageResp<WikibookResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(wikibookResps);

        return pageResp;
    }
}
