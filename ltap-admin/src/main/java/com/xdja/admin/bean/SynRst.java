package com.xdja.admin.bean;

import lombok.Data;

import java.util.List;

/**
 * ClassName：SynRst
 * Description：信息同步结果
 */
@Data
public class SynRst<T> {
	/**
	 * 响应结果
	 */
	private String code;
	/**
	 * 操作结果
	 */
	private Result result;
	/**
	 * 数据列表
	 */
	private List<T> list;
	/**
	 * 总数
	 */
	private String count;
	/**
	 * 服务器当前时间
	 */
	private String time;

	@Data
	public static class Result {
		/**
		 * 响应id
		 */
		private String id;
		/**
		 * 操作结果
		 */
		private String flag;
		/**
		 * 提示信息
		 */
		private String message;
		/**
		 * 补充添加token
		 */
		private String token;
	}

}
