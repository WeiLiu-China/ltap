package com.xdja.web.configure.token.operator;

import java.util.List;
import java.util.Map;

/**
 * @author lvhao
 * @version 1.0
 * @date 2019/5/16 14:09
 */
public interface TokenOperator {
    /**
     * 新增Token，返回Token
     *
     * @param value Token对应的值，{@code value} 应该实现Serializable
     * @return Token
     * @author : lvhao
     * @date : 2018年4月3日 下午1:33:38
     */
    String add(String value);

    /**
     * 根据Token获取值
     *
     * @param token
     * @return Token对应的值
     * @author : lvhao
     * @date : 2018年4月3日 下午1:34:42
     */
    String get(String token);

    /**
     * 返回当前全部有效的值
     *
     * @return
     * @author : lvhao
     * @date : 2018年4月3日 下午1:35:09
     */
    List<String> values();

    /**
     * 返回当前全部有效的Token键值
     *
     * @return
     * @author : lvhao
     * @date : 2018年4月3日 下午1:35:09
     */
    Map<String, String> all();

    /**
     * Token延期，延期时间为设置的过期时间
     *
     * @param token
     * @return true表示延期成功，false表示延期失败
     * @author : lvhao
     * @date : 2018年4月3日 下午1:37:02
     */
    boolean delay(String token);

    /**
     * Token状态设为无效，如果Token已经失效，返回true；如果Token存在，则设置Token状态为无效，返回true；如果设置失败，返回false。
     *
     * @param token
     * @return true表示设置成功，false表示设置失败
     * @author : lvhao
     * @date : 2018年4月3日 下午1:37:02
     */
    boolean invalidate(String token);
}