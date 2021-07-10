package com.xunxiang.xunxiangwikibase.controller;

import com.xunxiang.xunxiangwikibase.req.CategorySaveReq;
import com.xunxiang.xunxiangwikibase.req.WikibookQueryReq;
import com.xunxiang.xunxiangwikibase.resp.CategoryResp;
import com.xunxiang.xunxiangwikibase.resp.CommonResp;
import com.xunxiang.xunxiangwikibase.resp.PageResp;
import com.xunxiang.xunxiangwikibase.resp.WikibookResp;
import com.xunxiang.xunxiangwikibase.service.WikibookService;
import com.xunxiang.xunxiangwikibase.service.impl.CategoryServiceImpl;
import com.xunxiang.xunxiangwikibase.service.impl.WikibookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/wikibook")
public class WikibookController {

    @Autowired
    WikibookServiceImpl wikibookService;

    @GetMapping("/list")
    public CommonResp list(@Valid WikibookQueryReq wikibookQueryReq){
        CommonResp<PageResp<WikibookResp>> commonResp = new CommonResp<>();
        PageResp<WikibookResp> resp = wikibookService.list(wikibookQueryReq);
        commonResp.setContent(resp);
        return commonResp;
    }
}
