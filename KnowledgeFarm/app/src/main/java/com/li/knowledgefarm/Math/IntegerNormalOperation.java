//package com.li.knowledgefarm.Math;
//
//
//import dropwizard.demo.api.FormulaItem;
//import dropwizard.demo.api.IntegerOperate;
//import dropwizard.demo.common.BaseOperation;
//import dropwizard.demo.common.GenerateFormula;
//import dropwizard.demo.utils.IntegerCommon;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//public class IntegerNormalOperation extends BaseOperation implements GenerateFormula {
//
//    @Override
//    public FormulaItem getFormula(String arg1, String arg2) {
//        IntegerCommon integerCommon=new IntegerCommon();
//        Map argumentsMap=integerCommon.getArguments(arg1,arg2);
//        List<IntegerOperate> list = (List<IntegerOperate>) argumentsMap.get("argsList");
//        int position = (int) argumentsMap.get("position");
//        String format = (String) argumentsMap.get("format");
//        FormulaItem formulaItem = null;
//        Random rand = new Random();
//        String resultString = format;
//        while (true) {
//            format = resultString;
//            for (int i = 0; i < list.size()-1; i++) {
//                int randNum = rand.nextInt(list.get(i).getMax() - list.get(i).getMin() + 1) + list.get(i).getMin();
//                list.get(i).setData(randNum);
//                format = format.replace("a" + i, randNum + "");
//            }
//            Map resultMap=integerCommon.calculateResult(format,list.get(list.size()-1));
//            if ((boolean)resultMap.get("checkFlag")) {
//                list.get(list.size()-1).setData((int)resultMap.get("lastNum"));
//                formulaItem=integerCommon.getResult(list,resultString,position);
//                break;
//            }
//        }
//        return formulaItem;
//    }
//
//
//}
