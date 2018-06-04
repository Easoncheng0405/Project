package com.jlu.chengjie.util;/*
 *个人博客:http://www.chengjie-jlu.com
 *Github:https://github.com/Easoncheng0405
 *作者 程杰
 *创建时间 2018/6/4
 */

import com.jlu.chengjie.model.Constant;
import com.jlu.chengjie.repository.SavingRepository;

public class IDUtil {
    /**
     * 不允许实例化
     */
    private IDUtil() {
        throw new RuntimeException("can not instant a util class!");
    }


    /**
     *
     * @param savingRepository Repository
     * @param type 储蓄类型
     * @param mType 币种
     * @return ID
     */
    public static int getId(SavingRepository savingRepository,String type,String mType){
        int res = 0;

        switch (mType) {
            case Constant.RMB:
                res += Constant.RMB_ID;
                break;
            case Constant.MY:
                res += Constant.MY_ID;
                break;
            case Constant.GB:
                res += Constant.GB_ID;
                break;
            case Constant.RY:
                res += Constant.RY_ID;
                break;
            case Constant.OY:
                res += Constant.OY_ID;
                break;
            default:
                break;
        }

        switch (type) {
            case Constant.SAVE_ONE:
                res += Constant.ONE_ID;
                break;
            case Constant.SAVE_TWO:
                res += Constant.TWO_ID;
                break;
            case Constant.SAVE_THREE:
                res += Constant.THREE_ID;
                break;
            default:
                break;
        }

        return res + savingRepository.findByTypeAndMoneyType(type, mType).size();
    }

}
