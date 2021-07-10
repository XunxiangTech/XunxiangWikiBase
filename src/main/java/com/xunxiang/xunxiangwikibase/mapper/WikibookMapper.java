package com.xunxiang.xunxiangwikibase.mapper;

import com.xunxiang.xunxiangwikibase.domain.Wikibook;
import com.xunxiang.xunxiangwikibase.domain.WikibookExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WikibookMapper {
    long countByExample(WikibookExample example);

    int deleteByExample(WikibookExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Wikibook record);

    int insertSelective(Wikibook record);

    List<Wikibook> selectByExample(WikibookExample example);

    Wikibook selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Wikibook record, @Param("example") WikibookExample example);

    int updateByExample(@Param("record") Wikibook record, @Param("example") WikibookExample example);

    int updateByPrimaryKeySelective(Wikibook record);

    int updateByPrimaryKey(Wikibook record);
}