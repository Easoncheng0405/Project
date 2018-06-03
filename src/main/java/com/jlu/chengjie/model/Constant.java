package com.jlu.chengjie.model;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/30
 */


import java.math.BigDecimal;

/**
 * 各类常量
 */
public class Constant {

    /**
     * 不允许实例化
     */
    private Constant() {
        throw new RuntimeException("can not instant a constant class!");
    }


    /**
     * 初始10位已开通卡号
     */
    public final static long ID = 1000000000L;

    /**
     * 人民币活期起始储蓄子账户账号
     */
    public final static int RMB_ONE = 11000;


    /**
     * 人民币整存整取起始储蓄子账户账号
     */
    public final static int RMB_TWO = 12000;


    /**
     * 人民币定活两便起始储蓄子账户账号
     */
    public final static int RMB_THREE = 13000;

    /**
     * 账户正常
     */
    public final static String ACCOUNT_NORMAL = "正常";


    /**
     * 账户冻结
     */
    public final static String ACCOUNT_FREEZE = "冻结";


    /**
     * 活期储蓄
     */
    public final static String SAVE_ONE = "活期储蓄";


    /**
     * 整存整取
     */
    public final static String SAVE_TWO = "整存整取";


    /**
     * 四种整存整取储蓄类型
     */
    public final static String SAVE_TWO_ONE_YEAR_FALSE = "整存整取一年不续存";
    public final static String SAVE_TWO_FIVE_YEAR_FALSE = "整存整取五年不续存";
    public final static String SAVE_TWO_ONE_YEAR_TRUE = "整存整取一年续存";
    public final static String SAVE_TWO_FIVE_YEAR_TRUE = "整存整取五年续存";


    /**
     * 定活两便储蓄
     */
    public final static String SAVE_THREE = "定活两便";


    /**
     * 活期利率
     */
    public static BigDecimal V_ONE = new BigDecimal("0.01");


    /**
     *
     */
    public static BigDecimal V_TWO = new BigDecimal("0.03");

    /**
     * 消息
     */
    public final static String MESSAGE = "message";


    /**
     * 用一分钟代替一年
     */
    public final static long RANGE =5000L;


}
