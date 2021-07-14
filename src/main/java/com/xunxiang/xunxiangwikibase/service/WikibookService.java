package com.xunxiang.xunxiangwikibase.service;


import com.xunxiang.xunxiangwikibase.domain.Wikibook;
import com.xunxiang.xunxiangwikibase.req.WikibookQueryReq;
import com.xunxiang.xunxiangwikibase.req.WikibookSaveReq;
import com.xunxiang.xunxiangwikibase.resp.PageResp;
import com.xunxiang.xunxiangwikibase.resp.WikibookResp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

public interface WikibookService {

    /**
     * Select the wikibook based on id
     * @param id wikibook id
     * @return the wikibook of this id
     */
    Wikibook select(Long id);

    /**
     * Retrieve Correct wikibooks
     * @param wikiBookQueryReq requests param for retrieving
     * @return PageResp to include total count and pagination book list
     */
    PageResp<WikibookResp> list(WikibookQueryReq wikiBookQueryReq);

    /**
     * Create or Update wikibooks
     * @param wikibookSaveReq request param for editing
     */
    void save(WikibookSaveReq wikibookSaveReq) throws ParseException;

    /**
     * Upload Avatar for Wikibook
     * @param file the Multipart file(Image) from frontend
     */
    void uploadAvatar(MultipartFile file);

    /**
     * Delete wikibooks
     * @param id wikibook id
     */
    void delete(Long id);

}
