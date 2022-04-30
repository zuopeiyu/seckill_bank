package com.csse.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品返回对象
 * <p>
 * 乐字节：专注线上IT培训
 * 答疑老师微信：lezijie
 *
 * @author zhoubin
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVO {
  private Long goodsId;
	private BigDecimal goodsPrice;
	private Integer goodStock;
	private Date goodsStartTime;
	private Date goodsEndTime;
	//图片路径
}