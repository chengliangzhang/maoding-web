package com.maoding.statistic.controller;

import com.maoding.statistic.dto.StatisticDetailDTO;

import java.util.Comparator;

/**
 * 作    者 : DongLiu
 * 日    期 : 2017/12/15 16:03
 * 描    述 :
 */
public class SortClass implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        StatisticDetailDTO detailDTO = (StatisticDetailDTO) o1;
        StatisticDetailDTO detailDTO1 = (StatisticDetailDTO) o2;
        int flag = detailDTO.getCreateDate().compareTo(detailDTO1.getCreateDate());
        return flag;
    }
}
