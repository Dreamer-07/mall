package pers.prover.mall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.apache.ibatis.annotations.Update;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import pers.prover.mall.common.validation.anno.LimitValueValid;
import pers.prover.mall.common.validation.anno.UpdateStrValid;
import pers.prover.mall.common.validation.group.AddGroup;
import pers.prover.mall.common.validation.group.UpdateGroup;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author 小·木曾仪仲·哈牛柚子露·蛋卷
 * @email 2391105059@qq.com
 * @date 2022-05-29 15:29:02
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@Null(groups = {AddGroup.class}, message = "添加数据时不允许指定标识")
	@NotNull(groups = {UpdateGroup.class}, message = "修改数据时必须指定标识")
	private Long brandId;
	/**
	 * 品牌名
	 */
	@UpdateStrValid(groups = {UpdateGroup.class}, message = "品牌名不能为空字符串")
	@NotBlank(groups = {AddGroup.class}, message = "品牌名不能为空")
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(groups = {AddGroup.class}, message = "logo 不能为空")
	@URL(groups = {AddGroup.class, UpdateGroup.class}, message = "必须是一个合法的 URL 地址")
	private String logo;
	/**
	 * 介绍
	 */
	@UpdateStrValid(groups = {UpdateGroup.class}, message = "介绍不能为空字符串")
	@NotBlank(groups = {AddGroup.class}, message = "介绍不能为空")
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(groups = {AddGroup.class}, message = "显示状态不能为空")
	@LimitValueValid(value = {0, 1}, groups = {AddGroup.class, UpdateGroup.class}, message = "显示状态的值只能为 0/1")
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(groups = {AddGroup.class}, message = "检索首字母不能为空")
	@Pattern(regexp = "^[a-zA-Z]$",groups = {AddGroup.class, UpdateGroup.class}, message = "首字母必须是a-z/A-Z之间的且长度为1")
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(groups = {AddGroup.class}, message = "排序属性不能为空")
	@Min(value = 0, message = "排序属性不能小于0", groups = {AddGroup.class, UpdateGroup.class})
	private Integer sort;

}
