package com.jlu.chengjie.model;

/*
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


    public final static int ONE_ID = 1000;
    public final static int TWO_ID = 2000;
    public final static int THREE_ID = 3000;

    public final static int RMB_ID = 10000;
    public final static int MY_ID = 20000;
    public final static int GB_ID = 30000;
    public final static int RY_ID = 40000;
    public final static int OY_ID = 50000;

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
    public final static long RANGE = 60000L;


    /*
     *  各类币种
     */

    public static final String RMB = "人民币";
    public static final String MY = "美元";
    public static final String GB = "港币";
    public static final String RY = "日元";
    public static final String OY = "欧元";


    /*
     * 交易类型
     */

    public static final String SAVE = "存款";

    public static final String GET = "取款";

    public static final String AUTO = "自动计息";

    public static final String CONTINUE="续存";

    public static final String CHANGE="转存";


}
