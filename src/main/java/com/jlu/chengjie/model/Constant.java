package com.jlu.chengjie.model;

/**
 * 个人博客:http://www.chengjie-jlu.com
 * Github:https://github.com/Easoncheng0405
 * 作者 程杰
 * 创建时间 2018/5/30
 */
public class Constant {


    public Constant(){
        throw new RuntimeException();
    }


    /**
     * 初始10位已开通卡号
     */
    public final static long NUMBER= 1000000000L;

    /**
     * 账户正常
     */
    public final static String ACCOUNT_NORMAL="正常";


    /**
     * 账户冻结
     */
    public final static String ACCOUNT_FREEZE="冻结";


    /**
     * 活期储蓄
     */
    public final static String SAVE_ONE="活期储蓄";

    public final static String SAVE_TWO_ONE_YEAR_FALSE="整存整取一年不续存";


    public final static String SAVE_TWO_FIVE_YEAR_FALSE="整存整取五年不续存";


    public final static String SAVE_TWO_ONE_YEAR_TRUE="整存整取一年续存";


    public final static String SAVE_TWO_FIVE_YEAR_TRUE="整存整取五年续存";


    public final static String SAVE_THREE="定活两便";
}
