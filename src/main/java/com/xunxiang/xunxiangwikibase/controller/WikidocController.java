package com.xunxiang.xunxiangwikibase.controller;

import com.xunxiang.xunxiangwikibase.req.WikidocQueryReq;
import com.xunxiang.xunxiangwikibase.req.WikidocSaveReq;
import com.xunxiang.xunxiangwikibase.resp.CommonResp;
import com.xunxiang.xunxiangwikibase.resp.PageResp;
import com.xunxiang.xunxiangwikibase.resp.WikidocResp;
import com.xunxiang.xunxiangwikibase.service.impl.WikidocServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/wikidoc")
public class WikidocController {

    @Autowired
    WikidocServiceImpl wikidocService;

    //@RequiresPermissions("docInfo:delete")
    @GetMapping("/list")
    public CommonResp list(@Valid WikidocQueryReq wikidocQueryReq){
        CommonResp<PageResp<WikidocResp>> commonResp = new CommonResp<>();
        PageResp<WikidocResp> resp = wikidocService.list(wikidocQueryReq);
        commonResp.setContent(resp);
        return commonResp;
    }


    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody WikidocSaveReq wikidocSaveReq){
         CommonResp resp = new CommonResp();
         try {
             wikidocService.save(wikidocSaveReq);
         }
         catch (ParseException e){
             e.printStackTrace();
         }
         resp.setMessage("【"+wikidocSaveReq.getName() + "】 Successfully Created/Updated");
         return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable String id){
        CommonResp commonResp = new CommonResp();
        List<String> idstr = Arrays.asList(id.split(","));
        String docs = "";
        for(String idS: idstr){
            Long idL = Long.valueOf(idS);
            docs+=wikidocService.select(idL).getName()+", ";
        }
        commonResp.setMessage("Wikidoc 【"+docs+"】 Successfully deleted");
        wikidocService.delete(idstr);
        return commonResp;
    }
}
