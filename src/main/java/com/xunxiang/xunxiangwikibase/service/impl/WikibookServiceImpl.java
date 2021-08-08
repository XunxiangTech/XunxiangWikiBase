package com.xunxiang.xunxiangwikibase.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xunxiang.xunxiangwikibase.domain.Wikibook;
import com.xunxiang.xunxiangwikibase.domain.WikibookExample;
import com.xunxiang.xunxiangwikibase.mapper.WikibookMapper;
import com.xunxiang.xunxiangwikibase.req.WikibookQueryReq;
import com.xunxiang.xunxiangwikibase.req.WikibookSaveReq;
import com.xunxiang.xunxiangwikibase.resp.PageResp;
import com.xunxiang.xunxiangwikibase.resp.WikibookResp;
import com.xunxiang.xunxiangwikibase.service.WikibookService;
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
import java.util.Date;
import java.util.List;

@Service
public class WikibookServiceImpl implements WikibookService {

    @Resource
    WikibookMapper wikibookMapper;

    @Resource
    SnowFlake snowFlake;

    @Value("${wiki.avatar:xxwikiweb/public/images/}")
    private String avatarPath;

    @Override
    public Wikibook select(Long id) {
        return wikibookMapper.selectByPrimaryKey(id);
    }

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

    @Override
    public void save(WikibookSaveReq wikibookSaveReq) throws ParseException {
        String iconPath = "/images/"+wikibookSaveReq.getIcon();

        wikibookSaveReq.setIcon(iconPath);

        Wikibook wikibook = CopyUtil.copy(wikibookSaveReq,Wikibook.class);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        if(ObjectUtils.isEmpty(wikibookSaveReq.getId())){
            //New Post
            wikibook.setId(snowFlake.nextId());
            wikibook.setCreateTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dtf.format(now)));
            wikibook.setUpdateTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dtf.format(now)));
            wikibook.setDocCount(0);
            wikibook.setViewCount(0);
            wikibook.setVoteCount(0);
            wikibookMapper.insert(wikibook);
        }
        else {
            //Update
            wikibook.setUpdateTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dtf.format(now)));
            wikibookMapper.updateByPrimaryKeySelective(wikibook);
        }
    }

    @Override
    public void uploadAvatar(MultipartFile file) {
        Path path = Paths.get(avatarPath+file.getOriginalFilename());
        try {
            Files.write(path, file.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Long id) {
        Wikibook wikibook = wikibookMapper.selectByPrimaryKey(id);
        if(!ObjectUtils.isEmpty(wikibook)){
            String iconPath = wikibook.getIcon();
            Path path = Paths.get(avatarPath+iconPath.split("/")[2]);
            try {
                Files.delete(path);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        wikibookMapper.deleteByPrimaryKey(id);
    }

    public static void main(String[]args){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }
}
