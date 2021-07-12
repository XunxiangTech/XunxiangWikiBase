package com.xunxiang.xunxiangwikibase.controller;

import com.xunxiang.xunxiangwikibase.req.CategorySaveReq;
import com.xunxiang.xunxiangwikibase.req.WikibookQueryReq;
import com.xunxiang.xunxiangwikibase.req.WikibookSaveReq;
import com.xunxiang.xunxiangwikibase.resp.CategoryResp;
import com.xunxiang.xunxiangwikibase.resp.CommonResp;
import com.xunxiang.xunxiangwikibase.resp.PageResp;
import com.xunxiang.xunxiangwikibase.resp.WikibookResp;
import com.xunxiang.xunxiangwikibase.service.WikibookService;
import com.xunxiang.xunxiangwikibase.service.impl.CategoryServiceImpl;
import com.xunxiang.xunxiangwikibase.service.impl.WikibookServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
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

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody WikibookSaveReq wikibookSaveReq){
         CommonResp resp = new CommonResp();
         try {
             wikibookService.save(wikibookSaveReq);
         }
         catch (ParseException e){
             e.printStackTrace();
         }
         resp.setMessage("【"+wikibookSaveReq.getTitle() + "】 Successfully Created/Updated");
         return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp commonResp = new CommonResp();
        commonResp.setMessage("Wikibook 【"+wikibookService.select(id).getTitle()+"】 Successfully deleted");
        wikibookService.delete(id);
        return commonResp;
    }
}
