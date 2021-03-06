package com.xunxiang.xunxiangwikibase.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xunxiang.xunxiangwikibase.domain.Content;
import com.xunxiang.xunxiangwikibase.domain.ContentExample;
import com.xunxiang.xunxiangwikibase.domain.Wikidoc;
import com.xunxiang.xunxiangwikibase.domain.WikidocExample;
import com.xunxiang.xunxiangwikibase.mapper.ContentMapper;
import com.xunxiang.xunxiangwikibase.mapper.WikidocMapper;
import com.xunxiang.xunxiangwikibase.req.WikidocQueryReq;
import com.xunxiang.xunxiangwikibase.req.WikidocSaveReq;
import com.xunxiang.xunxiangwikibase.resp.PageResp;
import com.xunxiang.xunxiangwikibase.resp.WikidocResp;
import com.xunxiang.xunxiangwikibase.service.WikidocService;
import com.xunxiang.xunxiangwikibase.util.CopyUtil;
import com.xunxiang.xunxiangwikibase.util.SnowFlake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WikidocServiceImpl implements WikidocService {

    @Resource
    WikidocMapper wikidocMapper;

    @Resource
    ContentMapper contentMapper;

    @Resource
    SnowFlake snowFlake;

    @Override
    public Wikidoc select(Long id) {
        return wikidocMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageResp<WikidocResp> list(WikidocQueryReq wikiDocQueryReq) {

        // Create example to search for
        WikidocExample wikidocExample = new WikidocExample();
        wikidocExample.setOrderByClause("update_time asc");
        WikidocExample.Criteria criteria = wikidocExample.createCriteria();

        if(!ObjectUtils.isEmpty(wikiDocQueryReq.getWikibookId())) {
            criteria.andWikibookIdEqualTo(wikiDocQueryReq.getWikibookId());
        }

        if(!ObjectUtils.isEmpty(wikiDocQueryReq.getName())) {
            criteria.andNameLike("%"+wikiDocQueryReq.getName()+"%");
        }
        if(!ObjectUtils.isEmpty(wikiDocQueryReq.getChecked())) {
            criteria.andCheckedEqualTo(wikiDocQueryReq.getChecked());
        }

        // Get Pagination params
        PageHelper.startPage(wikiDocQueryReq.getPage(),wikiDocQueryReq.getSize());
        List<Wikidoc> wikidocList = wikidocMapper.selectByExample(wikidocExample);

        List<WikidocResp> wikidocResps = CopyUtil.copyList(wikidocList,WikidocResp.class);

        // Get Pagination info
        PageInfo<Wikidoc> pageInfo = new PageInfo<>(wikidocList);
        PageResp<WikidocResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(wikidocResps);

        return pageResp;
    }

    @Override
    public void save(WikidocSaveReq wikidocSaveReq) throws ParseException {
        Wikidoc wikidoc = CopyUtil.copy(wikidocSaveReq,Wikidoc.class);
        Content content = CopyUtil.copy(wikidocSaveReq, Content.class);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        if(ObjectUtils.isEmpty(wikidocSaveReq.getId())){
            //New Post
            Long id = snowFlake.nextId();
            wikidoc.setId(id);
            wikidoc.setCreateTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dtf.format(now)));
            wikidoc.setUpdateTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dtf.format(now)));
            wikidoc.setViewCount(0);
            wikidoc.setVoteCount(0);
            wikidoc.setChecked(false);
            wikidocMapper.insert(wikidoc);

            content.setId(id);
            contentMapper.insert(content);
        }
        else {
            //Update
            wikidoc.setUpdateTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dtf.format(now)));
            wikidocMapper.updateByPrimaryKeySelective(wikidoc);

            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if(count==0){
                contentMapper.insert(content);
            }
        }
    }


    @Override
    public void delete(Long id) {
        wikidocMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void delete(List<String> idsStr){
        WikidocExample docExample = new WikidocExample();
        WikidocExample.Criteria criteria = docExample.createCriteria();

        ContentExample contentExample = new ContentExample();
        ContentExample.Criteria criteria1 = contentExample.createCriteria();

        criteria.andIdIn(idsStr);
        wikidocMapper.deleteByExample(docExample);

        criteria1.andIdIn(idsStr);
        contentMapper.deleteByExample(contentExample);
    }

    @Override
    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        if(!ObjectUtils.isEmpty(content)) {
            return content.getContent();
        }
        return "";
    }

    public static void main(String[]args){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }
}
