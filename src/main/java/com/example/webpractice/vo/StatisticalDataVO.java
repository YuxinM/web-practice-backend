package com.example.webpractice.vo;

import com.example.webpractice.po.ChartData;

import java.util.List;

public class StatisticalDataVO {
    private List<ChartData> categoryPie;
    private List<ChartData> yearLine;

    public List<ChartData> getCategoryPie() {
        return categoryPie;
    }

    public void setCategoryPie(List<ChartData> categoryPie) {
        this.categoryPie = categoryPie;
    }

    public List<ChartData> getYearLine() {
        return yearLine;
    }

    public void setYearLine(List<ChartData> yearLine) {
        this.yearLine = yearLine;
    }

    public StatisticalDataVO(List<ChartData> categoryPie, List<ChartData> yearLine) {
        this.categoryPie = categoryPie;
        this.yearLine = yearLine;
    }
}
