package com.music.common.music.dao;

import com.music.common.music.domain.entity.Singer;
import com.music.common.music.mapper.SingerMapper;
import com.music.common.music.service.ISingerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌手表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/emberff">pf</a>
 * @since 2025-03-21
 */
@Service
public class SingerDao extends ServiceImpl<SingerMapper, Singer>{

}
